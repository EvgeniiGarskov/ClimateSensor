package ru.garskov.springcourse.ClimateSensorAPI.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.garskov.springcourse.ClimateSensorAPI.models.Measurement;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;
import ru.garskov.springcourse.ClimateSensorAPI.repositories.MeasurementRepository;
import ru.garskov.springcourse.ClimateSensorAPI.util.SensorException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MeasurementServiceTest {

    @InjectMocks
    private MeasurementService measurementService;

    @Mock
    private MeasurementRepository measurementRepository;

    @Mock
    private SensorService sensorService;

    private Measurement measurement;
    private Sensor sensor;

    @BeforeEach
    public void setUp() {
        sensor = new Sensor();
        sensor.setName("TestSensor");

        measurement = new Measurement();
        measurement.setSensor(sensor);
    }

    @Test
    public void shouldSaveMeasurement() {
        when(sensorService.getSensorByName(sensor.getName())).thenReturn(Optional.of(sensor));

        measurementService.save(measurement);

        assertNotNull(measurement.getCreatedAt());
        assertEquals(sensor, measurement.getSensor());
        verify(measurementRepository, times(1)).save(measurement);
    }

    @Test
    public void shouldFindAllMeasurements() {
        when(measurementRepository.findAll()).thenReturn(Collections.singletonList(measurement));

        List<Measurement> measurements = measurementService.findAll();

        assertEquals(1, measurements.size());
        assertEquals(measurement, measurements.get(0));
        verify(measurementRepository, times(1)).findAll();
    }

    @Test
    public void shouldCountRainyDays() {
        when(measurementRepository.rainyDaysCount()).thenReturn(5);

        int count = measurementService.rainyDaysCount();

        assertEquals(5, count);
        verify(measurementRepository, times(1)).rainyDaysCount();
    }
}