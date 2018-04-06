package cn.edu.cqupt.test.test.java.cn.edu.cqupt.test;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class FontFamilyTest extends Application{
	public void start(Stage primaryStage) {
		GridPane grid = new GridPane();
		int i = 0;
		for(String family : Font.getFamilies()) {
			Label familyLab = new Label(family);
			Label englishLab = new Label("hello world");
			englishLab.setFont(Font.font(family, 12));
			Label chinaLab = new Label("你好，世界");
			chinaLab.setFont(Font.font(family, 12));
			grid.add(familyLab, 0, i);
			grid.add(englishLab, 1, i);
			grid.add(chinaLab, 2, i);
			i++;
		}
		Label total = new Label("Total font family: " + i);
		grid.add(total, 0, i, 3, 1);
		Scene scene = new Scene(new ScrollPane(grid), 500, 800);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	public static void main(String[] args) {
		launch(args);
	}
}
