package ru.chsu.computer_devices.ui.view;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.chsu.computer_devices.exception.ExceptionHandler;
import ru.chsu.computer_devices.model.dto.ManufacturerForm;
import ru.chsu.computer_devices.model.dto.ManufacturerGrid;
import ru.chsu.computer_devices.service.ManufacturerService;

import static ru.chsu.computer_devices.ui.view.factory.ComponentFactory.createTextField;

@Route("manufacturers")
@PageTitle("Производители")
@Menu(order = 0, icon = "vaadin:factory", title = "Производители")
public class ManufacturerView extends BaseCrudView<ManufacturerGrid, ManufacturerForm, Long, ManufacturerService> {

    public ManufacturerView(ManufacturerService manufacturerService, ExceptionHandler exceptionHandler) {
        super(manufacturerService, exceptionHandler);
        initializeUI("Производители");
    }

    @Override
    protected void configureGrid() {
        grid.addColumn(ManufacturerGrid::getId).setHeader("ID");
        grid.addColumn(ManufacturerGrid::getCompanyName).setHeader("Название компании");
        grid.addColumn(ManufacturerGrid::getDescription).setHeader("Описание");
        grid.addColumn(manufacturer -> {
            if (manufacturer.getDeviceNames() == null || manufacturer.getDeviceNames().isEmpty()) {
                return "Нет устройств";
            }
            return String.join(", ", manufacturer.getDeviceNames());
        }).setHeader("Устройства");
    }

    @Override
    protected void openDialog(ManufacturerGrid existingGrid) {
        startDialog(existingGrid, "Создать производителя", "Изменить производителя");

        TextField companyNameField = createTextField("Название компании");
        TextField description = createTextField("Описание компании");

        if (isUpdate()) {
            companyNameField.setValue(existingGrid.getCompanyName());
            description.setValue(existingGrid.getDescription());
        }

        final Long id = isUpdate() ? existingGrid.getId() : null;

        configureAndRunDialog(
                e -> {
                    try {
                        ManufacturerForm form = new ManufacturerForm();
                        form.setCompanyName(companyNameField.getValue());
                        form.setDescription(description.getValue());

                        if (id == null) {
                            service.create(form);
                            Notification.show("Производитель создан", 3000, Notification.Position.BOTTOM_END);
                        } else {
                            service.update(id, form);
                            Notification.show("Производитель изменен", 3000, Notification.Position.BOTTOM_END);
                        }
                        refreshGrid();
                        closeDialog();
                    } catch (Exception ex) {
                        exceptionHandler.handleException(ex);
                    }
                },
                e -> closeDialog(),
                companyNameField, description);
    }

    @Override
    protected boolean hasRelatedData(Long id) {
        return service.hasDevices(id);
    }

    @Override
    protected String getRelatedDataInfo(Long id) {
        return "Устройства этого производителя:\n" + service.getDevicesList(id);
    }
}