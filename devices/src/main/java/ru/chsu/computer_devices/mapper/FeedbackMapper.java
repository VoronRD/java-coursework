package ru.chsu.computer_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.chsu.computer_devices.model.dto.FeedbackForm;
import ru.chsu.computer_devices.model.dto.FeedbackGrid;
import ru.chsu.computer_devices.model.entity.Feedback;

@Mapper(componentModel = "spring")
public interface FeedbackMapper {
    @Mapping(target = "device", ignore = true)
    @Mapping(target = "id", ignore = true)
    Feedback toEntityForm(FeedbackForm feedbackForm);

    @Mapping(target = "deviceName", source = "device.modelName")
    FeedbackGrid toGrid(Feedback feedback);

    @Mapping(target = "device", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromForm(FeedbackForm feedbackForm, @MappingTarget Feedback feedback);
}