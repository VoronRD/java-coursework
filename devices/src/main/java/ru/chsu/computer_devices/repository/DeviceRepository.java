package ru.chsu.computer_devices.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.chsu.computer_devices.model.entity.Device;

import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    @NotNull
    @EntityGraph(attributePaths = {"manufacturer", "deviceType", "connectionType", "feedbacks"})
    List<Device> findAll();

    @EntityGraph(attributePaths = {"manufacturer", "deviceType", "connectionType", "feedbacks"})
    @NotNull Optional<Device> findById(@NotNull Long id);

    boolean existsDeviceByModelName(String modelName);

    Optional<Device> findByModelName(@NotNull String modelName);
}