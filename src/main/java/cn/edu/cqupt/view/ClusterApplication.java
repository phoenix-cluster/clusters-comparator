package cn.edu.cqupt.view;

import cn.edu.cqupt.service.ClusterTableService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ClusterApplication extends Application{
	
	public static TabPane tabPane = new TabPane();
	
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();
		Scene scene = new Scene(tabPane);

		// clusters table
		ClusterTableService releaseI = new ClusterTableService("./compare/cli_clustering.pxd000021.0.7_4.clustering"); // read cluster data
		ClusterTableService releaseII = new ClusterTableService("./compare/hdp_clustering.pxd000021.0.7_4.clustering"); // read cluster data
//		ClusterTableService releaseI = new ClusterTableService("./compare/compare_1.clustering");
//		ClusterTableService releaseII = new ClusterTableService("./compare/compare_2.clustering");

		
		// create cluster table and spectrum table
		ClusterTable clusterTable = new ClusterTable(releaseI, 10, releaseII.getAllClusters());
		
		grid.add(clusterTable.getvBox(), 0, 0);
		
		// set tab pane
		Tab tabA = new Tab("   Tab I   ");
		tabA.setContent(grid);
		
		Tab tabB = new Tab("   Tab II   ");
		
		tabPane.getTabs().addAll(tabA, tabB);
		
		
		// set stage
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
	
}
