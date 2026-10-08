package ru.chsu.computer_devices.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.chsu.computer_devices.model.entity.DeviceType;

import java.util.List;
import java.util.Optional;

public interface DeviceTypeRepository extends JpaRepository<DeviceType, Long> {

    @NotNull
    @EntityGraph(attributePaths = {"devices"})
    List<DeviceType> findAll();

    @EntityGraph(attributePaths = {"devices"})
    @NotNull Optional<DeviceType> findById(@NotNull Long id);

    Optional<DeviceType> findByTypeName(String typeName);
}