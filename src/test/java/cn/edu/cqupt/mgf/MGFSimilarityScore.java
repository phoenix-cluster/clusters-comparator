package cn.edu.cqupt.mgf;

import cn.edu.cqupt.score.calculate.MS;
import cn.edu.cqupt.score.calculate.SimilarityScore;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

public class MGFSimilarityScore {
    public static void main(String[] args) throws Exception {

        // read mgf data
        File mgfFile = new File("D:\\@project\\program\\documents\\java\\cluster-comparer\\MGF_clustering_visualization_DengChuan\\mgf\\qExHF01_02580.mgf");
//        File mgfFile = new File("D:\\@project\\program\\documents\\java\\cluster-comparer\\MGF_clustering_visualization_DengChuan\\mgf\\sample1.mgf");
        try {

            List<MS> msList = MgfFileReader.getAllSpectra(mgfFile);
            final List<MS> subMsList = msList.subList(10000, 12000); // 10001-12000
            System.out.println("mgf file has been read!");

            // calculate distance
            int size = subMsList.size();
            double[][] distances = new double[size][size];
            int shardSize = 20;
            double shardCount = Math.ceil((double) size / shardSize);
            CountDownLatch latch = new CountDownLatch(1);

            for (int i = 0; i < shardCount; i++) {
                for (int j = 0; j < shardSize && i * shardSize + j < size; j++) {
                    final int index = i * shardSize + j;
                    new Thread(() -> {
                        SimilarityScore simiScore = new SimilarityScore(subMsList.get(index), subMsList, 0.5);
                        Map<MS, Double> score = simiScore.onlyCalSimilarityScore();
                        int k = 0;
                        for (MS ms : subMsList) {
                            distances[index][k++] = score.get(ms);
                        }
                    }).start();

                }
            }
            latch.countDown();
            latch.await();

            // print distances
            File outputFile = new File(mgfFile.getParent() + "/result.txt");
            if (!outputFile.exists()) {
                outputFile.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(outputFile);
            System.out.println(outputFile);
            for (int i = 0; i < size; i++) {

                // print data
                for (int j = 0; j < size; j++) {
                    System.out.print(distances[i][j] + "\t");
                }

                // save data into file
                for (int k = i + 1; k < size; k++) {
                    fileWriter.write(distances[i][k] + "\n");
                }
                System.out.println();
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
