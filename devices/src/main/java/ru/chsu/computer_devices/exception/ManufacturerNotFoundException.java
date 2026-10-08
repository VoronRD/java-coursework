package ru.chsu.computer_devices.exception;

public class ManufacturerNotFoundException extends RuntimeException {
    public ManufacturerNotFoundException(Long id) {
        super("Производитель с id: " + id + " не найден");
    }

    public ManufacturerNotFoundException(String message) {
        super(message);
    }
}