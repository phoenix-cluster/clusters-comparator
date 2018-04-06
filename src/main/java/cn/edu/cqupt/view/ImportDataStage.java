package cn.edu.cqupt.view;

import java.io.File;
import java.util.ArrayList;

import cn.edu.cqupt.model.Direction;
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

public class ImportDataStage {
	private String importMethod; // file or RESTFUL
	private File mgfFileI; // mgf file support
	private File mgfFileII; // mgf file support
	private File releaseIFile; // clustering file support
	private File releaseIIFile; // clustering file support
	private String releaseIName; // RESTFUL support
	private String releaseIIName; // RESTFUL support
	private String orderKey; // restful support
	private String orderDirection; // restful support
	private int startIndex; // restful support
	private int endIndex; // restful support


	public String getImportMethod() {
		return importMethod;
	}

	public File getMgfFileI() {
		return mgfFileI;
	}

	public File getMgfFileII() {
		return mgfFileII;
	}

	public File getReleaseIFile() {
		return releaseIFile;
	}

	public File getReleaseIIFile() {
		return releaseIIFile;
	}

	public String getReleaseIName() {
		return releaseIName;
	}

	public String getReleaseIIName() {
		return releaseIIName;
	}

	public String getOrderKey() {
		return orderKey;
	}

	public String getOrderDirection() {
		return orderDirection;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public int getEndIndex() {
		return endIndex;
	}


	private GridPane restfulDataPane() {
		/*********************************/
		ArrayList<String> allReleaseName = new ArrayList<>();
		allReleaseName.add("compare_5_clusters");
		allReleaseName.add("releaseII");
		allReleaseName.add("releaseIII");
		allReleaseName.add("releaseIV");

		ArrayList<String> allOrderKey = new ArrayList<>();
		allOrderKey.add("apm");
		allOrderKey.add("ration");
		allOrderKey.add("network graph edges count");

		ArrayList<String> allOrderDirection = new ArrayList<>();
		allOrderDirection.add(Direction.ASC.getDirection());
		allOrderDirection.add(Direction.DES.getDirection());
		/*********************************/

		// prepare data
		ObservableList<String> releaseItems = FXCollections.observableArrayList(allReleaseName);
		ObservableList<String> sortMethodItems = FXCollections.observableArrayList(allOrderKey);
		ObservableList<String> directionItems = FXCollections.observableArrayList(allOrderDirection);

		// releaseI
		Label releaseINameLabel = new Label("First Release");
		ChoiceBox<String> releaseINameBox = new ChoiceBox<>(releaseItems);
		releaseINameBox.setValue(releaseItems.get(0)); // set default value

		// releaseII
		Label releaseIINameLabel = new Label("Second Release");
		ChoiceBox<String> releaseIINameBox = new ChoiceBox<>(releaseItems);
		releaseIINameBox.setValue(releaseItems.get(0)); // set default value

		// order key
		Label orderKeyLabel = new Label("Ordered by");
		ChoiceBox<String> orderKeyBox = new ChoiceBox<>(sortMethodItems);
		orderKeyBox.setValue(allOrderKey.get(0));

		// order direction
		Label orderDirectionLabel = new Label("Order");
		ChoiceBox<String> orderDirectionBox = new ChoiceBox<>(directionItems);
		orderDirectionBox.setValue(allOrderDirection.get(0));

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
				importMethod = "RESTFUL";
				releaseIName = releaseINameBox.getValue();
				releaseIIName = releaseIINameBox.getValue();
				orderKey = orderKeyBox.getValue();
				orderDirection = orderDirectionBox.getValue();
				startIndex = Integer.parseInt(fromText.getText());
				endIndex = Integer.parseInt(toText.getText());
				submit.getScene().getWindow().hide();
			}
		});
		submit.setPrefWidth(435);
		submit.setMaxWidth(435);
		submit.setMinWidth(435);

		// add builds
		GridPane restfulDataPane = new GridPane();
		restfulDataPane.add(releaseINameLabel, 0, 0);
		restfulDataPane.add(releaseINameBox, 1, 0);
		restfulDataPane.add(releaseIINameLabel, 0, 1);
		restfulDataPane.add(releaseIINameBox, 1, 1);
		restfulDataPane.add(orderKeyLabel, 0, 2);
		restfulDataPane.add(orderKeyBox, 1, 2);
		restfulDataPane.add(orderDirectionLabel, 0, 3);
		restfulDataPane.add(orderDirectionBox, 1, 3);
		restfulDataPane.add(rangeLabel, 0, 4);
		restfulDataPane.add(hbox, 1, 4);
		restfulDataPane.add(submit, 0, 5, 2, 1);

		// restful pane setting
		restfulDataPane.setVgap(20);
		restfulDataPane.setHgap(5);
		restfulDataPane.setPadding(new Insets(10, 10, 10, 10));
		return restfulDataPane;
	}

	private BorderPane clusteringFileImportPane(Window window) {

		// releaseI
		Label releaseINameLabel = new Label("First Release");
		TextField releaseIPath = new TextField();
		Button chooseFile1 = new Button("Choose File");
		chooseFile1.setOnAction(event -> {
			FileChooser releaseI = new FileChooser();
			releaseI.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Clustering file", "*.clustering"),
					new FileChooser.ExtensionFilter("All files", "*.*")
			);
			releaseIFile = releaseI.showOpenDialog(window);

			// set text field
			if (releaseIFile != null)
				releaseIPath.setText(releaseIFile.getPath());
		});

		// releaseII
		Label releaseIINameLabel = new Label("Second Release");
		TextField releaseIIPath = new TextField();
		Button chooseFile2 = new Button("Choose File");
		chooseFile2.setOnAction(event -> {
			FileChooser releaseII = new FileChooser();
			releaseII.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("Clustering file", "*.clustering"),
					new FileChooser.ExtensionFilter("All files", "*.*")
			);
			releaseIIFile = releaseII.showOpenDialog(window);

			// set text field
			if (releaseIIFile != null)
				releaseIIPath.setText(releaseIIFile.getPath());
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
				importMethod = "Clustering FILE";
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

	private BorderPane mgfFileImportPane(Window window) {

		// first mgf file
		Label label1 = new Label("First MGF");
		TextField mgfFileIPath = new TextField();
		Button chooseFileBtn1 = new Button("Choose File");
		chooseFileBtn1.setOnAction(event -> {
			FileChooser mgfFileIChooser = new FileChooser();
			mgfFileIChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("MGF file", "*.mgf"),
					new FileChooser.ExtensionFilter("All files", "*.*")
			);
			mgfFileI = mgfFileIChooser.showOpenDialog(window);

			// set text field
			if (mgfFileI != null)
				mgfFileIPath.setText(mgfFileI.getPath());
		});

		// second mgf file
		Label label2 = new Label("Second MGF");
		TextField mgfFileIIPath = new TextField();
		Button chooseFileBtn2 = new Button("Choose File");
		chooseFileBtn2.setOnAction(event -> {
			FileChooser mgfFileIIChooser = new FileChooser();
			mgfFileIIChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("MGF file", "*.mgf"),
					new FileChooser.ExtensionFilter("All file", "*.*")
			);
			mgfFileII = mgfFileIIChooser.showOpenDialog(window);
			if(mgfFileII != null){
				mgfFileIIPath.setText(mgfFileII.getPath());
			}
		});

		// submit
		Button submit = new Button("submit");
		submit.setOnMouseClicked(event -> {
			if (mgfFileI == null || mgfFileII == null) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error Dialog");
				alert.setHeaderText(null);
				alert.setContentText("You should choose two *.mgf file");
				alert.showAndWait();
			} else {
				importMethod = "MGF FILE";
				submit.getScene().getWindow().hide();
			}
		});
		submit.setPrefWidth(435);
		submit.setMaxWidth(435);
		submit.setMinWidth(435);

		// add builds into pane
		GridPane centerPane = new GridPane();
		centerPane.add(label1, 0, 0);
		centerPane.add(mgfFileIPath, 1, 0);
		centerPane.add(chooseFileBtn1, 2, 0);
		centerPane.add(label2, 0, 1);
		centerPane.add(mgfFileIIPath, 1, 1);
		centerPane.add(chooseFileBtn2, 2, 1);

		// file choose pane setting
		centerPane.setVgap(20);
		centerPane.setHgap(5);

		// add grid pane into border pane
		BorderPane mgfFileImportPane = new BorderPane();
		mgfFileImportPane.setCenter(centerPane);
		mgfFileImportPane.setBottom(submit);
		BorderPane.setAlignment(submit, Pos.CENTER);
		mgfFileImportPane.setPadding(new Insets(30, 10, 10, 10));

		return mgfFileImportPane;
	}

	public Stage getImportDataStage() {
		Stage importDataStage = new Stage(); // return value
		TabPane importDataPane = new TabPane(); // file data pane and restful data pane

		// mgf file
		Tab mgfFileTab = new Tab("Get Data From MGF File");
		mgfFileTab.setContent(mgfFileImportPane(importDataStage));
		importDataPane.getTabs().add(mgfFileTab);

		// clustering file
		Tab fileTab = new Tab("Get Data From Clustering File");
		fileTab.setContent(clusteringFileImportPane(importDataStage));
		importDataPane.getTabs().add(fileTab);

		// restful
		Tab restfulTab = new Tab("Get Data From Database");
		restfulTab.setContent(restfulDataPane());
		importDataPane.getTabs().add(restfulTab);

		Scene scene = new Scene(importDataPane);
		importDataStage.setScene(scene);
		return importDataStage;
	}
}
