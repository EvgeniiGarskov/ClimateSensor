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
import ru.garskov.springcourse.ClimateSensorAPI.dto.SensorDTO;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;
import ru.garskov.springcourse.ClimateSensorAPI.services.SensorService;
import ru.garskov.springcourse.ClimateSensorAPI.util.SensorException;
import ru.garskov.springcourse.ClimateSensorAPI.util.SensorValidator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorsControllerTest {

    @InjectMocks
    private SensorsController sensorsController;

    @Mock
    private SensorService sensorService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SensorValidator sensorValidator;

    private SensorDTO sensorDTO;
    private Sensor sensor;
    private BindingResult bindingResult;

    @BeforeEach
    public void setUp() {
        sensorDTO = new SensorDTO();
        sensor = new Sensor();
        bindingResult = new BeanPropertyBindingResult(sensorDTO, "sensorDTO");
    }

    @Test
    public void shouldCreateSensor_Success() throws Exception {
        sensorDTO.setName("Sensor 3");

        when(modelMapper.map(any(SensorDTO.class), eq(Sensor.class))).thenReturn(sensor);

        ResponseEntity<HttpStatus> response = sensorsController.create(sensorDTO, bindingResult);

        verify(sensorValidator, times(1)).validate(sensor, bindingResult);
        verify(sensorService, times(1)).save(sensor);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void shouldCreateSensor_ValidationError() throws Exception {
        sensorDTO.setName("");

        bindingResult.rejectValue("name", "", "Название сенсора не должно быть пустым!");

        when(modelMapper.map(any(SensorDTO.class), eq(Sensor.class))).thenReturn(sensor);

        SensorException sensorException = assertThrows(SensorException.class, () -> {
            sensorsController.create(sensorDTO, bindingResult);
        });

        verify(sensorValidator, times(1)).validate(sensor, bindingResult);
        verify(sensorService, never()).save(sensor);
        assertEquals("name - Название сенсора не должно быть пустым!;", sensorException.getMessage());
    }
}
