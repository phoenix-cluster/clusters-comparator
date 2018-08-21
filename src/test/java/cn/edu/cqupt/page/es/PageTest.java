package cn.edu.cqupt.page.es;

import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.stream.IntStream;

public class PageTest {

    @Test
    public void testGetCurrentPageDataList() {
        Page page = new Page(10, "clusters");
        page.getCurrentPageDataList(1);
    }

    @Test
    public void testInsert() {
        TransportClient client = Page.client;
        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client, new BulkProcessor.Listener() {

                    @Override
                    public void beforeBulk(long l, BulkRequest bulkRequest) {

                    }

                    @Override
                    public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {

                    }

                    @Override
                    public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {

                    }
                })
                .setBulkActions(5000)
                .build();
        IntStream.range(0, 15_000)
                .forEach(i -> {
                    try {
                        XContentBuilder doc =
                                XContentFactory.jsonBuilder()
                                        .startObject()
                                        .field("id", i)
                                        .endObject();
                        bulkProcessor.add(new IndexRequest("page", "page", i + "")
                                .source(doc));

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        bulkProcessor.close();
    }
}