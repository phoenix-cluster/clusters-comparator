package cn.edu.cqupt.view;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.model.Vertex;
import cn.edu.cqupt.service.SpectrumComparerService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Side;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Tab;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class ComparerPieChart {
	private PieChart comparerPieChart;
	private GridPane tabBGridPane;
	private String releaseIName;
	private String releaseIIName;

	public PieChart getComparerPieChart() {
		return comparerPieChart;
	}

	public ComparerPieChart() {
		this.tabBGridPane = new GridPane();

		ColumnConstraints col1 = new ColumnConstraints();
		col1.setPercentWidth(50.0);
		ColumnConstraints col2 = new ColumnConstraints();
		col2.setPercentWidth(50.0);
		tabBGridPane.getColumnConstraints().addAll(col1, col2);

		tabBGridPane.setGridLinesVisible(true);
	}

	public ComparerPieChart(String releaseIName, String releaseIIName, Cluster cluster, List<Cluster> releaseCluster) {
		this();
		this.releaseIName = releaseIName;
		this.releaseIIName = releaseIIName;
		this.comparerPieChart = createComparerPieChart(cluster, releaseCluster);
	}

	public ComparerPieChart(String releaseIName, String releaseIIName, UndirectedGraph<Vertex, Edge> undirectedGraph, Vertex focusVertex) {
		this();
		this.releaseIName = releaseIName;
		this.releaseIIName = releaseIIName;
		this.comparerPieChart = createComparerPieChart(undirectedGraph, focusVertex);
	}

	public PieChart createComparerPieChart(Cluster cluster, List<Cluster> releaseCluster) {

		// return value
		PieChart pieChart = new PieChart();
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

			// calculate percentage
			String clusterId = entry.getKey();
			BigDecimal bd = new BigDecimal((double) entry.getValue() / cluster.getSpecCount() * 100);
			double rate = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			// add data
			pieChartData.add(new PieChart.Data(
					clusterId + "\ncount : " + entry.getValue() + "\npercentage : " + rate + "%", entry.getValue()));
		}

		// set pie chart data
		pieChart.setData(pieChartData);

		// set pie chart : title and legend
		pieChart.setTitle(cluster.getId() + "'s spectrum composition");
		pieChart.setLabelsVisible(false);
		pieChart.setLegendVisible(true);
		pieChart.setLegendSide(Side.BOTTOM);

		// set size
		pieChart.setMinSize(ClusterApplication.screenBounds.getWidth() * 0.8 / 3,
				ClusterApplication.screenBounds.getHeight() * 0.40);

		// add event : print spectrum tables of two overlap clusters
		for (PieChart.Data data : pieChart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					// get tabB
					Tab tabB = null;
					if (ClusterApplication.tabPane.getTabs().size() == 1) {
						tabB = new Tab("Cluster comparison");
						ClusterApplication.tabPane.getTabs().add(tabB);
					}else {
						tabB = ClusterApplication.tabPane.getTabs().get(1);
					}

					// obtain clusterID
					String clusterId = data.getName().split("\\n")[0];

					// get source cluster's spectrum table and object cluster's spectrum
					SpectrumTable spectrumTable = new SpectrumTable(
							releaseIName, cluster.getId(), cluster.getSpectra(), 
							releaseIIName, clusterId, overlapCluster.get(clusterId).getSpectra(), 
							overlapSpectrumOfCluster.get(clusterId));

					// peak map
					PeakMap peakMap = new PeakMap(cluster.getMzValues(), cluster.getIntensValues(),
							overlapCluster.get(clusterId).getMzValues(),
							overlapCluster.get(clusterId).getIntensValues());

					// delete all builds from tabBGridPane
					VBox trashCans = new VBox();
					trashCans.getChildren().addAll(tabBGridPane.getChildren());

					// add builds into tabB
					tabBGridPane.add(spectrumTable.getOverlapSpectraPane(), 0, 0, 2, 1);
					tabBGridPane.add(peakMap.getVbox(), 0, 1);
					tabBGridPane.add(createComparerPieChart(cluster, releaseCluster), 1, 1);
					tabB.setContent(tabBGridPane);

					// redirect to tabB
					ClusterApplication.tabPane.getSelectionModel().select(1);
				}
			});
		}
		return pieChart;
	}

	public PieChart createComparerPieChart(UndirectedGraph<Vertex, Edge> undirectedGraph, Vertex focusVertex) {

		// return value
		PieChart pieChart = new PieChart();

		Cluster cluster = focusVertex.getCluster();
		HashMap<String, Integer> overlapSpectrumCount = new HashMap<>();
		HashMap<String, Cluster> overlapCluster = new HashMap<>();
		HashMap<String, List<Spectrum>> overlapSpectraOfCluster = new HashMap<>();

		for (Entry<Vertex, Edge> link : undirectedGraph.getAdjacencyTableOfVertex(focusVertex).entrySet()) {
			Vertex vertex = link.getKey();
			Edge edge = link.getValue();
			overlapSpectrumCount.put(vertex.getCluster().getId(), (int) edge.getWeight());
			overlapCluster.put(vertex.getCluster().getId(), vertex.getCluster());
			overlapSpectraOfCluster.put(vertex.getCluster().getId(), edge.getOverlapSpectra());

		}

		// organize pie data
		ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
		Iterator<Entry<String, Integer>> interItr = overlapSpectrumCount.entrySet().iterator();
		while (interItr.hasNext()) {
			Entry<String, Integer> entry = interItr.next();

			// calculate percentage
			String clusterId = entry.getKey();
			BigDecimal bd = new BigDecimal((double) entry.getValue() / cluster.getSpecCount() * 100);
			double rate = bd.setScale(2, RoundingMode.HALF_UP).doubleValue();

			// add data
			pieChartData.add(new PieChart.Data(
					clusterId + "\ncount : " + entry.getValue() + "\npercentage : " + rate + "%", entry.getValue()));
		}

		// set pie chart data
		pieChart.setData(pieChartData);

		// set pie chart : title and legend
		pieChart.setTitle(cluster.getId() + "'s spectrum composition");
		pieChart.setLabelsVisible(false);
		pieChart.setLegendVisible(true);
		pieChart.setLegendSide(Side.BOTTOM);

		// set size
		pieChart.setMinSize(ClusterApplication.screenBounds.getWidth() * 0.8 / 3,
				ClusterApplication.screenBounds.getHeight() * 0.40);

		// add event : print spectrum tables of two overlap clusters
		for (PieChart.Data data : pieChart.getData()) {
			data.getNode().addEventHandler(MouseEvent.MOUSE_PRESSED, new EventHandler<MouseEvent>() {

				@Override
				public void handle(MouseEvent event) {

					// get tabB
					Tab tabB = null;
					if (ClusterApplication.tabPane.getTabs().size() == 1) {
						tabB = new Tab("Cluster comparison");
						ClusterApplication.tabPane.getTabs().add(tabB);
					}else {
						tabB = ClusterApplication.tabPane.getTabs().get(1);
					}

					// obtain clusterID
					String clusterId = data.getName().split("\\n")[0];

					// get source cluster's spectrum table and object cluster's spectrum
					SpectrumTable spectrumTable = new SpectrumTable(
							releaseIName, cluster.getId(), cluster.getSpectra(), 
							releaseIIName, clusterId, overlapCluster.get(clusterId).getSpectra(), 
							overlapSpectraOfCluster.get(clusterId));

					// peak map
					PeakMap peakMap = new PeakMap(cluster.getMzValues(), cluster.getIntensValues(),
							overlapCluster.get(clusterId).getMzValues(),
							overlapCluster.get(clusterId).getIntensValues());

					// delete all builds from tabBGridPane
					VBox trashCans = new VBox();
					trashCans.getChildren().addAll(tabBGridPane.getChildren());

					// add builds into tabB
					tabBGridPane.add(spectrumTable.getOverlapSpectraPane(), 0, 0, 2, 1);
					tabBGridPane.add(peakMap.getVbox(), 0, 1);
					tabBGridPane.add(createComparerPieChart(undirectedGraph, focusVertex), 1, 1);

					tabB.setContent(tabBGridPane);

					// redirect to tabB
					ClusterApplication.tabPane.getSelectionModel().select(1);
				}
			});
		}
		return pieChart;
	}
}