package cn.edu.cqupt.sankey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.view.SpectrumTable;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ClusteringSankeyDiagram {

	// input parameters
	private UndirectedGraph<Vertex, Edge> undirectedGraph;
	private String releaseIName;
	@SuppressWarnings("unused")
	private String releaseIIName;

	// default setting
	private SimpleDoubleProperty hGap;
	private SimpleDoubleProperty vGap;
	private SimpleDoubleProperty buildsFactor;
	private SimpleDoubleProperty verticesShapeWidth;
	private SimpleDoubleProperty leftMargin;
	private boolean firstRunFlag;

	// intermediate calculation value
	private HashMap<Vertex, Integer> layer; // vertex => the layer number of the vertex
	private HashMap<Vertex, Double> startYOfVerticesShape; // initial value: the Y coordinate of the upper-left corner
															// of each rectangles
	private int totalLayerNumber; // total layer number
	private HashMap<Vertex, Rectangle> verticesShape;
	private ArrayList<Path> edgesShape;
	private Label overlapCountLab;

	// result
	private Group group;
	private GridPane controller;

	public Group getGroup() {
		return group;
	}

	public GridPane getController() {
		return controller;
	}

	public ClusteringSankeyDiagram(String releaseIName, String releaseIIName, UndirectedGraph<Vertex, Edge> undirectedGraph,
								   Vertex startVertex) {
		this.undirectedGraph = undirectedGraph;
		this.releaseIName = releaseIName;
		this.releaseIIName = releaseIIName;

		// default value
		verticesShapeWidth = new SimpleDoubleProperty(10.0);
		buildsFactor = new SimpleDoubleProperty(1.0);
		vGap = new SimpleDoubleProperty(5.0);
		hGap = new SimpleDoubleProperty(200.0);
		leftMargin = new SimpleDoubleProperty(10.0);
		firstRunFlag = true;

		// initialize
		layer = new HashMap<>();
		verticesShape = new HashMap<>();
		startYOfVerticesShape = new HashMap<>();
		edgesShape = new ArrayList<>();
		overlapCountLab = new Label();

		// return value
		group = new Group();
		controller = new GridPane();

		// get the order of access vertices during breadth first search
		ArrayList<Vertex> bfsVerticesOrder = undirectedGraph.getBfsVerticesOrder(startVertex);
		arrangeLayer(bfsVerticesOrder);
		double[] eachLayerLength = getEachLayerLength(layer);
		double maxLengthOfAllLayer = getMaxLengthOfAllLayer(eachLayerLength);// max length of all layer(contain gap)

		// set vertices shape
		setVerticesShape(layer, eachLayerLength, maxLengthOfAllLayer);

		// set edges shape
		setEdgesShape(this.undirectedGraph, layer, verticesShape, verticesShapeWidth.get());

		// set controller
		setController();

		// add edges shape
		addEdgesShape(edgesShape);

		// add vertices shape
		addVerticesShape(verticesShape.values());

		// add overlap count label into group for avoiding duplicate children
		group.getChildren().add(overlapCountLab);
		firstRunFlag = false;
	}

	/**
	 * arrange layer according to releaseName, save message into variable layer:
	 * vertex => layer number(0-base system)
	 * 
	 * @param bfsVerticesOrder
	 */
	private void arrangeLayer(ArrayList<Vertex> bfsVerticesOrder) {
		int i = -1; // layer number : 0-base system
		String preReleaseName = null;
		for (Vertex vertex : bfsVerticesOrder) {
			if (vertex.getReleaseName().equals(preReleaseName)) {
				layer.put(vertex, i);

			} else {
				layer.put(vertex, ++i);
			}
			preReleaseName = vertex.getReleaseName();
		}
		totalLayerNumber = ++i; // set total layer number
		// System.out.println("layer: " + layer);
		// System.out.println("totalLayerNumber: " + totalLayerNumber);
	}

	/**
	 * calculate the total length of each layer
	 * 
	 * @param layer
	 * @return
	 */
	private double[] getEachLayerLength(HashMap<Vertex, Integer> layer) {
		double[] eachLayerLength = new double[totalLayerNumber];
		for (Vertex vertex : layer.keySet()) {
			eachLayerLength[layer.get(vertex)] += vertex.getWeight() + vGap.get();
		}
		return eachLayerLength;
	}

	/**
	 * get max length of all layer
	 * 
	 * @param eachLayerLength
	 * @return
	 */
	private double getMaxLengthOfAllLayer(double[] eachLayerLength) {
		double maxLengthOfAllLayer = 0.0;
		for (double tmpLen : eachLayerLength) {
			if (tmpLen > maxLengthOfAllLayer) {
				maxLengthOfAllLayer = tmpLen;
			}
		}
		return maxLengthOfAllLayer;
	}

	private void setVerticesShape(HashMap<Vertex, Integer> layer, double[] eachLayerLength,
			double maxLengthOfAllLayer) {

		// set the startX and startY of each layer
		double[] eachLayerStartX = new double[totalLayerNumber];// the index of array is layer number
		double[] eachLayerStartY = new double[totalLayerNumber];

		// fisrtly: set layer 0
		double preStartX = leftMargin.get(); // layer 0
		eachLayerStartX[0] = preStartX;
		preStartX = eachLayerStartX[0];
		eachLayerStartY[0] = (maxLengthOfAllLayer - eachLayerLength[0]) / 2;

		// secondly: set layer >0
		for (int i = 1; i < eachLayerLength.length; i++) {
			eachLayerStartX[i] = preStartX + verticesShapeWidth.get() + hGap.get();
			preStartX = eachLayerStartX[i];
			eachLayerStartY[i] = (maxLengthOfAllLayer - eachLayerLength[i]) / 2;
		}

		for (Vertex vertex : layer.keySet()) {
			int layerNumber = layer.get(vertex);

			// create vertex shape
			Rectangle rect = firstRunFlag ? new Rectangle() : verticesShape.get(vertex);

			// set event for vertex
			setVertexEvent(vertex, rect);

			// set vertex shape
			rect.setX(eachLayerStartX[layerNumber]);
			rect.setY(eachLayerStartY[layerNumber]);
			rect.setHeight(vertex.getWeight() * buildsFactor.get());
			rect.widthProperty().bind(verticesShapeWidth);
			rect.setStrokeWidth(0);
			rect.setStrokeType(StrokeType.OUTSIDE);
			if (vertex.getReleaseName().equals(releaseIName)) {
				rect.setFill(Color.web("#2f995b", 1));
			} else {
				rect.setFill(Color.web("#808c04", 1));
			}
			// rect.setStrokeWidth(1);
			// rect.setStroke(Color.BLACK);
			// rect.setStrokeType(StrokeType.OUTSIDE);
			// rect.setFill(null);
			if (firstRunFlag) {
				verticesShape.put(vertex, rect);
				startYOfVerticesShape.put(vertex, rect.getY());
			} else {

				// update startY of vertices shape in not first run
				startYOfVerticesShape.replace(vertex, rect.getY());
			}

			// update startY of visited layer
			eachLayerStartY[layerNumber] = eachLayerStartY[layerNumber] + vertex.getWeight() * buildsFactor.get()
					+ vGap.get();
		}
	}

	/**
	 * set event of each vertices
	 * 
	 * @param vertex
	 * @param rect
	 *            the rectangle of corresponding vertex
	 */
	private void setVertexEvent(Vertex vertex, Rectangle rect) {

		// set mouse click event for vertex
		rect.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				// show current cluster message
				Label cluIdLab = new Label("Cluster Id: " + vertex.getCluster().getId());
				Label specCountLab = new Label("Spectra Count: " + vertex.getCluster().getSpecCount());

				// show cluster cluster's spectra in table
				SpectrumTable specTable = new SpectrumTable(vertex.getCluster().getSpectra());

				VBox vbox = new VBox();
				vbox.getChildren().addAll(cluIdLab, specCountLab, specTable.getSpectrumTable());

				// create a new window
				Stage clusterStage = new Stage();
				Scene clusterScene = new Scene(vbox);
				clusterStage.setScene(clusterScene);
				clusterStage.setTitle("Cluster Message");
				clusterStage.show();
			}
		});
	}

	private void addVerticesShape(Collection<Rectangle> shape) {
		group.getChildren().addAll(shape);
	}

	private void setEdgesShape(UndirectedGraph<Vertex, Edge> undirectedGraph, HashMap<Vertex, Integer> layer,
			HashMap<Vertex, Rectangle> verticesShape, double verticesShapeWidth) {
		// HashMap<Vertex, HashMap<Vertex, Edge>> unredundantAdjacencyTable =
		// undirectedGraph
		// .getUnredundantAdjacencyTable();
		HashMap<Vertex, HashMap<Vertex, Edge>> unredundantAdjacencyTable = undirectedGraph
				.getUnredundantAdjacencyTable();
		int i = 0;
		for (Vertex vertex1 : unredundantAdjacencyTable.keySet()) {
			for (Vertex vertex2 : unredundantAdjacencyTable.get(vertex1).keySet()) {
				Edge edge = undirectedGraph.getEdge(vertex1, vertex2); // get edge
				double height = edge.getWeight() * buildsFactor.get(); // get weight of the edge

				// set start and end point
				double startX1, startY1, endX1, endY1, startX2, startY2, endX2, endY2;
				if (layer.get(vertex1) < layer.get(vertex2)) {

					// vertex1
					startX1 = verticesShape.get(vertex1).getX() + verticesShapeWidth;
					startY1 = startYOfVerticesShape.get(vertex1);
					endX1 = startX1;
					endY1 = startY1 + height;

					// vertex2
					startX2 = verticesShape.get(vertex2).getX();
					startY2 = startYOfVerticesShape.get(vertex2);
					endX2 = startX2;
					endY2 = startY2 + height;
				} else {

					// vertex1
					startX1 = verticesShape.get(vertex2).getX() + verticesShapeWidth;
					startY1 = startYOfVerticesShape.get(vertex2);
					endX1 = startX1;
					endY1 = startY1 + height;

					// vertex2
					startX2 = verticesShape.get(vertex1).getX();
					startY2 = startYOfVerticesShape.get(vertex1);
					endX2 = startX2;
					endY2 = startY2 + height;
				}

				// System.out.println(vertex1.getCluster().getId() + "->" +
				// vertex2.getCluster().getId());
				// System.out.println(startX1 + ", " + startY1 + "; " + startX2 + ", " + startY2
				// + "; " + endX2 + ", "
				// + endY2 + "; " + endX1 + ", " + endY1 + "; ");

				// update startY
				startYOfVerticesShape.replace(vertex1, startYOfVerticesShape.get(vertex1) + height);
				startYOfVerticesShape.replace(vertex2, startYOfVerticesShape.get(vertex2) + height);

				// create path
				if (firstRunFlag) {
					Path path = new Path();
					MoveTo moveTo = new MoveTo();
					moveTo.setX(startX1);
					moveTo.setY(startY1);

					// line1
					LineTo lineTo1 = new LineTo();
					lineTo1.setX(startX2);
					lineTo1.setY(startY2);

					// line2
					LineTo lineTo2 = new LineTo();
					lineTo2.setX(endX2);
					lineTo2.setY(endY2);

					// line3
					LineTo lineTo3 = new LineTo();
					lineTo3.setX(endX1);
					lineTo3.setY(endY1);

					// path setting
					path.setStrokeWidth(0);
					path.setStrokeType(StrokeType.OUTSIDE);
					path.setFill(Color.web("#808080", 0.4));

					// add elements
					path.getElements().addAll(moveTo, lineTo1, lineTo2, lineTo3);
					edgesShape.add(path);

					// set edge event
					setEdgeEvent(vertex1, vertex2, edge, path);
				} else {
					Path path = edgesShape.get(i++);
					MoveTo moveTo = (MoveTo) path.getElements().get(0);
					moveTo.setX(startX1);
					moveTo.setY(startY1);

					// line1
					LineTo lineTo1 = (LineTo) path.getElements().get(1);
					lineTo1.setX(startX2);
					lineTo1.setY(startY2);

					// line2
					LineTo lineTo2 = (LineTo) path.getElements().get(2);
					lineTo2.setX(endX2);
					lineTo2.setY(endY2);

					// line3
					LineTo lineTo3 = (LineTo) path.getElements().get(3);
					lineTo3.setX(endX1);
					lineTo3.setY(endY1);

					// path setting
					path.setStrokeWidth(0);
					path.setStrokeType(StrokeType.OUTSIDE);
					path.setFill(Color.web("#808080", 0.4));

					// set edge event
					setEdgeEvent(vertex1, vertex2, edge, path);
				}
			}
		}
	}

	private void setEdgeEvent(Vertex vertex1, Vertex vertex2, Edge edge, Path path) {

		path.setOnMouseEntered((MouseEvent event) -> {
//			double x = event.getX();
//			double y = event.getY();
//			overlapCountLab.setLayoutX(x - 20);
//			overlapCountLab.setLayoutY(y - 20);
//			overlapCountLab.setText(edge.getWeight() + "");
			path.setFill(Color.web("#96ddff"));
		});
		path.setOnMouseExited((MouseEvent event) -> {
//			overlapCountLab.setText("");
			path.setFill(Color.web("#808080", 0.4));
		});

		// click event
		path.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				// spectra message
				Vertex tmpVer1 = null, tmpVer2 = null;
				if (vertex1.getReleaseName().equals(releaseIName)) {
					tmpVer1 = vertex1;
					tmpVer2 = vertex2;
				} else {
					tmpVer1 = vertex2;
					tmpVer2 = vertex1;
				}
				SpectrumTable specTable = new SpectrumTable(releaseIName, tmpVer1.getCluster().getId(),
						tmpVer1.getCluster().getSpectra(), releaseIIName, tmpVer2.getCluster().getId(),
						tmpVer2.getCluster().getSpectra(), edge.getOverlapSpectra());

				// create a new window
				Stage overlapSpecStage = new Stage();
				Scene overlapSpecScene = new Scene(specTable.getOverlapSpectraPane());
				overlapSpecStage.setScene(overlapSpecScene);
				overlapSpecStage.setTitle("Cluster Message");
				overlapSpecStage.show();

			}
		});
	}

	private void addEdgesShape(ArrayList<Path> edgesShape) {
		group.getChildren().addAll(edgesShape);
	}

	private GridPane setController() {

		/*
		 * vertices shape width controller
		 */
		// label
		Label verticesShapeWidthLabel = new Label("Width");

		// slider
		Slider verticesShapeWidthController = new Slider(1.0, 50.0, verticesShapeWidth.get());
		verticesShapeWidthController.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				verticesShapeWidth.set(newValue.doubleValue());
				double[] eachLayerLength = getEachLayerLength(layer);
				double maxLengthOfAllLayer = getMaxLengthOfAllLayer(eachLayerLength);// max length of all layer(contain
																						// gap)
				// set vertices shape
				setVerticesShape(layer, eachLayerLength, maxLengthOfAllLayer);

				// set edges shape
				setEdgesShape(undirectedGraph, layer, verticesShape, verticesShapeWidth.get());
			}
		});

		// text field
		Text verticesShapeWidthText = new Text();
		verticesShapeWidthText.textProperty().bind(verticesShapeWidthController.valueProperty().asString());
		controller.add(verticesShapeWidthLabel, 0, 0);
		controller.add(verticesShapeWidthController, 1, 0);
		controller.add(verticesShapeWidthText, 2, 0);

		/*
		 * builds factor controller
		 */
		// label
		Label buildsFactorLabel = new Label("Zoom Factor");

		// slider
		Slider buildsFactorController = new Slider(0.1, 10.0, buildsFactor.get());
		buildsFactorController.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				buildsFactor.set(newValue.doubleValue());
				double[] eachLayerLength = getEachLayerLength(layer);
				double maxLengthOfAllLayer = getMaxLengthOfAllLayer(eachLayerLength);// max length of all layer(contain
																						// gap)
				// set vertices shape
				setVerticesShape(layer, eachLayerLength, maxLengthOfAllLayer);

				// set edges shape
				setEdgesShape(undirectedGraph, layer, verticesShape, verticesShapeWidth.get());
			}
		});

		// text field
		Text buildsFactorText = new Text();
		buildsFactorText.textProperty().bind(buildsFactorController.valueProperty().asString());
		controller.add(buildsFactorLabel, 0, 1);
		controller.add(buildsFactorController, 1, 1);
		controller.add(buildsFactorText, 2, 1);

		/*
		 * left margin controller
		 */
		// label
		Label leftMarginLabel = new Label("Left Margin");

		// slider
		Slider leftMarginController = new Slider(0, 500, leftMargin.get());
		leftMarginController.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				leftMargin.set(newValue.doubleValue());
				double[] eachLayerLength = getEachLayerLength(layer);
				double maxLengthOfAllLayer = getMaxLengthOfAllLayer(eachLayerLength);// max length of all layer(contain
																						// gap)
				// set vertices shape
				setVerticesShape(layer, eachLayerLength, maxLengthOfAllLayer);

				// set edges shape
				setEdgesShape(undirectedGraph, layer, verticesShape, verticesShapeWidth.get());
			}
		});

		// text field
		Text leftMarginText = new Text();
		leftMarginText.textProperty().bind(leftMarginController.valueProperty().asString());
		controller.add(leftMarginLabel, 0, 2);
		controller.add(leftMarginController, 1, 2);
		controller.add(leftMarginText, 2, 2);

		/*
		 * vGap controller
		 */
		// label
		Label vGapLabel = new Label("Vertical Gap");

		// slider
		Slider vGapController = new Slider(1.0, 50, vGap.get());
		vGapController.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				vGap.set(newValue.doubleValue());
				double[] eachLayerLength = getEachLayerLength(layer);
				double maxLengthOfAllLayer = getMaxLengthOfAllLayer(eachLayerLength);// max length of all layer(contain
																						// gap)
				// set vertices shape
				setVerticesShape(layer, eachLayerLength, maxLengthOfAllLayer);

				// set edges shape
				setEdgesShape(undirectedGraph, layer, verticesShape, verticesShapeWidth.get());
			}
		});

		// text field
		Text vGapText = new Text();
		vGapText.textProperty().bind(vGapController.valueProperty().asString());
		controller.add(vGapLabel, 0, 3);
		controller.add(vGapController, 1, 3);
		controller.add(vGapText, 2, 3);

		/*
		 * hGap controller
		 */
		// label
		Label hGapLabel = new Label("Horizontal Gap");

		// slider
		Slider hGapController = new Slider(100, 500, hGap.get());
		hGapController.valueProperty().addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				hGap.set(newValue.doubleValue());
				double[] eachLayerLength = getEachLayerLength(layer);
				double maxLengthOfAllLayer = getMaxLengthOfAllLayer(eachLayerLength);// max length of all layer(contain
																						// gap)
				// set vertices shape
				setVerticesShape(layer, eachLayerLength, maxLengthOfAllLayer);

				// set edges shape
				setEdgesShape(undirectedGraph, layer, verticesShape, verticesShapeWidth.get());
			}
		});

		// text field
		Text hGapText = new Text();
		hGapText.textProperty().bind(hGapController.valueProperty().asString());
		controller.add(hGapLabel, 0, 4);
		controller.add(hGapController, 1, 4);
		controller.add(hGapText, 2, 4);
		/*
		 * rotation controller
		 */
		// Button rotation = new Button("Horizontal");
		// rotation.setOnAction((ActionEvent event) -> {
		// if (rotation.getText().equals("Horizontal")) {
		// rotation.setText("Vertical");
		// Rotate rotate = new Rotate();
		// rotate.setAngle(90);
		// rotate.setPivotX(800);
		// rotate.setPivotY(800);
		// System.out.println("Math.abs(group.getBoundsInLocal().getMaxY()): "
		// + Math.abs(group.getBoundsInLocal().getMaxY()));
		// group.getTransforms().add(rotate);
		// } else {
		// rotation.setText("Horizontal");
		// Rotate rotate = new Rotate();
		// rotate.setAngle(-90);
		// rotate.setPivotX(800);
		// rotate.setPivotY(800);
		// group.getTransforms().add(rotate);
		// }
		// });
		// controller.add(rotation, 0, 4);
		return controller;
	}

}
