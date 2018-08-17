package cn.edu.cqupt.cmgf;

import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.calculate.Peak;

import java.util.ArrayList;

public class CMGF extends MS {

    private String clusterLabel;

    public CMGF(MS ms, String clusterLabel) {
        super(ms.getTitle(), ms.getCharge(), ms.getPeakList());
        this.clusterLabel = clusterLabel;
    }

    public CMGF(String title, String charge, ArrayList<Peak> peakList, String clusterLabel) {
        super(title, charge, peakList);
        this.clusterLabel = clusterLabel;
    }

    /**
     * use copy constructor replacing Cloneable
     *
     * @param cmgf
     */
    public CMGF(CMGF cmgf) {
        this(cmgf.getTitle(), cmgf.getCharge(), cmgf.getPeakList(), cmgf.getClusterLabel());
    }

    public String getClusterLabel() {
        return clusterLabel;
    }

    public void setClusterLabel(String clusterLabel) {
        this.clusterLabel = clusterLabel;
    }

    @Override
    public String toString() {
        String result = "BEGIN IONS\n";
        result += "TITLE=" + this.getTitle() + "\n";
        result += "CLUSTER_LABEL=" + clusterLabel + "\n";
        for (Peak peak : this.getPeakList()) {
            result += peak + "\n";
        }
        result += "END IONS";
        return result;
    }
}
