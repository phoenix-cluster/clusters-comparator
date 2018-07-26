package cn.edu.cqupt.main.data.stage;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Window;

import java.io.File;

public class MgfDataStage {
    private FileChooser fileChooser;
    private File mgfFileI;
    private File mgfFileII;

    public File getMgfFileI() {
        return mgfFileI;
    }

    public File getMgfFileII() {
        return mgfFileII;
    }

    public MgfDataStage() {
        fileChooser = new FileChooser();
    }

    public BorderPane create(Window window) {

        String[] fileFormatDescriptions = new String[]{"MGF File", "All File"};
        String[] fileExtensions = new String[]{"*.mgf", "*.*"};
        for (int i = 0; i < fileFormatDescriptions.length; i++) {
            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(fileFormatDescriptions[i], fileExtensions[i])
            );
        }

        // first mgf file
        Label label1 = new Label("First MGF");
        TextField mgfFileIPath = new TextField();
        mgfFileIPath.textProperty().addListener((ObservableValue<? extends String> observable,
                                                 String oldValue, String newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                mgfFileI = new File(newValue);
            }
        });
        Button chooseFileBtn1 = new Button("Choose File");
        setFileChooser(chooseFileBtn1, mgfFileIPath, window);


        // second mgf file
        Label label2 = new Label("Second MGF");
        TextField mgfFileIIPath = new TextField();
        mgfFileIPath.textProperty().addListener((ObservableValue<? extends String> observable,
                                                 String oldValue, String newValue) -> {
            if (newValue != null && !newValue.equals("")) {
                mgfFileII = new File(newValue);
            }
        });
        Button chooseFileBtn2 = new Button("Choose File");
        setFileChooser(chooseFileBtn2, mgfFileIIPath, window);


        // submit
        Button submit = new Button("submit");
        submit.setOnMouseClicked(event -> {
            if (mgfFileI == null || mgfFileII == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText(null);
                alert.setContentText("You should choose two *.mgf file");
                alert.showAndWait();
            } else {
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
        BorderPane mgfDataStage = new BorderPane();
        mgfDataStage.setCenter(centerPane);
        mgfDataStage.setBottom(submit);
        BorderPane.setAlignment(submit, Pos.CENTER);
        mgfDataStage.setPadding(new Insets(30, 10, 10, 10));

        return mgfDataStage;
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
