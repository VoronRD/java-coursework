package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ConnectionTypeForm {
    @NotBlank(message = "Введите название типа подключения")
    private String typeName;
}