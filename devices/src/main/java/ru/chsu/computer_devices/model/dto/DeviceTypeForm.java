package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class DeviceTypeForm {
    @NotBlank(message = "Введите название типа устройства")
    private String typeName;
}