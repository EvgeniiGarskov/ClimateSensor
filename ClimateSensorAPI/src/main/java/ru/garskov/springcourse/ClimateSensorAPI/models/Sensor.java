package ru.garskov.springcourse.ClimateSensorAPI.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "Sensor")
public class Sensor {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    @NotEmpty(message = "Название сенсора не должно быть пустым!")
    @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!")
    private String name;

    @Column(name = "created_at")
    @NotNull
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "sensor")
    @JsonIgnore // Игнорируем это поле для предотвращения бесконечной рекурсии
    private List<Measurement> measurements;

    public Sensor() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotEmpty(message = "Название сенсора не должно быть пустым!") @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!") String getName() {
        return name;
    }

    public void setName(@NotEmpty(message = "Название сенсора не должно быть пустым!") @Size(min = 3, max = 30, message = "Название сенсора должно быть от 3 до 30 символов!") String name) {
        this.name = name;
    }

    public @NotNull LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(@NotNull LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }
}
