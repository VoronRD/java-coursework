package ru.chsu.computer_devices.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.chsu.computer_devices.exception.ExceptionHandler;
import ru.chsu.computer_devices.model.dto.DeviceTypeForm;
import ru.chsu.computer_devices.model.dto.DeviceTypeGrid;
import ru.chsu.computer_devices.service.DeviceTypeService;

import static ru.chsu.computer_devices.ui.view.factory.ComponentFactory.createTextField;

@Route("device-types")
@PageTitle("Типы устройств")
@Menu(order = 3, icon = "vaadin:list", title = "Типы устройств")
public class DeviceTypeView extends BaseCrudView<DeviceTypeGrid, DeviceTypeForm, Long, DeviceTypeService> {

    public DeviceTypeView(DeviceTypeService deviceTypeService, ExceptionHandler exceptionHandler) {
        super(deviceTypeService, exceptionHandler);
        initializeUI("Типы устройств");
    }

    @Override
    protected void configureGrid() {
        grid.addColumn(DeviceTypeGrid::getId).setHeader("ID");
        grid.addColumn(DeviceTypeGrid::getTypeName).setHeader("Тип устройства");
        grid.addColumn(type -> {
            if (type.getDeviceNames() == null || type.getDeviceNames().isEmpty()) {
                return "Нет устройств";
            }
            return String.join(", ", type.getDeviceNames());
        }).setHeader("Устройства");
    }

    @Override
    protected void openDialog(DeviceTypeGrid existingGrid) {
        startDialog(existingGrid, "Создать тип устройства", "Изменить тип устройства");

        TextField typeName = createTextField("Название типа устройства");

        if (isUpdate()) {
            typeName.setValue(existingGrid.getTypeName());
        }

        final Long id = isUpdate() ? existingGrid.getId() : null;

        configureAndRunDialog(
                e -> {
                    try {
                        DeviceTypeForm form = new DeviceTypeForm();
                        form.setTypeName(typeName.getValue());

                        if (id == null) {
                            service.create(form);
                            Notification.show("Тип устройства создан", 3000, Notification.Position.BOTTOM_END);
                        } else {
                            service.update(id, form);
                            Notification.show("Тип устройства изменен", 3000, Notification.Position.BOTTOM_END);
                        }
                        refreshGrid();
                        closeDialog();
                    } catch (Exception ex) {
                        exceptionHandler.handleException(ex);
                    }
                },
                e -> closeDialog(),
                typeName);
    }

    @Override
    protected boolean hasRelatedData(Long id) {
        return service.hasDevices(id);
    }

    @Override
    protected String getRelatedDataInfo(Long id) {
        return "Устройства этого типа:\n" + service.getDevicesList(id);
    }
}