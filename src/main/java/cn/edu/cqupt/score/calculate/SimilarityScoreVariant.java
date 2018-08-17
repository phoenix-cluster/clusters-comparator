package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.util.MathUtil;

import java.util.*;
import java.util.stream.Collectors;

public class SimilarityScoreVariant {
    private int cycleTimes;
    private int windowSize;
    private double fragmentTolerance;

    public SimilarityScoreVariant(int cycleTimes, int windowSize, double fragmentTolerance) {
        this.cycleTimes = cycleTimes;
        this.windowSize = windowSize;
        this.fragmentTolerance = fragmentTolerance;
    }

    public List<Double> calSimilarityScore(MS ms1, List<MS> msList) {
        List<Double> simiScoreList = new ArrayList<>(msList.size());
        for (MS ms2 : msList) {
            double maxSimiScore = Double.NEGATIVE_INFINITY;
            for (int i = 0; i < cycleTimes; i++) {

                /** split and filter spectra **/
                List<Peak> sfPeakList1 = splitAndFilter(ms1, i + 1);
                List<Peak> sfPeakList2 = splitAndFilter(ms2, i + 1);

                /** match **/
                Set<Peak>[] matchedPeaks = searchMatchedPeaks(sfPeakList1,
                        sfPeakList2, fragmentTolerance);

                /** calculate probability-based score **/
                int n = sfPeakList1.size() > sfPeakList2.size()
                        ? sfPeakList1.size()
                        : sfPeakList2.size();
                int k = matchedPeaks[0].size() > matchedPeaks[1].size()
                        ? matchedPeaks[0].size()
                        : matchedPeaks[1].size();
                double p = (double) (i + 1) / windowSize;
                double probabilityScore = calProbabilityScore(n, k, p);
//                System.out.println("probability-based score(" + ms1.getTitle()
//                        + " vs " + ms2.getTitle() + "): " + probabilityScore);

                /** calculate intensity-based score **/
                double intensityScore = calIntensityScore(sfPeakList1, sfPeakList2,
                        matchedPeaks[0], matchedPeaks[1]);
//                System.out.println("intensity-based score(" + ms1.getTitle()
//                        + " vs " + ms2.getTitle() + "): " + intensityScore);

                // calculate a similarity score when retaining (i + 1) high intensity peaks
                double currSimiScore = -10 * Math.log10(probabilityScore) * intensityScore;
                if (currSimiScore > maxSimiScore) {
                    maxSimiScore = currSimiScore;
                }
            }
            simiScoreList.add(maxSimiScore);
        }
        return simiScoreList;
    }

    /**
     * split ms data by window size and filter the top q peaks
     *
     * @param ms
     * @param q
     * @return
     */
    public List<Peak> splitAndFilter(MS ms, int q) {
        ArrayList<Peak> peakListAscMz = ms.getPeakListAscMz();
        double minMz = peakListAscMz.get(0).getMz();
        double maxMz = peakListAscMz.get(peakListAscMz.size() - 1).getMz();
        int fragmentCount = (int) Math.ceil((maxMz - minMz) / windowSize);
        double[] fragment = new double[fragmentCount];
        for (int i = 0; i < fragmentCount; i++) {
            fragment[i] = minMz + i * windowSize;
        }

        Map<Integer, List<Peak>> group = peakListAscMz.stream()
                .collect(Collectors.groupingBy(
                        peak -> {
                            for (int i = 0; i < fragmentCount - 1; i++) {
                                if (peak.getMz() < fragment[i + 1]) {
                                    return i;
                                }
                            }
                            return -1;
                        }
                ));

        List<Peak> sfPeakList = group.values().stream()
                .map(peakList -> {
                    Collections.sort(peakList,
                            (ms1, ms2) -> Double.compare(ms2.getIntensity(), ms1.getIntensity()));
                    return peakList.size() > q ? peakList.subList(0, q) : peakList;
                })
                .flatMap(peakList -> peakList.stream())
                .collect(Collectors.toList());
        return sfPeakList;
    }

    /**
     * search matched peaks between two peak lists
     * taking into account a given fragment tolerance.
     *
     * @param sfPeakList1
     * @param sfPeakList2
     * @param fragmentTolerance
     * @return array having two Set type elements.
     * the first element contain all peak2 matched with list 2,
     * the second element contain all peaks matched with list 1.
     */
    public Set<Peak>[] searchMatchedPeaks(List<Peak> sfPeakList1,
                                          List<Peak> sfPeakList2,
                                          double fragmentTolerance) {
        Set<Peak> matchedPeakInList1 = new HashSet<>();
        Set<Peak> matchedPeakInList2 = new HashSet<>();
        Set<Peak>[] matchedPeaks = new Set[]{matchedPeakInList1, matchedPeakInList2};
        Collections.sort(sfPeakList1, Peak.AscMzComparator);
        Collections.sort(sfPeakList2, Peak.AscMzComparator);

        for (Peak peak1 : sfPeakList1) {
            for (Peak peak2 : sfPeakList2) {
                double distance = peak1.getMz() - peak2.getMz();
                if (Math.abs(distance) > fragmentTolerance) {
                    if (distance < 0) {
                        break;
                    }
                } else {
                    matchedPeakInList1.add(peak1);
                    matchedPeakInList2.add(peak2);
                }
            }
        }
        return matchedPeaks;
    }

    /**
     * calculate the probability-based score
     *
     * @param n represents the number of peaks in processed spectrum with the most peaks
     * @param k is the number of matched peaks taking into account a given fragment tolerance
     * @param p is the probability of finding a single matched peak by chance
     *          and is calculated by dividing the number of retained high intensity peaks
     *          by the mass window size
     * @return the probability-based score
     */
    public double calProbabilityScore(int n, int k, double p) {
        double probabilityScore = 0.0;
        for (int i = k; i <= n; i++) {
            try {
                double combination = MathUtil.calCombination(n, i);
                probabilityScore += combination * Math.pow(p, i) * Math.pow(1 - p, n - i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return probabilityScore;
    }

    public double calIntensityScore(List<Peak> sfPeakList1,
                                    List<Peak> sfPeakList2,
                                    Set<Peak> matchedPeakInList1,
                                    Set<Peak> matchedPeakInList2) {

        // intensity accumulation of peaks in precessed spectrum
        double sfPeakList1Sum = sfPeakList1.stream()
                .mapToDouble(Peak::getIntensity).sum();
        double sfPeakList2Sum = sfPeakList2.stream()
                .mapToDouble(Peak::getIntensity).sum();
        double matchedPeak1Sum = matchedPeakInList1.stream()
                .mapToDouble(Peak::getIntensity).sum();
        double matchedPeak2Sum = matchedPeakInList2.stream()
                .mapToDouble(Peak::getIntensity).sum();

        // calculate the intensity-based score
        double intensityScore = (matchedPeak1Sum / sfPeakList1Sum) * (matchedPeak2Sum / sfPeakList2Sum);
//        System.out.println("intensityScore = ( " + matchedPeak1Sum + " / " + sfPeakList1Sum + " )"
//                + " * " + "( " + matchedPeak2Sum + " / " + sfPeakList2Sum + " )");
        return intensityScore;
    }
}
