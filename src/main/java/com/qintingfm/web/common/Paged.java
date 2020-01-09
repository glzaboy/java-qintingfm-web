package com.qintingfm.web.common;
/**
 * 分页参数
 */
public class Paged {
    /**
     * 当前页
     */
    private int page;
    /**
     * 每页条数
     */
    private int size;
    /**
     * 总条数
     */
    private long total;
    /**
     * 总页数
     */
    private int totalPage;
    /**
     * 请求地址
     */
    private String requestUrl;

    public Paged() {
    }

    public Paged(int page, int size) {
        this.page = page;
        this.size = size;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * 获取jpa页码
     * @return
     */
    public int getJpaPage(){
        return page-1;
    }
}
