package cn.edu.cqupt.view;

import java.io.File;
import java.util.List;

import cn.edu.cqupt.service.ClusterTableService;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ClusterApplication extends Application {

	public static TabPane tabPane = new TabPane();
	public static ClusterTableService serviceReleaseI;
	public static ClusterTableService serviceReleaseII;
	public static HostServices hostServices;
	private int pageSize = 8;

	static void setIntroTab() {
		
		// add introduce tab to tab Pane
		Tab introTab = new Tab("Introduce");
		tabPane.getTabs().add(introTab);
		
		// introduce text
		TextArea introText = new TextArea("manual : the manual will be added soom");
		
		// add text area to tab
		introTab.setContent(introText);
	}

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
			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Clustering file", "*.clustering"),
					new FileChooser.ExtensionFilter("All files", "*.*"));
			List<File> clusteringFiles = fileChooser.showOpenMultipleDialog(ownerWindow);
			if (clusteringFiles == null) {

			} else if (clusteringFiles.size() != 2) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You must choose two clustering files");
				alert.showAndWait();
			} else {

				// read data
				serviceReleaseI = new ClusterTableService(clusteringFiles.get(0));
				serviceReleaseII = new ClusterTableService(clusteringFiles.get(1));

				// create cluster table and spectrum table
				ClusterSelection clusterTable = new ClusterSelection(serviceReleaseI, pageSize, serviceReleaseII.getAllClusters());

				// set tab pane
				tabPane.getTabs().clear();
				Tab tabA = new Tab("Cluster selection");
				tabA.setContent(clusterTable.getGridPane());

				Tab tabB = new Tab("Cluster comparison");

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
			ClusterTableService tmp = serviceReleaseI;
			serviceReleaseI = serviceReleaseII;
			serviceReleaseII = tmp;

			// create cluster table and spectrum table
			ClusterSelection clusterTable = new ClusterSelection(serviceReleaseI, pageSize, serviceReleaseII.getAllClusters());
			tabPane.getTabs().get(0).setContent(clusterTable.getGridPane());
		});

		toolMenu.getItems().add(swap);

		// add menu items into menu bar
		menuBar.getMenus().addAll(fileMenu, toolMenu);
		return menuBar;
	}

	@Override
	public void start(Stage primaryStage) {
		ClusterApplication.hostServices = this.getHostServices();
		
		// introduce tab
		setIntroTab();
		
		// vbox
		VBox vbox = new VBox();
		vbox.getChildren().addAll(getApplicationMenu(primaryStage), tabPane);

		// scene
		Scene scene = new Scene(vbox);

		// get maximized size
		Rectangle2D primaryScreenBounds = Screen.getPrimary().getBounds();
		
		// set stage
		primaryStage.setScene(scene);
		primaryStage.setX(primaryScreenBounds.getMinX());
		primaryStage.setY(primaryScreenBounds.getMinY());
		primaryStage.setWidth(primaryScreenBounds.getWidth());
		primaryStage.setHeight(primaryScreenBounds.getHeight());
		primaryStage.setMaximized(true); // maximized
		primaryStage.setTitle("Cluster Comparer GUI");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
