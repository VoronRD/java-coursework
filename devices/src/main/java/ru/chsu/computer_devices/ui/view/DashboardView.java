package ru.chsu.computer_devices.ui.view;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.*;
import com.vaadin.flow.component.dashboard.Dashboard;
import com.vaadin.flow.component.dashboard.DashboardSection;
import com.vaadin.flow.component.dashboard.DashboardWidget;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import ru.chsu.computer_devices.model.dto.DashboardStats;
import ru.chsu.computer_devices.service.DashboardService;

import java.util.Map;

@Route("")
@PageTitle("Дашборд")
@Menu(order = -1, icon = "vaadin:dashboard", title = "Дашборд")
public class DashboardView extends Main {

    private final DashboardService dashboardService;

    public DashboardView(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
        initializeUI();
    }

    private void initializeUI() {
        setSizeFull();
        getStyle().set("padding", "var(--lumo-space-l)");

        DashboardStats stats = dashboardService.getDashboardStats();

        Dashboard dashboard = new Dashboard();
        dashboard.setSizeFull();

        DashboardSection kpiSection = dashboard.addSection("Основные метрики");

        DashboardWidget manufacturersWidget = new DashboardWidget("Производители");
        manufacturersWidget.setContent(createKpiCard("Производители", stats.getTotalManufacturers(), VaadinIcon.BUILDING));
        kpiSection.add(manufacturersWidget);

        DashboardWidget devicesWidget = new DashboardWidget("Устройства");
        devicesWidget.setContent(createKpiCard("Устройства", stats.getTotalDevices(), VaadinIcon.GAMEPAD));
        kpiSection.add(devicesWidget);

        DashboardWidget feedbacksWidget = new DashboardWidget("Отзывы");
        feedbacksWidget.setContent(createKpiCard("Отзывы", stats.getTotalFeedbacks(), VaadinIcon.COMMENT));
        kpiSection.add(feedbacksWidget);

        DashboardWidget ratingWidget = new DashboardWidget("Средний рейтинг");
        ratingWidget.setContent(createKpiCard("Средний рейтинг", String.format("%.1f ★", stats.getAverageRating()), VaadinIcon.STAR));
        kpiSection.add(ratingWidget);

        DashboardSection chartsSection = dashboard.addSection("Аналитика");

        DashboardWidget devicesByManufacturerWidget = new DashboardWidget("Устройства по производителям");
        devicesByManufacturerWidget.setContent(createPieChart("Устройства по производителям", stats.getDevicesByManufacturer()));
        chartsSection.add(devicesByManufacturerWidget);

        DashboardWidget devicesByTypeWidget = new DashboardWidget("Устройства по типам");
        devicesByTypeWidget.setContent(createPieChart("Устройства по типам", stats.getDevicesByType()));
        chartsSection.add(devicesByTypeWidget);

        DashboardWidget devicesByConnectionWidget = new DashboardWidget("Устройства по типу подключения");
        devicesByConnectionWidget.setContent(createColumnChart("Устройства по типу подключения", stats.getDevicesByConnectionType()));
        chartsSection.add(devicesByConnectionWidget);

        DashboardWidget feedbacksByRatingWidget = new DashboardWidget("Отзывы по рейтингу");
        feedbacksByRatingWidget.setContent(createBarChart("Отзывы по рейтингу", stats.getFeedbacksByRating()));
        chartsSection.add(feedbacksByRatingWidget);

        add(dashboard);
    }

    private Div createKpiCard(String title, Object value, VaadinIcon icon) {
        Div card = new Div();
        card.getStyle()
                .set("background", "var(--lumo-contrast-5pct)")
                .set("padding", "var(--lumo-space-l)")
                .set("border-radius", "var(--lumo-border-radius-l)")
                .set("display", "flex")
                .set("flex-direction", "column")
                .set("align-items", "center")
                .set("gap", "var(--lumo-space-m)");

        Icon cardIcon = icon.create();
        cardIcon.getStyle()
                .set("width", "32px")
                .set("height", "32px")
                .set("color", "var(--lumo-primary-color)");

        Span valueSpan = new Span(String.valueOf(value));
        valueSpan.getStyle()
                .set("font-size", "var(--lumo-font-size-xxxl)")
                .set("font-weight", "bold")
                .set("color", "var(--lumo-primary-color)");

        Span titleSpan = new Span(title);
        titleSpan.getStyle()
                .set("font-size", "var(--lumo-font-size-s)")
                .set("color", "var(--lumo-secondary-text-color)");

        card.add(cardIcon, valueSpan, titleSpan);
        return card;
    }

    private Div createChartContainer(Chart chart) {
        Div container = new Div();
        container.getStyle()
                .set("background", "var(--lumo-contrast-5pct)")
                .set("padding", "var(--lumo-space-m)")
                .set("border-radius", "var(--lumo-border-radius-l)");
        chart.setWidth("100%");
        chart.setHeight("300px");
        container.add(chart);
        return container;
    }

    private Div createPieChart(String title, Map<String, Long> data) {
        Chart chart = new Chart(ChartType.PIE);
        Configuration conf = chart.getConfiguration();
        conf.setTitle(title);
        DataSeries series = new DataSeries();
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            DataSeriesItem item = new DataSeriesItem(entry.getKey(), entry.getValue());
            series.add(item);
        }
        conf.addSeries(series);
        PlotOptionsPie plotOptions = new PlotOptionsPie();
        plotOptions.setCursor(Cursor.POINTER);
        plotOptions.setShowInLegend(true);
        conf.setPlotOptions(plotOptions);
        return createChartContainer(chart);
    }

    private Div createColumnChart(String title, Map<String, Long> data) {
        Chart chart = new Chart(ChartType.COLUMN);
        Configuration conf = chart.getConfiguration();
        conf.setTitle(title);

        XAxis xAxis = new XAxis();
        xAxis.setCategories(data.keySet().toArray(new String[0]));
        conf.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Количество устройств");
        conf.addyAxis(yAxis);

        DataSeries series = new DataSeries("Устройства");
        for (Map.Entry<String, Long> entry : data.entrySet()) {
            DataSeriesItem item = new DataSeriesItem(entry.getKey(), entry.getValue());
            series.add(item);
        }
        conf.addSeries(series);
        return createChartContainer(chart);
    }

    private Div createBarChart(String title, Map<Integer, Long> data) {
        Chart chart = new Chart(ChartType.BAR);
        Configuration conf = chart.getConfiguration();
        conf.setTitle(title);

        XAxis xAxis = new XAxis();
        xAxis.setCategories(data.keySet().stream().map(i -> i + " ★").toArray(String[]::new));
        conf.addxAxis(xAxis);

        YAxis yAxis = new YAxis();
        yAxis.setTitle("Количество отзывов");
        conf.addyAxis(yAxis);

        DataSeries series = new DataSeries("Отзывы");
        for (Map.Entry<Integer, Long> entry : data.entrySet()) {
            DataSeriesItem item = new DataSeriesItem(entry.getKey() + " ★", entry.getValue());
            series.add(item);
        }
        conf.addSeries(series);
        return createChartContainer(chart);
    }
}