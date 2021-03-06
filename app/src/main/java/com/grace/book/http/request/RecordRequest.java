package com.grace.book.http.request;

/**
 * Created by DuoNuo on 2017/3/4.
 */
public class RecordRequest extends BaseRequest {

    private String type;
    private int pageSize;
    private int pageIndex;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
