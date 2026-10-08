package ru.chsu.computer_devices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.component.page.AppShellConfigurator;

@SpringBootApplication
@Theme("default")
public class ComputerDevicesApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(ComputerDevicesApplication.class, args);
    }
}