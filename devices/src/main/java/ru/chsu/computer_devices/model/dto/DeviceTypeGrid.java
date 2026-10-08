package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class DeviceTypeGrid implements GridAPI<Long> {
    @NotNull
    private Long id;
    private String typeName;
    private List<String> deviceNames;

    @Override
    public String toString() {
        return typeName;
    }

    @Override
    public List<String> searchableFields() {
        return List.of(
                id == null ? "" : id.toString(),
                typeName == null ? "" : typeName,
                deviceNames == null ? "" : String.join(", ", deviceNames)
        );
    }
}