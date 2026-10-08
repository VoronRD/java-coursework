package ru.chsu.computer_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.chsu.computer_devices.model.dto.ConnectionTypeForm;
import ru.chsu.computer_devices.model.dto.ConnectionTypeGrid;
import ru.chsu.computer_devices.model.entity.ConnectionType;
import ru.chsu.computer_devices.model.entity.Device;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ConnectionTypeMapper {

    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "id", ignore = true)
    ConnectionType toEntityForm(ConnectionTypeForm form);

    @Mapping(target = "devices", ignore = true)
    @Mapping(target = "id", ignore = true)
    void updateFromForm(ConnectionTypeForm form, @MappingTarget ConnectionType entity);

    @Mapping(target = "deviceNames", expression = "java(mapToDeviceNames(entity.getDevices()))")
    ConnectionTypeGrid toGrid(ConnectionType entity);

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