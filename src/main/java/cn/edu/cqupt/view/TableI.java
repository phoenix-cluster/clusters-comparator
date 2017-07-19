package cn.edu.cqupt.view;

import cn.edu.cqupt.service.ClusterTableService;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class TableI extends Application{

	
	public void start(Stage primaryStage) throws Exception {
		GridPane grid = new GridPane();
		Scene scene = new Scene(grid);

		// clusters table
//		ClusterTableService clusterTableService = new ClusterTableService("./compare/test.clustering"); // read cluster data
		ClusterTableService clusterTableService = new ClusterTableService("./compare/cli_clustering.pxd000021.0.7_4.clustering"); // read cluster data
		
		// create cluster table and spectrum table
		ClusterTable clusterTable = new ClusterTable(clusterTableService, 10);
//		grid.add(clusterTable.getAnchor(), 0, 0);
		
		grid.add(clusterTable.gethBox(), 0, 0);
		// set stage
		
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	public static void main(String[] args){
		launch(args);
	}
	
}
