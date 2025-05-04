package ru.garskov.springcourse.ClimateSensorAPI.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.garskov.springcourse.ClimateSensorAPI.dto.MeasurementDTO;
import ru.garskov.springcourse.ClimateSensorAPI.models.Measurement;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;
import ru.garskov.springcourse.ClimateSensorAPI.services.MeasurementService;
import ru.garskov.springcourse.ClimateSensorAPI.util.MeasurementValidator;

import java.util.Collections;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MeasurementsController.class)
@Import(MeasurementValidator.class)
public class MeasurementsControllerWebMvcTest {

    @InjectMocks
    private MeasurementsController measurementsController;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MeasurementService measurementService;

    @MockitoBean
    private ModelMapper modelMapper;

    @MockitoBean
    private MeasurementValidator measurementValidator;

    @Autowired
    private ObjectMapper objectMapper;

    private MeasurementDTO measurementDTO;
    private Measurement measurement;

    @BeforeEach
    public void setUp() {
        Sensor sensor = createSensor();
        measurementDTO = createMeasurementDTO(10.0, true, sensor);
        measurement = createMeasurement();
    }

    private Sensor createSensor() {
        Sensor sensor = new Sensor();
        sensor.setName("Sensor 1");
        return sensor;
    }

    private static MeasurementDTO createMeasurementDTO(Double value, Boolean raining, Sensor sensor) {
        MeasurementDTO measurementDTO = new MeasurementDTO();
        measurementDTO.setValue(value);
        measurementDTO.setRaining(raining);
        measurementDTO.setSensor(sensor);
        return measurementDTO;
    }

    private Measurement createMeasurement() {
        return new Measurement();
    }

    @Test
    public void shouldAddMeasurement_Success() throws Exception {
        when(modelMapper.map(any(MeasurementDTO.class), eq(Measurement.class))).thenReturn(measurement);

        mockMvc.perform(post("/measurements/add")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(measurementDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("\"OK\""))
                .andExpect(content().contentType(APPLICATION_JSON));

        verify(measurementService, times(1)).save(measurement);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidMeasurementDTOs")
    public void shouldAddMeasurement_ValidationError(MeasurementDTO measurementDTO, String expectedErrorMessage) throws Exception {
        when(modelMapper.map(any(MeasurementDTO.class), eq(Measurement.class))).thenReturn(measurement);

        mockMvc.perform(post("/measurements/add")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(measurementDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString(expectedErrorMessage)))
                .andExpect(content().contentType(APPLICATION_JSON));

        verify(measurementService, never()).save(any(Measurement.class));
    }

    private static Stream<Arguments> provideInvalidMeasurementDTOs() {
        Sensor sensor = new Sensor();
        return Stream.of(
                Arguments.of(createMeasurementDTO(null, true, sensor), "Значение не должно быть пустым!"),
                Arguments.of(createMeasurementDTO(-101.0, true, sensor), "Значение должно быть не меньше -100!"),
                Arguments.of(createMeasurementDTO(101.0, true, sensor), "Значение должно быть не больше 100!"),
                Arguments.of(createMeasurementDTO(10.0, null, sensor), "Значение не должно быть пустым!"),
                Arguments.of(createMeasurementDTO(10.0, true, null), "Значение не должно быть пустым!")
        );
    }

    @Test
    public void shouldGetMeasurements() throws Exception {
        when(modelMapper.map(any(Measurement.class), eq(MeasurementDTO.class))).thenReturn(measurementDTO);
        when(measurementService.findAll()).thenReturn(Collections.singletonList(measurement));

        mockMvc.perform(get("/measurements"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].value").value(10.0))
                .andExpect(jsonPath("$[0].raining").value(true))
                .andExpect(jsonPath("$[0].sensor.name").value("Sensor 1"));

        verify(measurementService, times(1)).findAll();
    }

    @Test
    public void shouldGetRainyDaysCount() throws Exception {
        when(measurementService.rainyDaysCount()).thenReturn(5);

        mockMvc.perform(get("/measurements/rainyDaysCount"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(5))
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(content().string("5"));

        verify(measurementService, times(1)).rainyDaysCount();
    }
}
