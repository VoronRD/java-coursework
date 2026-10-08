package ru.chsu.computer_devices.exception;

public class DeviceNotFoundException extends RuntimeException {
    public DeviceNotFoundException(Long id) {
        super("Устройство с id: " + id + " не найдено");
    }

    public DeviceNotFoundException(String message) {
        super(message);
    }
}