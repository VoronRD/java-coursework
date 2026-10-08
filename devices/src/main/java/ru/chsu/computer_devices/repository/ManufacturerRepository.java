package ru.chsu.computer_devices.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.chsu.computer_devices.model.entity.Manufacturer;

import java.util.List;
import java.util.Optional;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    @EntityGraph(attributePaths = {"devices"})
    @NotNull List<Manufacturer> findAll();

    @EntityGraph(attributePaths = {"devices"})
    @NotNull Optional<Manufacturer> findById(@NotNull Long id);

    boolean existsManufacturerByCompanyName(String companyName);

    Optional<Manufacturer> findByCompanyName(@NotNull String companyName);
}