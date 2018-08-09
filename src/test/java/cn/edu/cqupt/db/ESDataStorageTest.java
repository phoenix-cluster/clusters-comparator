package cn.edu.cqupt.db;

import cn.edu.cqupt.clustering.io.ClusteringFileHandler;
import cn.edu.cqupt.model.Cluster;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.net.UnknownHostException;
import java.util.List;

public class ESDataStorageTest extends TestCase {

    private List<Cluster> clusterList = null;
    private ESDataStorage storage = new ESDataStorage();

    @Before
    public void setUp() throws Exception {

        // delete content from clusters and spectra
        storage.deleteBySearch("clusters");
        storage.deleteBySearch("spectra");

//        // data
//        clusterList = ClusteringFileHandler.getAllClusters(
//                new File("C:\\@code\\java\\clusters-comparator\\testdata\\clustering\\cli_clustering.pxd000021.0.7_4.clustering")
//        );
//        System.out.println(clusterList.size());
    }

    @Test
    public void testWriteData() throws UnknownHostException {
        storage.writeData(clusterList);
    }

    @Test
    public void testIsConnected(){
        storage.isConnected();
    }

    @Test
    public void testBulkWriteData() throws Exception {
        storage.bulkWriteData("C:\\@code\\java\\clustering_data\\201504");
    }
}