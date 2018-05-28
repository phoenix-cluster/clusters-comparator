package cn.edu.cqupt.cmgf;

import cn.edu.cqupt.mgf.MgfFileReader;
import cn.edu.cqupt.score.calculate.MS;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class CMGFReader {

    /**
     * read mgf file and cluster labels of each spectrum, then add them into object CMGF[]
     * @param mgfFile mgf file path
     * @param labelsFile1 cluster labels file path using algorithm I
     * @param labelsFile2 cluster labels file path using algorithm II
     * @return
     * @throws IOException
     */
    public static CMGF[] readCMGF(File mgfFile, File labelsFile1, File labelsFile2) throws IOException {

        // read mgf file and cluster labels of each spectrum
        List<MS> msList = MgfFileReader.getAllSpectra(mgfFile);
        String[] labels1 = readClusterLabels(labelsFile1);
        String[] labels2 = readClusterLabels(labelsFile2);

        // add into CMGF object
        CMGF[] cmgfArr = new CMGF[msList.size()];
        for(int i = 0; i < msList.size(); i++){
            cmgfArr[i] = new CMGF(msList.get(i), labels1[i], labels2[i]);
        }
        return cmgfArr;
    }

    private static String[] readClusterLabels(File clusterLabelsFile) throws IOException {
        return Files.readAllLines(clusterLabelsFile.toPath()).get(0).split(",");
    }

}
