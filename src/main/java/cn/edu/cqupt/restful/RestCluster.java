package cn.edu.cqupt.restful;

import java.util.List;

public class RestCluster {
	private String id;
	private float avPrecursorMz;
	private float avPrecursorIntens;
	private int specCount;
	private float ratio;
	private List<RestSpectrum> spectra;
	private String CMz;
	private String CIntens;

	public String getId() {
		return id;
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

	public float getRatio() {
		return ratio;
	}

	public void setRatio(float ratio) {
		this.ratio = ratio;
	}

	public List<RestSpectrum> getSpectra() {
		return spectra;
	}

	public void setSpectra(List<RestSpectrum> spectra) {
		this.spectra = spectra;
	}

	public String getCMz() {
		return CMz;
	}

	public void setCMz(String cMz) {
		CMz = cMz;
	}

	public String getCIntens() {
		return CIntens;
	}

	public void setCIntens(String cIntens) {
		CIntens = cIntens;
	}

	@Override
	public String toString() {
		return "RestCluster [id=" + id + ", avPrecursorMz=" + avPrecursorMz + ", avPrecursorIntens=" + avPrecursorIntens
				+ ", specCount=" + specCount + ", ratio=" + ratio + ", spectra=" + spectra + ", CMz=" + CMz
				+ ", CIntens=" + CIntens + "]";
	}
}
