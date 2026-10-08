package ru.chsu.computer_devices.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.chsu.computer_devices.model.entity.ConnectionType;

import java.util.List;
import java.util.Optional;

public interface ConnectionTypeRepository extends JpaRepository<ConnectionType, Long> {

    @NotNull
    @EntityGraph(attributePaths = {"devices"})
    List<ConnectionType> findAll();

    @EntityGraph(attributePaths = {"devices"})
    @NotNull Optional<ConnectionType> findById(@NotNull Long id);

    Optional<ConnectionType> findByTypeName(String typeName);
}