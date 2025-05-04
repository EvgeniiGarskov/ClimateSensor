package ru.garskov.springcourse.ClimateSensorAPI.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import ru.garskov.springcourse.ClimateSensorAPI.models.Measurement;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;
import ru.garskov.springcourse.ClimateSensorAPI.services.SensorService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeasurementValidatorTest {

    @InjectMocks
    private MeasurementValidator measurementValidator;

    @Mock
    private SensorService sensorService;

    @Mock
    private Errors errors;

    private Sensor sensor;
    private Measurement measurement;

    private static final String TEST_SENSOR_NAME = "TestSensor";

    @BeforeEach
    public void setUp() {
        sensor = new Sensor();
        sensor.setName(TEST_SENSOR_NAME);

        measurement = new Measurement();
        measurement.setSensor(sensor);
    }

    @Test
    public void shouldNotAddError_WhenSensorExists() {
        // Настраиваем мок для sensorService, чтобы он возвращал существующий сенсор
        when(sensorService.getSensorByName(TEST_SENSOR_NAME)).thenReturn(Optional.of(sensor));

        // Вызываем метод validate
        measurementValidator.validate(measurement, errors);

        // Проверяем, что ошибок не было добавлено
        verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
        verify(sensorService, times(1)).getSensorByName(TEST_SENSOR_NAME);
    }

    @Test
    public void shouldAddError_WhenSensorDoesNotExist() {
        // Настраиваем мок для sensorService, чтобы он возвращал пустой Optional
        when(sensorService.getSensorByName(TEST_SENSOR_NAME)).thenReturn(Optional.empty());

        // Вызываем метод validate
        measurementValidator.validate(measurement, errors);

        // Проверяем, что ошибка была добавлена
        verify(errors, times(1)).rejectValue("name", "", "Сенсора с таким названием не существует!");
        verify(sensorService, times(1)).getSensorByName(TEST_SENSOR_NAME);
    }

    @Test
    public void shouldReturnTrueForMeasurementClass_WhenSupportsCalled() {
        // Проверяем, что supports возвращает true для Measurement.class
        assertTrue(measurementValidator.supports(Measurement.class));

        // Проверяем, что supports возвращает false для других классов
        assertFalse(measurementValidator.supports(String.class));
    }
}