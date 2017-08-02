package cn.edu.cqupt.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.edu.cqupt.util.ZoomManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PeakMap extends Application {
	private ComboBox<String> comboBox;
	private ColorPicker colorPicker;
	private LineChart<Number, Number> peakMap; // LineChart -> peak map
	private StackPane stackPane; // contain peakMap
	private HBox hbox; // contain comboBox and colorPicker
	private VBox vbox; // complete graph with color picker and peak map
	private List<XYChart.Series<Number, Number>> allSeries; // all data

	public void setVbox(VBox vbox) {
		this.vbox = vbox;
	}

	public VBox getVbox() {
		return vbox;
	}

	public PeakMap() {

	}

	public PeakMap(List<Float> mz, List<Float> intens) {
		this.allSeries = new ArrayList<>();

		// create comboBox
		this.comboBox = new ComboBox<>();
		this.comboBox.getItems().add("ClusterI  Color");
		this.comboBox.getSelectionModel().selectFirst();

		// create colorPicker
		this.colorPicker = new ColorPicker(Color.RED);

		// add event for comboBox
		this.comboBox.setOnAction((ActionEvent ae) -> {
			if (this.comboBox.getValue().equals("ClusterI  Color")) {
				this.colorPicker.setValue(ZoomManager.firstColor);
			} else {
				this.colorPicker.setValue(ZoomManager.secondColor);
			}
		});

		// add event for colorPicker
		this.colorPicker.setOnAction((ActionEvent ae) -> {
			if (this.comboBox.getValue().equals("ClusterI  Color")) {
				ZoomManager.firstColor = this.colorPicker.getValue();
				// System.out.println("firstColor");
			} else {
				ZoomManager.secondColor = this.colorPicker.getValue();
				// System.out.println("secondColor");
			}
			ZoomManager.colorChanged.setValue(true);

		});

		// create hbox
		 this.hbox = new HBox();
		 this.hbox.getChildren().addAll(this.comboBox, this.colorPicker);

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

		// create vbox
		this.vbox = new VBox();
		this.vbox.getChildren().addAll(this.hbox, this.stackPane);

		// create peak map
		createPeakMap(mz, intens);

		// Zoom in
		ZoomManager.anchorPoint = intens.size(); // dividing line of two clusters
		new ZoomManager<Number, Number>(this.stackPane, this.peakMap, this.allSeries);

	}

	public PeakMap(List<Float> mz1, List<Float> intens1, List<Float> mz2, List<Float> intens2) {

		this(mz1, intens1);

		// add second item for conboBox
		this.comboBox.getItems().add("ClusterII Color");

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
			// this.peakMap.getData().add(series);
			this.allSeries.add(series);
		}
	}

	@Override
	public void start(Stage primaryStage) {

		List<Float> mz = new ArrayList<Float>();
		List<Float> intens = new ArrayList<Float>();

		mz.add(1F);
		mz.add(2F);
		mz.add(3F);
		intens.add(1F);
		intens.add(2F);
		intens.add(3F);

		PeakMap peakMap = new PeakMap(mz, intens);

		// create scene
		Scene scene = new Scene(peakMap.getVbox());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
