package cn.edu.cqupt.view;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangjs on 2018/3/24.
 */
public class PeakMapTest extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        List<Double> mz1 = new ArrayList<>();
        List<Double> intens1 = new ArrayList<>();
        mz1.add(1.0);
        mz1.add(2.0);
        mz1.add(3.0);
        intens1.add(1.0);
        intens1.add(2.0);
        intens1.add(3.0);

        List<Double> mz2 = new ArrayList<Double>();
        List<Double> intens2 = new ArrayList<Double>();
        mz2.add(1.0);
        mz2.add(2.0);
        mz2.add(3.0);
        mz2.add(4.0);
        intens2.add(1.0);
        intens2.add(2.0);
        intens2.add(3.0);
        intens2.add(4.0);

        List<Double> mz3 = new ArrayList<Double>();
        List<Double> intens3 = new ArrayList<Double>();
        mz3.add(1.0);
        intens3.add(1.0);


        List<Double> mz4 = new ArrayList<Double>();
        List<Double> intens4 = new ArrayList<Double>();
        mz4.add(1.0);
        intens4.add(1.0);

        LineChart<Number, Number> peakMap = PeakMap.filteredPeakMap(mz1, intens1, mz2, intens2, mz3, intens3, mz4, intens4);

        // create scene
        Scene scene = new Scene(new VBox(peakMap));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
