package cn.edu.cqupt.page;

import java.util.List;

/**
 * Created by huangjs on 2018/4/6.
 */
public class Page<T> {
    private int totalRecord; // total record number in source data
    private int pageSize; // the number of data in per page
    private int totalPage; // total page number
    private int currentPage; // current page number
    private List<T> rowDataList; // total data
    private List<T> currentPageDataList; // data in current page

    // setter
    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setRowDataList(List<T> rowDataList) {
        this.rowDataList = rowDataList;
    }

    // getter
    public int getTotalRecord() {
        return totalRecord;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public List<T> getRowDataList() {
        return rowDataList;
    }

    /**
     * method initialize() must be executed after set parameters
     */
    public Page() {

    }

    /**
     * @param rowDataList
     * @param pageSize the number of data in per page
     */
    public Page(List<T> rowDataList, int pageSize) {
        this.rowDataList = rowDataList;
        this.pageSize = pageSize;
        initialize();
    }

    private void initialize(){
        this.totalRecord = rowDataList.size();
        this.totalPage = totalRecord % pageSize == 0 ? totalRecord / pageSize : totalRecord / pageSize + 1;
    }

    /**
     * current page number(0-based system)
     * @param currentPage
     * @return
     */
    public List<T> getCurrentPageDataList(int currentPage){
        this.currentPage = currentPage;
        int fromIndex = pageSize * currentPage;
        int tmp = pageSize * currentPage + pageSize - 1;
        int endIndex = tmp > totalRecord ? totalRecord - 1 : tmp;

        // subList(fromIndex, toIndex) -> [fromIndex, toIndex)
        this.currentPageDataList = rowDataList.subList(fromIndex, endIndex + 1);
        return currentPageDataList;
    }

    public void changePageSize(int pageSize){
        this.pageSize = pageSize;
        initialize();
    }
}
