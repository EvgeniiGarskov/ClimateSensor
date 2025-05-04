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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.garskov.springcourse.ClimateSensorAPI.dto.SensorDTO;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;
import ru.garskov.springcourse.ClimateSensorAPI.services.SensorService;
import ru.garskov.springcourse.ClimateSensorAPI.util.SensorValidator;

import java.util.Optional;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SensorsController.class)
@Import(SensorValidator.class)
public class SensorsControllerWebMvcTest {

    @InjectMocks
    private SensorsController sensorsController;

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SensorService sensorService;

    @MockitoBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Sensor sensor;

    @BeforeEach
    public void setUp() {
        sensor = new Sensor();
    }

    private static SensorDTO createSensorDTO(String name) {
        SensorDTO sensorDTO = new SensorDTO();
        sensorDTO.setName(name);
        return sensorDTO;
    }

    private void performPostRequest(SensorDTO sensorDTO, int expectedStatus, String expectedContent) throws Exception {
        mockMvc.perform(post("/sensors/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sensorDTO)))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().string(containsString(expectedContent)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void shouldCreateSensor_Success() throws Exception {
        SensorDTO sensorDTO = createSensorDTO("Sensor 3");

        when(modelMapper.map(any(SensorDTO.class), eq(Sensor.class))).thenReturn(sensor);

        performPostRequest(sensorDTO, HttpStatus.OK.value(), "\"OK\"");

        verify(sensorService, times(1)).save(sensor);
    }

    @ParameterizedTest
    @MethodSource("provideInvalidSensorDTOs")
    public void shouldCreateSensor_ValidationError(SensorDTO sensorDTO, String expectedErrorMessage) throws Exception {

        when(modelMapper.map(any(SensorDTO.class), eq(Sensor.class))).thenReturn(sensor);

        performPostRequest(sensorDTO, HttpStatus.BAD_REQUEST.value(), expectedErrorMessage);

        verify(sensorService, never()).save(any(Sensor.class));
    }

    private static Stream<Arguments> provideInvalidSensorDTOs() {
        return Stream.of(
                Arguments.of(createSensorDTO(""), "Название сенсора не должно быть пустым!"),
                Arguments.of(createSensorDTO("S"), "Название сенсора должно быть от 3 до 30 символов!"),
                Arguments.of(createSensorDTO("Это имя длиннее тридцати символов"), "Название сенсора должно быть от 3 до 30 символов!")
        );
    }

    @Test
    public void shouldCreate_ExceptionHandling() throws Exception {
        SensorDTO sensorDTO = createSensorDTO("Sensor 1");

        when(modelMapper.map(any(SensorDTO.class), eq(Sensor.class))).thenReturn(sensor);

        when(sensorService.getSensorByName(sensor.getName())).thenReturn(Optional.of(sensor));

        performPostRequest(sensorDTO, HttpStatus.BAD_REQUEST.value(), "Сенсор с таким названием уже существует!");

        verify(sensorService, never()).save(any(Sensor.class));
    }
}
