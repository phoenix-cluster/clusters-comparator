package cn.edu.cqupt.db;

import cn.edu.cqupt.clustering.io.ClusteringFileHandler;
import cn.edu.cqupt.model.Cluster;
import junit.framework.TestCase;
import org.junit.Before;

import java.io.File;
import java.sql.SQLException;
import java.util.List;

public class MysqlDataStorageTest extends TestCase {
    private MysqlDataStorage storage = new MysqlDataStorage();

    @Before
    public void setUp() throws Exception {
        storage.deleteData("cluster");
        storage.deleteData("spectrum");
    }

    public void testBulkWriteData() throws Exception {
        storage.bulkWriteData("C:\\@code\\java\\clustering_data\\201504");
    }

    public void testIsConnected() throws SQLException {
        storage.isConnected();
    }
}