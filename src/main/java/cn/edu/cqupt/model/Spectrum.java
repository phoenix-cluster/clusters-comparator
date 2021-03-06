package cn.edu.cqupt.model;

public class Spectrum implements Cloneable {
    private String id;
    private String sequence;
    private int charge;
    private float precursorMz;
    private String species;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public int getCharge() {
        return charge;
    }

    public void setCharge(int charge) {
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

    public Spectrum() {

    }

    public Spectrum(String id, String sequence, int charge, float precursorMz, String species) {
        super();
        this.id = id;
        this.sequence = sequence;
        this.charge = charge;
        this.precursorMz = precursorMz;
        this.species = species;
    }

    @Override

    public boolean equals(Object otherObject) {
        if (otherObject == null) {
            return false;
        }

        if (this == otherObject) {
            return true;
        }

        if (this.getClass() != otherObject.getClass()) {
            return false;
        }
        Spectrum other = (Spectrum) otherObject;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return "Spectrum [id=" + id + ", sequence=" + sequence + ", charge=" + charge + ", precursorMz=" + precursorMz
                + ", species=" + species + "]";
    }

    @Override
    public Spectrum clone() throws CloneNotSupportedException {
        Spectrum result = (Spectrum) super.clone();
        return result;
    }

    // public static void main(String[] args) {
    // Spectrum spectrum1 = new Spectrum("id", "AAA", 1.2F, 1.67F, "cat");
    // Spectrum spectrum2 = new Spectrum("id", "AAA", 1.2F, 1.67F, "cat");
    // Spectrum spectrum3 = new Spectrum("td", "AAA", 1.2F, 1.67F, "cat");
    // System.out.println(spectrum1);
    // System.out.println(spectrum1.hashCode());
    // System.out.println(spectrum2);
    // System.out.println(spectrum2.hashCode());
    // System.out.println(spectrum3);
    // System.out.println(spectrum3.hashCode());
    // }

}
