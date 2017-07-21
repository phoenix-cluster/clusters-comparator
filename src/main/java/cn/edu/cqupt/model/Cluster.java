package cn.edu.cqupt.model;

import java.util.ArrayList;
import java.util.List;

public class Cluster implements Cloneable{

	private String id;
	private float avPrecursorMz;
	private float avPrecursorIntens;
	private int specCount;
	private List<Spectrum> spectrums;

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
	
	@Override
	public Cluster clone() throws CloneNotSupportedException{
		Cluster result = (Cluster) super.clone();
		List<Spectrum> tmpSpectrums = new ArrayList<>(spectrums.size());
		for(Spectrum spec : spectrums) {
			tmpSpectrums.add(spec);
		}
		result.spectrums = tmpSpectrums;
		return result;
	}

}
