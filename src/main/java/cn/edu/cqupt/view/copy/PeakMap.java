package cn.edu.cqupt.view.copy;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.javafx.charts.zooming.ZoomManager;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class PeakMap extends Application {
	private LineChart<Number, Number> peakMap; // LineChart -> peak map
	private StackPane stackPane; // contain peakMap
	private List<XYChart.Series<Number, Number>> allSeries; // all data

	public StackPane getStackPane() {
		return stackPane;
	}

	public void setStackPane(StackPane stackPane) {
		this.stackPane = stackPane;
	}

	public PeakMap() {

	}

	public PeakMap(List<Float> mz, List<Float> intens) {
		this.allSeries = new ArrayList<>();

		// create peakMap
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("MZ");
		yAxis.setLabel("intensity");
		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);
		xAxis.setForceZeroInRange(false);
		yAxis.setForceZeroInRange(false);
		this.peakMap = new LineChart<>(xAxis, yAxis);
		this.peakMap.setLegendVisible(false);
		this.peakMap.setCreateSymbols(false);

		// create stackPane
		this.stackPane = new StackPane();
		this.stackPane.getChildren().add(this.peakMap);

		// create peak map
		createPeakMap(mz, intens);

		// Zoom in
		new ZoomManager<Number, Number>(this.stackPane, this.peakMap, this.allSeries);

	}

	public PeakMap(List<Float> mz1, List<Float> intens1, List<Float> mz2, List<Float> intens2) {

		this(mz1, intens1);

		// get negative number of intens
		List<Float> negativeIntens = new ArrayList<>(intens2.size());
		for (int i = 0; i < intens2.size(); i++) {
			negativeIntens.add(-intens2.get(i)); // can't use method add()
		}

		createPeakMap(mz2, negativeIntens);
		new ZoomManager<Number, Number>(this.stackPane, this.peakMap, this.allSeries);
	}

	public void createPeakMap(List<Float> mz, List<Float> intens) {

		// build data
		Iterator<Float> mzItr = mz.iterator();
		Iterator<Float> inItr = intens.iterator();

		while (mzItr.hasNext()) {
			float x = mzItr.next();
			float y = inItr.next();
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.getData().add(new XYChart.Data<>(x, 0));
			series.getData().add(new XYChart.Data<>(x, y));
			this.peakMap.getData().add(series);
			this.allSeries.add(series);
		}
	}

	@Override
	public void start(Stage primaryStage) {

		List<Float> mz = new ArrayList<Float>();
		List<Float> intens = new ArrayList<Float>();

		for (int i = 0; i < 10; i++) {
			mz.add(i + 0.1f);
			intens.add(i + 5.1f);
		}

		PeakMap peakMap = new PeakMap(mz, intens, mz, intens);

		// create scene
		Scene scene = new Scene(peakMap.getStackPane());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
