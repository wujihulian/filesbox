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
    private Long parentId;
    /** 父路径id; 例如:  ,2,5,10, */
    private String parentLevel;
    /** 对应存储资源id,文件夹则该处为0 */
    private Long fileId;
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
    private Long oldSourceId;
    private String path;
    private String fileName;
    /** 路径 */
    private String pathDisplay;
    private Long convertSize;
    private Long thumbSize;
    private Long fileSize;
    /**
     * 存储id 对应的 system_options 表主键
     */
    private Integer storageId;
    private Long tenantId;
    private String dingUnionId;
    private String dingDentryId;
    private String dingSpaceId;

    public IOSourceVo(){}
    public IOSourceVo(String name, Integer isFolder){
        this.name = name;
        this.isFolder = isFolder;
    }

    public IOSourceVo copyFromIoSource(IOSource ioSource) {
        this.id = ioSource.getId();
        this.sourceID = ioSource.getId();
        this.sourceHash = ioSource.getSourceHash();
        this.targetType = ioSource.getTargetType();
        this.targetID = ioSource.getTargetId();
        this.createUser = ioSource.getCreateUser();
        this.modifyUser = ioSource.getModifyUser();
        this.isFolder = ioSource.getIsFolder();
        this.name = ioSource.getName();
        this.fileType = ioSource.getFileType();
        this.parentId = ioSource.getParentId();
        this.parentLevel = ioSource.getParentLevel();
        this.fileId = ioSource.getFileId();
        this.isDelete = ioSource.getIsDelete();
        this.size = ioSource.getSize();
        this.createTime = ioSource.getCreateTime();
        this.modifyTime = ioSource.getModifyTime();
        this.viewTime = ioSource.getViewTime();
        this.storageId = ioSource.getStorageId();
        this.dingUnionId = ioSource.getDingUnionId();
        this.dingDentryId = ioSource.getDingDentryId();
        this.dingSpaceId = ioSource.getDingSpaceId();
        return this;
    }
}
