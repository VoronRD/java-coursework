package ru.chsu.computer_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.chsu.computer_devices.model.dto.DeviceTypeForm;
import ru.chsu.computer_devices.model.dto.DeviceTypeGrid;
import ru.chsu.computer_devices.model.entity.Device;
import ru.chsu.computer_devices.model.entity.DeviceType;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DeviceTypeMapper {

    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "id", ignore = true)
    DeviceType toEntityForm(DeviceTypeForm form);

    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromForm(DeviceTypeForm form, @MappingTarget DeviceType entity);

    @Mapping(target = "deviceNames", expression = "java(mapToDeviceNames(entity.getDevices()))")
    DeviceTypeGrid toGrid(DeviceType entity);

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