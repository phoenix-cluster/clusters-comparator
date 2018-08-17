package cn.edu.cqupt.db;

import junit.framework.TestCase;
import org.junit.Before;

import java.sql.SQLException;

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