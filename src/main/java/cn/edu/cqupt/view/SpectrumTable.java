package cn.edu.cqupt.view;

import java.util.List;

import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.util.ColorUtil;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class SpectrumTable {

	private TableView<Spectrum> spectrumTable;
	private GridPane overlapSpecs;
	private String[] colName = { "Spectrum ID", "charge", "precursorMz", "species" };
	private String[] property = { "spectrumId", "charge", "precursorMz", "species" };

	public TableView<Spectrum> getSpectrumTable() {
		return spectrumTable;
	}

	public void setSpectrumTable(TableView<Spectrum> spectrumTable) {
		this.spectrumTable = spectrumTable;
	}

	public SpectrumTable() {

	}

	public GridPane getOverlapSpecs() {
		return overlapSpecs;
	}

	public void setOverlapSpecs(GridPane overlapSpecs) {
		this.overlapSpecs = overlapSpecs;
	}

	public SpectrumTable(List<Spectrum> spectrums) {
		spectrumTable = new TableView<>();
		spectrumTable.setEditable(true);
		ObservableList<Spectrum> observableTableData = FXCollections.observableList(spectrums);
		// set table: table head and table cell value type

		for (int i = 0; i < colName.length; i++) {
			TableColumn<Spectrum, String> col = new TableColumn<>(colName[i]);
			col.setCellValueFactory(new PropertyValueFactory<Spectrum, String>(property[i]));
			spectrumTable.getColumns().add(col);
		}

		// set size
		spectrumTable.setPrefSize(800, 400);

		// set column resize policy
		spectrumTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// set data
		spectrumTable.setItems(observableTableData);
	}

	public SpectrumTable(List<Spectrum> spectrums, List<Spectrum> overlapSpectrums) {
		this(spectrums);
		// System.out.println("overlapSpectrums=>");
		// for (Spectrum spec : overlapSpectrums) {
		// System.out.println(spec.getSpectrumId());
		// }
		// System.out.println();
		this.spectrumTable.setRowFactory(row -> new TableRow<Spectrum>() {
			@Override
			public void updateItem(Spectrum item, boolean empty) {
				if (item != null && !empty) {
					if (overlapSpectrums.indexOf(item) != -1)
						setStyle("-fx-background-color:yellow");
					else
						setStyle("");
				}
			}
		});
	}

	public SpectrumTable(List<Spectrum> spectrums1, List<Spectrum> spectrums2, List<Spectrum> overlapSpectrums) {
		SpectrumTable spectrumTable1 = new SpectrumTable(spectrums1);
		SpectrumTable spectrumTable2 = new SpectrumTable(spectrums2);
		ColorPicker colorPicker = new ColorPicker(Color.BEIGE);
		this.overlapSpecs = new GridPane();

		// add builds in grid pane
//		overlapSpecs.add(colorPicker, 0, 0, 2, 1);
		overlapSpecs.add(spectrumTable1.getSpectrumTable(), 0, 1);
		overlapSpecs.add(spectrumTable2.getSpectrumTable(), 1, 1);

		//
		spectrumTable1.renderRow(spectrumTable1, overlapSpectrums, Color.BEIGE);
		spectrumTable2.renderRow(spectrumTable2, overlapSpectrums, Color.BEIGE);
		
		// add event for color picker
		colorPicker.setOnAction((ActionEvent e) -> {
			spectrumTable1.getSpectrumTable().setItems(FXCollections.observableArrayList(spectrums1));
			spectrumTable1.renderRow(spectrumTable1, overlapSpectrums, colorPicker.getValue());
			spectrumTable2.getSpectrumTable().setItems(FXCollections.observableArrayList(spectrums2));
			spectrumTable2.renderRow(spectrumTable2, overlapSpectrums, colorPicker.getValue());
			
		});

	}

	private void renderRow(SpectrumTable spectrumTable, List<Spectrum> overlapSpectrums, Color color) {

		// render overlap row
		spectrumTable.getSpectrumTable().setRowFactory(row -> new TableRow<Spectrum>() {
			@Override
			public void updateItem(Spectrum item, boolean empty) {
				if (item != null && !empty) {
					if (overlapSpectrums.indexOf(item) != -1)
						setStyle("-fx-background-color: " + ColorUtil.colorToHex(color));
					else
						setStyle("");
				}
			}
		});
	}
}
