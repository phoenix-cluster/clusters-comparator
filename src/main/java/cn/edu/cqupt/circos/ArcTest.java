package cn.edu.cqupt.circos;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.stage.Stage;

public class ArcTest extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		StackPane root = new StackPane();
		Group group = new Group();

		// Arc arc = new Arc(250, 250, 200, 200, 120.71428571428571,
		// 146.42857142857142);
		Arc arc = new Arc(250, 250, 200, 200, 2/3, 93/2);
		arc.setType(ArcType.ROUND);
		arc.setFill(null);
		arc.setStroke(Color.GREEN);
		arc.setStrokeWidth(10);

		// path
		Path path = new Path();

		double arcToRadius = arc.getRadiusX() - 10;
		double arcToStartX = arc.getCenterX() + arcToRadius * Math.cos(Math.toRadians(2/3));
		double arcToStartY = arc.getCenterY() + arcToRadius * Math.sin(Math.toRadians(2/3));
		double arcToEndX = arc.getCenterX() + arcToRadius * Math.cos(Math.toRadians(-93/2));
		double arcToEndY = arc.getCenterY() + arcToRadius * Math.sin(Math.toRadians(-93/2));
		MoveTo moveTo1 = new MoveTo(arcToStartX, arcToStartY);
		ArcTo arcTo1 = new ArcTo(arc.getRadiusX(), arc.getRadiusX(), 0, arcToEndX, arcToEndY, false, false);
		
		path.getElements().addAll(moveTo1, arcTo1);
		path.setStroke(Color.RED);
		path.setStrokeWidth(6);
		path.setFill(null);
		
		
		
		group.getChildren().addAll(arc, path);
		root.getChildren().add(group);
		Scene scene = new Scene(root, 1000, 1000);
		primaryStage.setScene(scene);
		primaryStage.show();

	}

	public static void main(String[] args) {
		launch(args);
	}

}
