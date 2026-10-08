package ru.chsu.computer_devices.exception;

public class ManufacturerExistException extends RuntimeException {
    public ManufacturerExistException(String message) {
        super(message);
    }
}