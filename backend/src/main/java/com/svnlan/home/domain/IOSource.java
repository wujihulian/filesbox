package com.svnlan.home.domain;

import com.svnlan.home.utils.ObjUtil;
import lombok.Data;

import java.util.Date;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/13 15:17
 */
@Data
public class IOSource {

    //    @TableField(exist = false)
//    private Long id;
//    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * id的hash
     */
    // @TableField(value = "sourcehash")
    private String sourceHash;
    /**
     * 文档所属类型 (0-sys,1-user,2-group)
     */
    //  @TableField(value = "targettype")
    private Integer targetType;
    /**
     * 拥有者对象id
     */
    //  @TableField(value = "targetid")
    private Long targetId;
    /**
     * 创建者id
     */
    //  @TableField(value = "createuser")
    private Long createUser;
    /**
     * 最后修改者
     */
    //  @TableField(value = "createuser")
    private Long modifyUser;
    /**
     * 是否为文件夹(0否,1是)
     */
    //   @TableField(value = "isFolder")
    private Integer isFolder;
    /**
     * 文件名
     */
//    @TableField(value = "name")
    private String name;
    /**
     * 文件扩展名，文件夹则为空
     */
    //  @TableField(value = "filetype")
    private String fileType;
    /**
     * 父级资源id，为0则为部门或用户根文件夹，添加用户部门时自动新建
     */
    //  @TableField(value = "parentid")
    private Long parentId;
    /**
     * 父路径id; 例如:  ,2,5,10,
     */
    //   @TableField(value = "parentlevel")
    private String parentLevel;
    //   @TableField(exist = false)
    private String oldSourceLevel;
    //   @TableField(exist = false)
    private String oldParentLevel;
    //    @TableField(exist = false)
    private Long oldParentId;
    /**
     * 对应存储资源id,文件夹则该处为0
     */
    //   @TableField(value = "fileid")
    private Long fileId;
    /**
     * 是否删除(0-正常 1-已删除)
     */
    //   @TableField(value = "isdelete")
    private Integer isDelete;
    /**
     * 占用空间大小
     */
    //   @TableField(value = "size")
    private Long size;
    /**
     *
     */
    //   @TableField(value = "createtime")
    private Long createTime;
    /**
     *
     */
    //  @TableField(value = "modifytime")
    private Long modifyTime;
    /**
     * 最后访问时间
     */
    //  @TableField(value = "viewtime")
    private Long viewTime;
    /**
     * 是否可以分享 1 正常 0 禁止分享
     */
    //   @TableField(value = "canshare")
    private Integer canShare;
    //   @TableField(exist = false)
    private Long userId;
    //   @TableField(exist = false)
    private Long oldSourceId;
    //   @TableField(exist = false)
    private Long groupId;
    //    @TableField(exist = false)
    private Integer type;
    //  @TableField(exist = false)
    private String hashMd5;
    //   @TableField(exist = false)
    private String icon;
    //   @TableField(exist = false)
    private String auth;
    //    @TableField("convertsize")
    private Long convertSize;
    //   @TableField("thumbsize")
    private Long thumbSize;
    //   @TableField("storageid")
    private Integer storageId;
    private Long tenantId;
    private String namePinyin;
    private String namePinyinSimple;
    private String description;
    private String dingUnionId;
    private String dingDentryId;
    private String dingSpaceId;
    private Integer isSafe;

    public IOSource() {
    }

    public IOSource(Long id, Long size) {
        this.id = id;
        this.size = size;
    }

    public IOSource(Long targetId, Integer isFolder, String name, Long parentID, String parentLevel) {
        this.targetId = this.createUser = this.modifyUser = targetId;
        this.isFolder = isFolder;
        this.name = name;
        this.parentId = parentID;
        this.parentLevel = parentLevel;
        this.createTime = this.modifyTime = System.currentTimeMillis();
        ObjUtil.initializefield(this);
    }
}
