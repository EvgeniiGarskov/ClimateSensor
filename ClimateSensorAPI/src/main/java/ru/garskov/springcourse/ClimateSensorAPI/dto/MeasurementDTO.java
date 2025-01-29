package ru.garskov.springcourse.ClimateSensorAPI.dto;

import jakarta.validation.constraints.*;
import ru.garskov.springcourse.ClimateSensorAPI.models.Sensor;

public class MeasurementDTO {

    @NotNull(message = "Значение не должно быть пустым!")
    @Min(value = -100, message = "Значение должно быть не меньше -100!")
    @Max(value = 100, message = "Значение должно быть не больше 100!")
    private Double value;

    @NotNull(message = "Значение не должно быть пустым!")
    private Boolean raining;

    @NotNull(message = "Значение не должно быть пустым!")
    private Sensor sensor;

    public @NotNull(message = "Значение не должно быть пустым!") @Min(value = -100, message = "Значение должно быть не меньше -100!") @Max(value = 100, message = "Значение должно быть не больше 100!") Double getValue() {
        return value;
    }

    public void setValue(@NotNull(message = "Значение не должно быть пустым!") @Min(value = -100, message = "Значение должно быть не меньше -100!") @Max(value = 100, message = "Значение должно быть не больше 100!") Double value) {
        this.value = value;
    }

    public @NotNull(message = "Значение не должно быть пустым!") Boolean getRaining() {
        return raining;
    }

    public void setRaining(@NotNull(message = "Значение не должно быть пустым!") Boolean raining) {
        this.raining = raining;
    }

    public @NotNull(message = "Значение не должно быть пустым!") Sensor getSensor() {
        return sensor;
    }

    public void setSensor(@NotNull(message = "Значение не должно быть пустым!") Sensor sensor) {
        this.sensor = sensor;
    }
}

