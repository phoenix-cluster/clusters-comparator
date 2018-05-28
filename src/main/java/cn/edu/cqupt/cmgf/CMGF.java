package cn.edu.cqupt.cmgf;

import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.calculate.Peak;

import java.util.List;
import java.util.Objects;

public class CMGF{

    // a spectrum from mgf file
    private MS ms;

    // the cluster labels of the MS
    private String label1;
    private String label2;

    /**
     * convert MS with label to CMGF
     * @param ms
     * @param label1
     * @param label2
     */
    public CMGF(MS ms, String label1, String label2) {
        this.ms = ms;
        this.label1 = label1;
        this.label2 = label2;
    }


    public CMGF(String title, String charge, List<Peak> peakList, String label1, String label2) {
        this.ms = new MS(title, charge, peakList);
        this.label1 = label1;
        this.label2 = label2;
    }

    /**
     * use copy constructor(拷贝构造器) replacing Cloneable
     * @param cmgf
     */
    public CMGF(CMGF cmgf){
        this.ms = new MS(cmgf.getMs());
        setLabel1(cmgf.getLabel1());
        setLabel2(cmgf.getLabel2());

    }

    public MS getMs() {
        return ms;
    }

    public void setMs(MS ms) {
        this.ms = ms;
    }

    public String getLabel1() {
        return label1;
    }

    public void setLabel1(String label1) {
        this.label1 = label1;
    }

    public String getLabel2() {
        return label2;
    }

    public void setLabel2(String label2) {
        this.label2 = label2;
    }

    @Override
    public String toString() {
        String result = "BEGIN IONS\n";
        result += "TITLE=" + ms.getTitle() + "\n";
        result += "CLUSTER_LABEL1=" + label1 + "\n";
        result += "CLUSTER_LABEL2=" + label2 + "\n";
        for (Peak peak : ms.getPeakList()) {
            result += peak + "\n";
        }
        result += "END IONS";
        return result;
    }
}
