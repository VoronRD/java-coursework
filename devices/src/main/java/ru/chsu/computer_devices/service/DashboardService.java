package ru.chsu.computer_devices.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chsu.computer_devices.model.dto.DashboardStats;
import ru.chsu.computer_devices.repository.DeviceRepository;
import ru.chsu.computer_devices.repository.FeedbackRepository;
import ru.chsu.computer_devices.repository.ManufacturerRepository;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final ManufacturerRepository manufacturerRepository;
    private final DeviceRepository deviceRepository;
    private final FeedbackRepository feedbackRepository;

    public DashboardStats getDashboardStats() {
        DashboardStats stats = new DashboardStats();

        stats.setTotalManufacturers(manufacturerRepository.count());
        stats.setTotalDevices(deviceRepository.count());
        stats.setTotalFeedbacks(feedbackRepository.count());

        Double avgRating = feedbackRepository.findAll().stream()
                .mapToInt(f -> f.getRating().getValue())
                .average()
                .orElse(0.0);
        stats.setAverageRating(avgRating);

        stats.setDevicesByManufacturer(getDevicesByManufacturer());
        stats.setDevicesByType(getDevicesByType());
        stats.setDevicesByConnectionType(getDevicesByConnectionType());
        stats.setFeedbacksByRating(getFeedbacksByRating());

        return stats;
    }

    private Map<String, Long> getDevicesByManufacturer() {
        return deviceRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        device -> device.getManufacturer().getCompanyName(),
                        Collectors.counting()
                ));
    }

    private Map<String, Long> getDevicesByType() {
        return deviceRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        device -> device.getDeviceType().getTypeName(),
                        Collectors.counting()
                ));
    }

    private Map<String, Long> getDevicesByConnectionType() {
        return deviceRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        device -> device.getConnectionType().getTypeName(),
                        Collectors.counting()
                ));
    }

    private Map<Integer, Long> getFeedbacksByRating() {
        return feedbackRepository.findAll().stream()
                .collect(Collectors.groupingBy(
                        feedback -> feedback.getRating().getValue(),
                        Collectors.counting()
                ));
    }
}