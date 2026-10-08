package ru.chsu.computer_devices.exception;

public class ReferenceExistException extends RuntimeException {
    public ReferenceExistException(String message) {
        super(message);
    }
}