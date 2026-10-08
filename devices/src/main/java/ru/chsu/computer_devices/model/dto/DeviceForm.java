package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DeviceForm {
    @NotBlank(message = "Введите название модели")
    private String modelName;
    @NotBlank(message = "Введите название производителя")
    private String manufacturerName;
    @NotBlank(message = "Введите тип устройства")
    private String deviceTypeName;
    @NotBlank(message = "Введите тип подключения")
    private String connectionTypeName;
    @NotNull(message = "Введите дату выпуска")
    private LocalDate releaseDate;
    @NotBlank(message = "Введите описание")
    private String description;
}