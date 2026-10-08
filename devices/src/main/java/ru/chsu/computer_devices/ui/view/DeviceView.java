package ru.chsu.computer_devices.ui.view;

import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.chsu.computer_devices.exception.ExceptionHandler;
import ru.chsu.computer_devices.model.dto.*;
import ru.chsu.computer_devices.service.ConnectionTypeService;
import ru.chsu.computer_devices.service.DeviceService;
import ru.chsu.computer_devices.service.DeviceTypeService;
import ru.chsu.computer_devices.service.ManufacturerService;
import ru.chsu.computer_devices.ui.view.component.DeleteDialog;

import static ru.chsu.computer_devices.ui.view.factory.ComponentFactory.*;

@Route("devices")
@PageTitle("Устройства")
@Menu(order = 1, icon = "vaadin:gamepad", title = "Устройства")
public class DeviceView extends BaseCrudView<DeviceGrid, DeviceForm, Long, DeviceService> {
    private final transient ManufacturerService manufacturerService;
    private final transient DeviceTypeService deviceTypeService;
    private final transient ConnectionTypeService connectionTypeService;

    public DeviceView(DeviceService deviceService,
                      ExceptionHandler exceptionHandler,
                      ManufacturerService manufacturerService,
                      DeviceTypeService deviceTypeService,
                      ConnectionTypeService connectionTypeService) {
        super(deviceService, exceptionHandler);
        this.manufacturerService = manufacturerService;
        this.deviceTypeService = deviceTypeService;
        this.connectionTypeService = connectionTypeService;
        initializeUI("Устройства");
    }

    @Override
    protected void configureGrid() {
        grid.addColumn(DeviceGrid::getId).setHeader("ID");
        grid.addColumn(DeviceGrid::getModelName).setHeader("Модель");
        grid.addColumn(DeviceGrid::getManufacturerName).setHeader("Производитель");
        grid.addColumn(DeviceGrid::getDeviceTypeName).setHeader("Тип устройства");
        grid.addColumn(DeviceGrid::getConnectionTypeName).setHeader("Тип подключения");
        grid.addColumn(DeviceGrid::getReleaseDate).setHeader("Дата выпуска");
        grid.addColumn(DeviceGrid::getDescription).setHeader("Описание");
        grid.addColumn(item -> String.format("%.1f ★", item.getAverageRating())).setHeader("Рейтинг");
    }

    @Override
    protected void openDialog(DeviceGrid existingGrid) {
        startDialog(existingGrid, "Создать устройство", "Изменить устройство");

        TextField modelName = createTextField("Название модели");

        ComboBox<String> cbManufacturer = new ComboBox<>("Производитель");
        cbManufacturer.setItems(manufacturerService.findAll().stream()
                .map(ManufacturerGrid::getCompanyName)
                .sorted()
                .toList());
        configureComboBox(cbManufacturer);

        ComboBox<String> cbDeviceType = new ComboBox<>("Тип устройства");
        cbDeviceType.setItems(deviceTypeService.findAll().stream()
                .map(DeviceTypeGrid::getTypeName)
                .sorted()
                .toList());
        configureComboBox(cbDeviceType);

        ComboBox<String> cbConnectionType = new ComboBox<>("Тип подключения");
        cbConnectionType.setItems(connectionTypeService.findAll().stream()
                .map(ConnectionTypeGrid::getTypeName)
                .sorted()
                .toList());
        configureComboBox(cbConnectionType);

        DatePicker releaseDate = createDatePicker("Дата выпуска");
        TextField description = createTextField("Описание");

        if (isUpdate()) {
            modelName.setValue(existingGrid.getModelName());
            cbManufacturer.setValue(existingGrid.getManufacturerName());
            cbDeviceType.setValue(existingGrid.getDeviceTypeName());
            cbConnectionType.setValue(existingGrid.getConnectionTypeName());
            releaseDate.setValue(existingGrid.getReleaseDate());
            description.setValue(existingGrid.getDescription());
        }

        final Long id = isUpdate() ? existingGrid.getId() : null;

        configureAndRunDialog(
                e -> {
                    try {
                        DeviceForm form = new DeviceForm();
                        form.setModelName(modelName.getValue());
                        form.setManufacturerName(cbManufacturer.getValue());
                        form.setDeviceTypeName(cbDeviceType.getValue());
                        form.setConnectionTypeName(cbConnectionType.getValue());
                        form.setReleaseDate(releaseDate.getValue());
                        form.setDescription(description.getValue());

                        if (id == null) {
                            service.create(form);
                            Notification.show("Устройство создано", 3000, Notification.Position.BOTTOM_END);
                        } else {
                            service.update(id, form);
                            Notification.show("Устройство изменено", 3000, Notification.Position.BOTTOM_END);
                        }
                        refreshGrid();
                        closeDialog();
                    } catch (Exception ex) {
                        exceptionHandler.handleException(ex);
                    }
                },
                e -> closeDialog(),
                modelName, cbManufacturer, cbDeviceType, cbConnectionType, releaseDate, description);
    }

    @Override
    protected void deleteWithCheck() {
        if (currentItem == null) {
            Notification.show("Выберите устройство для удаления", 3000, Notification.Position.BOTTOM_END);
            return;
        }

        Long id = currentItem.getId();
        String deviceName = currentItem.getModelName();
        int feedbacksCount = service.getFeedbacksCount(id);

        if (feedbacksCount > 0) {
            String feedbacksList = service.getFeedbacksList(id);
            DeleteDialog.showDeviceDelete(deviceName, feedbacksCount, feedbacksList, () -> {
                try {
                    service.delete(id);
                    refreshGrid();
                    Notification.show("Устройство удалено", 3000, Notification.Position.BOTTOM_END);
                } catch (Exception ex) {
                    exceptionHandler.handleException(ex);
                }
            });
        } else {
            DeleteDialog.show(deviceName, () -> {
                try {
                    service.delete(id);
                    refreshGrid();
                    Notification.show("Устройство удалено", 3000, Notification.Position.BOTTOM_END);
                } catch (Exception ex) {
                    exceptionHandler.handleException(ex);
                }
            });
        }
    }
}