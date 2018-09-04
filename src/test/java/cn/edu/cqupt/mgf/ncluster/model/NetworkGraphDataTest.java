package cn.edu.cqupt.mgf.ncluster.model;

import cn.edu.cqupt.graph.UndirectedGraph;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class NetworkGraphDataTest {
    private NetworkGraphData networkGraphData;
    private File mgfFile;
    private File labelFile1;
    private File labelFile2;

    @Before
    public void startUp() throws IOException {
        mgfFile = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\spectra2000.mgf");
        labelFile1 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\labels.txt");
        labelFile2 = new File("C:\\@code\\java\\clusters-comparator\\testdata\\cms\\labelFromN-Clust.txt");

        networkGraphData = new NetworkGraphData(mgfFile, labelFile1, labelFile2);
    }

    @Test
    public void testGroup() throws IOException {
        Map<Integer, List<Integer>> group =  networkGraphData.group(labelFile1);
        group.keySet().stream()
                .forEach((Integer key)->System.out.println(key + " => " + group.get(key)));
    }
}