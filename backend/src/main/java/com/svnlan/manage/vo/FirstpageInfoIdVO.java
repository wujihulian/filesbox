package com.svnlan.manage.vo;

import com.svnlan.utils.PageQuery;

import java.util.List;

/**
 * @Author:
 * @Description:
 */
public class FirstpageInfoIdVO extends PageQuery {
    /**
     * 资讯的主键集合
     */
    private List<Long> list;

    private Integer beginNum;

    private Integer endNum;

    private Integer infoTypeId;
    //"0"标示没有勾选，"1"标示勾选（包含下属子分类）
    private String isAll;
    /**网校入参：1平台，0网校 ，网校获取平台资讯列表*/
    private Integer type;
    private String infoType;

    public String getInfoType() {
        return infoType;
    }

    public void setInfoType(String infoType) {
        this.infoType = infoType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getIsAll() {
        return isAll;
    }

    public void setIsAll(String isAll) {
        this.isAll = isAll;
    }

    public Integer getBeginNum() {
        return beginNum;
    }

    public void setBeginNum(Integer beginNum) {
        this.beginNum = beginNum;
    }

    public Integer getEndNum() {
        return endNum;
    }

    public void setEndNum(Integer endNum) {
        this.endNum = endNum;
    }

    public Integer getInfoTypeId() {
        return infoTypeId;
    }

    public void setInfoTypeId(Integer infoTypeId) {
        this.infoTypeId = infoTypeId;
    }

    public List<Long> getList() {
        return list;
    }

    public void setList(List<Integer> Long) {
        this.list = list;
    }
}
