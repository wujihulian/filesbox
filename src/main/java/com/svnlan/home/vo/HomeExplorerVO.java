package com.svnlan.home.vo;

import lombok.Data;

import java.util.List;


/**
 * @author KingMgg
 * @data 2023/2/6 13:31
 */
@Data
public class HomeExplorerVO {

    private Double ignoreFileSize;
    private Long sizeUse;
    private Double userSizeMax;
    private Long userSizeUse;
    private Long groupID;
    private Long userID;
    private Long sort;
    private Double sizeMax;
    private String groupName;
    private String sourceType;
    private String path;
    private String hashMd5;
    /**
     * 来源id
     */
    private Long sourceID;
    private String keyword;
    /**
     * 拥有者对象id
     */
    private Long targetID;
    /**
     * 创建者id
     */
    private Long createUser;
    /**
     * 父级资源id，为0则为部门或用户根文件夹，添加用户部门时自动新建
     */
    private Long parentID;
    /**
     * 最后修改者
     */
    private Long modifyUser;
    /**
     * 对应存储资源id,文件夹则该处为0
     */
    private Long fileID;
    /**
     * 占用空间大小
     */
    private Long size;
    /**
     * 创建时间
     */
    private Long createTime;
    private Long fileCreateTime;
    /**
     * 最后修改时间
     */
    private Long modifyTime;
    /**
     * 最后访问时间
     */
    private Long viewTime;
    /**
     * id的hash
     */
    private String sourceHash;
    /**
     * 文件名
     */
    private String name="";
    /**
     * 文件扩展名，文件夹则为空
     */
    private String fileType;
    /**
     * 父路径id; 例如:  ,2,5,10,
     */
    private String parentLevel;
    /**
     * 文档所属类型 (0-sys,1-user,2-group)
     */
    private Integer targetType;
    /**
     * 是否为文件夹(0否,1是)
     */
    private Integer isFolder;
    /**
     * 是否删除(0-正常 1-已删除)
     */
    private Integer isDelete;

    private Boolean canDownload;
    private String ext;
    private String extType;
    private Boolean isTruePath;
    private Boolean isParent;
    private String type;
    private String pathDesc;
    private String pathDisplay;
    private String open;
    private String icon;
    private Integer hasFolder;
    private Integer hasFile;
    private Integer fileCount;
    private Boolean isChildren;

    private HomeExplorerResult children;

    private CommonLabelVo tagInfo;
    private String key;
    private String value;
    private String listViewKey;
    private String listViewValue;
    private Long id;

    private String resolution;
    private Integer length;
    private String thumb;
    private Integer authID;
    private String auth;

    private String createUserJson;
    private String modifyUserJson;
    private Integer isFav;
    private String tags;
    private String downloadUrl;
    private Integer isShare;


    private Long labelId;
    private String labelName;
    private Integer status;
    private String style;
    private Integer selected;

    /** 视频编码名称 */
    private String codecName;
    /** 平均帧率*/
    private String avgFrameRate;
    /** 实时帧率*/
    private String frameRate;
    /** 音频包的采样率 */
    private String sampleRate;
    /** 音频声道 */
    private String channels;
    /** 音频编码名称 */
    private String audioCodecName;
    /** 描述 */
    private String description;
    private String title;
    private String sourcePath;
    private Long timeTo;
    private Integer numView;
    private Integer numDownload;
    private String cover;
    private String authName;
    private List<CommonLabelVo> tagList;
    private String parentName;
    private String favPath;
    private String favType;
    private Long favID;
    private String favName;
    private String oexeContent;
    private HomeExplorerVO sourceInfo;
    private Long oexeSourceID;
    private Long oexeFileID;
    private Integer oexeIsFolder;
    private String oexeFileType;
    private String yzViewData;
    private String yzEditData;
    private String pptPreviewUrl;
    private String detail;
    private String avatar;
    private String nickname;
    private String shareHash;
    /**视频转h264的路径 */
    private String h264Path;
    /**  是否禁用 1 允许下载 0 禁用*/
    private Integer down;
    private Integer downNum;
    /** 是否禁用浏览 1 允许流量 0 禁用*/
    private Integer preview;

    private String roleList;
    private String label;
    private Integer isGroup;
    private Long shareID;
    private Integer isExistFile;
    private Integer isM3u8;
    private Integer canShare;
    private String viewUrl;
    private Integer isH264Preview;
    private String fileName;
    private String pUrl;

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    /*
    key: fileIconSize
value: 108
listViewKey: listType
listViewValue: icon:108
listViewPath: {source:1}/
     */

}
