package ru.garskov.springcourse.ClimateSensorAPI.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.validation.Errors;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;
import ru.garskov.springcourse.ClimateSensorAPI.services.SensorService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorValidatorTest {

    @InjectMocks
    private SensorValidator sensorValidator;

    @Mock
    private SensorService sensorService;

    @Mock
    private Errors errors;

    private Sensor sensor;

    private static final String SENSOR_NAME = "TestSensor";

    @BeforeEach
    public void setUp() {
        sensor = new Sensor();
        sensor.setName(SENSOR_NAME);
    }

    @Test
    public void shouldAddError_WhenSensorExists() {
        // Настраиваем мок для sensorService, чтобы он возвращал существующий сенсор
        when(sensorService.getSensorByName(SENSOR_NAME)).thenReturn(Optional.of(sensor));

        // Вызываем метод validate
        sensorValidator.validate(sensor, errors);

        // Проверяем, что ошибка была добавлена
        verify(errors, times(1)).rejectValue("name", "", "Сенсор с таким названием уже существует!");
        verify(sensorService, times(1)).getSensorByName(SENSOR_NAME);
    }

    @Test
    public void shouldNotAddError_WhenSensorDoesNotExist() {
        // Настраиваем мок для sensorService, чтобы он возвращал пустой Optional
        when(sensorService.getSensorByName(SENSOR_NAME)).thenReturn(Optional.empty());

        // Вызываем метод validate
        sensorValidator.validate(sensor, errors);

        // Проверяем, что ошибок не было добавлено
        verify(errors, never()).rejectValue(anyString(), anyString(), anyString());
        verify(sensorService, times(1)).getSensorByName(SENSOR_NAME);
    }

    @Test
    public void shouldReturnTrueForSensorClass_WhenSupportsCalled() {
        // Проверяем, что supports возвращает true для Sensor.class
        assertTrue(sensorValidator.supports(Sensor.class));

        // Проверяем, что supports возвращает false для других классов
        assertFalse(sensorValidator.supports(String.class));
    }
}