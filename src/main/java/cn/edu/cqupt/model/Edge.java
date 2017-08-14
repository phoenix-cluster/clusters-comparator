package cn.edu.cqupt.model;

import java.util.List;


public class Edge {
	private List<Spectrum> overlapSpectra;

	public List<Spectrum> getOverlapSpectra() {
		return overlapSpectra;
	}

	public Edge(List<Spectrum> overlapSpectra) {
		this.overlapSpectra = overlapSpectra;
	}
	
	public String toString() {
		return overlapSpectra.size() + "";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((overlapSpectra == null) ? 0 : overlapSpectra.hashCode());
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
		return true;
	}
	

}