package cn.edu.cqupt.restful;

public class RestSpectrum {

    private String title;
    private String sequence;
    private int charge;
    private float precursorMz;
    private float similarysocre;
    private String species;
    private String clusterId;
    private String projectId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public float getSimilarysocre() {
        return similarysocre;
    }

    public void setSimilarysocre(float similarysocre) {
        this.similarysocre = similarysocre;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    @Override
    public String toString() {
        return "RestSpectrum [title=" + title + ", sequence=" + sequence + ", charge=" + charge + ", precursorMz="
                + precursorMz + ", similarysocre=" + similarysocre + ", species=" + species + ", clusterId=" + clusterId
                + ", projectId=" + projectId + "]";
    }
}
