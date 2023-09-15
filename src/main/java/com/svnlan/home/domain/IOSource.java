package com.svnlan.home.domain;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.svnlan.home.utils.ObjUtil;
import lombok.Data;

import java.util.Date;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 15:17
 */
@Data
@TableName("io_source")
public class IOSource {

    @TableField(exist = false)
    private Long id;
    @TableId(value = "sourceID", type = IdType.AUTO)
    private Long sourceID;
    /**
     * id的hash
     */
    @TableField(value = "sourceHash")
    private String sourceHash;
    /**
     * 文档所属类型 (0-sys,1-user,2-group)
     */
    @TableField(value = "targetType")
    private Integer targetType;
    /**
     * 拥有者对象id
     */
    @TableField(value = "targetID")
    private Long targetID;
    /**
     * 创建者id
     */
    @TableField(value = "createUser")
    private Long createUser;
    /**
     * 最后修改者
     */
    @TableField(value = "createUser")
    private Long modifyUser;
    /**
     * 是否为文件夹(0否,1是)
     */
    @TableField(value = "isFolder")
    private Integer isFolder;
    /**
     * 文件名
     */
    @TableField(value = "name")
    private String name;
    /**
     * 文件扩展名，文件夹则为空
     */
    @TableField(value = "fileType")
    private String fileType;
    /**
     * 父级资源id，为0则为部门或用户根文件夹，添加用户部门时自动新建
     */
    @TableField(value = "parentID")
    private Long parentID;
    /**
     * 父路径id; 例如:  ,2,5,10,
     */
    @TableField(value = "parentLevel")
    private String parentLevel;
    @TableField(exist = false)
    private String oldSourceLevel;
    @TableField(exist = false)
    private String oldParentLevel;
    @TableField(exist = false)
    private Long oldParentID;
    /**
     * 对应存储资源id,文件夹则该处为0
     */
    @TableField(value = "fileID")
    private Long fileID;
    /**
     * 是否删除(0-正常 1-已删除)
     */
    @TableField(value = "isDelete")
    private Integer isDelete;
    /**
     * 占用空间大小
     */
    @TableField(value = "size")
    private Long size;
    /**
     *
     */
    @TableField(value = "createTime")
    private Long createTime;
    /**
     *
     */
    @TableField(value = "modifyTime")
    private Long modifyTime;
    /**
     * 最后访问时间
     */
    @TableField(value = "viewTime")
    private Long viewTime;
    /**
     * 是否可以分享 1 正常 0 禁止分享
     */
    @TableField(value = "canShare")
    private Integer canShare;
    @TableField(exist = false)
    private Long userID;
    @TableField(exist = false)
    private Long oldSourceID;
    @TableField(exist = false)
    private Long groupID;
//    @TableField(exist = false)
    private Integer type;
    @TableField(exist = false)
    private String hashMd5;
    @TableField(exist = false)
    private String icon;
    @TableField(exist = false)
    private String auth;
    @TableField("convertSize")
    private Long convertSize;
    @TableField("thumbSize")
    private Long thumbSize;
    @TableField("storageID")
    private Integer storageID;
    private String namePinyin;
    private String namePinyinSimple;

    public IOSource() {
    }

    public IOSource(Long sourceID, Long size) {
        this.sourceID = sourceID;
        this.size = size;
    }

    public IOSource(Long targetID, Integer isFolder, String name, Long parentID, String parentLevel) {
        this.targetID = this.createUser = this.modifyUser = targetID;
        this.isFolder = isFolder;
        this.name = name;
        this.parentID = parentID;
        this.parentLevel = parentLevel;
        this.createTime = this.modifyTime = System.currentTimeMillis();
        ObjUtil.initializefield(this);
    }
}
