package cn.edu.cqupt.sample;

import java.util.ArrayList;
import java.util.List;

import javax.swing.SwingUtilities;

import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import uk.ac.ebi.pride.toolsuite.mzgraph.chart.data.annotation.IonAnnotation;
import uk.ac.ebi.pride.toolsuite.mzgraph.chart.data.annotation.IonAnnotationInfo;
import uk.ac.ebi.pride.toolsuite.mzgraph.chart.graph.SpectrumPanel;
import uk.ac.ebi.pride.utilities.iongen.ion.FragmentIonType;
import uk.ac.ebi.pride.utilities.mol.NeutralLoss;

public class MZTest extends Application {
	SwingNode swingNode = new SwingNode();
	
	@Override
	public void start(Stage stage) {
		StackPane stackPane = new StackPane();
		stackPane.getChildren().add(swingNode);
		Scene scene = new Scene(stackPane, 500, 500);

		createAndSetSwingContent();
		
		
		stage.setScene(scene);
		stage.show();
	}

	private void createAndSetSwingContent() {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
//				// Create a m/z data array
//				double[] mzArr = new double[]{1.0, 2.012312313, 3.0, 4.234, 6.0, 7.34342};
//				// Create an intensity data array
//				double[] intensArr = new double[]{2.0, 4.345345345, 6.0, 1.4545, 5.0, 8.23423};
//
//				// Create a spectrum panel
//				SpectrumPanel spectrum = new SpectrumPanel(mzArr, intensArr);
				
				
				SpectrumPanel spectrum = new SpectrumPanel();

				// Paint the spectrum peaks
				spectrum.paintGraph();
				
				// New m/z array
				double[] newMz = new double[]{2.0, 3.0, 12.23, 1.45};
				// New intensity array
				double[] newIntent = new double[]{45, 67, 18.34, 34.78};
				// Set a new peak list
				spectrum.setPeaks(newMz, newIntent);
				
				// Create a new y ion with charge -2 and location 2 as well as a water loss
				IonAnnotationInfo yIonInfo = new IonAnnotationInfo();
				// Create and add an annotation item which describes the ion.
				IonAnnotationInfo.Item yIonItem = new IonAnnotationInfo.Item(-2, FragmentIonType.Y_ION, 2, NeutralLoss.WATER_LOSS);
				yIonInfo.addItem(yIonItem);
				// Create the y ion
				IonAnnotation yIon = new IonAnnotation(2.0, 45, yIonInfo);

				// Create a new b ion with charge +1 and location 3
				IonAnnotationInfo bIonInfo = new IonAnnotationInfo();
				IonAnnotationInfo.Item bIonItem = new IonAnnotationInfo.Item(1, FragmentIonType.B_ION, 3, null);
				bIonInfo.addItem(bIonItem);
				IonAnnotation bIon = new IonAnnotation(12.23, 18.34, bIonInfo);

				// Add these ions to the spectrum
				List<IonAnnotation> ions = new ArrayList<IonAnnotation>();
				ions.add(yIon);
				ions.add(bIon);
				spectrum.addFragmentIons(ions);
				
				// Set the length of peptide and PTMs as annotation parameters
//				spectrum.setAminoAcidAnnotationParameters(8, modifications);
				spectrum.addFragmentIons(ions);

				// Attaches a JComponent instance to display in this SwingNode.
				swingNode.setContent(spectrum);
				
			}
		});
	}

	public static void main(String[] args) {
		launch(args);
	}
}
