package ru.chsu.computer_devices.service;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.chsu.computer_devices.exception.ManufacturerExistException;
import ru.chsu.computer_devices.exception.ManufacturerNotFoundException;
import ru.chsu.computer_devices.mapper.ManufacturerMapper;
import ru.chsu.computer_devices.model.dto.ManufacturerForm;
import ru.chsu.computer_devices.model.dto.ManufacturerGrid;
import ru.chsu.computer_devices.model.entity.Device;
import ru.chsu.computer_devices.model.entity.Manufacturer;
import ru.chsu.computer_devices.repository.ManufacturerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Validated
public class ManufacturerService implements CrudService<ManufacturerGrid, ManufacturerForm, Long> {
    private final ManufacturerRepository manufacturerRepository;
    private final ManufacturerMapper manufacturerMapper;

    @Override
    public List<ManufacturerGrid> findAll() {
        return manufacturerRepository.findAll().stream()
                .map(manufacturerMapper::toGrid)
                .toList();
    }

    @Override
    public ManufacturerGrid findById(@NotNull Long id) {
        return manufacturerRepository.findById(id)
                .map(manufacturerMapper::toGrid)
                .orElseThrow(() -> new ManufacturerNotFoundException(id));
    }

    @Transactional
    @Override
    public ManufacturerGrid create(@Valid ManufacturerForm manufacturerForm) {
        validateCompanyName(manufacturerForm.getCompanyName());
        Manufacturer manufacturer = manufacturerMapper.toEntityForm(manufacturerForm);
        return manufacturerMapper.toGrid(manufacturerRepository.save(manufacturer));
    }

    @Transactional
    @Override
    public ManufacturerGrid update(@NotNull Long id, @Valid ManufacturerForm manufacturerForm) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ManufacturerNotFoundException(id));
        if (!manufacturer.getCompanyName().equals(manufacturerForm.getCompanyName())) {
            validateCompanyName(manufacturerForm.getCompanyName());
        }
        manufacturerMapper.updateFromForm(manufacturerForm, manufacturer);
        return manufacturerMapper.toGrid(manufacturerRepository.save(manufacturer));
    }

    @Transactional
    @Override
    public void delete(@NotNull Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ManufacturerNotFoundException(id));
        manufacturerRepository.delete(manufacturer);
    }

    private void validateCompanyName(String companyName) {
        if (manufacturerRepository.existsManufacturerByCompanyName(companyName)) {
            throw new ManufacturerExistException("Производитель с названием " + companyName + " уже существует");
        }
    }

    public boolean hasDevices(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ManufacturerNotFoundException(id));
        return manufacturer.getDevices() != null && !manufacturer.getDevices().isEmpty();
    }

    public String getDevicesList(Long id) {
        Manufacturer manufacturer = manufacturerRepository.findById(id)
                .orElseThrow(() -> new ManufacturerNotFoundException(id));
        if (manufacturer.getDevices() == null || manufacturer.getDevices().isEmpty()) {
            return "Нет устройств";
        }
        return manufacturer.getDevices().stream()
                .map(Device::getModelName)
                .collect(Collectors.joining("\n• ", "• ", ""));
    }
}