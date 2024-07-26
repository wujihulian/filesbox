package com.svnlan.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author:
 * @Description: 分页结果器
 */
public class PageResult<E> {

    /** 总记录数 */
    private Long total;

    /** 列表 */
    private List<E> list;

    private Map<String, Object> result;

    private Boolean hasNextPage;

    public PageResult(){}

    public PageResult(Long total, List<E> list){
        this.total = total;
        this.list = list;
    }
    public PageResult(Long total, List<E> list, Map<String, Object> result){
        this.total = total;
        this.list = list;
        this.result = result;
    }
    public static PageResult emptyResult() {
        return new PageResult(0L, new ArrayList());
    }

    public PageResult(List<E> list, Boolean hasNextPage) {
        this.list = list;
        this.hasNextPage = hasNextPage;
    }
    //多取一条的分页处理
    public void checkNextPageWithRemove(List<E> list, PageQuery pageQuery) {
        Long total = list.size() > pageQuery.getPageSize() ? pageQuery.getStartIndex() + pageQuery.getPageSize() + 1L : 0L;
        if (total > 0){
            //查询数大于分页数时, 删掉最后一条
            list.remove(list.size() - 1);
            this.hasNextPage = true;
        } else {
            this.hasNextPage = false;
        }
        this.total = total;
        this.list = list;
    }
    
    public Map<String, Object> getResult() {
        return result;
    }

    public void setResult(Map<String, Object> result) {
        this.result = result;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public List<E> getList() {
        return list;
    }

    public void setList(List<E> list) {
        this.list = list;
    }

    public Boolean getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(Boolean hasNextPage) {
        this.hasNextPage = hasNextPage;
    }
}
