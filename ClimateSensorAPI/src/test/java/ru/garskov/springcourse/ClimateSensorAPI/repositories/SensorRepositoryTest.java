package ru.garskov.springcourse.ClimateSensorAPI.repositories;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) //Используем, чтобы избежать запуска встроенной базы данных
class SensorRepositoryTest {

    @Autowired
    private SensorRepository sensorRepository;

    @Test
    @Rollback() // Откат транзакции после теста
    public void shouldFindSensorByName() {
        Sensor sensor = new Sensor();
        sensor.setName("TemperatureSensor3");
        sensor.setCreatedAt(LocalDateTime.now());
        sensorRepository.save(sensor);

        Optional<Sensor> foundSensor = sensorRepository.findByName("TemperatureSensor3");

        assertThat(foundSensor).isPresent();
        assertThat(foundSensor.get().getName()).isEqualTo("TemperatureSensor3");
    }

    @Test
    public void shouldNotFindSensorByName_WhenSensorDoesNotExist() {
        Optional<Sensor> foundSensor = sensorRepository.findByName("NonExistentSensor3");

        assertThat(foundSensor).isNotPresent();
    }
}