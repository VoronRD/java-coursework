package ru.chsu.computer_devices.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.chsu.computer_devices.exception.ReferenceExistException;
import ru.chsu.computer_devices.exception.ReferenceNotFoundException;
import ru.chsu.computer_devices.mapper.ConnectionTypeMapper;
import ru.chsu.computer_devices.model.dto.ConnectionTypeForm;
import ru.chsu.computer_devices.model.dto.ConnectionTypeGrid;
import ru.chsu.computer_devices.model.entity.ConnectionType;
import ru.chsu.computer_devices.model.entity.Device;
import ru.chsu.computer_devices.repository.ConnectionTypeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class ConnectionTypeService implements CrudService<ConnectionTypeGrid, ConnectionTypeForm, Long> {
    private final ConnectionTypeRepository connectionTypeRepository;
    private final ConnectionTypeMapper connectionTypeMapper;


    @Override
    public List<ConnectionTypeGrid> findAll() {
        return connectionTypeRepository.findAll().stream()
                .map(connectionTypeMapper::toGrid)
                .toList();
    }

    @Override
    public ConnectionTypeGrid findById(@NotNull Long id) {
        return connectionTypeRepository.findById(id)
                .map(connectionTypeMapper::toGrid)
                .orElseThrow(() -> new ReferenceNotFoundException("Тип подключения с id: " + id + " не найден"));
    }

    @Transactional
    @Override
    public ConnectionTypeGrid create(@Valid ConnectionTypeForm form) {
        validateTypeName(form.getTypeName());
        ConnectionType entity = connectionTypeMapper.toEntityForm(form);
        return connectionTypeMapper.toGrid(connectionTypeRepository.save(entity));
    }

    @Transactional
    @Override
    public ConnectionTypeGrid update(@NotNull Long id, @Valid ConnectionTypeForm form) {
        ConnectionType entity = connectionTypeRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Тип подключения с id: " + id + " не найден"));
        if (!entity.getTypeName().equals(form.getTypeName())) {
            validateTypeName(form.getTypeName());
        }
        connectionTypeMapper.updateFromForm(form, entity);
        return connectionTypeMapper.toGrid(connectionTypeRepository.save(entity));
    }

    @Transactional
    @Override
    public void delete(@NotNull Long id) {
        if (!connectionTypeRepository.existsById(id)) {
            throw new ReferenceNotFoundException("Тип подключения с id: " + id + " не найден");
        }
        connectionTypeRepository.deleteById(id);
    }

    private void validateTypeName(String typeName) {
        if (connectionTypeRepository.findByTypeName(typeName).isPresent()) {
            throw new ReferenceExistException("Тип подключения '" + typeName + "' уже существует");
        }
    }

    public boolean hasDevices(Long id) {
        ConnectionType connectionType = connectionTypeRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Тип подключения с id: " + id + " не найден"));
        return connectionType.getDevices() != null && !connectionType.getDevices().isEmpty();
    }

    public String getDevicesList(Long id) {
        ConnectionType connectionType = connectionTypeRepository.findById(id)
                .orElseThrow(() -> new ReferenceNotFoundException("Тип подключения с id: " + id + " не найден"));
        if (connectionType.getDevices() == null || connectionType.getDevices().isEmpty()) {
            return "Нет устройств";
        }
        return connectionType.getDevices().stream()
                .map(Device::getModelName)
                .collect(Collectors.joining("\n• ", "• ", ""));
    }
}