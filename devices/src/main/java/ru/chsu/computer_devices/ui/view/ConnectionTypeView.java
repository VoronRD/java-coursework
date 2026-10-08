package ru.chsu.computer_devices.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.chsu.computer_devices.exception.ExceptionHandler;
import ru.chsu.computer_devices.model.dto.ConnectionTypeForm;
import ru.chsu.computer_devices.model.dto.ConnectionTypeGrid;
import ru.chsu.computer_devices.service.ConnectionTypeService;

import static ru.chsu.computer_devices.ui.view.factory.ComponentFactory.createTextField;

@Route("connection-types")
@PageTitle("Типы подключения")
@Menu(order = 4, icon = "vaadin:plug", title = "Типы подключения")
public class ConnectionTypeView extends BaseCrudView<ConnectionTypeGrid, ConnectionTypeForm, Long, ConnectionTypeService> {

    public ConnectionTypeView(ConnectionTypeService connectionTypeService, ExceptionHandler exceptionHandler) {
        super(connectionTypeService, exceptionHandler);
        initializeUI("Типы подключения");
    }

    @Override
    protected void configureGrid() {
        grid.addColumn(ConnectionTypeGrid::getId).setHeader("ID");
        grid.addColumn(ConnectionTypeGrid::getTypeName).setHeader("Тип подключения");
        grid.addColumn(type -> {
            if (type.getDeviceNames() == null || type.getDeviceNames().isEmpty()) {
                return "Нет устройств";
            }
            return String.join(", ", type.getDeviceNames());
        }).setHeader("Устройства");
    }

    @Override
    protected void openDialog(ConnectionTypeGrid existingGrid) {
        startDialog(existingGrid, "Создать тип подключения", "Изменить тип подключения");

        TextField typeName = createTextField("Название типа подключения");

        if (isUpdate()) {
            typeName.setValue(existingGrid.getTypeName());
        }

        final Long id = isUpdate() ? existingGrid.getId() : null;

        configureAndRunDialog(
                e -> {
                    try {
                        ConnectionTypeForm form = new ConnectionTypeForm();
                        form.setTypeName(typeName.getValue());

                        if (id == null) {
                            service.create(form);
                            Notification.show("Тип подключения создан", 3000, Notification.Position.BOTTOM_END);
                        } else {
                            service.update(id, form);
                            Notification.show("Тип подключения изменен", 3000, Notification.Position.BOTTOM_END);
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
        return "Устройства с этим типом подключения:\n" + service.getDevicesList(id);
    }
}