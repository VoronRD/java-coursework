package ru.chsu.computer_devices.ui.view.component;

import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H4;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class DeleteDialog {

    public static void show(String itemName, Runnable onConfirm) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Подтверждение удаления");
        confirmDialog.setText("Вы уверены, что хотите удалить \"" + itemName + "\"?");
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Отмена");
        confirmDialog.setConfirmText("Удалить");
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.addConfirmListener(e -> onConfirm.run());
        confirmDialog.open();
    }

    public static void showWithCheck(String itemName, boolean hasRelations, String relationsInfo, Runnable onConfirm) {
        if (!hasRelations) {
            show(itemName, onConfirm);
            return;
        }

        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Удаление с зависимостями");

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(false);

        Span warningSpan = new Span("Удаление затронет связанные данные:");
        warningSpan.getStyle().set("color", "var(--lumo-error-text-color)");
        warningSpan.getStyle().set("font-weight", "bold");

        Div relationsDiv = new Div();
        relationsDiv.setText(relationsInfo);
        relationsDiv.getStyle().set("background", "var(--lumo-contrast-5pct)");
        relationsDiv.getStyle().set("padding", "var(--lumo-space-m)");
        relationsDiv.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
        relationsDiv.getStyle().set("font-family", "monospace");
        relationsDiv.getStyle().set("font-size", "var(--lumo-font-size-s)");
        relationsDiv.getStyle().set("white-space", "pre-wrap");

        Span confirmWarning = new Span("Вы уверены, что хотите продолжить? Это действие нельзя отменить.");
        confirmWarning.getStyle().set("color", "var(--lumo-error-text-color)");

        content.add(warningSpan, relationsDiv, confirmWarning);

        confirmDialog.add(content);
        confirmDialog.setConfirmText("Удалить всё равно");
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Отмена");

        confirmDialog.addConfirmListener(e -> onConfirm.run());
        confirmDialog.open();
    }

    public static void showDeviceDelete(String deviceName, int feedbacksCount, String feedbacksPreview, Runnable onConfirm) {
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setHeader("Удаление устройства");

        VerticalLayout content = new VerticalLayout();
        content.setSpacing(true);
        content.setPadding(false);

        Span deviceSpan = new Span("Устройство: " + deviceName);
        deviceSpan.getStyle().set("font-weight", "bold");

        Span countSpan = new Span("Количество отзывов: " + feedbacksCount);

        Div feedbacksDiv = new Div();
        if (feedbacksCount > 0) {
            feedbacksDiv.add(new H4("Отзывы:"));
            Span previewSpan = new Span(feedbacksPreview);
            previewSpan.getStyle().set("font-size", "var(--lumo-font-size-s)");
            previewSpan.getStyle().set("white-space", "pre-wrap");
            feedbacksDiv.add(previewSpan);
            feedbacksDiv.getStyle().set("background", "var(--lumo-contrast-5pct)");
            feedbacksDiv.getStyle().set("padding", "var(--lumo-space-m)");
            feedbacksDiv.getStyle().set("border-radius", "var(--lumo-border-radius-m)");
        }

        Span warning = new Span("Все отзывы об этом устройстве будут удалены!");
        warning.getStyle().set("color", "var(--lumo-error-text-color)");
        warning.getStyle().set("font-weight", "bold");

        Span confirmWarning = new Span("Продолжить удаление?");
        confirmWarning.getStyle().set("color", "var(--lumo-error-text-color)");

        content.add(deviceSpan, countSpan);
        if (feedbacksCount > 0) {
            content.add(feedbacksDiv);
        }
        content.add(warning, confirmWarning);

        confirmDialog.add(content);
        confirmDialog.setConfirmText("Удалить устройство и отзывы");
        confirmDialog.setConfirmButtonTheme("error primary");
        confirmDialog.setCancelable(true);
        confirmDialog.setCancelText("Отмена");

        confirmDialog.addConfirmListener(e -> onConfirm.run());
        confirmDialog.open();
    }
}