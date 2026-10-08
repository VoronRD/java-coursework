package ru.chsu.computer_devices.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.chsu.computer_devices.exception.DeviceExistException;
import ru.chsu.computer_devices.exception.DeviceNotFoundException;
import ru.chsu.computer_devices.exception.ManufacturerNotFoundException;
import ru.chsu.computer_devices.exception.ReferenceNotFoundException;
import ru.chsu.computer_devices.mapper.DeviceMapper;
import ru.chsu.computer_devices.model.dto.DeviceForm;
import ru.chsu.computer_devices.model.dto.DeviceGrid;
import ru.chsu.computer_devices.model.entity.ConnectionType;
import ru.chsu.computer_devices.model.entity.Device;
import ru.chsu.computer_devices.model.entity.DeviceType;
import ru.chsu.computer_devices.model.entity.Manufacturer;
import ru.chsu.computer_devices.repository.*;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class DeviceService implements CrudService<DeviceGrid, DeviceForm, Long> {
    private final DeviceRepository deviceRepository;
    private final DeviceMapper deviceMapper;
    private final ManufacturerRepository manufacturerRepository;
    private final DeviceTypeRepository deviceTypeRepository;
    private final ConnectionTypeRepository connectionTypeRepository;
    private final FeedbackRepository feedbackRepository;

    @Override
    public List<DeviceGrid> findAll() {
        List<Device> devices = deviceRepository.findAll();
        return devices.stream()
                .map(device -> {
                    DeviceGrid grid = deviceMapper.toGrid(device);
                    Double avgRating = feedbackRepository.getAverageRatingByDeviceId(device.getId());
                    grid.setAverageRating(avgRating != null ? avgRating : 0.0);
                    return grid;
                })
                .toList();
    }

    @Override
    public DeviceGrid findById(@NotNull Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        DeviceGrid grid = deviceMapper.toGrid(device);
        Double avgRating = feedbackRepository.getAverageRatingByDeviceId(id);
        grid.setAverageRating(avgRating != null ? avgRating : 0.0);
        return grid;
    }

    @Transactional
    @Override
    public DeviceGrid create(@Valid DeviceForm form) {
        validateDeviceName(form.getModelName());

        Manufacturer manufacturer = manufacturerRepository.findByCompanyName(form.getManufacturerName())
                .orElseThrow(() -> new ManufacturerNotFoundException("Производитель: " + form.getManufacturerName() + " не найден"));

        DeviceType deviceType = deviceTypeRepository.findByTypeName(form.getDeviceTypeName())
                .orElseThrow(() -> new ReferenceNotFoundException("Тип устройства: " + form.getDeviceTypeName() + " не найден"));

        ConnectionType connectionType = connectionTypeRepository.findByTypeName(form.getConnectionTypeName())
                .orElseThrow(() -> new ReferenceNotFoundException("Тип подключения: " + form.getConnectionTypeName() + " не найден"));

        Device device = deviceMapper.toEntityForm(form);
        device.setManufacturer(manufacturer);
        device.setDeviceType(deviceType);
        device.setConnectionType(connectionType);

        Device saved = deviceRepository.save(device);
        DeviceGrid grid = deviceMapper.toGrid(saved);
        grid.setAverageRating(0.0);
        return grid;
    }

    @Transactional
    @Override
    public DeviceGrid update(@NotNull Long id, @Valid DeviceForm form) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));

        if (!device.getModelName().equals(form.getModelName())) {
            validateDeviceName(form.getModelName());
        }

        Manufacturer manufacturer = manufacturerRepository.findByCompanyName(form.getManufacturerName())
                .orElseThrow(() -> new ManufacturerNotFoundException("Производитель: " + form.getManufacturerName() + " не найден"));

        DeviceType deviceType = deviceTypeRepository.findByTypeName(form.getDeviceTypeName())
                .orElseThrow(() -> new ReferenceNotFoundException("Тип устройства: " + form.getDeviceTypeName() + " не найден"));

        ConnectionType connectionType = connectionTypeRepository.findByTypeName(form.getConnectionTypeName())
                .orElseThrow(() -> new ReferenceNotFoundException("Тип подключения: " + form.getConnectionTypeName() + " не найден"));

        device.setManufacturer(manufacturer);
        device.setDeviceType(deviceType);
        device.setConnectionType(connectionType);
        deviceMapper.updateFromForm(form, device);

        Device saved = deviceRepository.save(device);
        DeviceGrid grid = deviceMapper.toGrid(saved);
        Double avgRating = feedbackRepository.getAverageRatingByDeviceId(id);
        grid.setAverageRating(avgRating != null ? avgRating : 0.0);
        return grid;
    }

    @Transactional
    @Override
    public void delete(@NotNull Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        deviceRepository.delete(device);
    }

    private void validateDeviceName(String modelName) {
        if (deviceRepository.existsDeviceByModelName(modelName)) {
            throw new DeviceExistException("Устройство с названием '" + modelName + "' уже существует");
        }
    }

    // Методы для проверки связей с отзывами
    public boolean hasFeedbacks(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return device.getFeedbacks() != null && !device.getFeedbacks().isEmpty();
    }

    public int getFeedbacksCount(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        return device.getFeedbacks() != null ? device.getFeedbacks().size() : 0;
    }

    public String getFeedbacksList(Long id) {
        Device device = deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException(id));
        if (device.getFeedbacks() == null || device.getFeedbacks().isEmpty()) {
            return "Нет отзывов";
        }
        return device.getFeedbacks().stream()
                .limit(5)
                .map(f -> "• " + f.getAuthor() + ": " +
                        (f.getComment().length() > 50 ? f.getComment().substring(0, 50) + "..." : f.getComment()))
                .collect(Collectors.joining("\n"));
    }
}