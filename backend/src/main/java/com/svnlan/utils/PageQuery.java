package com.svnlan.utils;


/**
 * @Author:
 * @Description:
 */
public class PageQuery {


    /** 排序 */
    private String orderByClause;

    /** 是否需要统计查询 */
    private boolean needCount = false;

    /** 每页记录数 */
    private int pageSize = 20;

    /** 当前页 */
    private int currentPage = 1;

    /** 自定义分页用，查询的起始序号 */
    private Integer startIndex;

    public PageQuery() {
    }


    public String getOrderByClause() {
        return this.orderByClause;
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public boolean isNeedCount() {
        return this.needCount;
    }

    public void setNeedCount(boolean needCount) {
        this.needCount = needCount;
    }

    public static long getSerialversionuid() {
        return -6109041301910469195L;
    }

    public int getSize() {
        return this.getPageSize();
    }

    public int getPageSize() {
        return this.pageSize;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getStartIndex() {
        if(null == startIndex) {
            startIndex = (currentPage - 1)*pageSize;
            if(startIndex < 0) {
                startIndex = 0;
            }
        }
        return startIndex;
    }

    public void setStartIndex(Integer startIndex) {
        this.startIndex = startIndex;
    }

}

