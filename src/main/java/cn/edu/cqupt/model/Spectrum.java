package cn.edu.cqupt.model;

public class Spectrum {
	private String spectrumId;
	private float charge;
	private float precursorMz;
	private String species;

	public Spectrum() {

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

	@Override
	public boolean equals(Object otherObject) {
		if (otherObject == null)
			return false;

		if (this == otherObject)
			return true;

		if (this.getClass() != otherObject.getClass())
			return false;
		else {
			Spectrum other = (Spectrum) otherObject;
			if (this.species == null || this.species.isEmpty() || this.species == "" || other.species == null
					|| other.species.isEmpty() || other.species == "")
				return this.spectrumId.equals(other.spectrumId) && this.charge == other.charge
						&& this.precursorMz == other.precursorMz;
			else
				return this.spectrumId.equals(other.spectrumId) && this.charge == other.charge
						&& this.precursorMz == other.precursorMz && this.species.equals(other.species);
		}
	}

	@Override
	public String toString() {
		return "Spectrum [spectrumId=" + spectrumId + ", charge=" + charge + ", precursorMz=" + precursorMz
				+ ", species=" + species + "]";
	}
}
