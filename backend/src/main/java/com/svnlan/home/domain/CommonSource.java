package com.svnlan.home.domain;


import lombok.ToString;

@ToString
public class CommonSource {

    private Long groupID;
    private Long sourceID;
    private Long fileID;

    private String hashMd5;

    private String path;

    private String name;

    private String fileType;

    private Long size;

    private Integer isPreview;

    private Integer isM3u8;

    private String previewUrl;

    private Integer appPreview;

    private String appPreviewUrl;

    private String thumb;

    private Integer sourceLength;

    private Long userID;

    private Integer sourceType;

    private String approvalState;

    private Long commonPicClassifyId;

    // private String sourcePathPre;

    private String classifyType;

    private Long fkCommonSourceId;

    private Long sourceDeleteId;

    private Long newCommonSourceId;

    private String downloadUrl;

    private String resolution;

    //视频转h264的路径
    private String h264Path;
    //视频转h264是否成功, 0未成功,1成功,2失败
    private Integer isH264Preview;

    private Long tempCommonSourceId;
    private String docPreviewUrl;
    private Long parentID;
    private String parentLevel;
    private String value;
    private Integer isFolder;
    /** 文档所属类型 (0-sys,1-user,2-group 3-资讯)*/
    private Integer targetType;
    private String sourceName;
    private String requestRootUrl;

    private String opType;
    private Long createTime;
    private String domain;
    private String isEdit;
    private String yzViewData;
    private String yzEditData;
    private String token;
    private Integer type;
    private Integer needHashMd5;
    private String pdfPath;
    private Long convertSize;
    private Long thumbSize;
    private Long fileConvertSize;
    private Long fileThumbSize;
    private String frame;
    //合并文件成功与否
    private Boolean checkMerge;
    private String description;

    private Long tenantId;
    private Long userId;
    private Long parentId;
    private Long targetId;
    private Long targetID;
    private Long fileId;
    private Long groupId;
    private String dingUnionId;
    private String dingDentryId;
    private String dingSpaceId;
    private String dingEventJson;
    private String sourceHash;
    private boolean updateParentSize;
    private String oexeContent;
    private Integer isExistFile;
    private Integer isDelete;
    private Integer sort;

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }


    public Integer getIsExistFile() {
        return isExistFile;
    }

    public void setIsExistFile(Integer isExistFile) {
        this.isExistFile = isExistFile;
    }

    public String getOexeContent() {
        return oexeContent;
    }

    public void setOexeContent(String oexeContent) {
        this.oexeContent = oexeContent;
    }

    public boolean isUpdateParentSize() {
        return updateParentSize;
    }

    public void setUpdateParentSize(boolean updateParentSize) {
        this.updateParentSize = updateParentSize;
    }

    public String getSourceHash() {
        return sourceHash;
    }

    public void setSourceHash(String sourceHash) {
        this.sourceHash = sourceHash;
    }

    public String getDingEventJson() {
        return dingEventJson;
    }

    public void setDingEventJson(String dingEventJson) {
        this.dingEventJson = dingEventJson;
    }

    public String getDingUnionId() {
        return dingUnionId;
    }

    public void setDingUnionId(String dingUnionId) {
        this.dingUnionId = dingUnionId;
    }

    public String getDingDentryId() {
        return dingDentryId;
    }

    public void setDingDentryId(String dingDentryId) {
        this.dingDentryId = dingDentryId;
    }

    public String getDingSpaceId() {
        return dingSpaceId;
    }

    public void setDingSpaceId(String dingSpaceId) {
        this.dingSpaceId = dingSpaceId;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
        this.groupID = groupId;
    }

    public Long getTargetId() {
        return targetId;
    }

    public void setTargetId(Long targetId) {
        this.targetId = targetId;
        this.targetID = targetId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
        this.fileID = fileId;
    }

    public Long getTargetID() {
        return targetID;
    }

    public void setTargetID(Long targetID) {
        this.targetID = targetID;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
        this.userID = userId;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
        this.parentID = parentId;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getCheckMerge() {
        return checkMerge;
    }

    public void setCheckMerge(Boolean checkMerge) {
        this.checkMerge = checkMerge;
    }

    public String getFrame() {
        return frame;
    }

    public void setFrame(String frame) {
        this.frame = frame;
    }

    public Long getFileConvertSize() {
        return fileConvertSize;
    }

    public void setFileConvertSize(Long fileConvertSize) {
        this.fileConvertSize = fileConvertSize;
    }

    public Long getFileThumbSize() {
        return fileThumbSize;
    }

    public void setFileThumbSize(Long fileThumbSize) {
        this.fileThumbSize = fileThumbSize;
    }

    public Long getConvertSize() {
        return convertSize;
    }

    public void setConvertSize(Long convertSize) {
        this.convertSize = convertSize;
    }

    public Long getThumbSize() {
        return thumbSize;
    }

    public void setThumbSize(Long thumbSize) {
        this.thumbSize = thumbSize;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    private Integer canShare;

    public Integer getCanShare() {
        return canShare;
    }

    public void setCanShare(Integer canShare) {
        this.canShare = canShare;
    }

    public Integer getNeedHashMd5() {
        return needHashMd5;
    }

    public void setNeedHashMd5(Integer needHashMd5) {
        this.needHashMd5 = needHashMd5;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getYzViewData() {
        return yzViewData;
    }

    public void setYzViewData(String yzViewData) {
        this.yzViewData = yzViewData;
    }

    public String getYzEditData() {
        return yzEditData;
    }

    public void setYzEditData(String yzEditData) {
        this.yzEditData = yzEditData;
    }

    public String getIsEdit() {
        return isEdit;
    }

    public void setIsEdit(String isEdit) {
        this.isEdit = isEdit;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getOpType() {
        return opType;
    }

    public void setOpType(String opType) {
        this.opType = opType;
    }

    public String getRequestRootUrl() {
        return requestRootUrl;
    }

    public void setRequestRootUrl(String requestRootUrl) {
        this.requestRootUrl = requestRootUrl;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public Integer getIsFolder() {
        return isFolder;
    }

    public void setIsFolder(Integer isFolder) {
        this.isFolder = isFolder;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getHashMd5() {
        return hashMd5;
    }

    public void setHashMd5(String hashMd5) {
        this.hashMd5 = hashMd5;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public String getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(String parentLevel) {
        this.parentLevel = parentLevel;
    }

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }

    public Long getGroupID() {
        return groupID;
    }

    public void setGroupID(Long groupID) {
        this.groupID = groupID;
    }

    public String getDocPreviewUrl() {
        return docPreviewUrl;
    }

    public void setDocPreviewUrl(String docPreviewUrl) {
        this.docPreviewUrl = docPreviewUrl;
    }

    public Long getTempCommonSourceId() {
        return tempCommonSourceId;
    }

    public void setTempCommonSourceId(Long tempCommonSourceId) {
        this.tempCommonSourceId = tempCommonSourceId;
    }

    public String getH264Path() {
        return h264Path;
    }

    public void setH264Path(String h264Path) {
        this.h264Path = h264Path;
    }

    public Integer getIsH264Preview() {
        return isH264Preview;
    }

    public void setIsH264Preview(Integer isH264Preview) {
        this.isH264Preview = isH264Preview;
    }

    public Integer getIsPreview() {
        return isPreview;
    }

    public void setIsPreview(Integer isPreview) {
        this.isPreview = isPreview;
    }

    public Integer getIsM3u8() {
        return isM3u8;
    }

    public void setIsM3u8(Integer isM3u8) {
        this.isM3u8 = isM3u8;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public Integer getAppPreview() {
        return appPreview;
    }

    public void setAppPreview(Integer appPreview) {
        this.appPreview = appPreview;
    }

    public String getAppPreviewUrl() {
        return appPreviewUrl;
    }

    public void setAppPreviewUrl(String appPreviewUrl) {
        this.appPreviewUrl = appPreviewUrl;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public Integer getSourceLength() {
        return sourceLength;
    }

    public void setSourceLength(Integer sourceLength) {
        this.sourceLength = sourceLength;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Integer getSourceType() {
        return sourceType;
    }

    public void setSourceType(Integer sourceType) {
        this.sourceType = sourceType;
    }

    public String getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(String approvalState) {
        this.approvalState = approvalState;
    }

    public Long getCommonPicClassifyId() {
        return commonPicClassifyId;
    }

    public void setCommonPicClassifyId(Long commonPicClassifyId) {
        this.commonPicClassifyId = commonPicClassifyId;
    }

    public String getClassifyType() {
        return classifyType;
    }

    public void setClassifyType(String classifyType) {
        this.classifyType = classifyType;
    }

    public Long getFkCommonSourceId() {
        return fkCommonSourceId;
    }

    public void setFkCommonSourceId(Long fkCommonSourceId) {
        this.fkCommonSourceId = fkCommonSourceId;
    }

    public Long getSourceDeleteId() {
        return sourceDeleteId;
    }

    public void setSourceDeleteId(Long sourceDeleteId) {
        this.sourceDeleteId = sourceDeleteId;
    }

    public Long getNewCommonSourceId() {
        return newCommonSourceId;
    }

    public void setNewCommonSourceId(Long newCommonSourceId) {
        this.newCommonSourceId = newCommonSourceId;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
    }
}