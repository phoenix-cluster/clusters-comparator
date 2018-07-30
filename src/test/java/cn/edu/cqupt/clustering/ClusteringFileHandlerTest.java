package cn.edu.cqupt.clustering;

import cn.edu.cqupt.clustering.io.ClusteringFileHandler;
import cn.edu.cqupt.clustering.io.ClusteringFileReader;
import cn.edu.cqupt.object.ICluster;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;


import java.io.File;
import java.util.List;

public class ClusteringFileHandlerTest extends TestCase {

    private List<ICluster> clusterList;
    @Before
    public void setUp() throws Exception {
        clusterList = new ClusteringFileReader(
                new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\cli_clustering.pxd000021.0.7_4.clustering"))
                .readAllClusters();
    }

    @Test
    public void testOldReassembleCluster() {
        ClusteringFileHandler.oldReassembleCluster(clusterList);
    }

    @Test
    public void testReassembleCluster() {
        ClusteringFileHandler.reassembleCluster(clusterList);
    }
}