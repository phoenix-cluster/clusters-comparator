package cn.edu.cqupt.circos;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class CircosTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		
		// data
		double[] releaseI = new double[] {4, 2, 2};
		double[] releaseII = new double[] {1, 3, 1, 2};
		
//		double[] releaseI = new double[] {2, 2};
//		double[] releaseII = new double[] {1, 1};
		
		
		Circos circos = new Circos(releaseI, releaseII);
		circos.createPath(0, 0, 1);
		circos.createPath(1, 3, 2);
		circos.createPath(0, 1, 3);
		circos.createPath(2, 2, 1);
		root.getChildren().add(circos.getGroup());
		
		Scene scene = new Scene(root, 1000, 1000);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
