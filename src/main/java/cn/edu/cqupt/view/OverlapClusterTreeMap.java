package cn.edu.cqupt.view;

import java.io.File;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cn.edu.cqupt.dao.ClusterDaoFileImpl;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.OverlapClusterComponent;
import cn.edu.cqupt.service.ClusterComparerService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class OverlapClusterTreeMap extends Application {
	private Group relationChartPane = new Group();
	private static double X = 0; // horizontal distance overlay
	private static double preCircleDiameter = 0; // the distance of each circle to the top of the pane
	private boolean isFirst = true;

	// line
	private static double startCenterX;
	private static double startCenterY;
	private static double endCenterX;
	private static double endCenterY;
	private static double lineWidth;

	public OverlapClusterTreeMap() {
		super();
	}

	public Group getRelationChartPane() {
		resetOrder();
		return relationChartPane;
	}

	public void createRelationChart(OverlapClusterComponent overlapClusterComponent, double hgap, double vgap,
			double factor) {

		// circle
		Circle middleNode = getCircle(overlapClusterComponent, hgap, vgap, factor);
		if (endCenterX != 0) {

			// line
			Line line = new Line(startCenterX, startCenterY, endCenterX, endCenterY);
			line.setStrokeWidth(lineWidth);
//			line.setStroke(Color.web("#E6E6FA", 0.9));
			line.setStroke(Color.DARKMAGENTA);
			line.setStrokeLineCap(StrokeLineCap.ROUND);
			relationChartPane.getChildren().addAll(line, middleNode);
		} else { // to paint first circle without a line
			relationChartPane.getChildren().add(middleNode);
		}

		isFirst = !isFirst;

		Iterator<OverlapClusterComponent> itr = overlapClusterComponent.getChildren().iterator();
		while (itr.hasNext()) {
			OverlapClusterComponent tmp = itr.next();
			if (tmp.isLeafNode()) {

				// circle
				Circle leafNode = getCircle(tmp, hgap, vgap, factor);

				// line
				Line line = new Line(startCenterX, startCenterY, endCenterX, endCenterY);
				line.setStrokeWidth(lineWidth);
//				line.setStroke(Color.web("#E6E6FA", 0.9));
				line.setStroke(Color.DARKMAGENTA);
				line.setStrokeLineCap(StrokeLineCap.ROUND);
				relationChartPane.getChildren().addAll(line, leafNode);
			} else {
				createRelationChart(tmp, hgap, vgap, factor);
			}
		}
	}

	private Circle getCircle(OverlapClusterComponent overlapClusterComponent, double hgap, double vgap, double factor) {

		// set circle size
		double radius = overlapClusterComponent.getOverlapCluster().getSpecCount() * factor;
		double centerX = X + hgap + radius;
		double centerY = preCircleDiameter + vgap + radius;
		
		// prepare for next paint
		X = X + hgap + 2 * radius;
		if (isFirst) {
			preCircleDiameter = 0;
		} else {
			preCircleDiameter = radius * 2 + vgap;
		}
		System.out.println(overlapClusterComponent.getOverlapCluster().getId() + 
				"-preCircleDiameter: " + preCircleDiameter );
		

		Circle circle = new Circle(centerX, centerY, radius);
		circle.setId(overlapClusterComponent.getOverlapCluster().getId());

		// set circle style
		if (isFirst) {
			circle.setFill(Color.GREEN);
		} else {
			circle.setFill(Color.RED);
		}

		// set line
		if (isFirst) {
			startCenterX = centerX;
			startCenterY = centerY;
		} else {
			endCenterX = centerX;
			endCenterY = centerY;
		}
		// lineWidth = overlapClusterComponent.getOverlapSpectrums().size(); // overlap spectra number
//		lineWidth = overlapClusterComponent.getOverlapSpectrums().size() * factor; // overlap spectra number
		lineWidth = overlapClusterComponent.getOverlapSpectrums().size() * 2 * factor; // overlap spectra number

		return circle;
	}

	private void resetOrder() {
		ObservableList<Node> tmp = this.relationChartPane.getChildren();
		Node[] children = new Node[tmp.size()];
		for (int i = 0, start = 0, end = tmp.size() - 1; i < tmp.size(); i++) {

			if (Line.class.isAssignableFrom(tmp.get(i).getClass())) {
				children[start++] = tmp.get(i);
			}else {
				children[end--] = tmp.get(i);
			}
		}
		this.relationChartPane.getChildren().removeAll(children);
		for (Node node : children) {
			System.out.println(node);
			this.relationChartPane.getChildren().add(node);
		}
	}

	public void start(Stage primaryStage) {
		List<Cluster> releaseI = new ClusterDaoFileImpl(new File("./compare/compare_4.clustering")).findAllClusters();
		List<Cluster> releaseII = new ClusterDaoFileImpl(new File("./compare/compare_3.clustering")).findAllClusters();
		Cluster cluster = releaseI.get(0);
		ClusterComparerService clusterComparer = null;
		try {
			clusterComparer = new ClusterComparerService(cluster, releaseI, releaseII);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		OverlapClusterTreeMap treeMap = new OverlapClusterTreeMap();

		// method
		StackPane root = new StackPane();
		treeMap.createRelationChart(clusterComparer.getOverlapClusterComponent(), 50, 50, 10);
		root.getChildren().add(treeMap.getRelationChartPane());
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
