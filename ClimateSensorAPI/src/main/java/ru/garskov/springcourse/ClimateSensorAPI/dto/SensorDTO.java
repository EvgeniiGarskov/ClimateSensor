package ru.garskov.springcourse.ClimateSensorAPI.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import ru.garskov.springcourse.ClimateSensorAPI.models.Measurement;

import java.util.List;

public class SensorDTO {
    @NotEmpty(message = "Название сенсора не должно быть пустым!")
    @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!")
    private String name;

    private List<Measurement> measurements;

    public @NotEmpty(message = "Название сенсора не должно быть пустым!") @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Название сенсора не должно быть пустым!") @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!") String name) {
        this.name = name;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
