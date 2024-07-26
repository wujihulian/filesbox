package com.svnlan.home.domain;

/**
 * @Author:
 * @Description:
 */
public class ScaleInfo {
    private Double scale;
    private Integer w;
    private Integer h;

    private String typeString;

    public ScaleInfo(Double scale, String typeString){
        this.scale = scale;
        this.typeString = typeString;
    }
    public ScaleInfo(Integer w, Integer h, String typeString){
        this.w = w;
        this.h = h;
        this.typeString = typeString;
    }

    public Integer getW() {
        return w;
    }

    public void setW(Integer w) {
        this.w = w;
    }

    public Integer getH() {
        return h;
    }

    public void setH(Integer h) {
        this.h = h;
    }

    public Double getScale() {
        return scale;
    }

    public void setScale(Double scale) {
        this.scale = scale;
    }

    public String getTypeString() {
        return typeString;
    }

    public void setTypeString(String typeString) {
        this.typeString = typeString;
    }

}
