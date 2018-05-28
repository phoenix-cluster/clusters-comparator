package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.model.Cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Created by huangjs on 2018/3/22.
 */
public class MS {
    private String title;
    private String charge;
    private List<Peak> peakList;

    public MS() {
    }

    public MS(String title, String charge, List<Peak> peakList) {
        this.title = title;
        this.charge = charge;
        this.peakList = peakList;
    }

    public MS(MS ms){
        this(ms.getTitle(), ms.getCharge(), null);
        List<Peak> peakList = new ArrayList<>();
        for(Peak peak : ms.getPeakList()){
            peakList.add(new Peak(peak.getMz(), peak.getIntensity()));
        }
        setPeakList(peakList);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCharge() {
        return charge;
    }

    public void setCharge(String charge) {
        this.charge = charge;
    }

    public List<Peak> getPeakList() {
        return peakList;
    }

    public void setPeakList(List<Peak> peakList) {
        this.peakList = peakList;
    }

    /**
     * @return return peaks list in ascending order of mz value
     */
    public List<Peak> getPeakListAscMz() {
        Collections.sort(peakList, Peak.AscMzComparator);
        return peakList;
    }

    /**
     * @return return peaks list in descending order of mz value
     */
    public List<Peak> getPeakListDescMz() {
        Collections.sort(peakList, Peak.DescMzComparator);
        return peakList;
    }

    /**
     * @return return peaks list in ascending order of intensity value
     */
    public List<Peak> getPeakListAscIntensity() {
        Collections.sort(peakList, Peak.AscIntensityComparator);
        return peakList;
    }

    /**
     * @return return peaks list in descending order of intensity value
     */
    public List<Peak> getPeakListDescIntensity() {
        Collections.sort(peakList, Peak.DescIntensityComparator);
        return peakList;
    }

    public String format() {
        String result = "BEGIN IONS\n";
        result += "TITLE=" + title + "\n";
        for (Peak peak : peakList) {
            result += peak + "\n";
        }
        result += "END IONS";
        return result;
    }

    @Override
    public String toString() {
        return "MS{" +
                "title='" + title + '\'' +
                '}';
    }

    public List<Double> getMzList() {
        List<Double> mzList = new ArrayList<>();
        for (Peak peak : peakList) {
            mzList.add(peak.getMz());
        }
        return mzList;
    }

    public List<Double> getIntensityList() {
        List<Double> intensityList = new ArrayList<>();
        for (Peak peak : peakList) {
            intensityList.add(peak.getIntensity());
        }
        return intensityList;
    }

    public static MS clustering2MS(Cluster cluster) {
        String title = cluster.getId();
        ArrayList<Peak> peakList = new ArrayList<>();
        for (int i = 0; i < cluster.getMzValues().size(); i++) {
            Peak peak = new Peak(cluster.getMzValues().get(i), cluster.getIntensValues().get(i));
            peakList.add(peak);
        }

        // cluster in clustering file does not have attribute charge
        MS ms = new MS(title, null, peakList);
        return ms;
    }

    public static MS[] clustering2MS(Cluster... cluster) {
        MS[] msArr = new MS[cluster.length];
        for (int i = 0; i < cluster.length; i++) {
            msArr[i] = clustering2MS(cluster[i]);
        }
        return msArr;
    }

    public static List<MS> clustering2MS(List<Cluster> clusterList) {
        List<MS> msList = new ArrayList<>();
        for (Cluster cluster : clusterList) {
            msList.add(clustering2MS(cluster));
        }
        return msList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MS ms = (MS) o;
        return Objects.equals(title, ms.title);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title);
    }
}
