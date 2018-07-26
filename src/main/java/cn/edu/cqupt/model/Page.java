package cn.edu.cqupt.model;

import java.util.List;

public class Page<T> {

    private int totalRecord; // total record number in source data
    private int pageSize; // the number of data in per page
    private int totalPage; // total page number
    private int currentPage; // current page number
    private List<T> dataList; // data in current page

    public Page() {

    }

    public Page(int totalRecord, int pageSize, int totalPage, int currentPage, List<T> dataList) {
        super();
        this.totalRecord = totalRecord;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.currentPage = currentPage;
        this.dataList = dataList;
    }

    public int getTotalRecord() {
        return totalRecord;
    }

    public void setTotalRecord(int totalRecord) {
        this.totalRecord = totalRecord;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public List<T> getDataList() {
        return dataList;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }
}
