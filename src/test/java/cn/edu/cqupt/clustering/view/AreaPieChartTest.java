package cn.edu.cqupt.clustering.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AreaPieChartTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<String, Number> data = new HashMap<>();
        data.put("00062faa-abe6-46d1-ab11-c45beaf75115", 50);
        data.put("0d48c1cb-831f-4e64-85a2-2175f07e25d1", 50);
        data.put("0d48c1cb-831f-4e64-85a2-2175f07e2533", 40);
        data.put("0d48c1cb-831f-4e64-85a2-2175f07e2534", 30);
        data.put("0d48c1cb-831f-4e64-85a2-2175f07e2535", 10);
        data.put("0d48c1cb-831f-4e64-85a2-2175f07e2536", 20);
        AreaPieChart chart = new AreaPieChart(250, 250);
        chart.setData(data);
        Scene scene = new Scene(chart.getDefaultLayout(), 800, 800);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}