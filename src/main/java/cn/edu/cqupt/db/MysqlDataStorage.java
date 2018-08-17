package cn.edu.cqupt.db;

import cn.edu.cqupt.clustering.io.ClusteringFileHandler;
import cn.edu.cqupt.model.Cluster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MysqlDataStorage {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://192.168.6.20:3306/huangjs";
    private static final String USER = "root";
    private static final String PASSWD = "root";

    // logger
    private Logger logger = LoggerFactory.getLogger(MysqlDataStorage.class);
    private static Long dataSize = 0L;

    // mysql query
    private static Connection conn = null;
    private static PreparedStatement clusterPS = null;
    private static PreparedStatement specPS = null;


    static {
        try {
            Class.forName(DRIVER);
            String clusterSql = "insert into cluster(id, ratio, av_precursor_mz, " +
                    "av_precursor_inten, mz, intensity, spec_count, spec_list) " +
                    "value(?, ?, ?, ?, ?, ?, ?, ?)";
            String specSql = "insert into spectrum(id, sequence, charge, precursor_mz, species) " +
                    "value(?, ?, ?, ?, ?)";
            conn = getConnection();
            clusterPS = conn.prepareStatement(clusterSql);
            specPS = conn.prepareStatement(specSql);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWD);
    }

    public void deleteData(String table) throws SQLException {
        String sql = "delete from " + table;
        PreparedStatement ps = getConnection().prepareStatement(sql);
        ps.execute();
    }

    public void bulkWriteData(String dir) throws Exception {
        int bulkSize = 6000;

        File[] fileArr = new File(dir).listFiles(file ->
                file.getName().endsWith(".clustering")
        );
        int remainingFileCount = fileArr.length;

        // write data
        List<Cluster> clusterList = new ArrayList<>(bulkSize);
        for (File f : fileArr) {
            clusterList.addAll(ClusteringFileHandler.getAllClusters(f));
            int counts = clusterList.size() / bulkSize;
            for (int m = 0; m < counts; m++) {
                int spectrumCount = clusterList.subList(m * bulkSize, bulkSize * (1 + m))
                        .parallelStream()
                        .mapToInt(c -> c.getSpecCount())
                        .reduce(0, Integer::sum);

                logger.info("the remaining {} files are to be processed", remainingFileCount);
                logger.info("{} clusters has {} spectra", bulkSize, spectrumCount);
                writeData(clusterList.subList(m * bulkSize, bulkSize * (1 + m)));
            }
            int rest = clusterList.size() % bulkSize;
            List<Cluster> tmp = clusterList.subList(clusterList.size() - rest, clusterList.size());
            clusterList = new ArrayList<>(tmp);
            remainingFileCount--;
        }
        if (clusterList.size() != 0) {
            int spectrumCount = clusterList
                    .parallelStream()
                    .mapToInt(c -> c.getSpecCount())
                    .reduce(0, Integer::sum);

            logger.info("{} clusters has {} spectra", clusterList.size(), spectrumCount);
            writeData(clusterList);
        }

    }

    public void writeData(List<Cluster> clusterList) throws SQLException {
        dataSize += clusterList.size();
        Long startTime = System.nanoTime();
        clusterList.stream().forEach(c -> {
            c.getSpectra().stream().forEach(s -> {
                try {
                    specPS.setString(1, s.getId());
                    specPS.setString(2, s.getSequence());
                    specPS.setInt(3, s.getCharge());
                    specPS.setFloat(4, s.getPrecursorMz());
                    specPS.setString(5, s.getSpecies());
                    specPS.addBatch();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });

            try {
                clusterPS.setString(1, c.getId());
                clusterPS.setFloat(2, c.getRatio());
                clusterPS.setFloat(3, c.getAvPrecursorMz());
                clusterPS.setFloat(4, c.getAvPrecursorIntens());
                clusterPS.setString(5, convert(c.getMzValues()));
                clusterPS.setString(6, convert(c.getIntensValues()));
                clusterPS.setInt(7, c.getSpecCount());
                clusterPS.setString(8, c.getSpectra().stream()
                        .map(s -> s.getId())
                        .reduce("", (a, b) -> String.join(",", a, b)));
                clusterPS.addBatch();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        specPS.executeBatch();
        clusterPS.execute();
        Long endTime = System.nanoTime();
        logger.info("{} data costs {}m", dataSize, (endTime - startTime) / 1.0e9);
    }

    private String convert(List<Float> floatList) {
        return floatList.stream().map(f -> f + "")
                .reduce("", (a, b) -> String.join(",", a, b)
                );
    }

    public void isConnected() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        String sql = "select * from test";
        conn = getConnection();
        ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            System.out.println("id = " + rs.getInt(1));
        }
        rs.close();
    }
}
