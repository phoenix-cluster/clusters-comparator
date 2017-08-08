package cn.edu.cqupt.model;

import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.layout.Pane;

public abstract class OverlapClusterComponent {
	private Cluster overlapCluster;
	private List<Spectrum> overlapSpectrums;

	public Cluster getOverlapCluster() {
		// throw new UnsupportedOperationException();
		return this.overlapCluster;
	}

	public List<Spectrum> getOverlapSpectrums() {
		// throw new UnsupportedOperationException();
		return this.overlapSpectrums;
	}
	
	public OverlapClusterComponent() {
		super();
	}
	
	public OverlapClusterComponent(Cluster overlapCluster, List<Spectrum> overlapSpectrums) {
		super();
		this.overlapCluster = overlapCluster;
		this.overlapSpectrums = overlapSpectrums;
	}

	public void add(OverlapClusterComponent overlapClusterComponent) {
		throw new UnsupportedOperationException();
	}

	public List<OverlapClusterComponent> getChildren() {
		throw new UnsupportedOperationException();
	}
	
	public abstract boolean isLeafNode();

	public void print() {
		throw new UnsupportedOperationException();
	}
}
