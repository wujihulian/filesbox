package com.svnlan.home.dto;

/**
 * @Author:
 * @Description: 上传过程中信息
 */
public class UploadStateDTO {
    //单个分段成功与否
    private Boolean upState;
    private Boolean checkExist;

    //分段文件是否全部完成
    private Boolean partAllDone;

    //原始文件md5
    private String checksum;

    //临时文件目录
    private String tempPath;

    //分段数
    private Integer chunks;

    //合并文件成功与否
    private Boolean mergeState;

    //业务类型
    private String busType;

    //文件名
    private String fileName;
    private String ext;

    public Boolean getCheckExist() {
        return checkExist;
    }

    public void setCheckExist(Boolean checkExist) {
        this.checkExist = checkExist;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Boolean getUpState() {
        return upState;
    }

    public void setUpState(Boolean upState) {
        this.upState = upState;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public String getTempPath() {
        return tempPath;
    }

    public void setTempPath(String tempPath) {
        this.tempPath = tempPath;
    }

    public Boolean getPartAllDone() {
        return partAllDone;
    }

    public void setPartAllDone(Boolean partAllDone) {
        this.partAllDone = partAllDone;
    }


    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public Boolean getMergeState() {
        return mergeState;
    }

    public void setMergeState(Boolean mergeState) {
        this.mergeState = mergeState;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
