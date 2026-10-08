package ru.chsu.computer_devices.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.chsu.computer_devices.exception.DeviceNotFoundException;
import ru.chsu.computer_devices.exception.FeedbackNotFoundException;
import ru.chsu.computer_devices.mapper.FeedbackMapper;
import ru.chsu.computer_devices.model.Rating;
import ru.chsu.computer_devices.model.dto.FeedbackForm;
import ru.chsu.computer_devices.model.dto.FeedbackGrid;
import ru.chsu.computer_devices.model.entity.Device;
import ru.chsu.computer_devices.model.entity.Feedback;
import ru.chsu.computer_devices.repository.DeviceRepository;
import ru.chsu.computer_devices.repository.FeedbackRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Validated
public class FeedbackService implements CrudService<FeedbackGrid, FeedbackForm, Long> {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final DeviceRepository deviceRepository;

    @Override
    public List<FeedbackGrid> findAll() {
        return feedbackRepository.findAll().stream()
                .map(feedbackMapper::toGrid)
                .toList();
    }

    @Override
    public FeedbackGrid findById(@NotNull Long id) {
        return feedbackRepository.findById(id)
                .map(feedbackMapper::toGrid)
                .orElseThrow(() -> new FeedbackNotFoundException(id));
    }

    @Transactional
    @Override
    public FeedbackGrid create(@Valid FeedbackForm feedbackForm) {
        Device device = validateDeviceName(feedbackForm.getDeviceName());
        Feedback feedback = feedbackMapper.toEntityForm(feedbackForm);
        feedback.setDevice(device);
        feedback.setRating(Rating.fromValue(feedbackForm.getRating()));
        return feedbackMapper.toGrid(feedbackRepository.save(feedback));
    }

    @Transactional
    @Override
    public FeedbackGrid update(@NotNull Long id, @Valid FeedbackForm feedbackForm) {
        Device device = validateDeviceName(feedbackForm.getDeviceName());
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));

        if (!feedback.getDevice().getId().equals(device.getId())) {
            feedback.setDevice(device);
        }

        feedbackMapper.updateFromForm(feedbackForm, feedback);
        feedback.setRating(Rating.fromValue(feedbackForm.getRating()));

        return feedbackMapper.toGrid(feedbackRepository.save(feedback));
    }

    @Transactional
    @Override
    public void delete(@NotNull Long id) {
        Feedback feedback = feedbackRepository.findById(id)
                .orElseThrow(() -> new FeedbackNotFoundException(id));
        feedbackRepository.delete(feedback);
    }

    private Device validateDeviceName(String deviceName) {
        return deviceRepository.findByModelName(deviceName)
                .orElseThrow(() -> new DeviceNotFoundException("Устройство с названием: " + deviceName + " не найдено"));
    }
}