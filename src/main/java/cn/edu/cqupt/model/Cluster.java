package cn.edu.cqupt.model;

import java.util.ArrayList;
import java.util.List;

public class Cluster implements Cloneable {

	private String id;
	private float avPrecursorMz;
	private float avPrecursorIntens;
	private int specCount;
	private List<Spectrum> spectra;
	private List<Float> mzValues;
	private List<Float> intensValues;

	public Cluster() {

	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getAvPrecursorMz() {
		return avPrecursorMz;
	}

	public void setAvPrecursorMz(float avPrecursorMz) {
		this.avPrecursorMz = avPrecursorMz;
	}

	public float getAvPrecursorIntens() {
		return avPrecursorIntens;
	}

	public void setAvPrecursorIntens(float avPrecursorIntens) {
		this.avPrecursorIntens = avPrecursorIntens;
	}

	public int getSpecCount() {
		return specCount;
	}

	public void setSpecCount(int specCount) {
		this.specCount = specCount;
	}

	public List<Spectrum> getSpectra() {
		return spectra;
	}

	public void setSpectrums(List<Spectrum> spectra) {
		this.spectra = spectra;
	}

	public List<Float> getMzValues() {
		return mzValues;
	}

	public void setMzValues(List<Float> mzValues) {
		this.mzValues = mzValues;
	}

	public List<Float> getIntensValues() {
		return intensValues;
	}

	public void setIntensValues(List<Float> intensValues) {
		this.intensValues = intensValues;
	}

	@Override
	public Cluster clone() throws CloneNotSupportedException {
		Cluster result = (Cluster) super.clone();
		List<Spectrum> tmpSpectra = new ArrayList<>(spectra.size());
		for (Spectrum spec : spectra) {
			tmpSpectra.add(spec.clone());
		}
		result.spectra = tmpSpectra;
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Cluster other = (Cluster) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
