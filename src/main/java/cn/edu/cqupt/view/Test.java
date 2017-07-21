package cn.edu.cqupt.view;

import java.util.List;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.service.ClusterTableService;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Test extends Application{
	@Override
	public void start(Stage stage) {
		Scene scene = new Scene(new Group());
		
		ClusterTableService releaseI = new ClusterTableService("./compare/compare_1.clustering");
		ClusterTableService releaseII = new ClusterTableService("./compare/compare_2.clustering");
		Cluster cluster = releaseI.getAllClusters().get(0);
		List<Cluster> releaseCluster = releaseII.getAllClusters();
		
		ComparerPieChart pieChart = new ComparerPieChart();
		pieChart.createComparerPieChart(cluster, releaseCluster);
		
		((Group) scene.getRoot()).getChildren().add(pieChart.getComparerPieChart());
		
		stage.setScene(scene);
		stage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
