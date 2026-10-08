package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class FeedbackForm {
    @NotBlank(message = "Введите название устройства")
    private String deviceName;
    @NotBlank(message = "Введите ваше имя")
    private String author;
    @NotBlank(message = "Введите комментарий")
    private String comment;
    @NotNull(message = "Введите рейтинг")
    @Min(value = 1, message = "Рейтинг должен быть от 1 до 5")
    @Max(value = 5, message = "Рейтинг должен быть от 1 до 5")
    private Integer rating;
    @NotNull(message = "Введите дату")
    private LocalDate date;
}