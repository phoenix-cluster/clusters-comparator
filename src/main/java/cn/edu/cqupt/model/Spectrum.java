package cn.edu.cqupt.model;

public class Spectrum {
	private String spectrumId;
	private float charge;
	private float precursorMz;
	private String species;
	
	public Spectrum(){
		
	}

	public String getSpectrumId() {
		return spectrumId;
	}

	public void setSpectrumId(String spectrumId) {
		this.spectrumId = spectrumId;
	}

	public float getCharge() {
		return charge;
	}

	public void setCharge(float charge) {
		this.charge = charge;
	}

	public float getPrecursorMz() {
		return precursorMz;
	}

	public void setPrecursorMz(float precursorMz) {
		this.precursorMz = precursorMz;
	}

	public String getSpecies() {
		return species;
	}

	public void setSpecies(String species) {
		this.species = species;
	}
	
}
