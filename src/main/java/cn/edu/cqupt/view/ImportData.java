package cn.edu.cqupt.view;

import java.io.File;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

public class ImportData {
	public static String method; // FILE or RESTFUL
	public static File releaseIFile;
	public static File releaseIIFile;
	private Window window;

	private static GridPane restfulDataPane() {
		/*********************************/
		ArrayList<String> allReleaseName = new ArrayList<>();
		allReleaseName.add("releaseI");
		allReleaseName.add("releaseII");
		allReleaseName.add("releaseIII");
		allReleaseName.add("releaseIV");

		ArrayList<String> allSortMethod = new ArrayList<>();
		allSortMethod.add("Sorted by spectra count");
		allSortMethod.add("Sorted by ratio");
		allSortMethod.add("Sorted by network graph edges count");
		/*********************************/

		// prepare data
		ObservableList<String> releaseItems = FXCollections.observableArrayList(allReleaseName);
		ObservableList<String> sortMethodItems = FXCollections.observableArrayList(allSortMethod);

		// releaseI
		Label releaseINameLabel = new Label("First Release");
		ChoiceBox<String> releaseIName = new ChoiceBox<>(releaseItems);

		// releaseII
		Label releaseIINameLabel = new Label("Second Release");
		ChoiceBox<String> releaseIIName = new ChoiceBox<>(releaseItems);

		// sort method
		Label sortMethodLabel = new Label("Sort Method");
		ChoiceBox<String> sortMethod = new ChoiceBox<>(sortMethodItems);

		// range
		Label rangeLabel = new Label("Set Data Range");
		Label fromLabel = new Label("From");
		TextField fromText = new TextField();
		Label linkLine = new Label("-");
		Label toLabel = new Label("To");
		TextField toText = new TextField();

		fromText.setPrefWidth(55);
		fromText.setMinWidth(55);
		fromText.setMaxWidth(55);
		toText.setPrefWidth(55);
		toText.setMinWidth(55);
		toText.setMaxWidth(55);
		HBox hbox = new HBox(fromLabel, fromText, linkLine, toLabel, toText);
		hbox.setAlignment(Pos.CENTER_LEFT);

		// submit
		Button submit = new Button("submit");
		submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent event) {
				ImportData.method = "RESTFUL";
				submit.getScene().getWindow().hide();
			}
		});
		submit.setPrefWidth(435);
		submit.setMaxWidth(435);
		submit.setMinWidth(435);

		// add builds
		GridPane restfulDataPane = new GridPane();
		restfulDataPane.add(releaseINameLabel, 0, 0);
		restfulDataPane.add(releaseIName, 1, 0);
		restfulDataPane.add(releaseIINameLabel, 0, 1);
		restfulDataPane.add(releaseIIName, 1, 1);
		restfulDataPane.add(sortMethodLabel, 0, 2);
		restfulDataPane.add(sortMethod, 1, 2);
		restfulDataPane.add(rangeLabel, 0, 3);
		restfulDataPane.add(hbox, 1, 3);
		restfulDataPane.add(submit, 0, 4, 2, 1);

		// restful pane setting
		restfulDataPane.setVgap(20);
		restfulDataPane.setHgap(5);
		restfulDataPane.setPadding(new Insets(10, 10, 10, 10));
		return restfulDataPane;
	}

	private static BorderPane fileDataPane(Window window) {

		// releaseI
		Label releaseINameLabel = new Label("First Release");
		TextField releaseIPath = new TextField();
		Button chooseFile1 = new Button("Choose File");
		chooseFile1.setOnAction((ActionEvent event) -> {
			FileChooser releaseI = new FileChooser();
			releaseI.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Clustering file", "*.clustering"),
					new FileChooser.ExtensionFilter("All files", "*.*"));
			releaseIFile = releaseI.showOpenDialog(window);

			// set text field
			if (releaseIFile != null)
				releaseIPath.setText(releaseIFile.toString());
		});

		// releaseII
		Label releaseIINameLabel = new Label("Second Release");
		TextField releaseIIPath = new TextField();
		Button chooseFile2 = new Button("Choose File");
		chooseFile2.setOnAction((ActionEvent event) -> {
			FileChooser releaseII = new FileChooser();
			releaseII.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Clustering file", "*.clustering"),
					new FileChooser.ExtensionFilter("All files", "*.*"));
			releaseIIFile = releaseII.showOpenDialog(window);

			// set text field
			if (releaseIIFile != null)
				releaseIIPath.setText(releaseIIFile.toString());
		});

		// submit
		Button submit = new Button("submit");
		submit.setOnMouseClicked((MouseEvent e) -> {
			if (releaseIFile == null || releaseIIFile == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You should choose two *.clustering file");
				alert.showAndWait();
			} else {
				ImportData.method = "FILE";
				submit.getScene().getWindow().hide();
			}
		});
		submit.setPrefWidth(435);
		submit.setMaxWidth(435);
		submit.setMinWidth(435);

		// add builds into pane
		GridPane fileChoosePane = new GridPane();
		fileChoosePane.add(releaseINameLabel, 0, 0);
		fileChoosePane.add(releaseIPath, 1, 0);
		fileChoosePane.add(chooseFile1, 2, 0);
		fileChoosePane.add(releaseIINameLabel, 0, 1);
		fileChoosePane.add(releaseIIPath, 1, 1);
		fileChoosePane.add(chooseFile2, 2, 1);

		// file choose pane setting
		fileChoosePane.setVgap(20);
		fileChoosePane.setHgap(5);

		// add grid pane into border pane
		BorderPane fileDataPane = new BorderPane();
		fileDataPane.setCenter(fileChoosePane);
		fileDataPane.setBottom(submit);
		BorderPane.setAlignment(submit, Pos.CENTER);
		fileDataPane.setPadding(new Insets(30, 10, 10, 10));

		return fileDataPane;
	}

	public TabPane getImportDataPane() {
		TabPane importDataPane = new TabPane();

		// file
		Tab fileTab = new Tab("Get Data From File");
		fileTab.setContent(fileDataPane(window));
		importDataPane.getTabs().add(fileTab);

		// restful
		Tab restfulTab = new Tab("Get Data From Database");
		restfulTab.setContent(restfulDataPane());
		importDataPane.getTabs().add(restfulTab);

		// import data pane setting
		// importDataPane.setPrefSize(458, 328);
		// importDataPane.setMaxSize(458, 328);
		// importDataPane.setMinSize(458, 328);

		return importDataPane;
	}

	public static Stage getImportDataStage() {
		Stage importDataStage = new Stage(); // return value
		TabPane importDataPane = new TabPane(); // file data pane and restful data pane

		// file
		Tab fileTab = new Tab("Get Data From File");
		fileTab.setContent(fileDataPane(importDataStage));
		importDataPane.getTabs().add(fileTab);

		// restful
		Tab restfulTab = new Tab("Get Data From Database");
		restfulTab.setContent(restfulDataPane());
		importDataPane.getTabs().add(restfulTab);

		Scene scene = new Scene(importDataPane);
		importDataStage.setScene(scene);
		return importDataStage;
	}

	// @Override
	// public void start(Stage primaryStage) throws Exception {
	// // window = primaryStage;
	// // Scene scene = new Scene(getImportDataPane());
	// // primaryStage.setScene(scene);
	// // primaryStage.show();
	//
	// Stage importDataStage = getImportDataStage();
	//
	// Button importDataButton = new Button("import");
	// importDataButton.setOnAction((ActionEvent event) -> {
	// importDataStage.showAndWait();
	// });
	//
	// Scene scene = new Scene(importDataButton);
	// primaryStage.setScene(scene);
	// primaryStage.show();
	// }
	//
	// public static void main(String[] args) {
	// launch(args);
	// }
}
