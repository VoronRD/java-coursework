package ru.chsu.computer_devices.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.chsu.computer_devices.model.entity.Feedback;

import java.util.List;
import java.util.Optional;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
    @NotNull
    @EntityGraph(attributePaths = {"device"})
    List<Feedback> findAll();

    @EntityGraph(attributePaths = {"device"})
    @NotNull Optional<Feedback> findById(@NotNull Long id);

    List<Feedback> findByDeviceId(Long deviceId);

    @Query("SELECT AVG(f.rating) FROM Feedback f WHERE f.device.id = :deviceId")
    Double getAverageRatingByDeviceId(Long deviceId);
}