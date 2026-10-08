package ru.chsu.computer_devices.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.chsu.computer_devices.exception.ReferenceExistException;
import ru.chsu.computer_devices.exception.ReferenceNotFoundException;
import ru.chsu.computer_devices.mapper.DeviceTypeMapper;
import ru.chsu.computer_devices.model.dto.DeviceTypeForm;
import ru.chsu.computer_devices.model.dto.DeviceTypeGrid;
import ru.chsu.computer_devices.model.entity.Device;
import ru.chsu.computer_devices.model.entity.DeviceType;
import ru.chsu.computer_devices.repository.DeviceTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class DeviceTypeService implements CrudService<DeviceTypeGrid, DeviceTypeForm, Long> {
    private final DeviceTypeRepository deviceTypeRepository;
    private final DeviceTypeMapper deviceTypeMapper;

    @Override
    public List<DeviceTypeGrid> findAll() {
        return deviceTypeRepository.findAll().stream()
                .map(deviceTypeMapper::toGrid)
                .toList();
    }

    @Override
    public DeviceTypeGrid findById(@NotNull Long id) {
        return deviceTypeRepository.findById(id)
                .map(deviceTypeMapper::toGrid)
                .orElseThrow(() -> new ReferenceNotFoundException("Тип устройства с id: " + id + " не найден"));
    }

    @Transactional
    @Override
    public DeviceTypeGrid create(@Valid DeviceTypeForm form) {
        validateTypeName(form.getTypeName());
        DeviceType entity = deviceTypeMapper.toEntityForm(form);
        return deviceTypeMapper.toGrid(deviceTypeRepository.save(entity));
    }

    @Transactional
    @Override
    public DeviceTypeGrid update(@NotNull Long id, @Valid DeviceTypeForm form) {
        DeviceType entity = deviceTypeRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Тип устройства с id: " + id + " не найден"));
        if (!entity.getTypeName().equals(form.getTypeName())) {
            validateTypeName(form.getTypeName());
        }
        deviceTypeMapper.updateFromForm(form, entity);
        return deviceTypeMapper.toGrid(deviceTypeRepository.save(entity));
    }

    @Transactional
    @Override
    public void delete(@NotNull Long id) {
        if (!deviceTypeRepository.existsById(id)) {
            throw new ReferenceNotFoundException("Тип устройства с id: " + id + " не найден");
        }
        deviceTypeRepository.deleteById(id);
    }

    private void validateTypeName(String typeName) {
        if (deviceTypeRepository.findByTypeName(typeName).isPresent()) {
            throw new ReferenceExistException("Тип устройства '" + typeName + "' уже существует");
        }
    }

    public boolean hasDevices(Long id) {
        DeviceType deviceType = deviceTypeRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Тип устройства с id: " + id + " не найден"));
        return deviceType.getDevices() != null && !deviceType.getDevices().isEmpty();
    }

    public String getDevicesList(Long id) {
        DeviceType deviceType = deviceTypeRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Тип устройства с id: " + id + " не найден"));
        if (deviceType.getDevices() == null || deviceType.getDevices().isEmpty()) {
            return "Нет устройств";
        }
        return deviceType.getDevices().stream()
                .map(Device::getModelName)
                .collect(Collectors.joining("\n• ", "• ", ""));
    }
}