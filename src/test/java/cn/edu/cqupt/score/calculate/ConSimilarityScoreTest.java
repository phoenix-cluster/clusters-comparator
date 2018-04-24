package cn.edu.cqupt.score.calculate;

import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.service.ClusterTableService;

import java.io.File;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConSimilarityScoreTest {
    public static void main(String[] args) {

        File clusterFile1 = new File(
                "C:\\Users\\huangjs\\Desktop\\testdata\\clutering\\cli_clustering.pxd000021.0.7_4.clustering");
        File clusterFile2 = new File(
                "C:\\Users\\huangjs\\Desktop\\testdata\\clutering\\hdp_clustering.pxd000021.0.7_4.clustering");
        ClusterTableService serviceReleaseI = new ClusterTableService(clusterFile1);
        ClusterTableService serviceReleaseII = new ClusterTableService(clusterFile2);
        List<Cluster> clusterList1 = serviceReleaseI.getAllClusters();
        List<Cluster> clusterList2 = serviceReleaseII.getAllClusters();

        // transform clustering to ms
        MS ms1 = MS.clustering2MS(clusterList1.get(0));
        List<MS> msList = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            msList.add(MS.clustering2MS(clusterList2.get(i)));
        }

        // score
        SimilarityScore score = new SimilarityScore(ms1, msList, 0.5);
        long time0 = System.currentTimeMillis();
        score.calSimilarityScore();
        long time1 = System.currentTimeMillis();
        score.conCalSimilarityScore();
//        long time2 = System.currentTimeMillis();
        System.out.println("串行时间：" + (time1 - time0) + "ms");
//        System.out.println("并行时间：" + (time2 - time1) + "ms");
    }
}
