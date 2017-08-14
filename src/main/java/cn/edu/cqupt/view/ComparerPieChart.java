package cn.edu.cqupt.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.service.SpectrumComparerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ComparerPieChart {
	private PieChart comparerPieChart;

	public PieChart getComparerPieChart() {
		return comparerPieChart;
	}

	public void setComparerPieChart(PieChart comparerPieChart) {
		this.comparerPieChart = comparerPieChart;
	}

	public ComparerPieChart() {

	}
	
	public ComparerPieChart(Cluster cluster, List<Cluster> releaseCluster) {
		createComparerPieChart(cluster, releaseCluster);
	}

	public void createComparerPieChart(Cluster cluster, List<Cluster> releaseCluster) {

		// get source data
		SpectrumComparerService comparer = null;
		try {
			comparer = new SpectrumComparerService(cluster, releaseCluster);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		HashMap<String, Integer> overlapSpectrumCount = comparer.getOverlapSpectrumCount();
		HashMap<String, Cluster> overlapCluster = comparer.getOverlapCluster();
		HashMap<String, List<Spectrum>> overlapSpectrumOfCluster = comparer.getOverlapSpectrumOfCluster();

		// organize pie data
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		Iterator<Entry<String, Integer>> interItr = overlapSpectrumCount.entrySet().iterator();
		while (interItr.hasNext()) {
			Entry<String, Integer> entry = interItr.next();
			String clusterId = entry.getKey();
			BigDecimal bd = new BigDecimal((double) entry.getValue() / cluster.getSpecCount() * 100);
			double rate = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();
			pieChartData.add(new PieChart.Data(
					clusterId + "\ncount : " + entry.getValue() + "\npercentage : " + rate + "%", entry.getValue()));
		}

		// paint pie chart
		this.comparerPieChart = new PieChart(pieChartData);

		// set pie chart : title and legend
		this.comparerPieChart.setTitle(cluster.getId() + "'s spectrum composition");
		this.comparerPieChart.setLegendVisible(false);
		this.comparerPieChart.setMinSize(800, 500);
		// this.comparerPieChart.setStyle("-fx-border-color: black;");

		// add event : print spectrum tables of two overlap clusters
		for (PieChart.Data data : this.comparerPieChart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					
					VBox vbox = new VBox();
					
					// get tabB
					Tab tabB = null;
					if (ClusterApplication.tabPane.getTabs().size() == 2)
						tabB = ClusterApplication.tabPane.getTabs().get(1);
					else {
						tabB = new Tab("Cluster comparison");
						ClusterApplication.tabPane.getTabs().add(tabB);
					}
					
					// redirect to tabB
					ClusterApplication.tabPane.getSelectionModel().select(1);
					
					// obtain clusterID
					String clusterId = data.getName().split("\\n")[0];

					/*// source spectrum table
					SpectrumTable sourceSpectrumTable = new SpectrumTable(cluster.getSpectrums(),
							overlapSpectrumOfCluster.get(clusterId));

					// object spectrum table
					SpectrumTable objectSpectrumTable = new SpectrumTable(overlapCluster.get(clusterId).getSpectrums(),
							overlapSpectrumOfCluster.get(clusterId));*/
					SpectrumTable spectrumTable = new SpectrumTable(cluster.getSpectra(), overlapCluster.get(clusterId).getSpectra(),
							overlapSpectrumOfCluster.get(clusterId));
					GridPane overlapSpecs = spectrumTable.getOverlapSpecs();
					// System.out.println(cluster.getId() + " : " + cluster.getSpecCount());
					// System.out.println("overlap spectrum counts" + " : " +
					// overlapSpectrumOfCluster.get(clusterId).size());
					// System.out.println(overlapCluster.get(clusterId).getId() + " : " +
					// overlapCluster.get(clusterId).getSpecCount());

					// add color picker for two spectrum table
					
					
					// peak map
					PeakMap peakMap = new PeakMap(cluster.getMzValues(), cluster.getIntensValues(),
							overlapCluster.get(clusterId).getMzValues(),
							overlapCluster.get(clusterId).getIntensValues());
					
					// add bulids into tabB
					vbox.getChildren().addAll(overlapSpecs, peakMap.getVbox());
					tabB.setContent(vbox);
				}
			});
		}
	}
}