package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ManufacturerForm {
    @NotBlank(message = "Введите название компании")
    private String companyName;
    @NotBlank(message = "Введите описание компании")
    private String description;
}