package ru.garskov.springcourse.ClimateSensorAPI.repositories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ru.garskov.springcourse.ClimateSensorAPI.models.Measurement;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Используем, чтобы избежать запуска встроенной базы данных
public class MeasurementRepositoryTest {

    @Autowired
    private MeasurementRepository measurementRepository;

    @Autowired
    private SensorRepository sensorRepository;

    @BeforeEach
    public void setUp() {
        measurementRepository.deleteAll();
        Sensor sensor = createSensor();

        createMeasurement(15.3, true, sensor, LocalDateTime.of(2025, 1, 1, 12, 0));
        createMeasurement(20.5, true, sensor, LocalDateTime.of(2025, 1, 1, 15, 0));
        createMeasurement(-20.5, false, sensor, LocalDateTime.of(2025, 1, 2, 12, 0));
        createMeasurement(-10.7, true, sensor, LocalDateTime.of(2025, 1, 3, 12, 0));
    }

    private Sensor createSensor() {
        Sensor sensor = new Sensor();
        sensor.setName("TemperatureSensor2");
        sensor.setCreatedAt(LocalDateTime.now());
        return sensorRepository.save(sensor);
    }

    private Measurement createMeasurement(double value, boolean raining, Sensor sensor, LocalDateTime createdAt) {
        Measurement measurement = new Measurement();
        measurement.setValue(value);
        measurement.setRaining(raining);
        measurement.setSensor(sensor);
        measurement.setCreatedAt(createdAt);
        return measurementRepository.save(measurement);
    }

    @Test
    @Rollback // Откат транзакции после теста
    public void shouldCountRainyDays() {
        // Проверка, что данные были созданы
        assertThat(measurementRepository.count()).isEqualTo(4);

        // Проверка, что метод rainyDaysCount() возвращает правильное количество дождливых дней
        int count = measurementRepository.rainyDaysCount();
        assertThat(count).isEqualTo(2); // Ожидаем 2 дождливых дня (1 и 3 января)
    }
}