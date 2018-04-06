package cn.edu.cqupt.score.calculate;

import java.util.Comparator;

/**
 * Created by huangjs on 2018/3/22.
 */
public class Peak implements Cloneable{
    private double mz;
    private double intensity;
    public static final Comparator<Peak> AscMzComparator = (o1, o2) -> {
        return o1.getMz() > o2.getMz() ? 1 : (o1.getMz() < o2.getMz() ? -1 : 0);
    };
    public static final Comparator<Peak> DescMzComparator = (o1, o2) -> {
        return o1.getMz() > o2.getMz() ? -1 : (o1.getMz() < o2.getMz() ? 1 : 0);
    };
    public static final Comparator<Peak> AscIntensityComparator = (o1, o2) -> {
        return o1.getIntensity() > o2.getIntensity() ? 1 : (o1.getIntensity() < o2.getIntensity() ? -1 : 0);
    };
    public static final Comparator<Peak> DescIntensityComparator = (o1, o2) -> {
        return o1.getIntensity() > o2.getIntensity() ? -1 : (o1.getIntensity() < o2.getIntensity() ? 1 : 0);
    };


    public Peak() {
    }

    public Peak(double mz, double intensity) {
        this.mz = mz;
        this.intensity = intensity;
    }

    public double getMz() {
        return mz;
    }

    public void setMz(double mz) {
        this.mz = mz;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        return getMz() + "\t" + getIntensity() ;
    }

    @Override
    public Peak clone() throws CloneNotSupportedException{
        return (Peak) super.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Peak peak = (Peak) o;

        if (Double.compare(peak.mz, mz) != 0) return false;
        return Double.compare(peak.intensity, intensity) == 0;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        temp = Double.doubleToLongBits(mz);
        result = (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(intensity);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }
}
