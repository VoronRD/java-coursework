package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DeviceGrid implements GridAPI<Long> {
    @NotNull
    private Long id;
    private String modelName;
    private String manufacturerName;
    private String deviceTypeName;
    private String connectionTypeName;
    private LocalDate releaseDate;
    private String description;
    private Double averageRating;

    @Override
    public String toString() {
        return modelName;
    }

    @Override
    public List<String> searchableFields() {
        return List.of(
                id == null ? "" : id.toString(),
                modelName == null ? "" : modelName,
                manufacturerName == null ? "" : manufacturerName,
                deviceTypeName == null ? "" : deviceTypeName,
                connectionTypeName == null ? "" : connectionTypeName,
                releaseDate == null ? "" : releaseDate.toString(),
                description == null ? "" : description
        );
    }
}