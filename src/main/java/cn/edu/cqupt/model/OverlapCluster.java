package cn.edu.cqupt.model;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class OverlapCluster extends OverlapClusterComponent {
	// private Cluster overlapCluster;
	// private List<Spectrum> overlapSpectrums;

	// @Override
	// public Cluster getOverlapCluster() {
	// return overlapCluster;
	// }
	//
	// @Override
	// public List<Spectrum> getOverlapSpectrums() {
	// return overlapSpectrums;
	// }

	public OverlapCluster() {
		super();
	}

	public OverlapCluster(Cluster overlapCluster, List<Spectrum> overlapSpectrums) {
		super(overlapCluster, overlapSpectrums);
		// this.overlapCluster = overlapCluster;
		// this.overlapSpectrums = overlapSpectrums;
	}

	@Override
	public void print() {
		System.out.println("++++++++++++叶子结点开始+++++++++++++++++++++++++++++");
		System.out.println(getOverlapCluster().getId() + ": " + getOverlapSpectrums().size());
		System.out.println("+++++++++++++++叶子节点结束++++++++++++++++++++++++++");
	}

	@Override
	public boolean isLeafNode() {
		return true;
	}

}
