
package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ManufacturerGrid implements GridAPI<Long> {
    @NotNull
    private Long id;
    private String companyName;
    private String description;
    private List<String> deviceNames;

    @Override
    public String toString() {
        return companyName;
    }

    @Override
    public List<String> searchableFields() {
        return List.of(
                id == null ? "" : id.toString(),
                companyName == null ? "" : companyName,
                description == null ? "" : description,
                deviceNames == null ? "" : String.join(", ", deviceNames)
        );
    }
}