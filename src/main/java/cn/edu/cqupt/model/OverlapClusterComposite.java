package cn.edu.cqupt.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class OverlapClusterComposite extends OverlapClusterComponent {
	// private Cluster overlapCluster;
	// private List<Spectrum> overlapSpectrums;
	private List<OverlapClusterComponent> children;

	// @Override
	// public Cluster getOverlapCluster() {
	// return overlapCluster;
	// }
	//
	// @Override
	// public List<Spectrum> getOverlapSpectrums() {
	// return overlapSpectrums;
	// }

	public OverlapClusterComposite() {
		super();
	}

	public OverlapClusterComposite(Cluster overlapCluster, List<Spectrum> overlapSpectrums) {
		super(overlapCluster, overlapSpectrums);
		// this.overlapCluster = overlapCluster;
		// this.overlapSpectrums = overlapSpectrums;
		this.children = new ArrayList<>();
	}

	public OverlapClusterComposite(Cluster overlapCluster, List<Spectrum> overlapSpectrums,
			List<OverlapClusterComponent> children) {
		super(overlapCluster, overlapSpectrums);
		this.children = children;
	}

	@Override
	public void add(OverlapClusterComponent element) {
		children.add(element);
	}

	@Override
	public void print() {

		Iterator<OverlapClusterComponent> itr = children.iterator();
		System.out.println("+++++++++++++++++++++中间结点开始++++++++++++++++++++");
		System.out.println(this.getOverlapCluster().getId() + ": " + this.getOverlapSpectrums().size());

		while (itr.hasNext()) {
			OverlapClusterComponent element = itr.next();
			element.print();
		}
		System.out.println("+++++++++++++++++++中间节点结束++++++++++++++++++++++");
	}

	@Override
	public boolean isLeafNode() {
		return false;
	}

	@Override
	public List<OverlapClusterComponent> getChildren() {
		return this.children;
	}
}
