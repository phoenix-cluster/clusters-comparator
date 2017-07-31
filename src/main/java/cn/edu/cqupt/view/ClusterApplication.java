package cn.edu.cqupt.view;

import java.io.File;
import java.util.List;

import cn.edu.cqupt.service.ClusterTableService;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ClusterApplication extends Application {
	
	public static TabPane tabPane = new TabPane();
	private ClusterTableService releaseI;
	private ClusterTableService releaseII;
	private int pageSize = 8;

	public MenuBar getApplicationMenu(Window ownerWindow) {

		// create menu bar
		MenuBar menuBar = new MenuBar();

		// create File menu
		Menu fileMenu = new Menu("File");
		
		// add "select clustering files" item to File menu
		MenuItem openFile = new MenuItem("Select clustering files");
		openFile.setAccelerator(KeyCombination.keyCombination("Ctrl + O"));
		openFile.setOnAction((ActionEvent e) -> {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Clustering File");
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Clustering file", "*.clustering"),
					new FileChooser.ExtensionFilter("All files", "*.*")
					);
			List<File> clusteringFiles = fileChooser.showOpenMultipleDialog(ownerWindow);
			if(clusteringFiles == null) {
				
			}
			else if(clusteringFiles.size() != 2) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You must choose two clustering files");
				alert.showAndWait();
			}else {
				
				// read data
				this.releaseI = new ClusterTableService(clusteringFiles.get(0));
				this.releaseII = new ClusterTableService(clusteringFiles.get(1));
				
				// create cluster table and spectrum table
				ClusterTable clusterTable = new ClusterTable(releaseI, pageSize, releaseII.getAllClusters());
				
				// set tab pane
				Tab tabA = new Tab("Tab I");
				tabA.setContent(clusterTable.getGridPane());
				
				Tab tabB = new Tab("Tab II");
				
				tabPane.getTabs().addAll(tabA, tabB);
			}
			
		});
		
		// add "exit" item to file menu
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction((ActionEvent e) -> {
			System.exit(0);
		});
		
		fileMenu.getItems().addAll(openFile, exit);
		
		// create Tools menu
		Menu toolMenu = new Menu("Tools");
		
		// add "Switch clustering files" item to tool menu
		MenuItem swap = new MenuItem("Swap principal-subordinate relationship");
		swap.setOnAction((ActionEvent e) -> {
			
			// exchange
			ClusterTableService tmp = this.releaseI;
			this.releaseI = this.releaseII;
			this.releaseII = tmp;
			
			// create cluster table and spectrum table
			ClusterTable clusterTable = new ClusterTable(releaseI, pageSize, releaseII.getAllClusters());
			tabPane.getTabs().get(0).setContent(clusterTable.getGridPane());
		});

		toolMenu.getItems().add(swap);
		
		// add menu items into menu bar
		menuBar.getMenus().addAll(fileMenu, toolMenu);
		return menuBar;
	}

	@Override
	public void start(Stage primaryStage) {
		
		// vbox
		VBox vbox = new VBox();
		vbox.getChildren().addAll(getApplicationMenu(primaryStage), tabPane);
		
		// scene
		Scene scene = new Scene(vbox, 1600, 900);

		// set stage
		primaryStage.setScene(scene);
		primaryStage.setTitle("Cluster Comparer GUI");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
