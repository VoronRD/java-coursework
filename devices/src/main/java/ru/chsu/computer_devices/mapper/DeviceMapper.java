package ru.chsu.computer_devices.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.chsu.computer_devices.model.dto.DeviceForm;
import ru.chsu.computer_devices.model.dto.DeviceGrid;
import ru.chsu.computer_devices.model.entity.Device;

@Mapper(componentModel = "spring")
public interface DeviceMapper {

    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    @Mapping(target = "deviceType", ignore = true)
    @Mapping(target = "connectionType", ignore = true)
    Device toEntityForm(DeviceForm form);

    @Mapping(target = "manufacturerName", source = "manufacturer.companyName")
    @Mapping(target = "deviceTypeName", source = "deviceType.typeName")
    @Mapping(target = "connectionTypeName", source = "connectionType.typeName")
    @Mapping(target = "averageRating", ignore = true)
    DeviceGrid toGrid(Device device);

    @Mapping(target = "feedbacks", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "manufacturer", ignore = true)
    @Mapping(target = "deviceType", ignore = true)
    @Mapping(target = "connectionType", ignore = true)
    void updateFromForm(DeviceForm form, @MappingTarget Device device);
}