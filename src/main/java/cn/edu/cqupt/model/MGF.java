package cn.edu.cqupt.model;

import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by huangjs on 2018/3/22.
 */
public class MGF {
    private String title;
    private TreeMap<Float, Float> mzIntensityPairs;

    public MGF() {
    }

    public MGF(String title, TreeMap<Float, Float> mzIntensityPairs) {
        this.title = title;
        this.mzIntensityPairs = mzIntensityPairs;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TreeMap<Float, Float> getMzIntensityPairs() {
        return mzIntensityPairs;
    }

    public void setMzIntensityPairs(TreeMap<Float, Float> mzIntensityPairs) {
        this.mzIntensityPairs = mzIntensityPairs;
    }

    public String format2Mgf(){
        String result = "BEGIN IONS\n";
        result += "TITLE=" + title + "\n";
        Iterator<Map.Entry<Float, Float>> itr = mzIntensityPairs.entrySet().iterator();
        while(itr.hasNext()){
            Map.Entry<Float, Float> entry = itr.next();
            result += entry.getKey() + "\t" + entry.getValue() + "\n";
        }
        result += "END IONS";
        return result;
    }
}
