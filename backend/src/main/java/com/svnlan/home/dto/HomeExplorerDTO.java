package com.svnlan.home.dto;

import com.svnlan.utils.PageQuery;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 13:35
 */
@Data
public class HomeExplorerDTO extends PageQuery {

    private Long id;
    /**
     * 对应存储资源id,文件夹则该处为0
     */
    private Long fileID;
    private Long sourceID;
    private String keyword;
    private Long userID;
    /**
     * 父级资源id，为0则为部门或用户根文件夹，添加用户部门时自动新建
     */
    private Long parentID;
    /**
     * 占用空间大小
     */
    private Long usedSize;
    /**
     * 文件扩展名，文件夹则为空
     */
    private String fileType;
    /**
     * 父路径id; 例如:  ,2,5,10,
     */
    private String parentLevel;
    /**
     * 占用空间大小  最小
     */
    private Double minSize;
    /**
     * 占用空间大小  最大
     */
    private Double maxSize;
    /**
     * 创建时间  最小
     */
    private String timeFrom;
    /**
     * 创建时间  最大
     */
    private String timeTo;

    private String block;

    private Boolean fromType;
    private Long tagID;

    /** 排序方式	string	倒序desc 正序asc */
    private String sortType;
    /** 排序字段	 */
    private String sortField;
    private String shareCode;
    private Integer isFolder;

    /**  是否禁用浏览 */
    private Integer notDownload;
    /** 是否禁用下载 */
    private Integer notView;
    private Integer isShareTo;
    private Integer isLink;
    private String detail;
    private String authName;
    private Integer authID;
    private Long groupID;
    private String label;
    private String fullName;
    private String password;
    private Integer index;
    private Long repeatSourceID;
    private String hashMd5;
    private String repeatName;
    private Integer checkRecycle;
    private Long coverId;
}
