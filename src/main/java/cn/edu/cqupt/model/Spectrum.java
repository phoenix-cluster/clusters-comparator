package cn.edu.cqupt.model;

public class Spectrum implements Cloneable{
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
		if (otherObject == null) {
			// System.out.println("otherObject == null");
			return false;
		}

		if (this == otherObject) {
			// System.out.println("this == otherObject");
			return true;
		}

		if (this.getClass() != otherObject.getClass()) {
			// System.out.println("this.getClass() != otherObject.getClass()");
			return false;
		} else {
			Spectrum other = (Spectrum) otherObject;
			if (this.species == null || this.species.isEmpty() || this.species == "" || other.species == null
					|| other.species.isEmpty() || other.species == "")
				return this.spectrumId.equals(other.spectrumId) && this.charge == other.charge
						&& this.precursorMz == other.precursorMz;
			else
				return this.spectrumId.equals(other.spectrumId) && this.charge == other.charge
						&& this.precursorMz == other.precursorMz && this.species.equals(other.species);

			// if (this.spectrumId.equals(other.spectrumId)) {
			// System.out.println("this.spectrumId.equals(other.spectrumId)");
			// return true;
			// } else {
			// System.out.println("this.spectrumId = " + this.spectrumId);
			// System.out.println("other.spectrumId = " + other.spectrumId);
			// System.out.println("!this.spectrumId.equals(other.spectrumId)");
			// return false;
			// }
		}
	}

	@Override
	public String toString() {
		return "Spectrum [spectrumId=" + spectrumId + ", charge=" + charge + ", precursorMz=" + precursorMz
				+ ", species=" + species + "]";
	}
	
	@Override
	public Spectrum clone() throws CloneNotSupportedException{
		Spectrum result = (Spectrum) super.clone();	
		return result;
	}
}
