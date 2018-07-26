package cn.edu.cqupt.main.data.stage;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class ClusteringDataStage {
    private FileChooser fileChooser;
    private File clusteringFileI;
    private File clusteringFileII;

    public File getClusteringFileI() {
        return clusteringFileI;
    }

    public File getClusteringFileII() {
        return clusteringFileII;
    }

    public ClusteringDataStage() {
        fileChooser = new FileChooser();
    }

    public BorderPane create(Window window) {

        String[] fileFormatDescriptions = new String[]{"Clustering File", "All File"};
        String[] fileExtensions = new String[]{"*.clustering", "*.*"};
        for (int i = 0; i < fileFormatDescriptions.length; i++) {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(fileFormatDescriptions[i], fileExtensions[i])
            );
        }

        // clusteringI
        Label clusteringINameLabel = new Label("First clustering");
        TextField clusteringIPath = new TextField();
        clusteringIPath.textProperty().addListener((ObservableValue<? extends String> observable,
                                                    String oldValue, String newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                clusteringFileI = new File(newValue);
            }
        });
        Button chooseFileBtn1 = new Button("Choose File");
        setFileChooser(chooseFileBtn1, clusteringIPath, window);

        // clusteringII
        Label clusteringIINameLabel = new Label("Second clustering");
        TextField clusteringIIPath = new TextField();
        clusteringIIPath.textProperty().addListener((ObservableValue<? extends String> observable,
                                                     String oldValue, String newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                clusteringFileII = new File(newValue);
            }
        });
        Button chooseFileBtn2 = new Button("Choose File");
        setFileChooser(chooseFileBtn2, clusteringIIPath, window);

        // submit
        Button submit = new Button("submit");
        submit.setOnMouseClicked((MouseEvent e) -> {
            if (clusteringFileI == null || clusteringFileII == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("You should choose two *.clustering file");
                alert.showAndWait();
            } else {
                submit.getScene().getWindow().hide();
            }
        });
        submit.setPrefWidth(435);
        submit.setMaxWidth(435);
        submit.setMinWidth(435);

        // add builds into pane
        GridPane fileChoosePane = new GridPane();
        fileChoosePane.add(clusteringINameLabel, 0, 0);
        fileChoosePane.add(clusteringIPath, 1, 0);
        fileChoosePane.add(chooseFileBtn1, 2, 0);
        fileChoosePane.add(clusteringIINameLabel, 0, 1);
        fileChoosePane.add(clusteringIIPath, 1, 1);
        fileChoosePane.add(chooseFileBtn2, 2, 1);

        // file choose pane setting
        fileChoosePane.setVgap(20);
        fileChoosePane.setHgap(5);

        // add grid pane into border pane
        BorderPane clusteringDataPane = new BorderPane();
        clusteringDataPane.setCenter(fileChoosePane);
        clusteringDataPane.setBottom(submit);
        BorderPane.setAlignment(submit, Pos.CENTER);
        clusteringDataPane.setPadding(new Insets(30, 10, 10, 10));

        return clusteringDataPane;
    }

    private void setFileChooser(Button btn, TextField tf, Window window) {
        btn.setOnAction(event -> {
            String tmpFilePath = fileChooser.showOpenDialog(window).getPath();
            if (tmpFilePath != null && !tmpFilePath.equals("")) {
                tmpFilePath = tmpFilePath.replace(File.separator, "/");
                tf.setText(tmpFilePath);
            }
        });
    }
}
