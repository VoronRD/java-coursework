package ru.chsu.computer_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.chsu.computer_devices.model.dto.ManufacturerForm;
import ru.chsu.computer_devices.model.dto.ManufacturerGrid;
import ru.chsu.computer_devices.model.entity.Device;
import ru.chsu.computer_devices.model.entity.Manufacturer;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ManufacturerMapper {

    ManufacturerForm toForm(Manufacturer manufacturer);

    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "id", ignore = true)
    Manufacturer toEntityForm(ManufacturerForm manufacturerForm);

    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromForm(ManufacturerForm manufacturerForm, @MappingTarget Manufacturer manufacturer);

    @Mapping(target = "deviceNames", expression = "java(mapToDeviceNames(entity.getDevices()))")
    ManufacturerGrid toGrid(Manufacturer entity);

    default List<String> mapToDeviceNames(List<Device> devices) {
        if (devices == null) {
            return List.of();
        }
        return devices.stream()
                .map(Device::getModelName)
                .filter(name -> name != null && !name.isEmpty())
                .toList();
    }
}