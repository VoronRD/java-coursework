package ru.chsu.computer_devices.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.chsu.computer_devices.model.Rating;

import java.time.LocalDate;
import java.util.List;

@Data
public class FeedbackGrid implements GridAPI<Long> {
    @NotNull
    private Long id;
    private String deviceName;
    private String author;
    private String comment;
    private Rating rating;
    private LocalDate date;

    @Override
    public String toString() {
        return author + " - " + deviceName;
    }

    @Override
    public List<String> searchableFields() {
        return List.of(
                id == null ? "" : id.toString(),
                deviceName == null ? "" : deviceName,
                author == null ? "" : author,
                comment == null ? "" : comment,
                rating == null ? "" : rating.getStars(),
                date == null ? "" : date.toString()
        );
    }
}