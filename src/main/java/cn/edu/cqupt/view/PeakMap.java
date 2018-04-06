package cn.edu.cqupt.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.edu.cqupt.util.ColorUtil;
import cn.edu.cqupt.util.ZoomManager;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import umontreal.iro.lecuyer.util.Num;

public class PeakMap {
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
		this.comboBox.setOnAction((e) -> {
			if (this.comboBox.getValue().equals("ClusterI  Color")) {
				this.colorPicker.setValue(ZoomManager.firstColor);
			} else {
				this.colorPicker.setValue(ZoomManager.secondColor);
			}
		});

		// add event for colorPicker
		this.colorPicker.setOnAction((e) -> {
			if (this.comboBox.getValue().equals("ClusterI  Color")) {
				ZoomManager.firstColor = this.colorPicker.getValue();
			} else {
				ZoomManager.secondColor = this.colorPicker.getValue();
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
		createPeakMap(mz, intens, false);

		// Zoom in
		ZoomManager.anchorPoint = intens.size(); // dividing line of two clusters
		new ZoomManager<>(this.stackPane, this.peakMap, this.allSeries);
	}

	public PeakMap(List<Float> mz1, List<Float> intens1, List<Float> mz2, List<Float> intens2) {

		this(mz1, intens1);

		// add second item for conboBox
		this.comboBox.getItems().add("ClusterII Color");

//		// get negative number of intens
//		List<Float> negativeIntens = new ArrayList<>(intens2.size());
//		for (int i = 0; i < intens2.size(); i++) {
//			negativeIntens.add(-intens2.get(i)); // can't use method add()
//		}

		createPeakMap(mz2, intens2, true);
		new ZoomManager<>(this.stackPane, this.peakMap, this.allSeries);
	}

	public void createPeakMap(List<Float> mz, List<Float> intens, boolean isNegativeIntens) {

		// build data
		Iterator<Float> mzItr = mz.iterator();
		Iterator<Float> inItr = intens.iterator();

		while (mzItr.hasNext()) {
			double x = mzItr.next();
			double y = isNegativeIntens ? -inItr.next() : inItr.next();
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.getData().add(new XYChart.Data<>(x, 0));
			series.getData().add(new XYChart.Data<>(x, y));
			this.allSeries.add(series);
		}
	}

	public static LineChart<Number, Number> filteredPeakMap(List<Double> mz1, List<Double> intens1,
								List<Double> mz2, List<Double> intens2,
								List<Double> filteredMz1, List<Double> filteredIntens1,
								   List<Double> filteredMz2, List<Double> filteredIntens2){
		// create filtered peak map
		NumberAxis xAxis = new NumberAxis();
		NumberAxis yAxis = new NumberAxis();
		xAxis.setLabel("MZ");
		yAxis.setLabel("intensity");
		xAxis.setAutoRanging(true);
		yAxis.setAutoRanging(true);
		xAxis.setForceZeroInRange(false);
		yAxis.setForceZeroInRange(false);
		LineChart<Number, Number> filteredPeakMap = new LineChart<>(xAxis, yAxis);
		filteredPeakMap.setLegendVisible(false);
		filteredPeakMap.setCreateSymbols(false);
		filteredPeakMap.setMinWidth(800);
		// set color
		Color filteredSeriesColor1 = Color.RED;
		Color filteredSeriesColor2 = Color.GREEN;
		double opacity = 0.2;
		Color SeriesColor1 = Color.color(
				calColorWithOpacity(filteredSeriesColor1.getRed(), 255, opacity),
				calColorWithOpacity(filteredSeriesColor1.getGreen(), 255, opacity),
				calColorWithOpacity(filteredSeriesColor1.getBlue(), 255, opacity));
		Color SeriesColor2 = Color.color(
				calColorWithOpacity(filteredSeriesColor2.getRed(), 255, opacity),
				calColorWithOpacity(filteredSeriesColor2.getGreen(), 255, opacity),
				calColorWithOpacity(filteredSeriesColor2.getBlue(), 255, opacity));
		// add data
		organizePeakMapData(filteredPeakMap, mz1, intens1, false, SeriesColor1);
		organizePeakMapData(filteredPeakMap, mz2, intens2, true, SeriesColor2);
		organizePeakMapData(filteredPeakMap, filteredMz1, filteredIntens1, false, filteredSeriesColor1);
		organizePeakMapData(filteredPeakMap, filteredMz2, filteredIntens2, true, filteredSeriesColor2);
//		ArrayList<XYChart.Series<Number, Number>> allSeriesList = new ArrayList<>();
//		for(XYChart.Series<Number, Number> series : seriesList1){
//			series.getNode().lookup(".chart-series-line")
//					.setStyle("-fx-stroke: " + ColorUtil.colorToHex(Color.RED));
//		}
//		allSeriesList.addAll(seriesList1);
//		allSeriesList.addAll(seriesList2);
//		allSeriesList.addAll(seriesList3);
//		allSeriesList.addAll(seriesList4);
//		ObservableList obsList = FXCollections.observableArrayList(allSeriesList);
//		filteredPeakMap.getData().addAll(obsList);

		return filteredPeakMap;

	}

	/**
	 * organize peak map data
	 * @param mz
	 * @param intens
	 * @param isNegativeIntens
	 * @param strokeColor
	 * @return
	 */
	private static void organizePeakMapData(LineChart<Number, Number> filteredPeakMap,
											List<Double> mz, List<Double> intens,
											boolean isNegativeIntens, Color strokeColor) {
//		XYChart.Series<Number, Number> series = new XYChart.Series<>();
//		series.getNode().lookup(".chart-series-line")
//				.setStyle("-fx-stroke: " + ColorUtil.colorToHex(strokeColor));
//		ArrayList<XYChart.Series<Number, Number>> seriesList = new ArrayList<>();
		// build data
		Iterator<Double> mzItr = mz.iterator();
		Iterator<Double> inItr = intens.iterator();

		while (mzItr.hasNext()) {
			double x = mzItr.next();
			double y = isNegativeIntens ? -inItr.next() : inItr.next();
			XYChart.Series<Number, Number> series = new XYChart.Series<>();
			series.getData().add(new XYChart.Data<>(x, 0));
			series.getData().add(new XYChart.Data<>(x, y));
			filteredPeakMap.getData().add(series);
			series.getNode().lookup(".chart-series-line")
				.setStyle("-fx-stroke: " + ColorUtil.colorToHex(strokeColor));
//			seriesList.add(series);
		}
	}

//	public void addFilteredPeakMap(List<Double> filteredMz, List<Double> filteredIntens){
//		double opacity = 1;
//		createPeakMap(filteredMz, filteredIntens);
//		new ZoomManager<>(this.stackPane, this.peakMap, this.allSeries);
//		Color secondColor = ZoomManager.secondColor;
//		ZoomManager.secondColor = Color.color(
//				calColorWithOpacity(secondColor.getRed(), 255, opacity),
//				calColorWithOpacity(secondColor.getGreen(), 255, opacity),
//				calColorWithOpacity(secondColor.getBlue(), 255, opacity));
//		ZoomManager.colorChanged.set(true);
//	}

	private static double calColorWithOpacity(double color, double backgroundColor, double opacity){
		return ((color * 255 - backgroundColor) * opacity + backgroundColor) / 255;
	}

//	@Override
//	public void start(Stage primaryStage) {
//
//		List<Double> mz = new ArrayList<Double>();
//		List<Double> intens = new ArrayList<Double>();
//
//		mz.add(1F);
//		mz.add(2F);
//		mz.add(3F);
//		intens.add(1F);
//		intens.add(2F);
//		intens.add(3F);
//
//		PeakMap peakMap = new PeakMap(mz, intens);
//
//		// create scene
//		Scene scene = new Scene(peakMap.getVbox());
//		primaryStage.setScene(scene);
//		primaryStage.show();
//	}
//
//	public static void main(String[] args) {
//		launch(args);
//	}
}
