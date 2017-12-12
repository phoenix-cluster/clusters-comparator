package cn.edu.cqupt.graph;

import java.util.ArrayList;
import java.util.HashMap;

import com.jfoenix.controls.JFXSlider;

import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.view.ClusterApplication;
import cn.edu.cqupt.view.ComparerPieChart;
import cn.edu.cqupt.view.PeakMap;
import cn.edu.cqupt.view.SpectrumTable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PaintGraph {
	private UndirectedGraph<Vertex, Edge> undirectedGraph;
	private Vertex focusVertex; // the starting point of traversal

	// track circle
	private double centerX; // the horizontal center of track circle
	private double centerY; // the vertical center of track circle
	private double trackCircleRadius; // the radius of track circle
	private Circle trackCircle;

	// component circle
	private HashMap<Vertex, Circle> componentCircle;// key: vertex, value: coordinate of vertex

	// track circle and component circle setting
	private SimpleDoubleProperty trackCircleFactor = new SimpleDoubleProperty(4.0); // approximate track circle zoom
																					// multiple
	// private double margin = 600.0; // outside margin of track circle
	private SimpleDoubleProperty buildFactor = new SimpleDoubleProperty(1.0); // build zoom multiple
	private SimpleDoubleProperty buildsInterval = new SimpleDoubleProperty(10.0); // build interval

	// path
	private UndirectedGraph<Circle, Line> pathGraph;

	// graph pane
	private Group group; // contain network graph
	private VBox messagePane;
	private GridPane networkGraphPane; // contain all builds: slider, network graph(group), spectrum table

	// get value only in horizontal or vertical
	private enum Direction {
		HORIZONTAL, VERTICAL
	};

	public GridPane getSlider() {
		return setSlider();
	}

	public Group getGroup() {
		return group;
	}

	public VBox getMessagePane() {
		return messagePane;
	}

	public GridPane getNetworkGraphPane() {
		return networkGraphPane;
	}

	public PaintGraph(UndirectedGraph<Vertex, Edge> undirectedGraph, Vertex vertex) {
		super();
		this.undirectedGraph = undirectedGraph;
		this.focusVertex = vertex;

		// track circle
		this.trackCircle = new Circle();

		// component circle
		this.componentCircle = new HashMap<>();

		// path
		this.pathGraph = new UndirectedGraph<>();

		// graph pane
		this.group = new Group();
		this.messagePane = new VBox();
		this.networkGraphPane = new GridPane();

		// organize builds
		networkGraphPane.add(setSlider(), 0, 0);
		networkGraphPane.add(new ScrollPane(group), 0, 1);
		networkGraphPane.add(messagePane, 1, 1);

		// add track circle]
		setTrackCircle(vertex);
		group.getChildren().add(trackCircle);

		// set component circle
		setCircle(vertex);

		// set path
		// setStraightPath();
		// setStraightPath(componentCircle);
		setStraightPath();

		// add path, circle and label
		// addPath(path);
		addPath(pathGraph.getAllEdges());
		addCircle(componentCircle);
	}

	public GridPane networkGraph(UndirectedGraph<Vertex, Edge> undirectedGraph, Vertex vertex) {

		this.undirectedGraph = undirectedGraph;
		this.focusVertex = vertex;

		// track circle
		this.trackCircle = new Circle();

		// component circle
		this.componentCircle = new HashMap<>();

		// path
		this.pathGraph = new UndirectedGraph<>();

		// graph pane
		this.group = new Group();
		this.networkGraphPane = new GridPane(); // return value

		// organize builds
		networkGraphPane.add(setSlider(), 0, 0);
		networkGraphPane.add(new ScrollPane(group), 0, 1);

		// add track circle]
		setTrackCircle(vertex);
		group.getChildren().add(trackCircle);

		// set component circle
		setCircle(vertex);

		// set path
		// setStraightPath();
		// setStraightPath(componentCircle);
		setStraightPath();

		// add path, circle and label
		// addPath(path);
		addPath(pathGraph.getAllEdges());
		addCircle(componentCircle);

		return networkGraphPane;
	}

	private void setTrackCircle(Vertex vertex) {

		// calculate circumference of track circle
		double firstArc = 0.0;
		double secondArc = 0.0;
		for (Vertex tmpVer : undirectedGraph.getAllVertices()) {
			if (tmpVer.getReleaseName().equals(ClusterApplication.releaseIName)) {
				firstArc += tmpVer.getWeight() * buildFactor.get();
			} else {
				secondArc += tmpVer.getWeight() * buildFactor.get();
			}
		}

		trackCircleRadius = firstArc > secondArc ? firstArc / Math.PI * trackCircleFactor.get()
				: secondArc / Math.PI * trackCircleFactor.get();

		// set track circle center point
		// centerX = trackCircleRadius + margin;
		// centerY = trackCircleRadius + margin;
		centerX = trackCircleRadius;
		centerY = trackCircleRadius;

		// add trace circle
		// trackCircle.setLayoutX(centerX);
		// trackCircle.setLayoutY(centerY);
		trackCircle.setCenterX(centerX);
		trackCircle.setCenterY(centerY);

		trackCircle.setRadius(trackCircleRadius);
		trackCircle.setFill(null);
		trackCircle.setStroke(Color.web("#808080", 0.4));
		trackCircle.setStrokeWidth(2.0);
		// trackCircle.getStrokeDashArray().add(25.0);
	}

	private void setCircle(Vertex vertex) {

		double leftArcLength = 0.0;
		double rightArcLength = 0.0;

		// set middle circle
		Circle middleCircle = componentCircle.containsKey(vertex) ? componentCircle.get(vertex) : new Circle();
		// middleCircle.setLayoutX(centerX);
		// middleCircle.setLayoutY(centerY - trackCircleRadius);
		middleCircle.setCenterX(centerX);
		middleCircle.setCenterY(centerY - trackCircleRadius);
		middleCircle.setRadius(vertex.getWeight() * buildFactor.get());
		middleCircle.setFill(Color.web("#FF1493"));
		middleCircle.setStroke(Color.web("#FF1493"));
		setCircleEvent(middleCircle, vertex); // add event for circle
		componentCircle.put(vertex, middleCircle);
		leftArcLength += middleCircle.getRadius();
		rightArcLength += middleCircle.getRadius();

		// set other circle
		for (Vertex tmpVer : undirectedGraph.getAllVertices()) {
			if (!tmpVer.equals(vertex)) {
				double arcLength = 0.0;
				if (tmpVer.getReleaseName().equals(ClusterApplication.releaseIName)) {
					leftArcLength += buildsInterval.get() + tmpVer.getWeight() * buildFactor.get();
					arcLength = leftArcLength;
					leftArcLength += tmpVer.getWeight() * buildFactor.get();
				} else {
					rightArcLength += buildsInterval.get() + tmpVer.getWeight() * buildFactor.get();
					arcLength = rightArcLength;
					rightArcLength += tmpVer.getWeight() * buildFactor.get();
				}

				// line equation
				double apha;
				if (tmpVer.getReleaseName().equals(ClusterApplication.releaseIName))
					apha = Math.PI / 2 - arcLength / trackCircleRadius;
				else
					apha = Math.PI / 2 + arcLength / trackCircleRadius;
				double m = Math.tan(apha);
				double n = centerY - m * centerX;

				// calculate circle coordinate
				double[] centerCoordinate = intersectionPoint(centerX, centerY, trackCircleRadius, m, n,
						Direction.HORIZONTAL, tmpVer.getReleaseName().equals(ClusterApplication.releaseIName));

				double circleRadius = tmpVer.getWeight() * buildFactor.get();
				Circle circle = componentCircle.containsKey(tmpVer) ? componentCircle.get(tmpVer) : new Circle();
				// circle.setLayoutX(centerCoordinate[0]);
				// circle.setLayoutY(centerCoordinate[1]);

				circle.setCenterX(centerCoordinate[0]);
				circle.setCenterY(centerCoordinate[1]);

				circle.setRadius(circleRadius);
				if (tmpVer.getReleaseName().equals(ClusterApplication.releaseIName)) {

					// first release style
					circle.setFill(Color.web("#7FFF00", 0.6));
					circle.setStroke(Color.web("#7FFF00", 0.6));
				} else {

					// second release style
					circle.setFill(Color.web("#9932CC", 0.6));
					circle.setStroke(Color.web("#9932CC", 0.6));

				}
				circle.setId(tmpVer.getCluster().getId());
				setCircleEvent(circle, tmpVer);// add event for circle
				componentCircle.put(tmpVer, circle);
			}
		}
	}

	// add event for circle
	private void setCircleEvent(Circle circle, Vertex vertex) {

		// hover event
		circle.hoverProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue.booleanValue()) {

					// set other circle and line
					for (Circle tmpCir : pathGraph.getAllVertices()) {
						String tmpCol = "#" + tmpCir.getFill().toString().substring(2, 8);
						System.out.println(tmpCol);
						tmpCir.setStroke(Color.web(tmpCol, 0.1));
						tmpCir.setFill(Color.web(tmpCol, 0.1));
					}

					for (Line tmpLine : pathGraph.getAllEdges()) {
						tmpLine.setStroke(Color.web("#696969", 0.1));
					}

					// set circle and line focused on
					String circleColor = "#" + circle.getFill().toString().substring(2, 8);
					circle.setStroke(Color.web(circleColor, 1.0));
					circle.setFill(Color.web(circleColor, 1.0));
					for (Circle tmpCir : pathGraph.getAdjacencyVertices(circle)) {
						String tmpCol = "#" + tmpCir.getFill().toString().substring(2, 8);
						tmpCir.setStroke(Color.web(tmpCol, 1.0));
						tmpCir.setFill(Color.web(tmpCol, 1.0));
					}

					// print cluster message

					for (Line tmpLine : pathGraph.getAdjacencyEdge(circle)) {
						tmpLine.setStroke(Color.web("#00FFFF", 0.4));
					}
				} else {
					// set other circle and line
					for (Circle tmpCir : pathGraph.getAllVertices()) {
						String tmpCol = "#" + tmpCir.getFill().toString().substring(2, 8);
						tmpCir.setStroke(Color.web(tmpCol, 0.6));
						tmpCir.setFill(Color.web(tmpCol, 0.6));
					}

					for (Line tmpLine : pathGraph.getAllEdges()) {
						tmpLine.setStroke(Color.web("#696969", 0.4));
					}
				}
			}
		});

		// click event
		circle.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (event.getClickCount() == 1 && event.getButton().equals(MouseButton.PRIMARY)) {

					// delete all children of messagePane
					VBox trashCans = new VBox();
					trashCans.getChildren().addAll(messagePane.getChildren());

					SpectrumTable spectrumTable = new SpectrumTable(vertex.getCluster().getSpectra());
					Label clusterLabel = new Label(vertex.getCluster().getId());
					clusterLabel.setFont(Font.font("Arial", FontWeight.BLACK, 16));
					messagePane.getChildren().addAll(clusterLabel, spectrumTable.getSpectrumTable());
				}
			}
		});
		Text clusterLabel = new Text();
		circle.setOnMouseEntered((MouseEvent event) -> {
			double x = event.getX();
			double y = event.getY();
			
			String clusterMess = "cluster Id: " + vertex.getCluster().getId() + "\n";
			clusterMess += "spectra count: " + vertex.getCluster().getSpecCount();
			clusterLabel.setText(clusterMess);
			clusterLabel.setX(x);
			clusterLabel.setY(y);
			clusterLabel.setManaged(false);
			group.getChildren().add(clusterLabel);
			clusterLabel.setManaged(false);
		});
		
		circle.setOnMouseExited((MouseEvent event) -> {
			Group trashCans = new Group();
			trashCans.getChildren().add(clusterLabel);
		});
		
	}

	private void setStraightPath() {
		HashMap<Vertex, HashMap<Vertex, Edge>> allEdges = undirectedGraph.getUnredundantAdjacencyTable();

		// traversal all edges
		for (Vertex tmpVer : allEdges.keySet()) {
			for (Vertex adjVer : allEdges.get(tmpVer).keySet()) {

				// create and set line
				Line line = new Line();

				// line.startXProperty().bind(componentCircle.get(tmpVer).layoutXProperty());
				// line.startYProperty().bind(componentCircle.get(tmpVer).layoutYProperty());
				// line.endXProperty().bind(componentCircle.get(adjVer).layoutXProperty());
				// line.endYProperty().bind(componentCircle.get(adjVer).layoutYProperty());

				line.startXProperty().bind(componentCircle.get(tmpVer).centerXProperty());
				line.startYProperty().bind(componentCircle.get(tmpVer).centerYProperty());
				line.endXProperty().bind(componentCircle.get(adjVer).centerXProperty());
				line.endYProperty().bind(componentCircle.get(adjVer).centerYProperty());
				buildFactor.addListener(new ChangeListener<Number>() {

					@Override
					public void changed(ObservableValue<? extends Number> observable, Number oldValue,
							Number newValue) {
						line.strokeWidthProperty()
								.set(allEdges.get(tmpVer).get(adjVer).getWeight() * buildFactor.get());
					}

				});
				line.setStroke(Color.web("#696969", 0.4));
				line.setFill(null);
				line.setStrokeLineCap(StrokeLineCap.ROUND);
				setLineEvent(line, tmpVer, adjVer);
				pathGraph.addEdge(componentCircle.get(tmpVer), componentCircle.get(adjVer), line);
			}
		}
	}

	private void setLineEvent(Line line, Vertex vertex1, Vertex vertex2) {
		Text overlapCountLabel = new Text();
		// mouse event
		line.setOnMouseEntered((MouseEvent event) -> {
			int overlapCount = undirectedGraph.getEdge(vertex1, vertex2).getOverlapSpectra().size();
			double x = event.getX();
			double y = event.getY();
			overlapCountLabel.setX(x);
			overlapCountLabel.setY(y);
			overlapCountLabel.setText(overlapCount + "");
			overlapCountLabel.setManaged(false);
			group.getChildren().add(overlapCountLabel);
			overlapCountLabel.setManaged(false);
		});
		
		line.setOnMouseExited((MouseEvent event) -> {
			@SuppressWarnings("unused")
			Group tashCans = new Group(overlapCountLabel);
			tashCans = null;
		});
		line.setOnMouseClicked((MouseEvent event) -> {
			System.out.println("Click");

			if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 1) {

				// get tabB
				Tab tabB = null;
				if (ClusterApplication.tabPane.getTabs().size() == 1) {
					tabB = new Tab("Cluster comparison");
					ClusterApplication.tabPane.getTabs().add(tabB);
				} else {
					tabB = ClusterApplication.tabPane.getTabs().get(1);
				}

				// tabB grid pane
				GridPane tabBGridPane = new GridPane();
				ColumnConstraints col1 = new ColumnConstraints();
				col1.setPercentWidth(50.0);
				ColumnConstraints col2 = new ColumnConstraints();
				col2.setPercentWidth(50.0);
				tabBGridPane.getColumnConstraints().addAll(col1, col2);

				// get source cluster's spectrum table and object cluster's spectrum
				SpectrumTable spectrumTable = null;
//				spectrumTable = new SpectrumTable(vertex1.getCluster().getId(),
//						vertex1.getCluster().getSpectra(), vertex2.getCluster().getId(),
//						vertex2.getCluster().getSpectra(),
//						undirectedGraph.getEdge(vertex1, vertex2).getOverlapSpectra());

				// peak map
				PeakMap peakMap = new PeakMap(vertex1.getCluster().getMzValues(),
						vertex1.getCluster().getIntensValues(), vertex2.getCluster().getMzValues(),
						vertex2.getCluster().getIntensValues());

				// pie chart
				ComparerPieChart comparerPieChart = null;
//				comparerPieChart = new ComparerPieChart(undirectedGraph, vertex1);

				// add builds into tabB
				tabBGridPane.add(spectrumTable.getOverlapSpectraPane(), 0, 0, 2, 1);
				tabBGridPane.add(peakMap.getVbox(), 0, 1);
				tabBGridPane.add(comparerPieChart.getComparerPieChart(), 1, 1);
				tabB.setContent(tabBGridPane);
				ClusterApplication.tabPane.getSelectionModel().select(1);
			}
		});

		// hover event
		line.hoverProperty()
				.addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
					if (newValue.booleanValue()) {

						// set other circle and line
						for (Circle tmpCir : pathGraph.getAllVertices()) {
							String tmpCol = "#" + tmpCir.getFill().toString().substring(2, 8);
							tmpCir.setStroke(Color.web(tmpCol, 0.1));
							tmpCir.setFill(Color.web(tmpCol, 0.1));
						}

						for (Line tmpLine : pathGraph.getAllEdges()) {
							tmpLine.setStroke(Color.web("#696969", 0.1));
						}

						// set circle and line focused on
						Circle circle1 = componentCircle.get(vertex1);
						Circle circle2 = componentCircle.get(vertex2);
						String circleCol1 = "#" + circle1.getFill().toString().substring(2, 8);
						String circleCol2 = "#" + circle2.getFill().toString().substring(2, 8);
						circle1.setStroke(Color.web(circleCol1, 1.0));
						circle1.setFill(Color.web(circleCol1, 1.0));
						circle2.setStroke(Color.web(circleCol2, 1.0));
						circle2.setFill(Color.web(circleCol2, 1.0));

						line.setStroke(Color.web("00FFFF", 0.4));

					} else {
						for (Circle tmpCir : pathGraph.getAllVertices()) {
							String tmpCol = "#" + tmpCir.getFill().toString().substring(2, 8);
							tmpCir.setStroke(Color.web(tmpCol, 0.6));
							tmpCir.setFill(Color.web(tmpCol, 0.6));
						}

						for (Line tmpLine : pathGraph.getAllEdges()) {
							tmpLine.setStroke(Color.web("#696969", 0.4));
						}
					}
				});
	}

	/**
	 * solve the intersection point of circle: (x - cx) ^ 2 + (y - cy) ^ 2 = r ^ 2
	 * and line: y = m * x + n
	 * 
	 * @param cx
	 * @param cy
	 * @param r
	 * @param m
	 * @param n
	 * @param isTopValue
	 *            determine the only solution: choose y < cy if true, otherwise
	 *            choose y > cy
	 * @return intersection point
	 */
	private double[] intersectionPoint(double cx, double cy, double r, double m, double n, Direction direction,
			boolean isNegative) {

		// get x, y
		double a = 1 + m * m;
		double b = 2 * m * (n - cy) - 2 * cx;
		double c = cx * cx + (n - cy) * (n - cy) - r * r;

		double x1 = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
		double x2 = (-b - Math.sqrt(b * b - 4 * a * c)) / (2 * a);
		double y1 = m * x1 + n;
		double y2 = m * x2 + n;
		switch (direction) {
		case HORIZONTAL:
			if (isNegative) {
				return x1 < x2 ? new double[] { x1, y1 } : new double[] { x2, y2 };
			} else {
				return x1 > x2 ? new double[] { x1, y1 } : new double[] { x2, y2 };
			}
		case VERTICAL:
			if (isNegative) {
				return y1 < y2 ? new double[] { x1, y1 } : new double[] { x2, y2 };
			} else {
				return y1 > y2 ? new double[] { x1, y1 } : new double[] { x2, y2 };
			}
		}
		throw new RuntimeException("calculate error in method intersectionPoint");
	}

	public void addPath(ArrayList<Line> path) {

		// add path
		group.getChildren().addAll(path);

	}

	public void addCircle(HashMap<Vertex, Circle> componentCircle) {

		// add circle
		group.getChildren().addAll(componentCircle.values());
	}

	public GridPane setSlider() {
		GridPane sliderPane = new GridPane();
		sliderPane.setPadding(new Insets(30, 0, 20, 0));

		// track circle radius factor
		Label trackCircleFactorLabel = new Label("track circle radius");
		JFXSlider trackCircleFactorRegulator = new JFXSlider(3, 20, 1);
		trackCircleFactorRegulator.setPrefSize(500, 50);
		trackCircleFactorRegulator.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
				trackCircleFactor.set(newVal.doubleValue());
				setTrackCircle(focusVertex);
				setCircle(focusVertex);
			}
		});
		sliderPane.add(trackCircleFactorLabel, 0, 0);
		sliderPane.add(trackCircleFactorRegulator, 1, 0);

		// build radius factor
		Label buildFactorLabel = new Label("member circle radius");
		JFXSlider buildFactorRegulator = new JFXSlider(0.1, 10, 0.2);
		buildFactorRegulator.setPrefSize(500, 50);
		buildFactorRegulator.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
				buildFactor.set(newVal.doubleValue());
				;
				setTrackCircle(focusVertex);
				setCircle(focusVertex);
			}
		});
		sliderPane.add(buildFactorLabel, 0, 1);
		sliderPane.add(buildFactorRegulator, 1, 1);

		// build interval factor
		Label buildIntervalLabel = new Label("member circle radius");
		JFXSlider buildIntervalRegulator = new JFXSlider(1, 1000, 10);
		buildIntervalRegulator.setPrefSize(500, 50);
		buildIntervalRegulator.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> ov, Number oldVal, Number newVal) {
				buildsInterval.set(newVal.doubleValue());
				;
				// setTrackCircle(focusVertex);
				setCircle(focusVertex);
			}
		});
		sliderPane.add(buildIntervalLabel, 0, 2);
		sliderPane.add(buildIntervalRegulator, 1, 2);

		return sliderPane;
	}
}

class CircleVertex {

	private Circle circle;

	public CircleVertex(Circle circle) {
		this.circle = circle;
	}

	public Circle getCircle() {
		return circle;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (this.getClass() != obj.getClass())
			return false;
		CircleVertex other = (CircleVertex) obj;
		if (this.circle == null) {
			if (other.circle != null)
				return false;
		}
		// return circle.getCenterX() == other.circle.getLayoutX() ||
		// circle.getLayoutY() == other.circle.getCenterY()
		// || circle.getRadius() == other.circle.getRadius();

		return circle.getCenterX() == other.circle.getCenterX() || circle.getCenterY() == other.circle.getCenterY()
				|| circle.getRadius() == other.circle.getRadius();
	}

	@Override
	public int hashCode() {
		final int primary = 31;
		int result = 1;
		long field = Double.doubleToLongBits(circle.getCenterX());
		result = primary * result + (int) (field ^ (field >>> 32));
		field = Double.doubleToLongBits(circle.getCenterY());
		result = primary * result + (int) (field ^ (field >>> 32));
		field = Double.doubleToLongBits(circle.getRadius());
		result = primary * result + (int) (field ^ (field >>> 32));
		return result;
	}

}

class PathEdge {

	private Line path;

	public Line getPath() {
		return path;
	}

	public PathEdge(Line path) {
		this.path = path;
	}

}