package ru.garskov.springcourse.ClimateSensorAPI.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import ru.garskov.springcourse.ClimateSensorAPI.dto.MeasurementDTO;
import ru.garskov.springcourse.ClimateSensorAPI.models.Measurement;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;
import ru.garskov.springcourse.ClimateSensorAPI.services.MeasurementService;
import ru.garskov.springcourse.ClimateSensorAPI.util.MeasurementValidator;
import ru.garskov.springcourse.ClimateSensorAPI.util.SensorException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeasurementsControllerTest {

    @InjectMocks
    private MeasurementsController measurementsController;

    @Mock
    private MeasurementService measurementService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private MeasurementValidator measurementValidator;

    private MeasurementDTO measurementDTO;
    private Measurement measurement;
    private BindingResult bindingResult;

    private static final String VALIDATION_ERROR_MESSAGE = "Значение не должно быть пустым!";

    @BeforeEach
    public void setUp() {
        Sensor sensor = new Sensor();
        sensor.setName("Sensor 1");

        measurementDTO = new MeasurementDTO();
        measurementDTO.setValue(10.0);
        measurementDTO.setRaining(true);
        measurementDTO.setSensor(sensor);

        measurement = new Measurement();
        bindingResult = new BeanPropertyBindingResult(measurementDTO, "measurementDTO");
    }

    @Test
    public void shouldAddMeasurement_Success() throws Exception {
        when(modelMapper.map(any(MeasurementDTO.class), eq(Measurement.class))).thenReturn(measurement);

        ResponseEntity<HttpStatus> response = measurementsController.addMeasurement(measurementDTO, bindingResult);

        verify(measurementValidator, times(1)).validate(measurement, bindingResult);
        verify(measurementService, times(1)).save(measurement);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldAddMeasurement_ValidationError() throws Exception {
        measurementDTO.setValue(null);

        bindingResult.rejectValue("value", "", VALIDATION_ERROR_MESSAGE);

        when(modelMapper.map(any(MeasurementDTO.class), eq(Measurement.class))).thenReturn(measurement);

        SensorException sensorException = assertThrows(SensorException.class, () -> {
            measurementsController.addMeasurement(measurementDTO, bindingResult);
        });

        verify(measurementValidator, times(1)).validate(measurement, bindingResult);
        verify(measurementService, never()).save(any(Measurement.class));
        assertEquals("value - " + VALIDATION_ERROR_MESSAGE + ";", sensorException.getMessage());
    }

    @Test
    public void shouldGetMeasurements() throws Exception {
        when(modelMapper.map(any(Measurement.class), eq(MeasurementDTO.class))).thenReturn(measurementDTO);
        when(measurementService.findAll()).thenReturn(Collections.singletonList(measurement));

        List<MeasurementDTO> measurementDTOList = measurementsController.getMeasurements();

        verify(measurementService, times(1)).findAll();
        assertEquals(1, measurementDTOList.size());
        assertEquals(measurementDTO, measurementDTOList.get(0)); // Проверка содержимого
    }

    @Test
    public void shouldGetRainyDaysCount() throws Exception {
        when(measurementService.rainyDaysCount()).thenReturn(5);

        int rainyDaysCount = measurementsController.getRainyDaysCount();

        verify(measurementService, times(1)).rainyDaysCount();
        assertEquals(5, rainyDaysCount); // Проверка результата
    }
}