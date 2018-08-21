package cn.edu.cqupt.mgf;

import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.calculate.Peak;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

/**
 * Created by huangjs on 2018/4/1.
 */
public class MgfFileReader {
    /**
     * read a mgf file content and assemble it into an List<MS> object
     *
     * @param filePath
     * @return
     */
    public static List<MS> getAllSpectra(File filePath) throws IOException {
        List<MS> msList = new ArrayList<>();

        // match pattern
        String startPattern = "BEGIN IONS";
        String titlePattern = "TITLE=(.+)";
        String chargePattern = "CHARGE=(.+)";
        String peakPattern = "(\\d*\\.*\\d*)\\s(\\d*\\.*\\d*)";
        String endPattern = "END IONS";


        InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath));
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        MS tmpMS = null;
        while ((line = br.readLine()) != null) {
            if (Pattern.matches(startPattern, line)) {
                tmpMS = new MS();
                ArrayList<Peak> peakList = new ArrayList<>();
                tmpMS.setPeakList(peakList);
                continue;
            }
            if (Pattern.matches(titlePattern, line)) {
                Matcher matcher = Pattern.compile(titlePattern).matcher(line);
                matcher.find();
                tmpMS.setTitle(matcher.group(1));
                continue;
            }
            if (Pattern.matches(chargePattern, line)) {
                Matcher matcher = Pattern.compile(chargePattern).matcher(line);
                matcher.find();
                tmpMS.setCharge(matcher.group(1));
                continue;
            }
            if (Pattern.matches(peakPattern, line)) {
                Matcher matcher = Pattern.compile(peakPattern).matcher(line);
                matcher.find();
                Peak peak = new Peak(Double.parseDouble(matcher.group(1)),
                        Double.parseDouble(matcher.group(2)));
                tmpMS.getPeakList().add(peak);
                continue;
            }
            if (Pattern.matches(endPattern, line)) {
                msList.add(tmpMS);
                continue;
            }
        }
        return msList;
    }

    /**
     * read a mgf file content specified range and assemble it into an List<MS> object
     *
     * @param filePath
     * @param startInclusive 0-based system, inclusive
     * @param endExclusive   0-based system. exclusive
     * @return
     * @throws IOException
     */

    public static List<MS> getSpectraByRange(File filePath, int startInclusive, int endExclusive) throws IOException {
        int count = 0;
        List<MS> msList = new ArrayList<>();

        // match pattern
        String startPattern = "BEGIN IONS";
        String titlePattern = "TITLE=(.+)";
        String chargePattern = "CHARGE=(.+)";
        String peakPattern = "(\\d*\\.*\\d*)\\s(\\d*\\.*\\d*)";
        String endPattern = "END IONS";


        InputStreamReader reader = new InputStreamReader(new FileInputStream(filePath));
        BufferedReader br = new BufferedReader(reader);
        String line = null;
        MS tmpMS = null;
        while ((line = br.readLine()) != null) {
            if (Pattern.matches(startPattern, line)) {
                if (count++ < startInclusive) {
                    continue;
                } else {
                    tmpMS = new MS();
                    ArrayList<Peak> peakList = new ArrayList<>();
                    tmpMS.setPeakList(peakList);
                    break;
                }
            }
        }
        while ((line = br.readLine()) != null) {
            if (Pattern.matches(startPattern, line)) {
                if (count++ == endExclusive) {
                    break;
                }
                tmpMS = new MS();
                ArrayList<Peak> peakList = new ArrayList<>();
                tmpMS.setPeakList(peakList);
                continue;
            }

            if (Pattern.matches(titlePattern, line)) {
                Matcher matcher = Pattern.compile(titlePattern).matcher(line);
                matcher.find();
                tmpMS.setTitle(matcher.group(1));
                continue;
            }
            if (Pattern.matches(chargePattern, line)) {
                Matcher matcher = Pattern.compile(chargePattern).matcher(line);
                matcher.find();
                tmpMS.setCharge(matcher.group(1));
                continue;
            }
            if (Pattern.matches(peakPattern, line)) {
                Matcher matcher = Pattern.compile(peakPattern).matcher(line);
                matcher.find();
                Peak peak = new Peak(Double.parseDouble(matcher.group(1)),
                        Double.parseDouble(matcher.group(2)));
                tmpMS.getPeakList().add(peak);
                continue;
            }
            if (Pattern.matches(endPattern, line)) {
                msList.add(tmpMS);
                continue;
            }
        }
        return msList;
    }
}
