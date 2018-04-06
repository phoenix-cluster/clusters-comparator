package cn.edu.cqupt.view;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.mgf.view.MgfInfoDisplayPane;
import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.service.ClusterTableService;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ClusterApplication extends Application {

	public static TabPane tabPane;
	public static HashMap<String, Tab> tabs;
	public static ClusterTableService serviceReleaseI;
	public static ClusterTableService serviceReleaseII;
	public static HostServices hostServices;
	public static SimpleIntegerProperty pageSize; // the number of items per page
	public static SimpleIntegerProperty pageCount; // the number of total pages
	public static String releaseIName;
	public static String releaseIIName;
	public static Rectangle2D screenBounds; // screen size
	public static Stage window;

	public ClusterApplication() {

		// set tab pane and tabs
		tabPane = new TabPane();
		tabs = new HashMap<>();
		tabs.put("Cluster Selection", new Tab("Cluster Selection"));
		tabs.put("Cluster Comparison", new Tab("Cluster Comparison"));
		tabs.put("MGF Selection", new Tab("MGF Selection"));

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

//		// add "select clustering files" item to File menu
//		MenuItem openFile = new MenuItem("Select clustering files");
//		openFile.setAccelerator(KeyCombination.keyCombination("Ctrl + O"));
//
//		/*********************** MY TEST *****************/
//		// // read data
//		// serviceReleaseI = new ClusterTableService(new
//		// File("C:\\Users\\huangjs\\Desktop\\clustering\\myData\\compare_5.clustering"));
//		// serviceReleaseII = new ClusterTableService(new
//		// File("C:\\Users\\huangjs\\Desktop\\clustering\\myData\\compare_6.clustering"));
//		//
//		// // create cluster table and spectrum table
//		// releaseIName = "compare_5.clustering";
//		// releaseIIName = "compare_6.clustering";
//		// ClusterSelection clusterTable = new ClusterSelection(releaseIName,
//		// serviceReleaseI, pageSize,
//		// serviceReleaseII.getAllClusters());
//		//
//		// // set tab pane
//		// tabPane.getTabs().clear();
//		// Tab tabA = new Tab("Cluster selection");
//		// tabA.setContent(clusterTable.getGridPane());
//		//
//		// Tab tabB = new Tab("Cluster comparison");
//		//
//		// tabPane.getTabs().addAll(tabA, tabB);
//		/***************************************************************/
//
//		openFile.setOnAction((ActionEvent e) -> {
//
//			// set file chooser
//			FileChooser fileChooser = new FileChooser();
//			fileChooser.setTitle("Select Clustering File");
//			// fileChooser.setInitialDirectory(new
//			// File("C:\\Users\\huangjs\\Desktop\\clustering\\myData"));
//
//			// file filter
//			fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Clustering file", "*.clustering"),
//					new FileChooser.ExtensionFilter("All files", "*.*"));
//			List<File> clusteringFiles = fileChooser.showOpenMultipleDialog(ownerWindow);
//			if (clusteringFiles == null) {
//
//			} else if (clusteringFiles.size() != 2) {
//				Alert alert = new Alert(AlertType.ERROR);
//				alert.setTitle("Error Dialog");
//				alert.setHeaderText(null);
//				alert.setContentText("You must choose two clustering files");
//				alert.showAndWait();
//			} else {
//
//				serviceReleaseI = null;
//				serviceReleaseII = null;
//
//				// read data
//				serviceReleaseI = new ClusterTableService(clusteringFiles.get(0));
//				serviceReleaseII = new ClusterTableService(clusteringFiles.get(1));
//				releaseIName = clusteringFiles.get(0).getName();
//				releaseIIName = clusteringFiles.get(1).getName();
//
//				// create cluster table and spectrum table
//				pageSize.set(8);
//				pageCount.set(serviceReleaseI.getCurrentPageClusters(1, pageSize.get()).getTotalPage());
//				ClusterSelection clusterTable = new ClusterSelection(releaseIName, serviceReleaseI, pageSize.get(),
//						pageCount.get());
//
//				// set tab pane
//				tabPane.getTabs().clear();
//				Tab tabA = new Tab("Cluster selection");
//				tabA.setContent(clusterTable.getGridPane());
//				tabA.setClosable(false);
//				tabPane.getTabs().add(tabA);
//			}
//
//		});

		MenuItem importData = new MenuItem("Import Data");
		importData.setAccelerator(KeyCombination.keyCombination("Ctrl + I"));
		importData.setOnAction((ActionEvent event) -> {
			ImportDataStage importDataStage = new ImportDataStage();
			Stage stage = importDataStage.getImportDataStage();
			stage.showAndWait();
			if(importDataStage.getImportMethod().equals("MGF FILE")){
				File mgfFileI = importDataStage.getMgfFileI();
				File mgfFileII = importDataStage.getMgfFileII();
				ArrayList<MS> msList1 = null;
				ArrayList<MS> msList2 = null;
				try {
					msList1 = MgfFileReader.getAllSpectra(mgfFileI);
					msList2 = MgfFileReader.getAllSpectra(mgfFileII);
				} catch (IOException e) {
					e.printStackTrace();
				}

				// set pane
				MgfInfoDisplayPane mgfInfoDisplayPane = new MgfInfoDisplayPane(msList1, msList2, 8);
				GridPane pane = mgfInfoDisplayPane.getMgfInfoDisplayPane();
				double screenHeight = screenBounds.getHeight();
				double screenWidth = screenBounds.getWidth();
				double c1Width = screenWidth * 1 / 3;
				double c2Width = screenWidth * 2 / 3;
				double r1Height = 20.0;
				double r2Height = (screenHeight - 40.0) * 1 / 2;
				double r3Height = (screenHeight - 50.0) * 1 / 2;
				ColumnConstraints col1 = new ColumnConstraints(c1Width);
				ColumnConstraints col2 = new ColumnConstraints(c2Width);
				pane.getColumnConstraints().addAll(col1, col2);
				RowConstraints row1 = new RowConstraints(r1Height, r1Height, r1Height);
				RowConstraints row2 = new RowConstraints(r2Height, r2Height, r2Height);
				RowConstraints row3 = new RowConstraints(r3Height, r3Height, r3Height);
				pane.getRowConstraints().addAll(row1, row2, row3);
				pane.setVgap(10.0);


				// add the pane into tab
				Tab mgfSelectionTab = ClusterApplication.tabs.get("MGF Selection");
				if(mgfSelectionTab.getContent() != null){
					new HBox(mgfSelectionTab.getContent());
				}
				mgfSelectionTab.setContent(pane);
				if(!ClusterApplication.tabPane.getTabs().contains(mgfSelectionTab)){
					ClusterApplication.tabPane.getTabs().add(mgfSelectionTab);
				}
				ClusterApplication.tabPane.getSelectionModel().select(mgfSelectionTab);

			} else if (importDataStage.getImportMethod().equals("Clustering FILE")) {

				// read data from file
				serviceReleaseI = null;
				serviceReleaseII = null;
				serviceReleaseI = new ClusterTableService(importDataStage.getReleaseIFile());
				serviceReleaseII = new ClusterTableService(importDataStage.getReleaseIIFile());
				releaseIName = importDataStage.getReleaseIFile().getName();
				releaseIIName = importDataStage.getReleaseIIFile().getName();

				// create cluster table and spectrum table
				pageSize.set(8);
				pageCount.set(serviceReleaseI.getCurrentPageClusters(1, pageSize.get()).getTotalPage());
				ClusterSelection clusterTable = new ClusterSelection(releaseIName, serviceReleaseI, pageSize.get(),
						pageCount.get());

				// set tab pane
				tabPane.getTabs().clear();
				Tab tabA = new Tab("Cluster selection");
				tabA.setContent(clusterTable.getGridPane());
				tabA.setClosable(false);
				tabPane.getTabs().add(tabA);
			}else if (importDataStage.getImportMethod().equals("RESTFUL")) {
				
				// read data from restful
				serviceReleaseI = null;
				serviceReleaseII = null;
				releaseIName = importDataStage.getReleaseIName();
				releaseIIName = importDataStage.getReleaseIIName();
				serviceReleaseI = new ClusterTableService(releaseIName,
						importDataStage.getOrderKey(), importDataStage.getOrderDirection(),
						importDataStage.getStartIndex(), importDataStage.getEndIndex());
				serviceReleaseII = new ClusterTableService(releaseIIName,
						importDataStage.getOrderKey(), importDataStage.getOrderDirection(),
						importDataStage.getStartIndex(), importDataStage.getEndIndex());

				// create cluster table and spectrum table
				pageSize.set(1);
				pageCount.set(serviceReleaseI.getCurrentPageClusters(1, pageSize.get())
						.getTotalPage());
				ClusterSelection clusterTable = new ClusterSelection(releaseIName, serviceReleaseI,
						pageSize.get(),	pageCount.get());

				// set tab pane
				tabPane.getTabs().clear();
				Tab tabA = new Tab("Cluster selection");
				tabA.setContent(clusterTable.getGridPane());
				tabA.setClosable(false);
				tabPane.getTabs().add(tabA);
			}else{
				System.err.print("Data Import Error, Please Re-import!");
			}
		});

		// add "exit" item to file menu
		MenuItem exit = new MenuItem("Exit");
		exit.setOnAction((ActionEvent e) -> {
			System.exit(0);
		});

		fileMenu.getItems().addAll(importData, exit);

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
