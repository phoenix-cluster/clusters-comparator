package cn.edu.cqupt.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ZoomManager<X, Y> {

    static <X, Y> ObservableList<XYChart.Series<X, Y>> deepCopySeries(final Collection<XYChart.Series<X, Y>> data) {
        final ObservableList<XYChart.Series<X, Y>> backup = FXCollections.observableArrayList();
        for (final Series<X, Y> s : data) {
            backup.add(deepCopySeries(s));
        }
        return backup;
    }

    static <X, Y> XYChart.Series<X, Y> deepCopySeries(final XYChart.Series<X, Y> series) {
        final XYChart.Series<X, Y> result = new XYChart.Series<X, Y>();
        result.setName(series.getName());
        result.setData(deepCopySeriesData(series.getData()));
        return result;
    }

    static <X, Y> ObservableList<XYChart.Data<X, Y>> deepCopySeriesData(
            final Collection<? extends XYChart.Data<X, Y>> data) {
        final ObservableList<XYChart.Data<X, Y>> result = FXCollections.observableArrayList();
        for (final Data<X, Y> i : data) {
            result.add(new Data<X, Y>(i.getXValue(), i.getYValue()));
        }
        return result;
    }

    private final ObservableList<XYChart.Series<X, Y>> series;

    private final XYChart<X, Y> chart;

    private volatile boolean zoomed = false;

    public static Color firstColor = Color.RED;

    public static Color secondColor = Color.GREEN;

    public static BooleanProperty colorChanged = new SimpleBooleanProperty(false);

    public static int anchorPoint;

    public ZoomManager(final Pane chartParent, final XYChart<X, Y> chart,
                       final Collection<? extends Series<X, Y>> series) {
        super();
        this.chart = chart;
        this.series = FXCollections.observableArrayList(series);

        // backup data
        restoreData();

        // listen color change
        colorChange();

        final Rectangle zoomRect = new Rectangle();
        zoomRect.setManaged(false);
        zoomRect.setFill(Color.LIGHTSEAGREEN.deriveColor(0, 1, 1, 0.5));
        chartParent.getChildren().add(zoomRect);
        // setUpZooming(zoomRect, chart);
        setZoomRectangle(zoomRect);
    }

    @SafeVarargs
    public ZoomManager(final Pane chartParent, final XYChart<X, Y> chart, final Series<X, Y>... series) {
        this(chartParent, chart, Arrays.asList(series));

    }

    private void restoreData() {
        // make a tmp variable of data, since we will modify it but need to be
        // able to restore
        final ObservableList<XYChart.Series<X, Y>> backup2 = deepCopySeries(series);
        chart.getData().setAll(backup2);
        for (int i = 0; i < backup2.size(); i++) {
            if (i < anchorPoint) {
                if (firstColor != null) {
                    backup2.get(i).getNode().lookup(".chart-series-line")
                            .setStyle("-fx-stroke: " + ColorUtils.colorToHex(firstColor));
                    // System.out.println("firstColor-Zoom");
                }
            } else {
                backup2.get(i).getNode().lookup(".chart-series-line")
                        .setStyle("-fx-stroke: " + ColorUtils.colorToHex(secondColor));
                // System.out.println("secondColor-Zoom");
            }
        }
    }

    private void colorChange() {
        colorChanged.addListener(new ChangeListener<Boolean>() {

            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                System.out.println("changed " + oldValue + "->" + newValue);
                restoreData();
                colorChanged.setValue(false);
            }
        });
    }

    private void setZoomRectangle(Rectangle rect) {

        SimpleObjectProperty<Point2D> rectinit = new SimpleObjectProperty<>();
        Node chartBackground = chart.lookup(".chart-plot-background");

        // set mouse handler
        EventHandler<MouseEvent> mouseHandler = new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                    rectinit.set(new Point2D(event.getX(), event.getY()));
                    double x = ((Number) chart.getXAxis().getValueForDisplay(event.getX())).doubleValue();
                    double y = ((Number) chart.getYAxis().getValueForDisplay(event.getY())).doubleValue();
                    System.out.println("x=" + x + "\ty=" + y);
                } else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                    System.out.println("X: " + Math.min(event.getX(), rectinit.get().getX()));
                    rect.setX(Math.min(event.getX(), rectinit.get().getX()));
                    rect.setY(Math.min(event.getY(), rectinit.get().getY()));
                    rect.setWidth(Math.abs(event.getX() - rectinit.get().getX()));
                    rect.setHeight(Math.abs(event.getY() - rectinit.get().getY()));
                } else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {

                    // Bounds boundInChart = chartBackground.sceneToLocal(rect.getBoundsInLocal());
                    // double minXAxis = (double)
                    // chart.getXAxis().getValueForDisplay(boundInChart.getMinX());
                    // double maxXAxis = (double)
                    // chart.getXAxis().getValueForDisplay(boundInChart.getMaxX());
                    // double minYAxis = (double)
                    // chart.getYAxis().getValueForDisplay(boundInChart.getMinY());
                    // double maxYAxis = (double)
                    // chart.getYAxis().getValueForDisplay(boundInChart.getMaxY());
                    //
                    // System.out.println("boundX: " + boundInChart.getMinX() + "\tboundX: " +
                    // boundInChart.getMaxX());
                    // System.out.println("minXAxis: " + minXAxis + "\tmaxXAxis: " + maxXAxis);
                    // System.out.println("boundY: " + boundInChart.getMinY() + "\tboundY: " +
                    // boundInChart.getMaxY());
                    // System.out.println("minYAxis: " + minYAxis + "\tmaxYAxis: " + maxYAxis);

                    /*
                     * Bounds boundInChart = chartBackground.getBoundsInLocal(); double minXAxis =
                     * (double) chart.getXAxis().getValueForDisplay(boundInChart.getMinX()); double
                     * maxXAxis = (double)
                     * chart.getXAxis().getValueForDisplay(boundInChart.getMaxX()); double minYAxis
                     * = (double) chart.getYAxis().getValueForDisplay(boundInChart.getMinY());
                     * double maxYAxis = (double)
                     * chart.getYAxis().getValueForDisplay(boundInChart.getMaxY());
                     *
                     * System.out.println("boundX: " + boundInChart.getMinX() + "\tboundX: " +
                     * boundInChart.getMaxX()); System.out.println("minXAxis: " + minXAxis +
                     * "\tmaxXAxis: " + maxXAxis); System.out.println("boundY: " +
                     * boundInChart.getMinY() + "\tboundY: " + boundInChart.getMaxY());
                     * System.out.println("minYAxis: " + minYAxis + "\tmaxYAxis: " + maxYAxis);
                     */
                    //
                    // rect.setWidth(0);
                    // rect.setHeight(0);

                    final Bounds bb = chartBackground.sceneToLocal(rect.getBoundsInLocal());

                    final double minx = bb.getMinX();
                    final double maxx = bb.getMaxX();

                    doZoom(true, chart.getXAxis().getValueForDisplay(minx), chart.getXAxis().getValueForDisplay(maxx));

                    rect.setWidth(0);
                    rect.setHeight(0);

                } else if (event.getEventType() == MouseEvent.MOUSE_CLICKED) {
                    System.out.println("click");
                    if (event.getClickCount() == 2 && zoomed) {
                        System.out.println("click2");
                        restoreData();
                        zoomed = false;
                        event.consume();
                    }
                }
            }
        };

        chart.setOnMousePressed(mouseHandler);
        chart.setOnMouseDragged(mouseHandler);
        chart.setOnMouseReleased(mouseHandler);
        chart.setOnMouseClicked(mouseHandler);
    }

    private void doZoom(final boolean x, final Number n1, final Number n2) {
        final double min = Math.min(n1.doubleValue(), n2.doubleValue());
        final double max = Math.max(n1.doubleValue(), n2.doubleValue());
        if (max - min > 1) {
            zoomed = true;
            final Iterator<XYChart.Series<X, Y>> it = chart.getData().iterator();
            while (it.hasNext()) {
                final XYChart.Series<X, Y> s = it.next();
                final Iterator<XYChart.Data<X, Y>> it2 = s.getData().iterator();
                while (it2.hasNext()) {
                    final XYChart.Data<X, Y> d = it2.next();
                    final Object value;
                    if (x) {
                        value = d.getXValue();
                    } else {
                        value = d.getYValue();
                    }
                    if (value instanceof Number) {
                        final Number n = (Number) value;
                        final double dd = n.doubleValue();
                        if (dd < min || dd > max) {
                            it2.remove();
                        } else {
                        }
                    }
                    if (s.getData().isEmpty()) {
                        it.remove();
                    }
                }
            }
        } else {
            // System.out.println("Skip tiny zoom");
        }

    }

    private void doZoom(final boolean x, final Object o1, final Object o2) {
        if (o1 instanceof Number && o2 instanceof Number) {
            doZoom(x, (Number) o1, (Number) o2);
        } else if (o1 instanceof String || o2 instanceof String) {
            doZoom(x, (String) o1, (String) o2);
        } else {
            // final int wait = 0;
        }
    }

    private void doZoom(final boolean x, String s1, String s2) {
        if (s1 == null && s2 == null) {
            return;
        }
        if (s1 == null) {
            s1 = s2;
        }
        if (s2 == null) {
            s2 = s1;
        }

        final Iterator<XYChart.Series<X, Y>> it = chart.getData().iterator();
        while (it.hasNext()) {
            final XYChart.Series<X, Y> s = it.next();
            final List<?> values;
            if (x) {
                values = extractXValues(s.getData());
            } else {
                values = extractYValues(s.getData());
            }
            final int index1 = values.indexOf(s1);
            final int index2 = values.indexOf(s2);
            final int lower = Math.min(index1, index2);
            final int upper = Math.max(index1, index2);
            final Iterator<Data<X, Y>> it2 = s.getData().iterator();
            while (it2.hasNext()) {
                final Data<X, Y> d = it2.next();
                final Object value;
                if (x) {
                    value = d.getXValue();
                } else {
                    value = d.getYValue();
                }
                final int index = values.indexOf(value);
                if (index != -1 && (index < lower || index > upper)) {
                    it2.remove();
                }
            }
        }
    }

    static <X, Y> ObservableList<X> extractXValues(final ObservableList<Data<X, Y>> data) {
        final ObservableList<X> result = FXCollections.observableArrayList();
        for (final Data<X, Y> d : data) {
            result.add(d.getXValue());
        }
        return result;
    }

    static <X, Y> ObservableList<Y> extractYValues(final ObservableList<Data<X, Y>> data) {
        final ObservableList<Y> result = FXCollections.observableArrayList();
        for (final Data<X, Y> d : data) {
            result.add(d.getYValue());
        }
        return result;
    }

}