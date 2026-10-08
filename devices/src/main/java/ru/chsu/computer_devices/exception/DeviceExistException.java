package ru.chsu.computer_devices.exception;

public class DeviceExistException extends RuntimeException {
    public DeviceExistException(String message) {
        super(message);
    }
}