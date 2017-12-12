package cn.edu.cqupt.view;

import java.io.File;
import java.util.List;

import cn.edu.cqupt.service.ClusterTableService;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
	public static SimpleIntegerProperty pageSize; // the number of items per page
	public static SimpleIntegerProperty pageCount; // the number of total pages
	public static String releaseIName;
	public static String releaseIIName;
	public static Rectangle2D screenBounds; // screen size
	public static Stage window;

	static {
		// get screen size
		screenBounds = Screen.getPrimary().getBounds();

		// set page size and page count listener
		pageSize = new SimpleIntegerProperty(0);
		pageCount = new SimpleIntegerProperty(0);
		pageSize.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				int pageSize = newValue.intValue();
				pageCount.set(serviceReleaseI.getCurrentPageClusters(1, pageSize).getTotalPage());
			}
		});
	}

	private void setIntroTab() {

		// add introduce tab to tab Pane
		Tab introTab = new Tab("Introduce");
		tabPane.getTabs().add(introTab);

		// introduce text
		TextArea introText = new TextArea("manual : the manual will be added soom");

		// add text area to tab
		introTab.setContent(introText);
	}

	private MenuBar getApplicationMenu(Window ownerWindow) {

		// create menu bar
		MenuBar menuBar = new MenuBar();

		// create File menu
		Menu fileMenu = new Menu("File");

		// add "select clustering files" item to File menu
		MenuItem openFile = new MenuItem("Select clustering files");
		openFile.setAccelerator(KeyCombination.keyCombination("Ctrl + O"));

		/*********************** MY TEST *****************/
		// // read data
		// serviceReleaseI = new ClusterTableService(new
		// File("C:\\Users\\huangjs\\Desktop\\clustering\\myData\\compare_5.clustering"));
		// serviceReleaseII = new ClusterTableService(new
		// File("C:\\Users\\huangjs\\Desktop\\clustering\\myData\\compare_6.clustering"));
		//
		// // create cluster table and spectrum table
		// releaseIName = "compare_5.clustering";
		// releaseIIName = "compare_6.clustering";
		// ClusterSelection clusterTable = new ClusterSelection(releaseIName,
		// serviceReleaseI, pageSize,
		// serviceReleaseII.getAllClusters());
		//
		// // set tab pane
		// tabPane.getTabs().clear();
		// Tab tabA = new Tab("Cluster selection");
		// tabA.setContent(clusterTable.getGridPane());
		//
		// Tab tabB = new Tab("Cluster comparison");
		//
		// tabPane.getTabs().addAll(tabA, tabB);
		/***************************************************************/

		openFile.setOnAction((ActionEvent e) -> {

			// set file chooser
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select Clustering File");
			// fileChooser.setInitialDirectory(new
			// File("C:\\Users\\huangjs\\Desktop\\clustering\\myData"));

			// file filter
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

				serviceReleaseI = null;
				serviceReleaseII = null;

				// read data
				serviceReleaseI = new ClusterTableService(clusteringFiles.get(0));
				serviceReleaseII = new ClusterTableService(clusteringFiles.get(1));
				releaseIName = clusteringFiles.get(0).getName();
				releaseIIName = clusteringFiles.get(1).getName();

				// create cluster table and spectrum table
				pageSize.set(8);
				pageCount.set(serviceReleaseI.getCurrentPageClusters(1, pageSize.get()).getTotalPage());
				ClusterSelection clusterTable = new ClusterSelection(releaseIName, serviceReleaseI, pageSize.get(),
						pageCount.get(), serviceReleaseII.getAllClusters());

				// set tab pane
				tabPane.getTabs().clear();
				Tab tabA = new Tab("Cluster selection");
				tabA.setContent(clusterTable.getGridPane());
				tabA.setClosable(false);
				tabPane.getTabs().add(tabA);
			}

		});

		MenuItem importData = new MenuItem("Import Data");
		importData.setAccelerator(KeyCombination.keyCombination("Ctrl + I"));
		importData.setOnAction((ActionEvent event) -> {
			Stage importDataStage = ImportData.getImportDataStage();
			importDataStage.showAndWait();
			if(ImportData.method == null) {
				
			}else if (ImportData.method.equals("FILE")) {

				// read data from file
				serviceReleaseI = null;
				serviceReleaseII = null;
				serviceReleaseI = new ClusterTableService(ImportData.releaseIFile);
				serviceReleaseII = new ClusterTableService(ImportData.releaseIIFile);
				releaseIName = ImportData.releaseIFile.getName();
				releaseIIName = ImportData.releaseIIFile.getName();
				ImportData.releaseIFile = null;
				ImportData.releaseIIFile = null;

				// create cluster table and spectrum table
				pageSize.set(8);
				pageCount.set(serviceReleaseI.getCurrentPageClusters(1, pageSize.get()).getTotalPage());
				ClusterSelection clusterTable = new ClusterSelection(releaseIName, serviceReleaseI, pageSize.get(),
						pageCount.get(), serviceReleaseII.getAllClusters());

				// set tab pane
				tabPane.getTabs().clear();
				Tab tabA = new Tab("Cluster selection");
				tabA.setContent(clusterTable.getGridPane());
				tabA.setClosable(false);
				tabPane.getTabs().add(tabA);
			}else {
				
				// read data from restful
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("It is terrible, developing...");
				alert.showAndWait();
			}
		});

		// add "exit" item to file menu
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction((ActionEvent e) -> {
			System.exit(0);
		});

		fileMenu.getItems().addAll(openFile, importData, exit);

		// create Tools menu
		Menu toolMenu = new Menu("Tools");

		// add "Switch clustering files" item to tool menu
		// MenuItem swap = new MenuItem("Swap principal-subordinate relationship");
		// swap.setOnAction((ActionEvent e) -> {
		//
		// // exchange
		// ClusterTableService tmp = serviceReleaseI;
		// serviceReleaseI = serviceReleaseII;
		// serviceReleaseII = tmp;
		// String tmpName = releaseIName;
		// releaseIName = releaseIIName;
		// releaseIIName = tmpName;
		//
		// // create cluster table and spectrum table
		// ClusterSelection clusterTable = new ClusterSelection(releaseIName,
		// serviceReleaseI, pageSize,
		// serviceReleaseII.getAllClusters());
		// tabPane.getTabs().get(0).setContent(clusterTable.getGridPane());
		// });
		//
		// toolMenu.getItems().add(swap);

		// add menu items into menu bar
		menuBar.getMenus().addAll(fileMenu, toolMenu);
		return menuBar;
	}

	@Override
	public void start(Stage primaryStage) {
		ClusterApplication.hostServices = this.getHostServices();
		ClusterApplication.window = primaryStage;

		// introduce tab
		setIntroTab();

		// vbox
		VBox vbox = new VBox();
		vbox.getChildren().addAll(getApplicationMenu(primaryStage), tabPane);

		// scene
		Scene scene = new Scene(vbox);

		// set stage
		primaryStage.setScene(scene);
		primaryStage.setX(screenBounds.getMinX());
		primaryStage.setY(screenBounds.getMinY());
		primaryStage.setWidth(screenBounds.getWidth());
		primaryStage.setHeight(screenBounds.getHeight());
		primaryStage.setMaximized(true); // maximized
		primaryStage.setTitle("Cluster Comparer GUI");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
