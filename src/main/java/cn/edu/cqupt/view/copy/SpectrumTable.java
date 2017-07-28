package cn.edu.cqupt.view.copy;

import java.util.List;

import cn.edu.cqupt.model.Spectrum;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class SpectrumTable {

	private TableView<Spectrum> spectrumTable;
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
//		System.out.println("overlapSpectrums=>");
//		for (Spectrum spec : overlapSpectrums) {
//			System.out.println(spec.getSpectrumId());
//		}
//		System.out.println();
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
}
