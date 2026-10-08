package ru.chsu.computer_devices.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class DashboardStats {
    private Long totalManufacturers;
    private Long totalDevices;
    private Long totalFeedbacks;
    private Double averageRating;

    private Map<String, Long> devicesByManufacturer;
    private Map<String, Long> devicesByType;
    private Map<String, Long> devicesByConnectionType;
    private Map<Integer, Long> feedbacksByRating;
}