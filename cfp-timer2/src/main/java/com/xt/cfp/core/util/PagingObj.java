package com.xt.cfp.core.util;

import java.util.List;

/**
 * 分页对象
 * User: yulei
 * Date: 13-10-31
 * Time: 下午1:46
 */
public class PagingObj<T> {

    protected int pageNum = 1;
    protected int pageSize = 10;
    protected int pageCount;

    protected int count;
    protected int totalCount;
    protected List<T> data;

    public int getPageNum() {
        return pageNum;
    }

    public void setPageNum(int pageNum) {
        this.pageNum = pageNum;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        if (pageSize > 0) {
            int i = totalCount / pageSize;
            int m = totalCount % pageSize;
            if (m > 0) {
                i ++;
            }
            this.pageCount = i;
        }
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
        this.count = this.data != null ? this.data.size() : 0;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    @Override
    public String toString() {
        return "PagingObj{" +
                "pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", pageCount=" + pageCount +
                ", count=" + count +
                ", totalCount=" + totalCount +
                ", data=" + data +
                '}';
    }
}
