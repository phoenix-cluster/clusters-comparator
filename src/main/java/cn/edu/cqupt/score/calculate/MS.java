package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.model.Cluster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by huangjs on 2018/3/22.
 */
public class MS implements Cloneable {
    private String title;
    private String charge;
    private ArrayList<Peak> peakList;

    public MS() {
    }

    public MS(String title, ArrayList<Peak> peakList) {
        this.title = title;
        this.peakList = peakList;
    }

    public MS(String title, String charge, ArrayList<Peak> peakList) {
        this.title = title;
        this.charge = charge;
        this.peakList = peakList;
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

    public ArrayList<Peak> getPeakList() {
        return peakList;
    }

    public void setPeakList(ArrayList<Peak> peakList) {
        this.peakList = peakList;
    }

    /**
     * @return return peaks list in ascending order of mz value
     */
    public ArrayList<Peak> getPeakListAscMz() {
        Collections.sort(peakList, Peak.AscMzComparator);
        return peakList;
    }

    /**
     * @return return peaks list in descending order of mz value
     */
    public ArrayList<Peak> getPeakListDescMz() {
        Collections.sort(peakList, Peak.DescMzComparator);
        return peakList;
    }

    /**
     * @return return peaks list in ascending order of intensity value
     */
    public ArrayList<Peak> getPeakListAscIntensity() {
        Collections.sort(peakList, Peak.AscIntensityComparator);
        return peakList;
    }

    /**
     * @return return peaks list in descending order of intensity value
     */
    public ArrayList<Peak> getPeakListDescIntensity() {
        Collections.sort(peakList, Peak.DescIntensityComparator);
        return peakList;
    }

//    public String toString() {
//        String result = "BEGIN IONS\n";
//        result += "TITLE=" + title + "\n";
//        for (Peak peak : peakList) {
//            result += peak + "\n";
//        }
//        result += "END IONS";
//        return result;
//    }

    public String toString() {
        return getTitle();
    }

    public MS clone() {
        MS ms = null;
        try {
            ms = (MS) super.clone();
            ArrayList<Peak> tmpPeakList = new ArrayList<>(peakList.size());
            for (Peak peak : peakList) {
                tmpPeakList.add(peak.clone());
            }
            ms.setPeakList(tmpPeakList);
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return ms;
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
        MS ms = new MS(title, peakList);
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
}
