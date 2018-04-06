package cn.edu.cqupt.restful;

import cn.edu.cqupt.graph.UndirectedGraph;
import cn.edu.cqupt.model.Cluster;
import cn.edu.cqupt.model.Edge;
import cn.edu.cqupt.model.Spectrum;
import cn.edu.cqupt.model.Vertex;
import com.google.gson.Gson;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public class RestfulClient {

    public static void main(String[] args) {
        RestfulClient restfulClient = new RestfulClient();
        restfulClient.getClusterByRange("compare_5_clusters", "apm",
                null, 1, 3);
        restfulClient.getGraphByClusterId("cluster1_1", "compare_5_clusters",
                "compare_6_clusters");
    }

    /**
     * @param start
     * @param end
     * @param sortMethod
     * @param direction
     * @param releaseName
     * @return
     */
    public RestCluster[] getRestClusterByRange(String releaseName, String orderKey,
                                               String orderDirection, int startIndex,
                                               int endIndex) {
        int startLine = startIndex - 1;
        int size = endIndex - startIndex + 1;
        String url = "http://192.168.6.20:9091/cande/Test/clusters?" +
                "release=" + releaseName + "&" +
                "index_column=" + orderKey + "&" +
                "start_line=" + startLine + "&" +
                "size=" + size;
        RestTemplate restTemplate = new RestTemplate();
        String clusterStr = restTemplate.getForObject(url, String.class);

        // transform json to object
        Gson gson = new Gson();
        RestCluster[] restClusters = gson.fromJson(clusterStr, RestCluster[].class);
        return restClusters;
    }

    public List<Cluster> getClusterByRange(String releaseName, String orderKey,
                                           String orderDirection, int startIndex,
                                           int endIndex) {

        RestCluster[] restClusters = getRestClusterByRange(releaseName, orderKey,
                orderDirection, startIndex, endIndex);
        List<Cluster> clusters = new ArrayList<>(restClusters.length); // return value
        for (RestCluster restCluster : restClusters) {

            // correct CMz as mzValues
            String[] mzArr = restCluster.getCMz()
                    .replaceAll("[\\[|\\]]", "")
                    .replaceAll("\\s+", "")
                    .split(",");
            List<Float> mzValues = new ArrayList<>();
            for (String mz : mzArr) {
                mzValues.add(Float.parseFloat(mz));
            }

            // correct CIntens as intensValues
            String[] intensArr = restCluster.getCIntens()
                    .replaceAll("[\\[|\\]]", "")
                    .replaceAll("\\s+", "")
                    .split(",");
            List<Float> intensValues = new ArrayList<>();
            for (String intens : intensArr) {
                intensValues.add(Float.parseFloat(intens));
            }

            // correct species(RestSpectrum) as species(Spectrum)
            List<Spectrum> spectra = new ArrayList<>();
            for (RestSpectrum tmpSpec : restCluster.getSpectra()) {
                String species = tmpSpec.getSpecies()
                        .replaceAll("[\\[|\\]]", "");
                Spectrum spectrum = new Spectrum(tmpSpec.getTitle(),
                        tmpSpec.getSequence(), tmpSpec.getCharge(),
                        tmpSpec.getPrecursorMz(), species);
                spectra.add(spectrum);
            }
            Cluster cluster = new Cluster(restCluster.getId(),
                    restCluster.getAvPrecursorMz(), restCluster.getAvPrecursorIntens(),
                    restCluster.getSpecCount(), restCluster.getRatio(),
                    spectra, mzValues, intensValues);
            clusters.add(cluster);
        }
        return clusters;
    }

    public UndirectedGraph<Vertex, Edge> getGraphByClusterId(String clusterId, String releaseIName,
                                                             String releaseIIName){
        String url = "http://192.168.6.20:9091/cande/Test/graph?" +
                "clusterId=" + clusterId + "&" +
                "release1=" + releaseIName + "&" +
                "release2=" + releaseIIName;

        RestTemplate restTemplate = new RestTemplate();
        String undirectedGraphStr = restTemplate.getForObject(url, String.class);

        return null;
    }
//    private void printClusters(RestCluster[] restClusters) {
//        for (RestCluster restCluster : restClusters) {
//            System.out.println(restCluster);
//        }
//    }
}
