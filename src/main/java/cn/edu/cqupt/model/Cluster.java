package cn.edu.cqupt.model;

import java.util.ArrayList;
import java.util.List;

public class Cluster implements Cloneable {

	private String id;
	private float avPrecursorMz;
	private float avPrecursorIntens;
	private int specCount;
	private List<Spectrum> spectrums;
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

	public List<Spectrum> getSpectrums() {
		return spectrums;
	}

	public void setSpectrums(List<Spectrum> spectrums) {
		this.spectrums = spectrums;
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
		List<Spectrum> tmpSpectrums = new ArrayList<>(spectrums.size());
		for (Spectrum spec : spectrums) {
			tmpSpectrums.add(spec);
		}
		result.spectrums = tmpSpectrums;
		return result;
	}

}
