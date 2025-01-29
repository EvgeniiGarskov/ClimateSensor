package ru.garskov.springcourse.ClimateSensorAPI.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.garskov.springcourse.ClimateSensorAPI.models.Measurement;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, Integer> {

    @Query("SELECT COUNT(DISTINCT DATE(m.createdAt)) FROM Measurement m WHERE m.raining = true")
    int rainyDaysCount();
}
