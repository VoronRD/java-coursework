package ru.chsu.computer_devices.ui.view;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.chsu.computer_devices.exception.ExceptionHandler;
import ru.chsu.computer_devices.model.Rating;
import ru.chsu.computer_devices.model.dto.DeviceGrid;
import ru.chsu.computer_devices.model.dto.FeedbackForm;
import ru.chsu.computer_devices.model.dto.FeedbackGrid;
import ru.chsu.computer_devices.service.DeviceService;
import ru.chsu.computer_devices.service.FeedbackService;

import static ru.chsu.computer_devices.ui.view.factory.ComponentFactory.*;

@Route("feedbacks")
@PageTitle("Отзывы")
@Menu(order = 2, icon = "vaadin:comment", title = "Отзывы")
public class FeedbackView extends BaseCrudView<FeedbackGrid, FeedbackForm, Long, FeedbackService> {
    private final transient DeviceService deviceService;

    public FeedbackView(FeedbackService feedbackService,
                        ExceptionHandler exceptionHandler,
                        DeviceService deviceService) {
        super(feedbackService, exceptionHandler);
        this.deviceService = deviceService;
        initializeUI("Отзывы");
    }

    @Override
    protected void configureGrid() {
        grid.addColumn(FeedbackGrid::getId).setHeader("ID");
        grid.addColumn(FeedbackGrid::getDeviceName).setHeader("Устройство");
        grid.addColumn(FeedbackGrid::getAuthor).setHeader("Автор");
        grid.addColumn(FeedbackGrid::getComment).setHeader("Комментарий");
        grid.addColumn(item -> item.getRating().getStars()).setHeader("Рейтинг");
        grid.addColumn(FeedbackGrid::getDate).setHeader("Дата");
    }

    @Override
    protected void openDialog(FeedbackGrid existingGrid) {
        startDialog(existingGrid, "Создать отзыв", "Изменить отзыв");

        ComboBox<String> cbDevice = new ComboBox<>("Устройство");
        cbDevice.setItems(deviceService.findAll().stream()
                .map(DeviceGrid::getModelName)
                .sorted()
                .toList());
        configureComboBox(cbDevice);

        TextField author = createTextField("Автор");
        TextArea comment = new TextArea("Комментарий");
        comment.setWidthFull();
        comment.setRequired(true);
        comment.setHeight("150px");

        ComboBox<Rating> cbRating = new ComboBox<>("Рейтинг");
        cbRating.setItems(Rating.values());
        cbRating.setItemLabelGenerator(Rating::getStars);
        configureComboBox(cbRating);

        DatePicker date = createDatePicker("Дата");

        if (isUpdate()) {
            cbDevice.setValue(existingGrid.getDeviceName());
            author.setValue(existingGrid.getAuthor());
            comment.setValue(existingGrid.getComment());
            cbRating.setValue(existingGrid.getRating());
            date.setValue(existingGrid.getDate());
        }

        final Long id = isUpdate() ? existingGrid.getId() : null;

        configureAndRunDialog(
                e -> {
                    try {
                        FeedbackForm form = new FeedbackForm();
                        form.setDeviceName(cbDevice.getValue());
                        form.setAuthor(author.getValue());
                        form.setComment(comment.getValue());
                        form.setRating(cbRating.getValue().getValue());
                        form.setDate(date.getValue());

                        if (id == null) {
                            service.create(form);
                            Notification.show("Отзыв создан", 3000, Notification.Position.BOTTOM_END);
                        } else {
                            service.update(id, form);
                            Notification.show("Отзыв изменен", 3000, Notification.Position.BOTTOM_END);
                        }
                        refreshGrid();
                        closeDialog();
                    } catch (Exception ex) {
                        exceptionHandler.handleException(ex);
                    }
                },
                e -> closeDialog(),
                cbDevice, author, comment, cbRating, date);
    }
}