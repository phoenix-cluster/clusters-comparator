package cn.edu.cqupt.model;

public class Vertex{
	private String releaseName;
	private Cluster cluster;
	private double weight;
	

	public String getReleaseName() {
		return releaseName;
	}

	public Cluster getCluster() {
		return cluster;
	}
	
	public double getWeight() {
		return weight;
	}

	public Vertex() {

	}

	public Vertex(String releaseName, Cluster cluster, double weight) {
		this.cluster = cluster;
		this.weight = weight;
		this.releaseName = releaseName;
	}
	
	@Override
	public String toString() {
		return cluster.getId() + ": " + weight;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cluster.getId() == null) ? 0 : cluster.getId().hashCode());
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
		if (cluster.getId() == null) {
			if (other.cluster.getId() != null)
				return false;
		} else if (!cluster.getId().equals(other.cluster.getId()))
			return false;
		return true;
	}
}
