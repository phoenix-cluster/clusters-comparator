package cn.edu.cqupt.cmgf;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.score.calculate.MS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CMGFReader {

    public static List<CMGF> readCMGFList(File mgfFile, File clusterLabelsFile) throws IOException {
        List<MS> msList = MgfFileReader.getAllSpectra(mgfFile);
        String[] clusterLabels = readClusterLabels(clusterLabelsFile);
//        System.out.println("msList.size = " + msList.size() + "\n" + "labels.size = " + clusterLabels.length);
        List<CMGF> cmgfList = new ArrayList<>();
        for(int i = 0; i < msList.size(); i++){
            cmgfList.add(new CMGF(msList.get(i), clusterLabels[i]));
        }
        return cmgfList;
    }

    private static String[] readClusterLabels(File clusterLabelsFile) throws IOException {
        return Files.readAllLines(clusterLabelsFile.toPath()).get(0).split(",");
    }

}
