package cn.edu.cqupt.db;

import cn.edu.cqupt.clustering.io.ClusteringFileHandler;
import cn.edu.cqupt.model.Cluster;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class ESDataStorage {
    public static final String CLUSTER_NAME = "elasticsearch";
    public static final String HOST_IP = "192.168.6.20";
    public static final int PORT = 9300;
    public static TransportClient client;

    // logger
    private static final Logger logger = LoggerFactory.getLogger(ESDataStorage.class);
    private static Long dataSize = 0L;
    private int remainingFileCount;

    // other settings
    private final int bulkSize = 6000;

    static {
        Settings settings = Settings.builder()
                .put("cluster.name", CLUSTER_NAME)
                .build();

        try {
            client = new PreBuiltTransportClient(settings)
                    .addTransportAddress(new TransportAddress(InetAddress.getByName(HOST_IP), PORT));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void isConnected() {
        GetResponse response = client.prepareGet("test", "dataset", "1").get();
        System.out.println();
        System.out.println(response.getSourceAsString());

        try {
            XContentBuilder doc = XContentFactory.jsonBuilder().startObject()
                    .field("title", "client")
                    .array("letters", new String[]{"C", "D"})
                    .endObject();
            System.out.println("specDoc = " + Strings.toString(doc));
            IndexResponse indexResponse = client.prepareIndex("test", "dataset", "4")
                    .setSource(doc)
                    .get();
            System.out.println(indexResponse.status());
            System.out.println();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void deleteBySearch(String index) {
        BulkByScrollResponse response = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(client)
                .filter(QueryBuilders.matchAllQuery())
                .source(index)
                .get();
        System.out.println(response.getStatus());
    }

    public void bulkWriteData(String dir) throws Exception {
        File[] fileArr = new File(dir).listFiles(file ->
                file.getName().endsWith(".clustering")
        );
        remainingFileCount = fileArr.length;

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


    public void writeData(List<Cluster> clusterList) {
        dataSize += clusterList.size();
        Long startTime = System.nanoTime();
        final BulkRequestBuilder bulkRequest = client.prepareBulk();

        clusterList.stream().forEach(c -> {
            try {
                // spectrum
                c.getSpectra().stream().forEach(s -> {
                    try {
                        XContentBuilder specDoc = XContentFactory.jsonBuilder().startObject()
                                .field("id", s.getId())
                                .field("sequence", s.getSequence())
                                .field("charge", s.getCharge())
                                .field("precursorMz", s.getPrecursorMz())
                                .field("species", s.getSpecies())
                                .endObject();
                        bulkRequest.add(client.prepareIndex("spectra", "spectrum", s.getId())
                                .setSource(specDoc));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                // cluster
                XContentBuilder cluDoc = XContentFactory.jsonBuilder().startObject()
                        .field("id", c.getId())
                        .field("avPrecursorMz", c.getAvPrecursorMz())
                        .field("avPrecursorIntens", c.getAvPrecursorIntens())
                        .field("specCount", c.getSpecCount())
                        .field("ratio", c.getRatio())
                        .array("mzValues", convert(c.getMzValues()))
                        .array("intensValues", convert(c.getIntensValues()))
                        .array("spectraIdList",
                                c.getSpectra().stream()
                                        .map(s -> s.getId())
                                        .toArray(String[]::new))
                        .endObject();

                bulkRequest.add(client.prepareIndex("clusters", "cluster", c.getId())
                        .setSource(cluDoc));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        bulkRequest.execute().actionGet();
        Long endTime = System.nanoTime();
        logger.info("{} data costs {}m", dataSize, (endTime - startTime) / 1.0e9);
    }

    private float[] convert(List<Float> floatList) {
        float[] rs = new float[floatList.size()];
        IntStream.range(0, floatList.size())
                .forEach(i -> rs[i] = floatList.get(i));
        return rs;
    }
}
