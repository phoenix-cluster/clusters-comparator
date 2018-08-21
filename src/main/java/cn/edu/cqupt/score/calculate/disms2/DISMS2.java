package cn.edu.cqupt.score.calculate.disms2;

import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.calculate.Peak;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DISMS2 {

    /**
     * 1. only the top topN peaks of spectra shall be considered
     * 2. generate a new peak list with bin size binSize.
     *
     * @param peakList
     * @return
     */
    public static List<Peak> dataPreprocessing(List<Peak> peakList, int topN, double binSize) {
        Map<Double, Double> filteredData = peakList.stream()
                .sorted(Comparator.comparingDouble(Peak::getIntensity).reversed())
                .limit(topN)
                .sorted(Comparator.comparingDouble(Peak::getMz))
                .collect(Collectors.groupingBy((Peak p) -> Math.ceil(p.getMz() / binSize),
                        Collectors.collectingAndThen(
                                Collectors.toList(),
                                (List<Peak> l) -> l.stream()
                                        .max(Comparator.comparingDouble(Peak::getIntensity))
                                        .get()
                                        .getIntensity()
                        )));

        List<Peak> newPeakList = filteredData.keySet().stream()
                .map((Double key) -> new Peak((key + 0.5) * binSize, filteredData.get(key)))
                .collect(Collectors.toList());
        return newPeakList;
    }

    public static MatchedPeakList match(List<Peak> filteredPeakList1, List<Peak> filteredPeakList2) {
        List<Peak> peakList1 = new ArrayList<>();
        List<Peak> peakList2 = new ArrayList<>();
        for (Peak p1 : filteredPeakList1) {
            for (Peak p2 : filteredPeakList2) {
                if (p1.getMz() == p2.getMz()) {
                    peakList1.add(p1);
                    peakList2.add(p2);
                }
            }
        }
        return new MatchedPeakList(peakList1, peakList2);

    }

    /**
     * calculate cosine distance
     * if there is not any matched peak, return 1
     *
     * @param matchedPeakList
     * @return
     */
    public static double cosineDistance(MatchedPeakList matchedPeakList) {

        /** if there is not any matched peak, return 1 **/
        if(matchedPeakList.getPeakList1().size() == 0){
            return 1;
        }

        List<Peak> peakList1 = matchedPeakList.getPeakList1();
        List<Peak> peakList2 = matchedPeakList.getPeakList2();

        /** dot product **/
        double dotProduct = IntStream.range(0, peakList1.size())
                .mapToDouble((int index) -> peakList1.get(index).getIntensity() * peakList2.get(index).getIntensity())
                .sum();

        /** euclidean norms without extraction of a root **/
        double euclideanNorms1 = peakList1.stream()
                .mapToDouble((Peak p) -> p.getIntensity() * p.getIntensity())
                .sum();
        double euclideanNorms2 = peakList2.stream()
                .mapToDouble((Peak p) -> p.getIntensity() * p.getIntensity())
                .sum();

        /** cosine distance **/
        double distance = 1 - dotProduct / Math.sqrt(euclideanNorms1 * euclideanNorms2);
        return distance;
    }

    public static double angleDistance(List<Peak> peakList1, List<Peak> peakList2, double c) {
//        double pi = peakList1.size();
//        double pl = peakList2.size();
//        Collections.sort(peakList1, Comparator.comparingDouble(Peak::getIntensity));
//        Collections.sort(peakList2, Comparator.comparingDouble(Peak::getIntensity));
//        for(Peak p1 : peakList1){
//            for(Peak p2 : peakList2){
//                if(Math.abs(p2.getIntensity() - p1.getIntensity()) <= c){
//
//                }
//            }
//        }
        return 0.1;
    }

    /**
     * directly complete total process
     *
     * @param ms1
     * @param ms2
     * @return
     */
    public static double calCosineDistance(MS ms1, MS ms2, int topN, double binSize) {

        /** data preprocessing **/
//        long t1 = System.currentTimeMillis();
        List<Peak> filteredPeakList1 = dataPreprocessing(ms1.getPeakList(), topN, binSize);
//        long t2 = System.currentTimeMillis();
        List<Peak> filteredPeakList2 = dataPreprocessing(ms2.getPeakList(), topN, binSize);
//        long t3 = System.currentTimeMillis();

        /** match **/
        MatchedPeakList matchedPeakList = match(filteredPeakList1, filteredPeakList2);
//        long t4 = System.currentTimeMillis();

        double distance = cosineDistance(matchedPeakList);
//        long t5 = System.currentTimeMillis();
//        System.out.println("data preprocessing 1: " + (t2 - t1) / 1000.0);
//        System.out.println("data preprocessing 2: " + (t3 - t2) / 1000.0);
//        System.out.println("match: " + (t4 - t3) / 1000.0);
//        System.out.println("distance: " + (t5 - t4) / 1000.0);
        return distance;
    }

    static class MatchedPeakList {
        List<Peak> peakList1;
        List<Peak> peakList2;

        public MatchedPeakList(List<Peak> peakList1, List<Peak> peakList2) {
            this.peakList1 = peakList1;
            this.peakList2 = peakList2;
        }

        public List<Peak> getPeakList1() {
            return peakList1;
        }

        public List<Peak> getPeakList2() {
            return peakList2;
        }
    }
}
