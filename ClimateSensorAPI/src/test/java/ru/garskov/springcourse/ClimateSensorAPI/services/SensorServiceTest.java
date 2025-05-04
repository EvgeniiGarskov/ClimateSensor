package ru.garskov.springcourse.ClimateSensorAPI.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;
import ru.garskov.springcourse.ClimateSensorAPI.repositories.SensorRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SensorServiceTest {

    @InjectMocks
    private SensorService sensorService;

    @Mock
    private SensorRepository sensorRepository;

    private Sensor sensor;

    @BeforeEach
    public void setUp() {
        sensor = new Sensor();
        sensor.setName("Sensor");
    }

    @Test
    public void testSave() {
        sensorService.save(sensor);

        assertNotNull(sensor.getCreatedAt());
        verify(sensorRepository, times(1)).save(sensor);
    }

    @Test
    public void shouldReturnSensor_WhenSensorExists() {
        String sensorName = "TemperatureSensor";
        sensor.setName(sensorName);
        when(sensorRepository.findByName(sensorName)).thenReturn(Optional.of(sensor));

        Optional<Sensor> result = sensorService.getSensorByName(sensorName);

        assertTrue(result.isPresent());
        assertEquals(sensorName, result.get().getName());
        verify(sensorRepository, times(1)).findByName(sensorName);
    }

    @Test
    public void shouldNotReturnSensor_WhenSensorDoesNotExist() {
        String sensorName = "NonExistentSensor";
        when(sensorRepository.findByName(sensorName)).thenReturn(Optional.empty());

        Optional<Sensor> result = sensorService.getSensorByName(sensorName);

        assertFalse(result.isPresent());
        verify(sensorRepository, times(1)).findByName(sensorName);
    }
}