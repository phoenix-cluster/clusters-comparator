package cn.edu.cqupt.dao;

import cn.edu.cqupt.model.Cluster;
import junit.framework.TestCase;

import java.io.File;
import java.util.List;

public class ClusteringFileImportDaoImplTest extends TestCase {

    public void testGetResult() throws Exception {
        File clusteringFile = new File("C:\\@code\\java\\clusters-comparator\\testdata\\clutering\\cli_clustering.pxd000021.0.7_4.clustering");
        List<Cluster> data = new ClusteringFileImportDaoImpl().getResult(clusteringFile);
        data.stream().forEach(
                System.out::println
        );
    }
}