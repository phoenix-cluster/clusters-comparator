package cn.edu.cqupt.view;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.edu.cqupt.dao.ClusterDaoFileImpl;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.OverlapClusterComponent;
import cn.edu.cqupt.service.ClusterComparerService;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.CheckBoxTreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;

public class DonutChart extends Application {
	private Group donutChart = new Group();
	private boolean isFirst = true;

	public DonutChart() {
		super();
	}

	public Group geDonutChart() {
		return donutChart;
	}

	public void createRelationChart() {

		
	}


	public void start(Stage primaryStage) {
		
		List<Cluster> releaseI = new ClusterDaoFileImpl(new File("./compare/compare_1.clustering")).findAllClusters();
		List<Cluster> releaseII = new ClusterDaoFileImpl(new File("./compare/compare_2.clustering")).findAllClusters();
		Cluster cluster = releaseI.get(0);
		ClusterComparerService clusterComparer = null;
		try {
			clusterComparer = new ClusterComparerService(cluster, releaseI, releaseII);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		
		
		Scene scene = new Scene();
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}
}
