package com.svnlan.home.dto;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IoSourceAuth;
import com.svnlan.jwt.domain.LoginUser;
import lombok.ToString;

import java.util.List;

/**
 * @Author:
 * @Description:
 */
@ToString
public class CheckFileDTO {

    private List<Long> sourceIds;
    private String hashMd5;

    private String name;
    private String fileName;

    private String busType;
    private String sourceLevel;

    private Long size;
    private Long fileID;

    private String busTypeCode;

    //视频转h264的路径
    private String h264Path;
    //视频转h264是否成功, 0未成功,1成功,2失败
    private Integer isH264Preview;

    private String operation;
    private String thumb;
    private String resolution;
    private Integer isCommon;
    private Long sourceID;
    private String path;
    private String desc;

    private String originHashMd5;
    private Integer chunk;
    private Integer chunks;
    private Double ignoreFileSize;

    private Long busId;
    private Long sourceId;
    private Long parentID;
    private String token;
    private Boolean viewSwf;
    private LoginUser loginUser;
    private List<SourceOpDto> dataArr;
    private String type;
    private String sourceName;
    private String cover;
    private Integer getInfo;
    private Long userID;
    private String finalFolderPath;
    private CommonSource commonSource;
    private String taskID;
    private String uuid;
    private String tempFolder;
    private String finalFilePath;
    private String fileType;
    private String temp;
    private Integer status;
    private String content;
    private String pathTo;
    private Long sourceIDTo;
    private String shareCode;
    private String view;
    private String suffix;
    private Long f;
    private Long id;
    private String auth;
    private List<IoSourceAuth> sourceAuthList;
    private Boolean directory;
    private String fullName;
    private String password;
    private Integer index;

    private String pUrl;
    private Double beginTime;
    private Double endTime;
    private Integer level = 5;

    /* 视频设置 */
    // 转码后缀
    private String convertSuffix;
    // 帧率
    private Integer frameRate;
    private List<VideoCutDto> cutList;
    // 旋转类型 1 左转 2 右转
    private String rotateType;
    private Integer rotate;
    // 翻转  1 水平 2 垂直
    private String overturnType;
    private String markText ;
    private String markUrl;
    private String markName;
    // 字幕
    private List<VideoCutDto> captionsList;
    private String captions;
    private String captionsUrl;
    // 变速
    private String shifting;
    // 变速
    private String volumeShifting;
    // 音量
    private String volume;
    // 防抖 减震
    // 设置视频的抖动程度以及相机的速度。它接受1-10范围内的整数，值1表示少量抖动，值10表示强烈颤抖。默认值为5。
    private String dampingShakiness;
    // 设置检测过程的准确性。它必须是1-15范围内的值。值1表示精度低，值15表示高精度。默认值为15。
    private String dampingAccuracy = "15";
    /** 文档所属类型 (0-sys,1-user,2-group 3-资讯)*/
    private Integer targetType;
    private String parentLevel;
    private Long tenantId;
    private String fullPath;
    private String hash;
    private String preview;
    private String safePwd;
    private String oldSafePwd;
    private String code;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getOldSafePwd() {
        return oldSafePwd;
    }

    public void setOldSafePwd(String oldSafePwd) {
        this.oldSafePwd = oldSafePwd;
    }

    public String getSafePwd() {
        return safePwd;
    }

    public void setSafePwd(String safePwd) {
        this.safePwd = safePwd;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getFullPath() {
        return fullPath;
    }

    public void setFullPath(String fullPath) {
        this.fullPath = fullPath;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }

    public String getParentLevel() {
        return parentLevel;
    }

    public void setParentLevel(String parentLevel) {
        this.parentLevel = parentLevel;
    }

    public Integer getTargetType() {
        return targetType;
    }

    public void setTargetType(Integer targetType) {
        this.targetType = targetType;
    }

    public String getVolumeShifting() {
        return volumeShifting;
    }

    public void setVolumeShifting(String volumeShifting) {
        this.volumeShifting = volumeShifting;
    }

    public String getCaptionsUrl() {
        return captionsUrl;
    }

    public void setCaptionsUrl(String captionsUrl) {
        this.captionsUrl = captionsUrl;
    }

    public Integer getRotate() {
        return rotate;
    }

    public void setRotate(Integer rotate) {
        this.rotate = rotate;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getRotateType() {
        return rotateType;
    }

    public void setRotateType(String rotateType) {
        this.rotateType = rotateType;
    }

    public String getOverturnType() {
        return overturnType;
    }

    public void setOverturnType(String overturnType) {
        this.overturnType = overturnType;
    }

    public String getMarkText() {
        return markText;
    }

    public void setMarkText(String markText) {
        this.markText = markText;
    }

    public String getMarkUrl() {
        return markUrl;
    }

    public void setMarkUrl(String markUrl) {
        this.markUrl = markUrl;
    }

    public String getMarkName() {
        return markName;
    }

    public void setMarkName(String markName) {
        this.markName = markName;
    }

    public List<VideoCutDto> getCaptionsList() {
        return captionsList;
    }

    public void setCaptionsList(List<VideoCutDto> captionsList) {
        this.captionsList = captionsList;
    }

    public String getCaptions() {
        return captions;
    }

    public void setCaptions(String captions) {
        this.captions = captions;
    }

    public String getShifting() {
        return shifting;
    }

    public void setShifting(String shifting) {
        this.shifting = shifting;
    }

    public String getDampingShakiness() {
        return dampingShakiness;
    }

    public void setDampingShakiness(String dampingShakiness) {
        this.dampingShakiness = dampingShakiness;
    }

    public String getDampingAccuracy() {
        return dampingAccuracy;
    }

    public void setDampingAccuracy(String dampingAccuracy) {
        this.dampingAccuracy = dampingAccuracy;
    }

    public Integer getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(Integer frameRate) {
        this.frameRate = frameRate;
    }

    public String getConvertSuffix() {
        return convertSuffix;
    }

    public void setConvertSuffix(String convertSuffix) {
        this.convertSuffix = convertSuffix;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getpUrl() {
        return pUrl;
    }

    public void setpUrl(String pUrl) {
        this.pUrl = pUrl;
    }

    public Double getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Double beginTime) {
        this.beginTime = beginTime;
    }

    public Double getEndTime() {
        return endTime;
    }

    public void setEndTime(Double endTime) {
        this.endTime = endTime;
    }

    public List<VideoCutDto> getCutList() {
        return cutList;
    }

    public void setCutList(List<VideoCutDto> cutList) {
        this.cutList = cutList;
    }

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getDirectory() {
        return directory;
    }

    public void setDirectory(Boolean directory) {
        this.directory = directory;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<IoSourceAuth> getSourceAuthList() {
        return sourceAuthList;
    }

    public void setSourceAuthList(List<IoSourceAuth> sourceAuthList) {
        this.sourceAuthList = sourceAuthList;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public Long getF() {
        return f;
    }

    public void setF(Long f) {
        this.f = f;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getView() {
        return view;
    }

    public void setView(String view) {
        this.view = view;
    }

    public String getShareCode() {
        return shareCode;
    }

    public void setShareCode(String shareCode) {
        this.shareCode = shareCode;
    }

    public Long getSourceIDTo() {
        return sourceIDTo;
    }

    public void setSourceIDTo(Long sourceIDTo) {
        this.sourceIDTo = sourceIDTo;
    }

    public String getPathTo() {
        return pathTo;
    }

    public void setPathTo(String pathTo) {
        this.pathTo = pathTo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getTempFolder() {
        return tempFolder;
    }

    public void setTempFolder(String tempFolder) {
        this.tempFolder = tempFolder;
    }

    public String getFinalFilePath() {
        return finalFilePath;
    }

    public void setFinalFilePath(String finalFilePath) {
        this.finalFilePath = finalFilePath;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public CommonSource getCommonSource() {
        return commonSource;
    }

    public void setCommonSource(CommonSource commonSource) {
        this.commonSource = commonSource;
    }

    public String getFinalFolderPath() {
        return finalFolderPath;
    }

    public void setFinalFolderPath(String finalFolderPath) {
        this.finalFolderPath = finalFolderPath;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public Integer getGetInfo() {
        return getInfo;
    }

    public void setGetInfo(Integer getInfo) {
        this.getInfo = getInfo;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSourceLevel() {
        return sourceLevel;
    }

    public void setSourceLevel(String sourceLevel) {
        this.sourceLevel = sourceLevel;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public List<SourceOpDto> getDataArr() {
        return dataArr;
    }

    public void setDataArr(List<SourceOpDto> dataArr) {
        this.dataArr = dataArr;
    }

    public Long getParentID() {
        return parentID;
    }

    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }

    public List<Long> getSourceIds() {
        return sourceIds;
    }

    public void setSourceIds(List<Long> sourceIds) {
        this.sourceIds = sourceIds;
    }

    public Long getBusId() {
        return busId;
    }

    public void setBusId(Long busId) {
        this.busId = busId;
    }

    public Long getSourceId() {
        return sourceId;
    }

    public void setSourceId(Long sourceId) {
        this.sourceId = sourceId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getViewSwf() {
        return viewSwf;
    }

    public void setViewSwf(Boolean viewSwf) {
        this.viewSwf = viewSwf;
    }

    public LoginUser getLoginUser() {
        return loginUser;
    }

    public void setLoginUser(LoginUser loginUser) {
        this.loginUser = loginUser;
    }

    public Double getIgnoreFileSize() {
        return ignoreFileSize;
    }

    public void setIgnoreFileSize(Double ignoreFileSize) {
        this.ignoreFileSize = ignoreFileSize;
    }

    public Integer getChunks() {
        return chunks;
    }

    public void setChunks(Integer chunks) {
        this.chunks = chunks;
    }

    public String getOriginHashMd5() {
        return originHashMd5;
    }

    public void setOriginHashMd5(String originHashMd5) {
        this.originHashMd5 = originHashMd5;
    }

    public Integer getChunk() {
        return chunk;
    }

    public void setChunk(Integer chunk) {
        this.chunk = chunk;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getHashMd5() {
        return hashMd5;
    }

    public void setHashMd5(String hashMd5) {
        this.hashMd5 = hashMd5;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getFileID() {
        return fileID;
    }

    public void setFileID(Long fileID) {
        this.fileID = fileID;
    }

    public Integer getIsCommon() {
        return isCommon;
    }

    public void setIsCommon(Integer isCommon) {
        this.isCommon = isCommon;
    }

    public Long getSourceID() {
        return sourceID;
    }

    public void setSourceID(Long sourceID) {
        this.sourceID = sourceID;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getResolution() {
        return resolution;
    }

    public void setResolution(String resolution) {
        this.resolution = resolution;
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


    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public String getBusTypeCode() {
        return busTypeCode;
    }

    public void setBusTypeCode(String busTypeCode) {
        this.busTypeCode = busTypeCode;
    }

}
