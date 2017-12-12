package cn.edu.cqupt.model;

import java.util.List;

public class Edge {
	private List<Spectrum> overlapSpectra;
	private int weight;

	public List<Spectrum> getOverlapSpectra() {
		return overlapSpectra;
	}
	
	public int getWeight() {
		return weight;
	}

	public Edge() {
		super();
	}

	public Edge(List<Spectrum> overlapSpectra, int weight) {
		super();
		this.overlapSpectra = overlapSpectra;
		this.weight = weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((overlapSpectra == null) ? 0 : overlapSpectra.hashCode());
		result = prime * result + weight;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		if (overlapSpectra == null) {
			if (other.overlapSpectra != null)
				return false;
		} else if (!overlapSpectra.equals(other.overlapSpectra))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return weight + "";
	}

}
