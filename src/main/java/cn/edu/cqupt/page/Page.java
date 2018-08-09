package cn.edu.cqupt.page;

import javafx.beans.property.SimpleIntegerProperty;

import java.util.List;

/**
 * support display data, but it will lead to an error if delete something from row data
 */
public class Page<T> {
    private SimpleIntegerProperty totalRecord; // total record number in source data
    private SimpleIntegerProperty pageSize; // the number of data in per page
    private SimpleIntegerProperty totalPage; // total page number
    private SimpleIntegerProperty currentPage; // current page number
    private List<T> rowDataList; // total data
    private List<T> currentPageDataList; // data in current page


    // setter
    public void setPageSize(int pageSize) {
        this.pageSize.set(pageSize);
    }

    // getter
    public int getTotalRecord() {
        return totalRecord.get();
    }

    public SimpleIntegerProperty totalRecordProperty() {
        return totalRecord;
    }

    public int getPageSize() {
        return pageSize.get();
    }

    public SimpleIntegerProperty pageSizeProperty() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage.get();
    }

    public SimpleIntegerProperty totalPageProperty() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage.get();
    }

    public SimpleIntegerProperty currentPageProperty() {
        return currentPage;
    }

    public List<T> getRowDataList() {
        return rowDataList;
    }

    public List<T> getCurrentPageDataList() {
        return currentPageDataList;
    }

    /**
     * @param rowDataList
     * @param pageSize    the number of data in per page
     */
    public Page(List<T> rowDataList, int pageSize) {
        this.totalRecord = new SimpleIntegerProperty(0);
        this.totalPage = new SimpleIntegerProperty(0);
        this.currentPage = new SimpleIntegerProperty(0);
        this.rowDataList = rowDataList;
        this.pageSize = new SimpleIntegerProperty(pageSize);
        initialize();


    }

    private void initialize() {
        totalRecord.set(rowDataList.size());
        totalPage.set(
                totalRecord.get() % pageSize.get() == 0 ?
                        totalRecord.get() / pageSize.get() :
                        totalRecord.get() / pageSize.get() + 1);

        pageSize.addListener((observable, oldVal, newVal) ->
                totalPage.set(
                        totalRecord.get() % pageSize.get() == 0 ?
                                totalRecord.get() / pageSize.get() :
                                totalRecord.get() / pageSize.get() + 1)
        );
    }

    /**
     * current page number(0-based system)
     *
     * @param currentPageValue current page number
     * @return
     */
    public List<T> getCurrentPageDataList(int currentPageValue) {
        currentPage.set(currentPageValue);
        int fromIndex = pageSize.get() * currentPage.get();
        int tmp = pageSize.get() * currentPage.get() + pageSize.get() - 1;
        int endIndex = tmp >= totalRecord.get() ? totalRecord.get() - 1 : tmp;

        // subList(fromIndex, toIndex) -> [fromIndex, toIndex)
        this.currentPageDataList = rowDataList.subList(fromIndex, endIndex + 1);
        return currentPageDataList;
    }
}
