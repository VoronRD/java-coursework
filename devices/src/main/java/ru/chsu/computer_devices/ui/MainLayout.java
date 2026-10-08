package ru.chsu.computer_devices.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.Layout;
import com.vaadin.flow.server.menu.MenuConfiguration;
import com.vaadin.flow.server.menu.MenuEntry;

@Layout
public final class MainLayout extends AppLayout {

    MainLayout() {
        setPrimarySection(Section.DRAWER);
        addToDrawer(createHeader(), new Scroller(createSideNav()));
    }

    private Div createHeader() {
        var appLogo = VaadinIcon.DESKTOP.create();
        appLogo.getStyle().set("color", "var(--lumo-primary-color)");
        appLogo.getStyle().set("width", "32px");
        appLogo.getStyle().set("height", "32px");

        var appName = new Span("Computer Devices");
        appName.getStyle().set("font-weight", "600");
        appName.getStyle().set("font-size", "var(--lumo-font-size-xl)");

        var header = new Div(appLogo, appName);
        header.getStyle()
                .set("display", "flex")
                .set("padding", "var(--lumo-space-m)")
                .set("gap", "var(--lumo-space-m)")
                .set("align-items", "center");
        return header;
    }

    private SideNav createSideNav() {
        var nav = new SideNav();
        nav.getStyle().set("margin", "0 var(--lumo-space-m)");
        MenuConfiguration.getMenuEntries().forEach(entry -> nav.addItem(createSideNavItem(entry)));
        return nav;
    }

    private SideNavItem createSideNavItem(MenuEntry menuEntry) {
        if (menuEntry.icon() != null) {
            return new SideNavItem(menuEntry.title(), menuEntry.path(), new Icon(menuEntry.icon()));
        } else {
            return new SideNavItem(menuEntry.title(), menuEntry.path());
        }
    }
}