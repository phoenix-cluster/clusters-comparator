package cn.edu.cqupt.model;

public class Vertex {
	private Cluster cluster;

	public Cluster getCluster() {
		return cluster;
	}

	public Vertex(Cluster cluster) {
		this.cluster = cluster;
	}
	
	public String toString() {
		return cluster.getId();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cluster == null) ? 0 : cluster.hashCode());
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
		Vertex other = (Vertex) obj;
		if (cluster == null) {
			if (other.cluster != null)
				return false;
		} else if (!cluster.equals(other.cluster))
			return false;
		return true;
	}
	
}