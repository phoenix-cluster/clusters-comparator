package cn.edu.cqupt.page.es;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.index.reindex.ScrollableHitSource;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class Page {

    public static final String CLUSTER_NAME = "elasticsearch";
    public static final String HOST_IP = "192.168.6.20";
    public static int PORT = 9300;
    public static TransportClient client;

    /** page parameters **/
    private int pageSize;
    private long totalPageCount;
    private long totalRecordCount;
    private SearchResponse firstPageSearchResponse;
    private String index;

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

    public Page(int pageSize, String index) {
        this.pageSize = pageSize;
        this.index = index;
        initialize();
    }

    private void  initialize() {

        firstPageSearchResponse =  client.prepareSearch(index)
                .setSize(pageSize)
                .addSort("specCount", SortOrder.DESC)
                .addSort("_id", SortOrder.ASC)
                .get();
        totalRecordCount = firstPageSearchResponse.getHits().getTotalHits();
        totalPageCount = totalPageCount % pageSize == 0
                ? totalRecordCount / pageSize
                : totalRecordCount / pageSize + 1;
    }

    /**
     * we can use the sort values of the last document and
     * pass it to search_after to retrieve the next page of results:
     *
     * @param pageIndex the page number of current page, 0-based system
     */
    public void getCurrentPageDataList(long pageIndex) {
        SearchResponse response = null;

        pageIndex = pageIndex < totalPageCount ? pageIndex : totalPageCount - 1;

        if (pageIndex == 0) {
            response = firstPageSearchResponse;
        } else {
            // get the last document's sort values
           SearchHit[] hits = firstPageSearchResponse.getHits().getHits();
            for (int i = 1; i < pageIndex; i++) {
                response = client.prepareSearch("clusters")
                        .setSize(pageSize)
                        .addSort("specCount", SortOrder.DESC)
                        .addSort("_id", SortOrder.ASC)
                        .searchAfter(hits[hits.length - 1].getSortValues())
                        .setFetchSource(false)
                        .get();
                hits = response.getHits().getHits();
            }
            hits = pageIndex == 1
                    ? firstPageSearchResponse.getHits().getHits()
                    : response.getHits().getHits();
            response = client.prepareSearch("clusters")
                    .setSize(pageSize)
                    .addSort("specCount", SortOrder.DESC)
                    .addSort("_id", SortOrder.ASC)
                    .searchAfter(hits[hits.length - 1].getSortValues())
                    .get();
        }

        for (SearchHit hit : response.getHits().getHits()) {
            System.out.println(hit.getId());
        }
    }
}
