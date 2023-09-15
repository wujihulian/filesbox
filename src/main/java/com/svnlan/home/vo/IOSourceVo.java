package com.svnlan.home.vo;

import com.svnlan.home.domain.IOSource;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/20 12:45
 */
@Data
public class IOSourceVo {
    private Integer fileCount;
    private Long id;
    private Long sourceID;
    /** id的hash*/
    private String sourceHash;
    /** 文档所属类型 (0-sys,1-user,2-group)*/
    private Integer targetType;
    /** 拥有者对象id*/
    private Long targetID;
    /** 创建者id */
    private Long createUser;
    /** 最后修改者 */
    private Long modifyUser;
    /** 是否为文件夹(0否,1是) */
    private Integer isFolder;
    /** 文件名 */
    private String name;
    /** 文件扩展名，文件夹则为空 */
    private String fileType;
    /** 父级资源id，为0则为部门或用户根文件夹，添加用户部门时自动新建 */
    private Long parentID;
    /** 父路径id; 例如:  ,2,5,10, */
    private String parentLevel;
    /** 对应存储资源id,文件夹则该处为0 */
    private Long fileID;
    /** 是否删除(0-正常 1-已删除) */
    private Integer isDelete;
    /** 占用空间大小 */
    private Long size;
    /**  */
    private Long createTime;
    /**  */
    private Long modifyTime;
    /** 最后访问时间 */
    private Long viewTime;
    private Long userID;
    private Long oldSourceID;
    private String path;
    private String fileName;
    /** 路径 */
    private String pathDisplay;
    private Long convertSize;
    private Long thumbSize;
    /**
     * 存储id 对应的 system_options 表主键
     */
    private Integer storageID;

    public IOSourceVo(){}
    public IOSourceVo(String name, Integer isFolder){
        this.name = name;
        this.isFolder = isFolder;
    }

    public IOSourceVo copyFromIoSource(IOSource ioSource) {
        this.sourceID = ioSource.getSourceID();
        this.sourceHash = ioSource.getSourceHash();
        this.targetType = ioSource.getTargetType();
        this.targetID = ioSource.getTargetID();
        this.createUser = ioSource.getCreateUser();
        this.modifyUser = ioSource.getModifyUser();
        this.isFolder = ioSource.getIsFolder();
        this.name = ioSource.getName();
        this.fileType = ioSource.getFileType();
        this.parentID = ioSource.getParentID();
        this.parentLevel = ioSource.getParentLevel();
        this.fileID = ioSource.getFileID();
        this.isDelete = ioSource.getIsDelete();
        this.size = ioSource.getSize();
        this.createTime = ioSource.getCreateTime();
        this.modifyTime = ioSource.getModifyTime();
        this.viewTime = ioSource.getViewTime();
        this.storageID = ioSource.getStorageID();
        return this;
    }
}
