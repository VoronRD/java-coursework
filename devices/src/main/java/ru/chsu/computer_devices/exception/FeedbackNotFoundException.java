package ru.chsu.computer_devices.exception;

public class FeedbackNotFoundException extends RuntimeException {
    public FeedbackNotFoundException(Long id) {
        super("Отзыв с id: " + id + " не найден");
    }
}