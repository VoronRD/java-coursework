package ru.chsu.computer_devices.exception;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Component
public class ExceptionHandler {

    public void handleException(Exception ex) {
        if (UI.getCurrent() == null) return;
        UI.getCurrent().access(() -> {
            String message = ex.getMessage();

            if (ex instanceof ManufacturerExistException ||
                    ex instanceof DeviceExistException ||
                    ex instanceof ReferenceExistException) {
                Notification.show(message, 5000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_WARNING);

            } else if (ex instanceof ManufacturerNotFoundException ||
                    ex instanceof DeviceNotFoundException ||
                    ex instanceof FeedbackNotFoundException ||
                    ex instanceof ReferenceNotFoundException) {
                Notification.show(message, 5000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_WARNING);

            } else if (ex instanceof MethodArgumentNotValidException) {
                Notification.show("Ошибка валидации: " + message, 5000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);

            } else {
                Notification.show("Произошла ошибка: " + message, 5000, Notification.Position.BOTTOM_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
    }
}