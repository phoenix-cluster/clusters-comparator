package cn.edu.cqupt.clustering.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PeakMapTest extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        PeakMap peakMap = new PeakMap();
        peakMap.updateData(new double[]{1.1, 2.3}, new double[]{101, 203});
        Scene scene = new Scene(peakMap.getWebView());
        primaryStage.setScene(scene);
        primaryStage.show();

    }
}