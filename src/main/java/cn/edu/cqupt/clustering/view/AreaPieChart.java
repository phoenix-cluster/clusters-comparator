package cn.edu.cqupt.clustering.view;

import cn.edu.cqupt.util.ColorUtils;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.*;
import java.util.stream.IntStream;

public class AreaPieChart {
    private double centerX;
    private double centerY;
    private String[] nameArr;
    private double[] valueArr;
    private int[] isShowed;
    private SimpleDoubleProperty zoomFactor;
    private SimpleDoubleProperty offset;
    private SimpleIntegerProperty filter;
    private SimpleStringProperty clickedClusterId;
    private SimpleBooleanProperty isShowAreaChart;
    private Color[] colorArr; // color array
    private Group areaPieChartPane;
    private GridPane regulator;

    public String getClickedClusterId() {
        return clickedClusterId.get();
    }

    public SimpleStringProperty clickedClusterIdProperty() {
        return clickedClusterId;
    }


    public Group getAreaPieChartPane() {
        return areaPieChartPane;
    }


    public AreaPieChart(double centerX, double centerY) {
        this.centerX = centerX;
        this.centerY = centerY;
        zoomFactor = new SimpleDoubleProperty(100);
        offset = new SimpleDoubleProperty(10);
        filter = new SimpleIntegerProperty(-1);
        clickedClusterId = new SimpleStringProperty();
        isShowAreaChart = new SimpleBooleanProperty(false);
        areaPieChartPane = new Group();

    }

    public Group setData(Map<String, Number> data) {

        /** organize data **/
        nameArr = new String[data.size()];
        valueArr = new double[data.size()];
        int i = 0;
        for (String key : data.keySet()) {
            nameArr[i] = key;
            valueArr[i] = data.get(key).doubleValue();
            i++;
        }

        colorArr = ColorUtils.colorArray(nameArr.length);
        filter.set((int) Arrays.stream(valueArr).max().getAsDouble());

        /** create area pie chart **/
        isShowed = IntStream.range(0, nameArr.length).toArray();
        return createAreaPieChar();
    }

    private Group createAreaPieChar() {

        // calculate percentage
        double[] filteredValueArr = Arrays.stream(isShowed).mapToDouble(i -> valueArr[i]).toArray();
        double[] percentage = calPercentage(filteredValueArr);

        // get maximum radium
        final double maxRadius = Arrays.stream(percentage).max().getAsDouble() * zoomFactor.get();
        double[][] intersectionPoints = intersectionPoints(percentage, maxRadius, offset.get());

        double startAnglle = 0.0;
        Path[] paths = new Path[isShowed.length];
        Arc[] arcs = new Arc[isShowed.length];
        Text[] texts = new Text[isShowed.length];

        int i;
        for (int t = 0; t < isShowed.length; t++) {
            i = isShowed[t];

            /** legend **/
            // path
            Path path = new Path();
            MoveTo moveTo = new MoveTo(centerX, centerY);
            LineTo lineTo1 = new LineTo(intersectionPoints[t][0], intersectionPoints[t][1]);
            LineTo lineTo2 = new LineTo(intersectionPoints[t][2], intersectionPoints[t][3]);
            path.setStroke(colorArr[i]);
            path.getElements().addAll(moveTo, lineTo1, lineTo2);
            paths[t] = path;

            // label
            Text text = new Text(nameArr[i]);
            text.setFont(Font.font(12.0));
            text.setFill(colorArr[i]);
            if (intersectionPoints[t][2] < centerX) {
                double x = nameArr[i].length() * text.getFont().getSize() * 0.55;
                text.setX(intersectionPoints[t][2] - x);
            } else {
                text.setX(intersectionPoints[t][2]);
            }
            text.setY(intersectionPoints[t][3] + 5);
            texts[t] = text;

            /** arc **/
            double radius = isShowAreaChart.get()
                    ? percentage[t] * zoomFactor.get()
                    : maxRadius;

            Arc arc = new Arc();
            arc.setId(nameArr[i]);
            arc.setCenterX(centerX);
            arc.setCenterY(centerY);
            arc.setRadiusX(radius);
            arc.setRadiusY(radius);
            arc.setStartAngle(startAnglle);
            arc.setLength(360 * percentage[t]);
            arc.setType(ArcType.ROUND);
            arc.setFill(colorArr[i]);
            arc.setOnMouseClicked(event -> {
                clickedClusterId.set(null);
                clickedClusterId.set(arc.getId());
            });
            startAnglle += 360 * percentage[t];
            arcs[t] = arc;
        }
        if (areaPieChartPane.getChildren().size() > 0) {
            new Group(areaPieChartPane.getChildren()); // garbage collection
        }
        areaPieChartPane.getChildren().addAll(paths);
        areaPieChartPane.getChildren().addAll(arcs);
        areaPieChartPane.getChildren().addAll(texts);
        return areaPieChartPane;
    }

    private double[] calPercentage(double[] valueArr) {
        double[] percentage = new double[valueArr.length];
        double sum = Arrays.stream(valueArr).sum();
        IntStream.range(0, valueArr.length).forEach(
                i -> percentage[i] = valueArr[i] / sum
        );
        return percentage;
    }

    /**
     * if the various "zoomFactor" has changed, the method need been recall
     *
     * @param maxRadius
     * @return
     */
    private double[][] intersectionPoints(double[] percentage, double maxRadius, double offset) {
        maxRadius += offset;
        double[][] intersectionPoints = new double[percentage.length][];
        double endAngle = 0.0;
        for (int i = 0; i < percentage.length; i++) {
            double[] point = new double[4];
            double alpha = endAngle + percentage[i] * 360 / 2;
            point[0] = centerX + maxRadius * Math.cos(Math.toRadians(alpha));
            point[1] = centerY - maxRadius * Math.sin(Math.toRadians(alpha));
            if (alpha > 90 && alpha < 270) {
                point[2] = point[0] - offset;
                point[3] = point[1];
            } else {
                point[2] = point[0] + offset;
                point[3] = point[1];
            }
            intersectionPoints[i] = point;
            endAngle += percentage[i] * 360;
        }
        return intersectionPoints;
    }

    public GridPane getRegulator() {
        regulator = new GridPane();

        /** zoomFactor regulator **/
        // label
        Label factorLab = new Label("zoom: ");

        // text
        Text factorText = new Text(zoomFactor.get() + "");

        // slider
        Slider factorSlider = new Slider(1, 1000, zoomFactor.get());
        factorSlider.valueProperty().addListener(
                (observable, oldValue, newValue) -> {
                    zoomFactor.setValue(newValue);
                    createAreaPieChar();
                    factorText.setText(newValue.doubleValue() + "");
                }
        );

        /** overlap spectra count filter. those count less than threshold will not display. **/
        // maximum overlap spectra count
//        int maxCount = (int)Arrays.stream(valueArr).max().getAsDouble();

        // label
        Label filterLabel = new Label("filter: ");

        // text
        Text filterText = new Text("0");

        // slider
        Slider filterSlider = new Slider(0, 100, 0);
        filter.addListener((observable, oldValue, newValue) -> {
            filterSlider.setMax(newValue.doubleValue());
        });

        filterSlider.valueProperty().addListener((observable, oldValue, newValue) -> {

            // filter
            ArrayList<Integer> indices = new ArrayList<>();
            for (int i = 0; i < valueArr.length; i++) {
                if (valueArr[i] >= newValue.doubleValue()) {
                    indices.add(i);
                }
            }
            isShowed = indices.stream().mapToInt(i -> i.intValue()).toArray();
            createAreaPieChar();
            filterText.setText(newValue + "");

        });


        regulator.add(factorLab, 0, 0);
        regulator.add(factorSlider, 1, 0);
        regulator.add(factorText, 2, 0);
        regulator.add(filterLabel, 0, 1);
        regulator.add(filterSlider, 1, 1);
        regulator.add(filterText, 2, 1);
        return regulator;
    }

    private BorderPane borderPane = new BorderPane();

    public BorderPane getDefaultLayout() {
        borderPane.setTop(getRegulator());
        borderPane.setCenter(getAreaPieChartPane());
        return borderPane;
    }
}
