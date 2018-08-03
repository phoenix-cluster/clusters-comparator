package cn.edu.cqupt.db;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Spectrum;

import java.sql.*;
import java.util.List;

public class Mysql {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://192.168.6.20:3306/huangjs";
    private static final String USER = "huangjs";
    private static final String PASSWD = "huangjs";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWD);
    }

    public void executeSQL(List<Cluster> data) throws SQLException {
        Connection conn = null;
        PreparedStatement clusterPS = null;
        PreparedStatement specPS = null;
        String clusterSql = "insert into cluster(id, ratio, av_precursor_mz, av_precursor_inten, mz, intensity, spec_count, spec_list) " +
                "value(?, ?, ?, ?, ?, ?, ?, ?)";
        String specSql = "insert into spectrum(id, sequence, charge, precursor_mz, species) value(?, ?, ?, ?, ?)";
        int batchSize = 1000;
        try {
            conn = getConnection();
            conn.setAutoCommit(false);
            clusterPS = conn.prepareStatement(clusterSql);
            specPS = conn.prepareStatement(specSql);
            int i = 0;
            int j = 0;
            for (; i < data.size(); i++) {
                Cluster cluster = data.get(i);
                List<Spectrum> spectrumList = cluster.getSpectra();
                String[] specArr = new String[spectrumList.size()];

                // spectrum table
                for (int k = 0; k < spectrumList.size(); k++) {
                    j++;
                    Spectrum spec = spectrumList.get(k);
                    specArr[k] = spec.getId();
                    specPS.setString(1, spec.getId());
                    specPS.setString(2, spec.getSequence());
                    specPS.setFloat(3, spec.getCharge());
                    specPS.setFloat(4, spec.getPrecursorMz());
                    specPS.setString(5, spec.getSequence());
                    specPS.addBatch();
                    if(j % batchSize == 0){
                        specPS.executeBatch();
                    }
                }

                clusterPS.setString(1, cluster.getId());
                clusterPS.setFloat(2, cluster.getRatio());
                clusterPS.setFloat(3, cluster.getAvPrecursorMz());
                clusterPS.setFloat(4, cluster.getAvPrecursorIntens());
                clusterPS.setString(5, cluster.getMzValues().toString());
                System.out.println(cluster.getMzValues().toString());
                clusterPS.setString(6, cluster.getIntensValues().toString());
                clusterPS.setInt(7, cluster.getSpecCount());
                clusterPS.setString(8, String.join(",", specArr));
                clusterPS.addBatch();
                if (i % batchSize == 0) {
                    clusterPS.executeBatch();
                }
            }
            if (i % batchSize != 0) {
                clusterPS.executeBatch();
            }
            if(j % batchSize != 0){
                specPS.executeBatch();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
