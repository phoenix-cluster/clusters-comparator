package cn.edu.cqupt.mgf.ncluster.model;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.score.calculate.MS;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

public class NetworkGraphData {
    private Map<Integer, List<Integer>> group1;
    private Map<Integer, List<Integer>> group2;
    private String releaseIName;
    private String releaseIIName;
    private List<MS> msList;

    // some
    private Gson gson;

    public Map<Integer, List<Integer>> getGroup1() {
        return group1;
    }

    public Map<Integer, List<Integer>> getGroup2() {
        return group2;
    }

    public String getReleaseIName() {
        return releaseIName;
    }

    public String getReleaseIIName() {
        return releaseIIName;
    }

    public List<MS> getMsList() {
        return msList;
    }


    public NetworkGraphData(File mgfFile, File labelFile1, File labelFile2) throws IOException {
        gson = new Gson();

        /** read spectra data from mgf file **/
        msList = MgfFileReader.getAllSpectra(mgfFile);

        /** deal label file **/
        // group
        releaseIName = labelFile1.getName();
        releaseIIName = labelFile2.getName();
        group1 = group(labelFile1);
        group2 = group(labelFile2);
    }

    /**
     * @param labelFile
     * @return label => [ms_i, ms_j...]
     * @throws IOException
     */
    public Map<Integer, List<Integer>> group(File labelFile) throws IOException {
        Map<Integer, List<Integer>> group = new HashMap<>();
        int[] labelArr = Files.lines(labelFile.toPath())
                .map((String line) -> gson.fromJson(line, int[].class))
                .flatMapToInt((int[] arr) -> Arrays.stream(arr))
                .toArray();

        for (int i = 0; i < labelArr.length; i++) {
            Integer label = labelArr[i];
            if (!group.containsKey(label)) {
                group.put(label, new ArrayList<>());
            }
            group.get(label).add(i);
        }
        return group;
    }
}
