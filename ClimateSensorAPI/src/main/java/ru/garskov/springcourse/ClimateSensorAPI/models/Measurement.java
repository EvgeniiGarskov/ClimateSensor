package ru.garskov.springcourse.ClimateSensorAPI.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "Measurement")
public class Measurement {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "value")
    @NotNull(message = "Значение не должно быть пустым!")
    @Min(value = -100, message = "Значение должно быть не меньше -100!")
    @Max(value = 100, message = "Значение должно быть не больше 100!")
    private Double value;

    @Column(name = "raining")
    @NotNull(message = "Значение не должно быть пустым!")
    private Boolean raining;

    @NotNull(message = "Значение не должно быть пустым!")
    @ManyToOne
    @JoinColumn(name = "sensor_name", referencedColumnName = "name")
    private Sensor sensor;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

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

    public @NotNull LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
