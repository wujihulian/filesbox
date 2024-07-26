package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.DocumentTypeEnum;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.enums.MetaEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.*;
import com.svnlan.home.domain.*;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.vo.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.user.dao.GroupSourceDao;
import com.svnlan.user.dao.UserFavDao;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 15:14
 */
@Slf4j
@Component
public class FileOptionTool {
    @Resource
    IoSourceMetaDao ioSourceMetaDaoImpl;
    @Resource
    IoSourceMetaDao ioSourceMetaDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    IoFileDao ioFileDao;
    @Resource
    IoSourceRecycleDao ioSourceRecycleDao;
    @Resource
    SystemSortTool systemSortTool;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    UserFavDao userFavDao;
    @Resource
    UserFavDao userFavDaoImpl;
    @Resource
    ShareDao shareDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    AsyncUtil asyncUtil;
    @Resource
    GroupSourceDao groupSourceDao;
    @Resource
    AsyncSourceFileOtherUtil asyncSourceFileOtherUtil;
    @Resource
    StorageService storageService;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    UserAuthTool userAuthTool;
    @Resource
    CopyFileSaveUtil copyFileSaveUtil;

    @Value("${ppt.des.key}")
    private String pptDesKey;
    @Value("${ppt.des.vi}")
    private String pptDesIV;
    @Value("${ppt.server.url}")
    private String pptServerUrl;
    @Value("${cdn.domain}")
    private String cdnDomain;

    public IOSource addCommonSource(long opUserId, CommonSource commonSource, EventEnum eventEnum) {
        return addCommonSource(opUserId, commonSource, eventEnum, null);
    }

    // 写入资源的
    public IOSource addCommonSource(long opUserId, CommonSource commonSource, EventEnum eventEnum,
                                    Long fileId) {

        IOFile ioFile = new IOFile();
        ioFile.setId(fileId);
        ioFile.setHashSimple("");
        ioFile.setName(commonSource.getName());
        ioFile.setSize(commonSource.getSize());
        ioFile.setIoType(0L);
        ioFile.setHashMd5(commonSource.getHashMd5());
        ioFile.setPath(commonSource.getPath());
        ioFile.setIsM3u8(ObjectUtils.isEmpty(commonSource.getIsM3u8()) ? 0 : commonSource.getIsM3u8());
        ioFile.setIsH264Preview(ObjectUtils.isEmpty(commonSource.getIsH264Preview()) ? 0 : commonSource.getIsH264Preview());
        ioFile.setIsH264Preview((!ObjectUtils.isEmpty(commonSource.getFileType()) && "mp3".equals(commonSource.getFileType())) ? 1 : ioFile.getIsH264Preview());
        ioFile.setIsPreview(ObjectUtils.isEmpty(commonSource.getIsPreview()) ? 0 : commonSource.getIsPreview());
        ioFile.setAppPreview(ObjectUtils.isEmpty(commonSource.getAppPreview()) ? 0 : commonSource.getAppPreview());
        ioFile.setFileName(commonSource.getPath().substring(commonSource.getPath().lastIndexOf("/") + 1, commonSource.getPath().length()));
        ioFile.setThumbSize(ObjectUtils.isEmpty(commonSource.getThumbSize()) ? 0L : commonSource.getThumbSize());
        ioFile.setConvertSize(ObjectUtils.isEmpty(commonSource.getConvertSize()) ? 0L : commonSource.getConvertSize());
        ioFile.setTenantId(commonSource.getTenantId());
        ioFile.setUserId(opUserId);
        try {
            if (Objects.isNull(ioFile.getId())) {
                ioFileDao.insert(ioFile);
            } else {
                // 更新
                ioFileDao.updateById(ioFile);
            }
        } catch (Exception e) {
            LogUtil.error(e, " setUserDefaultSource addFile error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }

        if (ObjectUtils.isEmpty(ioFile.getId()) || ioFile.getId() <= 0) {
            return null;
        }

        commonSource.setFileID(ioFile.getId());
        asyncSourceFileOtherUtil.asyncAddFileMeta(commonSource, ioFile);

        LogUtil.info("editIoSourceDetail---------------commonSource=" + JsonUtils.beanToJson(commonSource));
        IOSource source = null;
        if (!ObjectUtils.isEmpty(commonSource.getIsEdit()) && "1".equals(commonSource.getIsEdit())) {
            this.editIoSourceDetail(commonSource, opUserId);
        } else {
            source = this.addIoSourceDetail(commonSource, opUserId, ioFile.getId(), eventEnum);
        }
        return source;
    }

    // fileSave 保存编辑文档时添加file信息替换老的file信息，老信息插入历史表
    public void editIoSourceDetail(CommonSource commonSource, Long opUserId) {
        IOSource source = new IOSource();
        source.setModifyUser(opUserId);
        source.setFileId(commonSource.getFileID());
        source.setSize(commonSource.getSize());
        source.setId(commonSource.getSourceID());
        if (!ObjectUtils.isEmpty(commonSource.getThumbSize()) && commonSource.getThumbSize() > 0) {
            source.setThumbSize(commonSource.getThumbSize());
        }
        if (!ObjectUtils.isEmpty(commonSource.getConvertSize()) && commonSource.getConvertSize() > 0) {
            source.setConvertSize(commonSource.getConvertSize());
        }
        if (!ObjectUtils.isEmpty(commonSource.getFileType())) {
            source.setFileType(commonSource.getFileType());
        }
        try {
            ioSourceDao.updateSourceAddSizeInfo(source);
        } catch (Exception e) {
            LogUtil.error(e, " editIoSourceDetail addSource  updateSourceAddSizeInfo error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        log.info("fileOptionTool edite source => {}", source);
        // 添加文档操作event
        this.addSourceEvent(source.getId(), commonSource.getParentID(), opUserId, source.getName(), EventEnum.edit);
    }

    public IOSource addIoSourceDetail(CommonSource commonSource, Long opUserId, Long fileID, EventEnum eventEnum) {

        IOSource source = new IOSource();
        source.setName(commonSource.getName());
        source.setParentLevel(commonSource.getParentLevel());
        source.setCreateUser(opUserId);
        source.setModifyUser(opUserId);
        source.setTargetId(opUserId);
        source.setIsFolder(fileID <= 0 ? 1 : 0);
        source.setTargetType(commonSource.getTargetType());
        source.setFileType(commonSource.getFileType());
        source.setType(ObjectUtils.isEmpty(commonSource.getFileType()) ? 0 : DocumentTypeEnum.getTypeByExt(commonSource.getFileType()));
        source.setFileId(fileID);
        source.setParentId(commonSource.getParentID());
        source.setSize(commonSource.getSize());
        source.setThumbSize(ObjectUtils.isEmpty(commonSource.getThumbSize()) ? 0L : commonSource.getThumbSize());
        source.setConvertSize(ObjectUtils.isEmpty(commonSource.getConvertSize()) ? 0L : commonSource.getConvertSize());
        source.setSourceHash(commonSource.getSourceHash());
        if (1 == source.getTargetType() && commonSource.getParentID() <= 0) {
            LogUtil.error("addIoSourceDetail 不能添加根目录 source=" + JsonUtils.beanToJson(source));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        source.setStorageId(storageService.getDefaultStorageDeviceId());

        source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
        source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
        source.setTenantId(commonSource.getTenantId());
        source.setDingUnionId(commonSource.getDingUnionId());
        source.setDingDentryId(commonSource.getDingDentryId());
        source.setDingSpaceId(commonSource.getDingSpaceId());
        try {
            ioSourceDao.insert(source);
        } catch (Exception e) {
            LogUtil.error(e, " setUserDefaultSource addSource error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }

        if (!ObjectUtils.isEmpty(source.getId()) && source.getId() > 0) {
            commonSource.setSourceID(source.getId());
            asyncSourceFileOtherUtil.asyncAddSourceMeta(commonSource);

            if (Arrays.asList(1, 2).contains(source.getTargetType())) {
                // 添加文档操作event
                log.info("fileOptionTool add source => {}", source);
                this.addSourceEvent(source.getId(), commonSource.getParentID(), opUserId, source.getName(), eventEnum);
            }
        }
        return source;
    }

    public IOSource updateCommonSource(long opUserId, CommonSource commonSource) {

        IOFile ioFile = new IOFile();
        ioFile.setHashSimple("");
        ioFile.setName(commonSource.getName());
        ioFile.setSize(commonSource.getSize());
        ioFile.setIoType(0L);
        ioFile.setHashMd5(commonSource.getHashMd5());
        ioFile.setPath(commonSource.getPath());
        ioFile.setUserId(opUserId);
        ioFile.setIsM3u8(ObjectUtils.isEmpty(commonSource.getIsM3u8()) ? 0 : commonSource.getIsM3u8());
        ioFile.setIsH264Preview(ObjectUtils.isEmpty(commonSource.getIsH264Preview()) ? 0 : commonSource.getIsH264Preview());
        ioFile.setIsH264Preview((!ObjectUtils.isEmpty(commonSource.getFileType()) && "mp3".equals(commonSource.getFileType())) ? 1 : ioFile.getIsH264Preview());
        ioFile.setIsPreview(ObjectUtils.isEmpty(commonSource.getIsPreview()) ? 0 : commonSource.getIsPreview());
        ioFile.setAppPreview(ObjectUtils.isEmpty(commonSource.getAppPreview()) ? 0 : commonSource.getAppPreview());
        ioFile.setFileName(commonSource.getPath().substring(commonSource.getPath().lastIndexOf("/") + 1, commonSource.getPath().length()));
        ioFile.setThumbSize(ObjectUtils.isEmpty(commonSource.getThumbSize()) ? 0L : commonSource.getThumbSize());
        ioFile.setConvertSize(ObjectUtils.isEmpty(commonSource.getConvertSize()) ? 0L : commonSource.getConvertSize());
        try {
            ioFileDao.insert(ioFile);
        } catch (Exception e) {
            LogUtil.error(e, " updateCommonSource setUserDefaultSource addFile error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }

        if (ObjectUtils.isEmpty(ioFile.getId()) || ioFile.getId() <= 0) {
            return null;
        }

        commonSource.setFileID(ioFile.getId());

        IOFileMeta fileMeta = new IOFileMeta();
        fileMeta.setFileID(ioFile.getId());
        fileMeta.setKey("fileInfoMore");
        FileMetaVo fileMetaVo = new FileMetaVo();
        fileMetaVo.setThumb("");
        fileMetaVo.setAppViewUrl(commonSource.getAppPreviewUrl());
        fileMetaVo.setH264Path(commonSource.getH264Path());
        fileMetaVo.setResolution(commonSource.getResolution());
        fileMetaVo.setViewUrl(commonSource.getPreviewUrl());
        fileMetaVo.setLength(commonSource.getSourceLength());
        fileMeta.setValue(!ObjectUtils.isEmpty(fileMetaVo) ? JsonUtils.beanToJson(fileMetaVo) : "");
        try {
            ioFileDao.insertMeta(fileMeta);
        } catch (Exception e) {
            LogUtil.error(e, " updateCommonSource setUserDefaultSource insertMeta error");
        }

        IOSource source = new IOSource();
        source.setModifyUser(opUserId);
        source.setFileId(ioFile.getId());
        source.setSize(commonSource.getSize());
        if (!ObjectUtils.isEmpty(commonSource.getFileThumbSize()) && commonSource.getFileThumbSize() > 0) {
            source.setThumbSize(commonSource.getFileThumbSize());
        }
        source.setId(commonSource.getSourceID());
        try {
            ioSourceDao.updateSourceAddSizeInfo(source);
        } catch (Exception e) {
            LogUtil.error(e, " updateCommonSource updateSourceAddSizeInfo addSource error source=" + JsonUtils.beanToJson(source));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        return source;
    }

    public CommonSource getSourceInfo(Long sourceID) {
        return ioSourceDao.getSourceInfo(sourceID);
    }

    public CommonSource getSourceInfoByDentryId(String dentryId) {
        return ioSourceDao.getSourceInfoByDentryId(dentryId);
    }

    public void clearUserDentryIdByDentryId(String dentryId) {
        ioSourceDao.clearUserDentryIdByDentryId(dentryId);
    }

    /**
     * @Description: 文档操作event
     * @params: [sourceID 文档id, sourceParent 文档父文件夹id, userID 操作者id, name, eventEnum 事件类型]
     * @Return: void
     * @Author: sulijuan
     * @Date: 2023/2/16 15:20
     * @Modified:
     */
    public void addSourceEvent(Long sourceID, Long sourceParent, Long userID, String name, EventEnum eventEnum) {
        addSourceEvent(sourceID, sourceParent, userID, name, eventEnum, null);
    }

    public void addSourceEvent(Long sourceID, Long sourceParent, Long userID, String name, EventEnum eventEnum, String oldName) {
        if (ObjectUtils.isEmpty(sourceID)){
            LogUtil.error("addSourceEvent 添加失败 sourceId is null name=" + name);
            return;
        }
        IoSourceEvent event = new IoSourceEvent(sourceID, sourceParent, userID, eventEnum.getCode(), getSourceEventDesc(eventEnum, name, oldName));
        event.setTenantId(tenantUtil.getTenantIdByServerName());
        asyncSourceFileOtherUtil.asyncAddSourceEvent(event);
    }

    public void addSourceEventList(List<IoSourceEvent> list) {
        if (!CollectionUtils.isEmpty(list)){
            Long tenantId = tenantUtil.getTenantIdByServerName();
            list.forEach(n->n.setTenantId(tenantId));
        }
        asyncSourceFileOtherUtil.asyncAddSourceEventList(list);
    }

    public static String getSourceEventDesc(EventEnum eventEnum, String name, String oldName) {
        Map<String, String> reMap = new HashMap<>(1);
        switch (eventEnum.getValue()) {
            case "mkdir":
                reMap.put("createType", "mkdir");
                reMap.put("name", name);
                break;
            case "mkfile":
                reMap.put("createType", "mkfile");
                reMap.put("name", name);
                break;
            case "copy":
                reMap.put("createType", "copy");
                reMap.put("name", name);
                break;
            case "upload":
                reMap.put("createType", "upload");
                reMap.put("name", name);
                break;
            case "uploadNew":
                reMap.put("createType", "uploadNew");
                reMap.put("name", name);
                break;
            case "rename":
                reMap.put("from", oldName);
                reMap.put("to", name);
                break;
            case "toRecycle":
                reMap.put("content", "toRecycle");
                break;
            case "addDesc":
                reMap.put("content", name);
                break;
            case "rollBack":
                reMap.put("name", name);
                reMap.put("content", oldName);
                break;
            case "edit":
                reMap.put("ua", name);
                break;
            case "remove":
                reMap.put("content", name);
                break;
            case "restore":
            case "shareLinkAdd":
            case "shareLinkEdit":
            case "shareLinkRemove":
            case "shareToAdd":
            case "shareEdit":
            case "shareToRemove":
                reMap.put("content", eventEnum.getValue());
                break;
            default:
                reMap.put("content", eventEnum.getValue());
        }
        return JsonUtils.beanToJson(reMap);
    }

    public CommonSource selectByChecksum(String checksum, Long fileSize) {
        return ioFileDao.selectByChecksum(checksum, fileSize);
    }
    public CommonSource selectByChecksum(String checksum, Long fileSize, LocalDateTime time) {
        if (!ObjectUtils.isEmpty(time)) {
            return ioFileDao.selectByChecksumAndTime(checksum, fileSize, time);
        }
        return ioFileDao.selectByChecksum(checksum, fileSize);
    }

    public CommonSource getFileAttachment(Long sourceID) {
        return getFileAttachment(sourceID, 0L);
    }

    public CommonSource getFileAttachment(Long sourceID, Long id) {
        CommonSource commonSource = null;
        if (!ObjectUtils.isEmpty(id) && id > 0) {
            commonSource = ioFileDao.getHistoryFileAttachment(id);
        } else {
            commonSource = ioFileDao.getFileAttachment(sourceID);
        }

        if (!ObjectUtils.isEmpty(commonSource) && !ObjectUtils.isEmpty(commonSource.getValue())) {
            try {

                FileMetaVo fileMetaVo = JsonUtils.jsonToBean(commonSource.getValue(), FileMetaVo.class);
                if (!ObjectUtils.isEmpty(fileMetaVo)) {
                    commonSource.setThumb(fileMetaVo.getThumb());
                    commonSource.setAppPreviewUrl(fileMetaVo.getAppViewUrl());
                    commonSource.setH264Path(fileMetaVo.getH264Path());
                    commonSource.setResolution(fileMetaVo.getResolution());
                    commonSource.setPreviewUrl(fileMetaVo.getViewUrl());
                    commonSource.setSourceLength(fileMetaVo.getLength());
                    commonSource.setYzEditData(fileMetaVo.getYzEditData());
                    commonSource.setYzViewData(fileMetaVo.getYzViewData());
                    commonSource.setOexeContent(fileMetaVo.getOexeContent());
                    commonSource.setFrame(ObjectUtils.isEmpty(fileMetaVo.getFrame()) ?  fileMetaVo.getFrameRate() : fileMetaVo.getFrame());
                    if (!ObjectUtils.isEmpty(commonSource.getFrame()) && commonSource.getFrame().indexOf(".") > 0){
                        commonSource.setFrame(commonSource.getFrame().substring(0,commonSource.getFrame().indexOf(".")));
                    }
                }
            } catch (Exception e) {
                LogUtil.error(e, "getFileAttachment fileMeta 解析错误");
            }
        }
        return commonSource;
    }

    public String getM3u8Param(Long sourceId, String m3u8Key) {
        return "?sourceID=" + sourceId
                + "&sourceType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + m3u8Key;
    }

    public String getDownloadParam(Long sourceId, String downloadKey, String code) {
        return "?busId=" + sourceId + code + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey;
    }

    public String getPptPdfPreview2(String sourceSuffix, Integer isH264Preview, String downloadUrl) {
        if (sourceSuffix.equals("ppt") || sourceSuffix.equals("pptx")) {
            return null;
        }
        String pptPreviewUrl = downloadUrl;
        pptPreviewUrl += "&getInfo=1";
        if (sourceSuffix.equals("doc") || sourceSuffix.equals("docx")) {
            if (isH264Preview != null && isH264Preview == 1) {
                pptPreviewUrl += "&forwtop=1";
            } else {
                return null;
            }
        }
        return pptPreviewUrl;
    }

    // checkOperationPermission
    public void getLock(Long sourceID) {
        /*boolean getLock = stringRedisTemplate.opsForValue().setIfAbsent(GlobalConfig.CLOUD_OPERATION_LOCK_KEY + sourceID, "1");
        if (!getLock){
            throw new SvnlanRuntimeException(CodeMessageEnum.fileLockError.getCode());
        }
        stringRedisTemplate.expire(GlobalConfig.CLOUD_OPERATION_LOCK_KEY + sourceID, 60000, TimeUnit.MILLISECONDS);*/
    }

    public void delLock(Long sourceID) {
        // stringRedisTemplate.delete(GlobalConfig.CLOUD_OPERATION_LOCK_KEY + sourceID);
    }


    /**
     * @Description: 移动文件
     * @params: [updateFileDTO, paramMap]
     * @Return: boolean
     * @Modified:
     */
    public boolean moveFile(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (updateFileDTO.getSourceID() == null) {
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        if (CollectionUtils.isEmpty(updateFileDTO.getDataArr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        // 判断层级  0,1,2   0,1,2,5
        if (updateFileDTO.getSourceLevel().indexOf(updateFileDTO.getDataArr().get(0).getParentLevel() + updateFileDTO.getDataArr().get(0).getSourceID() + ",") >= 0) {
            throw new SvnlanRuntimeException(CodeMessageEnum.moveSubPathError.getCode());
        }

        // 相同目录剪切
        Long fromSourceId = updateFileDTO.getDataArr().get(0).getParentID();
        if (!ObjectUtils.isEmpty(fromSourceId) && fromSourceId.longValue() == updateFileDTO.getSourceID().longValue()){
            return true;
        }

        CommonSource cs = ioSourceDao.getSourceInfo(updateFileDTO.getSourceID());
        if (ObjectUtils.isEmpty(cs)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, cs.getSourceID(), cs.getParentLevel(), "8", cs.getTargetType());

        updateFileDTO.setSourceLevel(cs.getParentLevel() + cs.getSourceID() + ",");
        int isSafe = ioSourceDao.getSourceIsSafe(loginUser.getUserID(), updateFileDTO.getSourceLevel());
        List<String> sourceNameList = ioSourceDao.getSourceNameList(updateFileDTO.getSourceID());

        Set<Long> parentIDList = new HashSet<>();
        List<Long> sourceIDList = new ArrayList<>();
        List<String> sourceLevelList = new ArrayList<>();

        // 校验用户、群组 容量
        /** 获取企业云盘 */

        HomeExplorerVO disk = systemSortTool.getUserSpaceSize(loginUser.getUserID(), updateFileDTO.getSourceID());

        String desktopSourceIDStr = ioSourceMetaDao.getSourceIDMetaByKey(loginUser.getUserID() + "", "myDesktop", null);
        long desktopSourceId = 0;
        if (!ObjectUtils.isEmpty(desktopSourceIDStr)) {
            desktopSourceId = Long.valueOf(desktopSourceIDStr);
        }

        for (SourceOpDto dto : updateFileDTO.getDataArr()) {
            if (ObjectUtils.isEmpty(dto.getSourceID())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            if (desktopSourceId == dto.getSourceID().longValue() ){
                throw new SvnlanRuntimeException(CodeMessageEnum.pathNotSupport.getCode());
            }
            sourceIDList.add(dto.getSourceID());
            parentIDList.add(dto.getParentID());
            if ("folder".equals(dto.getType())) {
                sourceLevelList.add(dto.getParentLevel() + dto.getSourceID() + ",");
            }
        }
        String oldParentLevel = updateFileDTO.getDataArr().get(0).getParentLevel();
        // Long oldParentId = updateFileDTO.getDataArr().get(0).getParentID();

        List<IOSource> moveList = ioSourceDao.copySourceList(sourceIDList);
        if (CollectionUtils.isEmpty(moveList)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.rptSelectTips.getCode());
        }
        int fileTargetType = moveList.get(0).getTargetType();
        List<IOSource> moveListByLevel = null;
        // 权限校验
        if (fileTargetType == 2){
            int a = 0;
            for (IOSource source : moveList) {
                userAuthTool.checkGroupDocAuth(loginUser, source.getId(), source.getParentLevel(), "10", fileTargetType, a == 0);
                a++;
            }
            if (!CollectionUtils.isEmpty(sourceLevelList)) {
                moveListByLevel = ioSourceDao.copySourceListByLevel(sourceLevelList);
                if (!CollectionUtils.isEmpty(moveListByLevel)){
                    List<Long> otherSourceIds = moveListByLevel.stream().map(IOSource::getId).collect(Collectors.toList());
                    userAuthTool.checkGroupDocAuthOther(loginUser, otherSourceIds, "10", fileTargetType);
                }
            }
        }
        List<IOSource> parentList = ioSourceDao.copySourceList(new ArrayList<>(parentIDList));

        long size = 0;
        for (IOSource source : moveList) {
            size = size + source.getSize();
            source.setParentLevel(updateFileDTO.getSourceLevel());
            source.setParentId(updateFileDTO.getSourceID());
            source.setTargetType(cs.getTargetType());
            source.setIsSafe(isSafe);
            if (source.getIsFolder().intValue() == 1) {
                source.setName(this.checkRepeatName(source.getName(), source.getName(), sourceNameList, 1));
            } else {
                source.setName(this.checkRepeatName(source.getName(), source.getName(), source.getFileType(), sourceNameList, 1));
            }
        }
        if (!CollectionUtils.isEmpty(sourceLevelList)) {
            if (CollectionUtils.isEmpty(moveListByLevel)) {
                moveListByLevel = ioSourceDao.copySourceListByLevel(sourceLevelList);
            }
            if (!CollectionUtils.isEmpty(moveListByLevel)) {
                for (IOSource mSource : moveListByLevel) {
                    mSource.setIsSafe(isSafe);
                    mSource.setTargetType(cs.getTargetType());
                    mSource.setParentLevel(mSource.getParentLevel().replace(oldParentLevel, updateFileDTO.getSourceLevel()));
                }
                moveList.addAll(moveListByLevel);
            }
        }
        for (IOSource mSource : moveList) {
            if (mSource.getParentId() <= 0) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
        }
        this.checkMemory(disk, size);
        try {
            ioSourceDao.batchUpdateParentAndName(moveList);
        } catch (Exception e) {
            LogUtil.error(e, "复制出错");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        // 更新容量
        this.updateMemoryMove(size, updateFileDTO.getDataArr().get(0).getParentLevel(), updateFileDTO.getSourceLevel());
        /*
        {"sourceID":"165","sourceParent":"183","pathName":"8888.txt","pathDisplay":"","userID":"1","type":"move",
        "desc":{"from":"140","fromName":"885885","fromPath":"","to":183,"toName":"88888888888","toPath":""},"sourceTarget":"165","ip":"101.69.252.186"}

         {"sourceID":"140","sourceParent":"5","pathName":"885885","pathDisplay":"","userID":"1","type":"moveOut",
         "desc":{"sourceID":"165","name":"8888.txt"},"sourceTarget":"140","ip":"101.69.252.186"}
         */

        /** 操作日志 */
        Map<Long, IOSource> sourceMap = parentList.stream().collect(Collectors.toMap(IOSource::getId, Function.identity(), (v1, v2) -> v2));
        List<Map<String, Object>> paramList = new ArrayList<>();
        List<Map<String, Object>> outParamList = new ArrayList<>();

        for (SourceOpDto dto : updateFileDTO.getDataArr()) {
            if (!ObjectUtils.isEmpty(sourceMap) && sourceMap.containsKey(dto.getParentID())) {
                Map<String, Object> reMap = new HashMap<>(5);
                reMap.put("sourceID", dto.getSourceID());
                reMap.put("pathName", dto.getName());
                reMap.put("sourceParent", cs.getSourceID());
                reMap.put("sourceParentName", cs.getSourceName());
                reMap.put("type", "move");
                reMap.put("fromSourceID", dto.getParentID());
                reMap.put("fromName", sourceMap.get(dto.getParentID()).getName());
                paramList.add(reMap);

                Map<String, Object> reOutMap = new HashMap<>(5);
                reOutMap.put("sourceID", dto.getParentID());
                reOutMap.put("sourceParent", sourceMap.get(dto.getParentID()).getParentId());
                reOutMap.put("pathName", sourceMap.get(dto.getParentID()).getName());
                reOutMap.put("type", "moveOut");
                reOutMap.put("fromSourceID", dto.getSourceID());
                reOutMap.put("fromName", dto.getName());
                outParamList.add(reOutMap);

            }
        }
        /** 操作日志 */
        systemLogTool.setSysLog(loginUser, LogTypeEnum.move.getCode(), paramList, systemLogTool.getRequest());
        systemLogTool.setSysLog(loginUser, LogTypeEnum.moveOut.getCode(), outParamList, systemLogTool.getRequest());
        return true;
    }


    /**
     * @Description: 重命名文件
     * @params: [updateFileDTO, paramMap]
     * @Return: boolean
     * @Modified:
     */
    public boolean renameFile(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (CollectionUtils.isEmpty(updateFileDTO.getDataArr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        List<IoSourceEvent> eventList = new ArrayList<>();
        List<Long> sourceIDList = new ArrayList<>();

        String desktopSourceIDStr = ioSourceMetaDao.getSourceIDMetaByKey(loginUser.getUserID() + "", "myDesktop", null);
        long desktopSourceId = 0;
        if (!ObjectUtils.isEmpty(desktopSourceIDStr)) {
            desktopSourceId = Long.valueOf(desktopSourceIDStr);
        }
        for (SourceOpDto data : updateFileDTO.getDataArr()) {
            if (ObjectUtils.isEmpty(data.getSourceID())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            if (desktopSourceId == data.getSourceID().longValue()){
                throw new SvnlanRuntimeException(CodeMessageEnum.pathNotSupport.getCode());
            }
            if (StringUtil.isEmpty(data.getName())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            /*if (data.getName().startsWith(".")) {
                // 文件名不符合规范, 请修改
                throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
            }*/
            if (data.getName().length() > 200) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            sourceIDList.add(data.getSourceID());
            eventList.add(new IoSourceEvent(data.getSourceID(), data.getParentID(), loginUser.getUserID(), EventEnum.rename.getCode(), getSourceEventDesc(EventEnum.rename, data.getName(), data.getOldName())));
        }

        List<IOSource> copyList = ioSourceDao.copySourceList(sourceIDList);
        if (CollectionUtils.isEmpty(copyList) || copyList.size() != sourceIDList.size()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.rptSelectTips.getCode());
        }

        // 校验权限
        for (IOSource ioSource : copyList){
            userAuthTool.checkGroupDocAuth(loginUser, ioSource.getId(), ioSource.getParentLevel(), "8", ioSource.getTargetType());
        }
        List<String> sourceNameList = ioSourceDao.getSourceNameList(copyList.get(0).getParentId());
        Map<Long, String> sourceNameMap = copyList.stream().collect(Collectors.toMap(IOSource::getId, IOSource::getName, (v1, v2) -> v2));
        List<SourceOpDto> list = updateFileDTO.getDataArr();

        if (!CollectionUtils.isEmpty(sourceNameList)) {
            sourceNameList = sourceNameList.stream().map(String::toLowerCase).collect(Collectors.toList());
            for (SourceOpDto data : list) {
                if (sourceNameList.contains(data.getName().toLowerCase())
                        && !data.getName().toLowerCase().equals(sourceNameMap.get(data.getSourceID()).toLowerCase())) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.pathExists.getCode());
                }
                data.setNamePinyin(ChinesUtil.getPingYin(data.getName()));
                data.setNamePinyinSimple(ChinesUtil.getFirstSpell(data.getName()));
            }
        }

        try {
            ioSourceDao.batchFileRename(list, loginUser.getUserID());
        } catch (Exception e) {
            LogUtil.error(e, " batchFileRename error operationsDTO=" + JsonUtils.beanToJson(updateFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        // 拼音
        this.setSourcePinYinList(updateFileDTO.getDataArr(), true);

        /** 添加文档操作event */
        if (!CollectionUtils.isEmpty(eventList)) {
            this.addSourceEventList(eventList);
        }
        /** 操作日志 */
        Map<String, Object> reMap = null;
        Map<Long, IOSource> sourceMap = copyList.stream().collect(Collectors.toMap(IOSource::getId, Function.identity(), (v1, v2) -> v2));
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (SourceOpDto dto : updateFileDTO.getDataArr()) {
            if (!ObjectUtils.isEmpty(sourceMap) && sourceMap.containsKey(dto.getSourceID())) {
                reMap = new HashMap<>(5);
                reMap.put("sourceID", dto.getSourceID());
                reMap.put("sourceParent", sourceMap.get(dto.getSourceID()).getParentId());
                reMap.put("pathName", dto.getName());
                reMap.put("type", "rename");
                reMap.put("fromName", sourceMap.get(dto.getSourceID()).getName());
                paramList.add(reMap);
            }
        }

        /** 操作日志 */
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileRename.getCode(), paramList, systemLogTool.getRequest());
        return true;
    }

    public void setSourcePinYinList(List<SourceOpDto> list, boolean isDel) {
        // 拼音
        try {
            List<Long> sourceIDList = new ArrayList<>();

            List<IOSourceMeta> paramList = new ArrayList<>();
            for (SourceOpDto dto : list) {
                sourceIDList.add(dto.getSourceID());

                paramList.add(new IOSourceMeta(dto.getSourceID(), MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(dto.getName())));
                paramList.add(new IOSourceMeta(dto.getSourceID(), MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(dto.getName())));
            }
            if (isDel) {
                ioSourceMetaDao.delMetaBySourceIDList(sourceIDList, MetaEnum.delKeyList());
            }

            ioSourceMetaDaoImpl.batchInsert(paramList);
        } catch (Exception e) {
            LogUtil.error(e, " setSourcePinYin meta error list=" + JsonUtils.beanToJson(list));
        }
    }

    /**
     * 容量校验
     */
    public void checkMemory(HomeExplorerVO disk, long size) {
        checkMemory(disk, size, false, null);
    }
    public void checkMemory(HomeExplorerVO disk, long size, boolean check, String taskId) {
        //未分段，或第一个分段时验证
        if (!ObjectUtils.isEmpty(size) && size > 0) {
            if (!ObjectUtils.isEmpty(disk) && !ObjectUtils.isEmpty(size) && size > 0 && !ObjectUtils.isEmpty(disk.getTargetType())) {
                // 文件大小校验
                if (!ObjectUtils.isEmpty(disk.getIgnoreFileSize()) && disk.getIgnoreFileSize().doubleValue() > 0) {
                    // 0 为不限制容量
                    if (size >= (disk.getIgnoreFileSize() * 1024 * 1024 * 1024)) {
                        LogUtil.info("文件超过上传大小  ignoreFileSize=" + disk.getIgnoreFileSize());
                        throw new SvnlanRuntimeException(CodeMessageEnum.ignoreFileSizeTips.getCode());
                    }
                }
                Long userSizeUse = null;
                String key = GlobalConfig.checkMemory_key ;
                boolean isPut = false;
                long usedSize = disk.getTargetType().intValue() == 1 ? disk.getUserSizeUse() : disk.getSizeUse();
                HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
                if (check && !ObjectUtils.isEmpty(taskId)){
                    String lockRedisKey = String.format(GlobalConfig.upload_local_captcha_checkMemory,taskId);

                    // 判断是否是同一个文件校验(分片上传有问题做改动)
                    Boolean onlyFile = stringRedisTemplate.opsForValue().setIfAbsent(lockRedisKey, "1", 2, TimeUnit.HOURS);
                    if (onlyFile){
                        if (disk.getTargetType().intValue() == 1) {
                            if (disk.getUserSizeMax().doubleValue() > 0){

                                isPut = true;
                                String sizeString = operations.get(key, "user_"+ disk.getUserID());
                                LogUtil.info("checkMemoryRedis   个人空间 user_"+disk.getUserID()+"  sourceId=" + disk.getSourceID() + "，sizeString=" +sizeString);
                                if (!ObjectUtils.isEmpty(sizeString) && StringUtil.isNumeric(sizeString)){
                                    usedSize = Long.valueOf(sizeString);
                                    usedSize = disk.getUserSizeUse() > usedSize ? disk.getUserSizeUse() : usedSize;
                                }
                            }
                        }else {
                            if (disk.getSizeMax().doubleValue() > 0 ) {
                                isPut = true;
                                String sizeString = operations.get(key, String.valueOf(disk.getSourceID()));
                                LogUtil.info("checkMemoryRedis   企业网盘  sourceId=" + disk.getSourceID() + "，sizeString=" +sizeString);
                                if (!ObjectUtils.isEmpty(sizeString) && StringUtil.isNumeric(sizeString)){
                                    usedSize = Long.valueOf(sizeString);
                                    usedSize = disk.getSizeUse() > usedSize ? disk.getSizeUse() : usedSize;
                                }
                            }
                        }

                    }
                }
                // 个人空间
                if (disk.getTargetType().intValue() == 1) {
                    userSizeUse = usedSize + size;
                    // 0 为不限制容量
                    if (disk.getUserSizeMax().doubleValue() > 0 && userSizeUse >= (disk.getUserSizeMax() * 1024 * 1024 * 1024)) {
                        throw new SvnlanRuntimeException(CodeMessageEnum.spaceIsFull.getCode());
                    }
                } else {
                    // 部门
                    userSizeUse = usedSize + size;
                    // 0 为不限制容量
                    if (disk.getSizeMax().doubleValue() > 0 && userSizeUse >= (disk.getSizeMax() * 1024 * 1024 * 1024)) {
                        throw new SvnlanRuntimeException(CodeMessageEnum.spaceIsFull.getCode());
                    }
                }

                if (isPut && !ObjectUtils.isEmpty(userSizeUse) && userSizeUse > 0) {
                    operations.put(key, ((disk.getTargetType().intValue() == 1) ? "user_"+ disk.getUserID() : String.valueOf(disk.getSourceID())) , String.valueOf(userSizeUse));
                    long t = DateUtil.getTimeDiff();
                    stringRedisTemplate.expire(key, t, TimeUnit.MINUTES);
                }
            }
        }
    }

    public void updateMemory(CommonSource commonSource) {
        commonSource.setGroupID(ObjectUtils.isEmpty(commonSource.getGroupID()) ? 0L : commonSource.getGroupID());
        asyncUtil.updateMemory(commonSource.getSize(), commonSource.getGroupID(), commonSource.getUserID(), commonSource.getTargetType(), commonSource.getParentLevel());
    }

    public void updateMemory(long size, long groupID, long userID, Integer targetType, String parentLevel) {
        asyncUtil.updateMemory(size, groupID, userID, targetType, parentLevel);
    }

    public void updateGroupMemoryCopyBySearch(Map<String, Object> paramMap, String parentLevel) {
        asyncUtil.updateGroupMemoryCopyBySearch(paramMap, parentLevel);
    }
    public void updateGroupMemoryCopy(Map<String, Object> paramMap, Long groupID) {
        asyncUtil.updateGroupMemoryCopy(paramMap, groupID);
    }

    public void updateMemoryCopy(long size, long groupID, long userID, Integer targetType, List<IOSource> sources) {
        if (size > 0 && !ObjectUtils.isEmpty(targetType)) {
            Map<String, Object> paramMap = new HashMap<>(2);
            paramMap.put("groupID", groupID);
            paramMap.put("memory", size);
            paramMap.put("userID", userID);
            try {
                if (targetType.intValue() == 1) {
                    homeExplorerDao.updateUserMemory(paramMap);
                } else {
                    this.updateGroupMemoryCopy(paramMap, groupID);
                }
            } catch (Exception e) {
                LogUtil.error(e, "更新 企业云盘 sources memory失败");
            }
            // source updateSourceMemoryList
            if (!CollectionUtils.isEmpty(sources)) {
                try {
                    List<IOSource> sourceList = new ArrayList<>();
                    Map<Long, Long> sourceSizeMap = new HashMap<>();
                    for (IOSource source : sources) {
                        List<Long> sourceIds = Arrays.asList(source.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                        if (!CollectionUtils.isEmpty(sourceIds)) {
                            for (Long id : sourceIds) {
                                long s = (!ObjectUtils.isEmpty(sourceSizeMap) && sourceSizeMap.containsKey(id)) ? sourceSizeMap.get(id) + source.getSize() : source.getSize();
                                sourceSizeMap.put(id, s);
                            }
                        }
                    }
                    if(!ObjectUtils.isEmpty(sourceSizeMap)){
                        IOSource vo = null;
                        for (Map.Entry<Long, Long> entry : sourceSizeMap.entrySet()) {
                            if (!ObjectUtils.isEmpty(entry.getKey())){
                                vo = new IOSource(entry.getKey(), entry.getValue());
                                sourceList.add(vo);
                            }
                        }
                    }
                    if (!CollectionUtils.isEmpty(sourceList)) {
                        LogUtil.info("updateMemoryCopy batchUpdateSourceMemoryList sourceSizeMap=" + JsonUtils.beanToJson(sourceSizeMap));
                        ioSourceDao.batchUpdateSourceMemoryList(sourceList);
                    }
                } catch (Exception e) {
                    LogUtil.error(e, " updateMemory error ");
                }
            }
        }
    }

    public void updateMemoryMove(long size, String fromParentLevel, String toParentLevel) {
        if (size > 0) {
            // source updateSourceMemoryList
            if (!ObjectUtils.isEmpty(fromParentLevel) && !ObjectUtils.isEmpty(toParentLevel)) {
                try {
                    List<Long> fromSourceIds = Arrays.asList(fromParentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    List<Long> toSourceIds = Arrays.asList(toParentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(toSourceIds)) {
                        ioSourceDao.updateSourceMemoryList(toSourceIds, size);
                    }
                    if (!CollectionUtils.isEmpty(fromSourceIds)) {
                        ioSourceDao.subtractSourceMemoryList(fromSourceIds, size);
                    }
                } catch (Exception e) {
                    LogUtil.error(e, " updateMemoryMove error ");
                }
            }
        }
    }

    /**
     * @Description: 复制文件
     * @params: [updateFileDTO, paramMap]
     * @Return: boolean
     * @Modified:
     */
    public List<IOSource> copyFile(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (updateFileDTO.getSourceID() == null) {
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        if (CollectionUtils.isEmpty(updateFileDTO.getDataArr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }

        CommonSource cs = ioSourceDao.getSourceInfo(updateFileDTO.getSourceID());
        if (ObjectUtils.isEmpty(cs)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, cs.getSourceID(), cs.getParentLevel(), "8", cs.getTargetType());
        List<String> sourceNameList = ioSourceDao.getSourceNameList(updateFileDTO.getSourceID());

        updateFileDTO.setSourceLevel(cs.getParentLevel() + cs.getSourceID() + ",");

        List<Long> sourceIDList = new ArrayList<>();
        Map<Long, String> sourceName = new HashMap<>();
        List<String> sourceLevelList = new ArrayList<>();
        for (SourceOpDto dto : updateFileDTO.getDataArr()) {
            sourceIDList.add(dto.getSourceID());
            sourceName.put(dto.getSourceID(), dto.getName());
            if ("folder".equals(dto.getType())) {
                sourceLevelList.add(dto.getParentLevel() + dto.getSourceID() + ",");
            }
        }
        List<IOSource> copyList = ioSourceDao.copySourceList(sourceIDList);
        if (CollectionUtils.isEmpty(copyList)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.rptSelectTips.getCode());
        }
        List<IOSource> copyListByLevelCo = CollectionUtils.isEmpty(sourceLevelList) ? null : ioSourceDao.copySourceListByLevel(sourceLevelList);
        int fileTargetType = copyList.get(0).getTargetType();
        // 复制权限校验
        if (fileTargetType == 2) {
            int a = 0;
            for (IOSource source : copyList) {
                // 权限校验
                userAuthTool.checkGroupDocAuth(loginUser, source.getId(), source.getParentLevel(), "4,10", source.getTargetType() , a == 0);
                a ++;
            }
            if (!CollectionUtils.isEmpty(sourceLevelList)) {
                List<Long> otherSourceIds = copyListByLevelCo.stream().map(IOSource::getId).collect(Collectors.toList());
                userAuthTool.checkGroupDocAuthOther(loginUser, otherSourceIds, "4,10", fileTargetType);
            }
        }
        // 复制权限校验
        List<IoSourceEvent> eventList = new ArrayList<>();
        Long size = 0L;
        for (IOSource source : copyList) {
            source.setOldParentLevel(source.getParentLevel());
            source.setOldSourceLevel(source.getParentLevel() + source.getId() + ",");
            source.setOldSourceId(source.getId());
            source.setOldParentId(source.getParentId());


            source.setParentLevel(updateFileDTO.getSourceLevel());
            source.setParentId(updateFileDTO.getSourceID());
            source.setTargetId(loginUser.getUserID());
            source.setCreateUser(loginUser.getUserID());
            source.setModifyUser(loginUser.getUserID());
            source.setTargetType(cs.getTargetType());
            if (!ObjectUtils.isEmpty(sourceName) && sourceName.containsKey(source.getId())) {
                source.setName(sourceName.get(source.getId()));
                if (1 == source.getIsFolder()) {
                    source.setName(this.checkRepeatName(source.getName(), source.getName(), sourceNameList, 1));
                } else {
                    source.setName(this.checkRepeatName(source.getName(), source.getName(), source.getFileType(), sourceNameList, 1));
                }
            }

            source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
            source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
            source.setId(null);
            size = size + source.getSize();
        }

        // 校验用户、群组 容量
        /** 获取企业云盘 */
        HomeExplorerVO disk = systemSortTool.getUserSpaceSize(loginUser.getUserID(), updateFileDTO.getSourceID());
        this.checkMemory(disk, size);

        try {
            ioSourceDao.batchInsert(copyList);
        } catch (Exception e) {
            LogUtil.error(e, "复制出错");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        Map<String, IOSource> parentMap = copyList.stream().collect(Collectors.toMap(IOSource::getOldSourceLevel, Function.identity(), (v1, v2) -> v2));
        for (IOSource source : copyList) {
            eventList.add(new IoSourceEvent(source.getId(), source.getParentId(), loginUser.getUserID(), EventEnum.copy.getCode(), getSourceEventDesc(EventEnum.copy, source.getName(), "")));
        }
        if (!CollectionUtils.isEmpty(sourceLevelList)) {
            if (!CollectionUtils.isEmpty(copyListByLevelCo)){
                size = size + copyListByLevelCo.stream().map(IOSource::getSize).collect(Collectors.toList()).stream().reduce(Long::sum).get();
            }
            int s = 0;
            // 复制选择文件夹下的所有文件
            copyFileSaveUtil.saveParentLevelSource(updateFileDTO, parentMap, ioSourceDao, loginUser, cs.getTargetType(), copyListByLevelCo,s);
        }

        /** 添加文档操作event */
        if (!CollectionUtils.isEmpty(eventList)) {
            this.addSourceEventList(eventList);
        }

        // 更新容量
        updateMemoryCopy(size, disk.getGroupID(), loginUser.getUserID(), disk.getTargetType(), copyList);


        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (IOSource dto : copyList) {
            reMap = new HashMap<>(5);
            reMap.put("sourceID", dto.getId());
            reMap.put("sourceParent", dto.getParentId());
            reMap.put("pathName", dto.getName());
            reMap.put("type", "copy");
            reMap.put("sourceParent", cs.getSourceID());
            reMap.put("sourceParentName", cs.getSourceName());
            paramList.add(reMap);
        }
        systemLogTool.setSysLog(loginUser, LogTypeEnum.copy.getCode(), paramList, systemLogTool.getRequest());
        return copyList;
    }


    /**
     * 添加到收藏夹
     */
    public void favFile(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (!ObjectUtils.isEmpty(updateFileDTO.getSourceID()) && updateFileDTO.getSourceID() <= 0) {
            updateFileDTO.setSourceID(null);
        }
        if (ObjectUtils.isEmpty(updateFileDTO.getSourceID()) && ObjectUtils.isEmpty(updateFileDTO.getPath())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(updateFileDTO.getName())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long sourceID = !ObjectUtils.isEmpty(updateFileDTO.getPath()) ? Long.parseLong(updateFileDTO.getPath()) : updateFileDTO.getSourceID();
        CommonSource commonSource = this.getSourceInfo(sourceID);
        List<String> nameList = userFavDao.getFavNameList(loginUser.getUserID());
        UserFav userFav = new UserFav();
        userFav.setUserId(loginUser.getUserID());
        userFav.setTagId(0);
        if (1 == commonSource.getIsFolder()) {
            userFav.setName(this.checkRepeatName(updateFileDTO.getName(), updateFileDTO.getName(), nameList, 1));
        }else {
            String suffix = FileUtil.getFileExtension(updateFileDTO.getName());
            userFav.setName(this.checkRepeatName(updateFileDTO.getName(), updateFileDTO.getName(), suffix, nameList, 1));
        }
        if (!ObjectUtils.isEmpty(updateFileDTO.getSourceID()) && updateFileDTO.getSourceID() > 0) {
            userFav.setPath(updateFileDTO.getSourceID().toString());
            userFav.setType("source");
        } else {
            userFav.setPath(updateFileDTO.getPath());
            if (ObjectUtils.isEmpty(updateFileDTO.getType())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            userFav.setType(updateFileDTO.getType());
        }
        Integer sort = userFavDao.getFavMaxSort(loginUser.getUserID());
        userFav.setSort(ObjectUtils.isEmpty(sort) ? 1 : sort + 1);
        try {
            userFavDao.insert(userFav);
        } catch (Exception e) {
            LogUtil.error(e, "收藏出错");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", updateFileDTO.getSourceID());
        reMap.put("path", userFav.getPath());
        reMap.put("pathName", updateFileDTO.getName());
        reMap.put("type", "favAdd");
        if (!ObjectUtils.isEmpty(updateFileDTO.getType())) {
            reMap.put("sourceType", updateFileDTO.getType());
        }
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, LogTypeEnum.favAdd.getCode(), paramList, systemLogTool.getRequest());
    }

    /**
     * 取消收藏
     */
    public void delFavFile(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (CollectionUtils.isEmpty(updateFileDTO.getDataArr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        List<Long> sourceIdList = new ArrayList<>();
        List<Long> favIdList = new ArrayList<>();
        for (SourceOpDto data : updateFileDTO.getDataArr()) {
            if (!ObjectUtils.isEmpty(data.getId()) && data.getId() > 0) {
                sourceIdList.add(data.getId());
            }
            if (!ObjectUtils.isEmpty(data.getFavID()) && data.getFavID() > 0) {
                favIdList.add(data.getFavID());
            }
        }
        try {
            if (!CollectionUtils.isEmpty(sourceIdList)) {
                userFavDao.removeUserFav(loginUser.getUserID(), sourceIdList);
            }
            if (!CollectionUtils.isEmpty(favIdList)) {
                userFavDao.removeUserFavByIdList(loginUser.getUserID(), favIdList);
            }
        } catch (Exception e) {
            LogUtil.error(e, "取消收藏出错");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (SourceOpDto data : updateFileDTO.getDataArr()) {
            reMap = new HashMap<>(5);
            reMap.put("name", data.getName());
            reMap.put("pathName", data.getName());
            paramList.add(reMap);
        }
        systemLogTool.setSysLog(loginUser, LogTypeEnum.favDel.getCode(), paramList, systemLogTool.getRequest());
    }

    /**
     * 添加描述
     */
    public void saveDesc(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(updateFileDTO.getSourceID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String desc = ObjectUtils.isEmpty(updateFileDTO.getDesc()) ? "" : updateFileDTO.getDesc();


        CommonSource cs = ioSourceDao.getSourceInfo(updateFileDTO.getSourceID());
        if (ObjectUtils.isEmpty(cs)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, cs.getSourceID(), cs.getParentLevel(), "8", cs.getTargetType());
        // 修改
        try {
            ioSourceDao.updateSourceDesc(updateFileDTO.getSourceID(), desc);
        } catch (Exception e) {
            LogUtil.error(e, " saveDesc updateSourceDesc error updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
        }
        return;
    }

    public void shareFile(CheckFileDTO updateFileDTO) {

    }

    public void checkSafeOperate(Object updateFileDTO, LoginUser loginUser, Long sourceId){
        if (!ObjectUtils.isEmpty(sourceId)){
            HomeExplorerVO homeExplorerVO = ioSourceDao.getMySafeBoxSource(loginUser.getUserID());
            if (!ObjectUtils.isEmpty(homeExplorerVO)){
                if (homeExplorerVO.getSourceID().longValue() == sourceId.longValue()){
                    LogUtil.error("私密保险箱不支持此操作：updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
                    throw new SvnlanRuntimeException(CodeMessageEnum.pathNotSupport.getCode());
                }
            }
        }
    }

    /**
     * @Description: 还原文件
     * @params: [updateFileDTO, paramMap, resultMap]
     * @Return: void
     * @Modified:
     */
    public void recycleFile(CheckFileDTO updateFileDTO, LoginUser loginUser, boolean isAll) {
        if (!isAll && CollectionUtils.isEmpty(updateFileDTO.getDataArr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        List<SourceOpDto> dataArr = null;
        if (isAll) {
            dataArr = this.getUserRecycleBinList(loginUser);
        } else {
            dataArr = updateFileDTO.getDataArr();
        }
        if (CollectionUtils.isEmpty(dataArr)) {
            return;
        }
        List<IoSourceEvent> eventList = new ArrayList<>();

        List<Long> sourceIdList = new ArrayList<>();
        List<String> paList = new ArrayList<>();
        for (SourceOpDto data : dataArr) {
            sourceIdList.add(data.getSourceID());
            if ("folder".equals(data.getType())) {
                paList.add(data.getParentLevel() + data.getSourceID() + ",");
            }
            eventList.add(new IoSourceEvent(data.getSourceID(), data.getParentID(), loginUser.getUserID(), EventEnum.restore.getCode(), getSourceEventDesc(EventEnum.restore, data.getName(), null)));
        }
        List<IOSource> deleteList = ioSourceDao.copySourceList(sourceIdList);
        Map<Long, List<String>> nameMap = new HashMap<>();
        List<String> sourceNameList = null;
        List<String> sourceNameListAdd = null;
        for (IOSource a : deleteList){
            sourceNameList = ioSourceDao.getSourceNameList(a.getParentId());
            if(!ObjectUtils.isEmpty(nameMap) && nameMap.containsKey(a.getParentId())){
                if (CollectionUtils.isEmpty(sourceNameList)){
                    sourceNameList = new ArrayList<>();
                }
                sourceNameList.addAll(nameMap.get(a.getParentId()));
            }
            a.setName(this.checkRepeatName(a.getName(), a.getName(), sourceNameList, 1));

            if(!ObjectUtils.isEmpty(nameMap) && nameMap.containsKey(a.getParentId())){
                sourceNameListAdd = nameMap.get(a.getParentId());
            }else {
                sourceNameListAdd = new ArrayList<>();
            }
            sourceNameListAdd.add(a.getName());
            nameMap.put(a.getParentId(), sourceNameListAdd);
        }
        try {
            ioSourceDao.restoreDirOrFileAndName(deleteList, loginUser.getUserID());
        } catch (Exception e) {
            LogUtil.error(e, " restoreDirOrFile error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        // 还原文件夹下的文件夹及文件
        if (!CollectionUtils.isEmpty(paList)) {
            try {
                ioSourceDao.restoreSourceByParent(paList, loginUser.getUserID());
            } catch (Exception e) {
                LogUtil.error(e, " restoreDirOrFile restoreSourceByParent error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
            }
        }
        /** 还原文件 删除回收站文档表相关信息 targetType 文档所属类型 (0-sys,1-user,2-group) */
        try {
            ioSourceRecycleDao.deleteUserRecycle(loginUser.getUserID(), 1, sourceIdList);
        } catch (Exception e) {
            LogUtil.error(e, " deleteDirOrFile restore error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
        }

        /** 添加文档操作event */
        if (!CollectionUtils.isEmpty(eventList)) {
            this.addSourceEventList(eventList);
        }
        /** 操作日志 */
        Map<String, Object> reMap = null;
        Map<Long, IOSource> sourceMap = deleteList.stream().collect(Collectors.toMap(IOSource::getId, Function.identity(), (v1, v2) -> v2));
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (SourceOpDto dto : dataArr) {
            if (!ObjectUtils.isEmpty(sourceMap) && sourceMap.containsKey(dto.getSourceID())) {
                reMap = new HashMap<>(4);
                reMap.put("sourceID", dto.getSourceID());
                reMap.put("sourceParent", sourceMap.get(dto.getSourceID()).getParentId());
                reMap.put("type", "restore");
                reMap.put("pathName", sourceMap.get(dto.getSourceID()).getName());
                paramList.add(reMap);
            }
        }
        systemLogTool.setSysLog(loginUser, LogTypeEnum.restore.getCode(), paramList, systemLogTool.getRequest());

    }

    private List<SourceOpDto> getUserRecycleBinList(LoginUser loginUser) {
        return ioSourceRecycleDao.getUserRecycleBinList(loginUser.getUserID());
    }

    /**
     * @Description: 彻底删除
     * @params: [updateFileDTO]
     * @Return: void
     * @Author: sulijuan
     * @Date: 2023/2/18 15:52
     * @Modified:
     */
    public void deleteBin(CheckFileDTO updateFileDTO, LoginUser loginUser, boolean isAll) {
        if (!isAll && CollectionUtils.isEmpty(updateFileDTO.getDataArr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        List<SourceOpDto> dataArr = null;
        if (isAll) {
            dataArr = this.getUserRecycleBinList(loginUser);
        } else {
            dataArr = updateFileDTO.getDataArr();
        }
        if (CollectionUtils.isEmpty(dataArr)) {
            return;
        }

        List<IoSourceEvent> eventList = new ArrayList<>();

        List<Long> sourceIdList = new ArrayList<>();
        List<String> paList = new ArrayList<>();
        for (SourceOpDto data : dataArr) {
            sourceIdList.add(data.getSourceID());
            if ("folder".equals(data.getType())) {
                paList.add(data.getParentLevel() + data.getSourceID() + ",");
            }
            eventList.add(new IoSourceEvent(data.getSourceID(), data.getParentID(), loginUser.getUserID(), EventEnum.remove.getCode()
                    , getSourceEventDesc(EventEnum.remove, data.getName(), null)));
        }


        List<CommonSource> copyList = ioSourceDao.getSourceFileInfoList(sourceIdList);
        if (CollectionUtils.isEmpty(copyList)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.rptSelectTips.getCode());
        }
        Map<Long, String> filePathMap = new HashMap<>();
        Map<Long, String> fileMd5Map = new HashMap<>();
        for (CommonSource n : copyList){
            if (!ObjectUtils.isEmpty(n.getPath())){
                filePathMap.put(n.getFileID(), n.getPath());
            }
            if (!ObjectUtils.isEmpty(n.getHashMd5())){
                fileMd5Map.put(n.getFileID(), n.getHashMd5());
            }
        }

        List<Long> deleteFileIDList = ioSourceDao.getFileIDBySourceID(sourceIdList);

        List<Long> delFileIDs = null;
        List<String> deletePath = null;
        // 排除fileID被引用被秒传
        if (!CollectionUtils.isEmpty(deleteFileIDList)) {
            List<IOSourceVo> fileCountList = ioSourceDao.getFileCountBySourceID(deleteFileIDList);
            if (!CollectionUtils.isEmpty(fileCountList)) {
                deletePath = new ArrayList<>();
                delFileIDs = new ArrayList<>();
                for (IOSourceVo vo : fileCountList) {
                    if (vo.getFileCount() == 1) {
                        delFileIDs.add(vo.getFileId());
                        if (filePathMap.containsKey(vo.getFileId())) {
                            deletePath.add(filePathMap.get(vo.getFileId()));
                        }
                    }
                }
            }
        }

        /** 彻底删除*/
        try {
            ioSourceDao.removeUserSource(sourceIdList);
        } catch (Exception e) {
            LogUtil.error(e, " deleteBin removeUserSource error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        // 删除文件夹下的文件夹及文件
        if (!CollectionUtils.isEmpty(paList)) {
            try {
                ioSourceDao.removeUserSourceByParent(paList);
            } catch (Exception e) {
                LogUtil.error(e, " deleteBin removeUserSourceByParent error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
            }
        }


        List<IOSourceVo> dList = null;
        if (!CollectionUtils.isEmpty(deletePath)) {
            dList = ioSourceDao.getFileCountByPath(deletePath);
            LogUtil.info(" deleteBin getFileCountByPath error dList=" + JsonUtils.beanToJson(dList));
        }

        // 删除file表信息
        if (!CollectionUtils.isEmpty(delFileIDs) && delFileIDs.size() > 0) {
            try {
                ioFileDao.removeUserFile(delFileIDs);
                ioFileDao.removeUserFileMeta(delFileIDs);
                ioFileDao.removeUserFileContents(delFileIDs);
            } catch (Exception e) {
                LogUtil.error(e, " deleteBin removeUserFile error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
            }
            if (!ObjectUtils.isEmpty(fileMd5Map) ) {
                try {
                    for (Long fid : delFileIDs) {
                        if (fileMd5Map.containsKey(fid)) {
                            String md5 = fileMd5Map.get(fid);
                            String uploadSourceIdKey = String.format(GlobalConfig.upload_md5_captcha_sourceId, loginUser.getTenantId(), md5);
                            String uploadKey = String.format(GlobalConfig.upload_md5_captcha, loginUser.getTenantId(), md5);
                            stringRedisTemplate.delete(uploadKey);
                            stringRedisTemplate.delete(uploadSourceIdKey);
                        }
                    }
                } catch (Exception e) {
                    LogUtil.error(e, " deleteBin removeUserFile error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
                }
            }
        }

        /** 删除回收站文档表相关信息 targetType 文档所属类型 (0-sys,1-user,2-group) */
        try {
            ioSourceRecycleDao.deleteUserRecycle(loginUser.getUserID(), 1, sourceIdList);
        } catch (Exception e) {
            LogUtil.error(e, " deleteBin deleteUserRecycle error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
        }

        // 更新容量 先排序后筛选出是否已经包含的文件 sourceIdList
        asyncUtil.asyncDeleteFileUpdateMemory(loginUser, copyList, sourceIdList, dList);


        /** 添加文档操作event */
        if (!CollectionUtils.isEmpty(eventList)) {
            this.addSourceEventList(eventList);
        }
        /** 操作日志 */
        Map<String, Object> reMap = null;
        CommonSource cs = null;
        Map<Long, CommonSource> sourceMap = copyList.stream().collect(Collectors.toMap(CommonSource::getSourceID, Function.identity(), (v1, v2) -> v2));
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (SourceOpDto dto : dataArr) {
            if (!ObjectUtils.isEmpty(sourceMap) && sourceMap.containsKey(dto.getSourceID())) {
                cs = sourceMap.get(dto.getSourceID());
                reMap = new HashMap<>(5);
                reMap.put("sourceID", dto.getSourceID());
                reMap.put("sourceParent", cs.getParentID());
                reMap.put("pathName", cs.getName());
                if (!ObjectUtils.isEmpty(cs.getParentLevel())) {
                    reMap.put("sourceParentLevel", cs.getParentLevel());
                }
                reMap.put("type", "remove");
                paramList.add(reMap);
            }
        }
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileRemove.getCode(), paramList, systemLogTool.getRequest());
    }


    /**
     * @Description: 删除文件
     * @params: [updateFileDTO, paramMap]
     * @Return: boolean
     * @Modified:
     */
    public Integer deleteFile(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (CollectionUtils.isEmpty(updateFileDTO.getDataArr())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        List<IoSourceEvent> eventList = new ArrayList<>();

        List<Long> sourceIdList = new ArrayList<>();
        List<String> paList = new ArrayList<>();

        String desktopSourceIDStr = ioSourceMetaDao.getSourceIDMetaByKey(loginUser.getUserID() + "", "myDesktop", null);
        long desktopSourceId = 0;
        if (!ObjectUtils.isEmpty(desktopSourceIDStr)) {
            desktopSourceId = Long.valueOf(desktopSourceIDStr);
        }
        for (SourceOpDto data : updateFileDTO.getDataArr()) {
            if (ObjectUtils.isEmpty(data.getSourceID())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            if (desktopSourceId == data.getSourceID().longValue()){
                throw new SvnlanRuntimeException(CodeMessageEnum.pathNotSupport.getCode());
            }
            sourceIdList.add(data.getSourceID());
            if ("folder".equals(data.getType())) {
                paList.add(data.getParentLevel() + data.getSourceID() + ",");
            }
            eventList.add(new IoSourceEvent(data.getSourceID(), data.getParentID(), loginUser.getUserID(), EventEnum.recycle.getCode(), getSourceEventDesc(EventEnum.recycle, data.getName(), null)));
        }
        List<IOSource> deleteList = ioSourceDao.copySourceList(sourceIdList);
        if (CollectionUtils.isEmpty(deleteList)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        for (IOSource ioSource : deleteList){
            // 权限校验
            userAuthTool.checkGroupDocAuth(loginUser, ioSource.getId(), ioSource.getParentLevel(), "10", ioSource.getTargetType());
        }
        Long tenantId = deleteList.get(0).getTenantId();
        // 判断是否是部门
        Long check = groupSourceDao.checkIsGroup(sourceIdList);
        if (!ObjectUtils.isEmpty(check) && check > 0) {
            throw new SvnlanRuntimeException(CodeMessageEnum.groupDelError.getCode());
        }

        for (IOSource io : deleteList) {
            if (io.getTargetType().intValue() == 1 && io.getParentId().longValue() == 0) {
                throw new SvnlanRuntimeException(CodeMessageEnum.groupDelError.getCode());
            } else if (io.getTargetType().intValue() == 2 && io.getParentId().longValue() == 0) {
                throw new SvnlanRuntimeException(CodeMessageEnum.groupDelError.getCode());
            }
        }
        int removedFileCount = 0;
        try {
            removedFileCount = ioSourceDao.deleteDirOrFile(sourceIdList);
        } catch (Exception e) {
            LogUtil.error(e, " deleteDirOrFile error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        // 删除文件夹下的文件夹及文件
        if (!CollectionUtils.isEmpty(paList)) {
            try {
                ioSourceDao.deleteSourceByParent(paList, tenantId);
            } catch (Exception e) {
                LogUtil.error(e, " deleteDirOrFile deleteSourceByParent error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
            }
        }
        /** 添加到回收站文档表*/

        for (IOSource source : deleteList) {
            source.setUserId(loginUser.getUserID());
        }
        try {
            ioSourceRecycleDao.batchInsert(deleteList);
        } catch (Exception e) {
            LogUtil.error(e, " deleteDirOrFile insert recycle error CheckFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
        }

        /** 添加文档操作event */
        if (!CollectionUtils.isEmpty(eventList)) {
            this.addSourceEventList(eventList);
        }
        /** 操作日志 */
        addOperateLog(deleteList, updateFileDTO, loginUser, LogTypeEnum.fileToRecycle);

        return removedFileCount;
    }

    /**
     * 添加操作日志
     *
     * @param ioSourceList  需要写入日志的数据
     * @param updateFileDTO 更新的数据
     * @param loginUser     当前登录用户
     * @param logTypeEnum   日志类型
     */
    private void addOperateLog(List<IOSource> ioSourceList, CheckFileDTO updateFileDTO, LoginUser loginUser, LogTypeEnum logTypeEnum) {

        Map<String, Object> reMap = null;
        Map<Long, IOSource> sourceMap = ioSourceList.stream().collect(Collectors.toMap(IOSource::getOldSourceId, Function.identity()));
        LogUtil.info(String.format("addOperateLog  删除日志 sourceMap=%s，dto=%s", JsonUtils.beanToJson(sourceMap), JsonUtils.beanToJson(updateFileDTO) ));
        List<Map<String, Object>> paramList = new ArrayList<>();
        IOSource cs = null;
        for (SourceOpDto dto : updateFileDTO.getDataArr()) {
            if (!ObjectUtils.isEmpty(sourceMap) && sourceMap.containsKey(dto.getSourceID())) {
                cs = sourceMap.get(dto.getSourceID());
                reMap = new HashMap<>(4);
                reMap.put("sourceID", dto.getSourceID());
                reMap.put("sourceParent", cs.getParentId());
                reMap.put("type", "recycle");
                if (!ObjectUtils.isEmpty(cs.getParentLevel())) {
                    reMap.put("sourceParentLevel", cs.getParentLevel());
                }
                reMap.put("pathName", cs.getName());
                paramList.add(reMap);
            }
        }
        systemLogTool.setSysLog(loginUser, logTypeEnum.getCode(), paramList, systemLogTool.getRequest());
    }

    public HomeFileDetailVO getFileDetail(Long fileID) {
        if (ObjectUtils.isEmpty(fileID) || fileID <= 0) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        return homeExplorerDao.getFileDetail(fileID);
    }

    /**
     * @Description: attachment 从key获取token
     * @params: [key]
     * @Return: java.lang.String
     * @Modified:
     */
    public String getAttachmentToken(String key) {
        String[] tokenArr;
        try {
            String keyString = AESUtil.decrypt(key, GlobalConfig.ATTACHMENT_AES_KEY, true);
            tokenArr = keyString.split(GlobalConfig.ATTACHMENT_KEY_SEPARATOR);
        } catch (Exception e) {
            LogUtil.error(e, "attachment,验证错误  AES解密失败");
            throw new SvnlanRuntimeException(CodeMessageEnum.errorAdminAuth.getCode());
        }
        String token = tokenArr[0];
        Long time = Long.parseLong(tokenArr[1]);
        if (time + 86400000 < System.currentTimeMillis()) {
            LogUtil.info("attachment链接已超时1天" + time + "," + System.currentTimeMillis());
        }
        return token;
    }

    public String getAttachmentToken2(String key) {
        String[] tokenArr;
        try {
            String keyString = AESUtil.decrypt(key, GlobalConfig.ATTACHMENT_AES_KEY, true);
            tokenArr = keyString.split(GlobalConfig.ATTACHMENT_KEY_SEPARATOR);
        } catch (Exception e) {
            LogUtil.error(e, "attachment,验证错误  AES解密失败");
            return "";
        }
        String token = tokenArr[0];
        Long time = Long.parseLong(tokenArr[1]);
        if (time + 86400000 < System.currentTimeMillis()) {
            LogUtil.info("attachment链接已超时1天" + time + "," + System.currentTimeMillis());
        }
        return token;
    }
    public String getAttachmentToken3(String key) {
        String[] tokenArr = getArrayAttachmentToken(key);
        return ObjectUtils.isEmpty(tokenArr) ? null : tokenArr[0];
    }

    public String[] getArrayAttachmentToken(String key) {
        String[] tokenArr;
        try {
            String keyString = AESUtil.decrypt(key, GlobalConfig.ATTACHMENT_AES_KEY, true);
            tokenArr = keyString.split(GlobalConfig.ATTACHMENT_KEY_SEPARATOR);
        } catch (Exception e) {
            LogUtil.error(e, "getArrayAttachmentToken,验证错误  AES解密失败");
            return null;
        }
        return tokenArr;
    }

    public String getPptPreviewUrl(String downloadUrl, String checksum) {
        return this.getPptPreviewUrl(downloadUrl, checksum, Boolean.TRUE);
    }

    public String getPptPreviewUrl(String downloadUrl, String checksum, Boolean isValidate) {

        try {
            HttpServletRequest request = HttpUtil.getRequest();
            String fullUrl = "";
            String serverUrl = HttpUtil.getRequestRootUrl(null);
            //若需要验证
            if (isValidate) {
                fullUrl = serverUrl + downloadUrl;
                fullUrl += "&targetServerNameForOverride=" + HttpUtil.getServerName(request);
            } else {//不需要验证 公开的则采用cdn域名
                serverUrl = "https://" + this.cdnDomain;
                fullUrl = serverUrl + downloadUrl;
            }
            String encryptUrl = StringUtil.isEmpty(pptDesKey) ? fullUrl : DESEncrypt.encode(fullUrl, pptDesKey, pptDesIV);
            //先从redis取
            String pptServerString = pptServerUrl;
            //转码服务器域名
            String[] servers = pptServerString.split(",");
            //服务器个数
            int serverCount = servers.length;

            // checksum最后一位 % 服务器数量, 选中某一个服务器
            int idIndex = 0;
            if (!ObjectUtils.isEmpty(checksum)) {
                char c = checksum.charAt(31);
                idIndex = (c % serverCount);
            }
            String pptUrl = "";
            Boolean isHttps = serverUrl.indexOf("https") == 0;
            //若是要通过加密接口校验，则
            if (isValidate) {
                pptUrl = servers[idIndex] + "?furl=" + URLEncoder.encode(encryptUrl, "UTF-8")
                        + (isHttps ? "&ssl=1" : "");
            } else { //不要通过接口加密校验
                pptUrl = servers[idIndex] + (isHttps ? "?ssl=1" : "")
                        + (isHttps ? "&" : "?")
                        + "furl=" + URLEncoder.encode(encryptUrl, "UTF-8");
            }
            return pptUrl;
        } catch (Exception e) {
            LogUtil.error(e, "获取ppt预览地址失败");
            return "";
        }
    }

    /**
     * 置顶
     */
    public void addTop(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(updateFileDTO.getSourceID()) || ObjectUtils.isEmpty(updateFileDTO.getParentID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }

        CommonSource cs = ioSourceDao.getSourceInfo(updateFileDTO.getSourceID());
        if (ObjectUtils.isEmpty(cs)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, cs.getSourceID(), cs.getParentLevel(), "14", cs.getTargetType());

        Integer sort = ioSourceDao.getMaxSort(updateFileDTO.getParentID());
        try {
            ioSourceDao.updateSort(updateFileDTO.getSourceID(), ObjectUtils.isEmpty(sort) ? 1 : sort + 1);
        } catch (Exception e) {
            LogUtil.error(e, "置顶失败");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
    }

    /**
     * 取消置顶
     */
    public void cancelTop(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(updateFileDTO.getSourceID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }

        CommonSource cs = ioSourceDao.getSourceInfo(updateFileDTO.getSourceID());
        if (ObjectUtils.isEmpty(cs)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, cs.getSourceID(), cs.getParentLevel(), "14", cs.getTargetType());
        try {
            ioSourceDao.updateSort(updateFileDTO.getSourceID(), 0);
        } catch (Exception e) {
            LogUtil.error(e, " 取消置顶失败");
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
    }

    /**
     * 新建文件
     */
    public void makeFile(CheckFileDTO updateFileDTO, LoginUser loginUser, Map<String, Object> resultMap) {
        if (ObjectUtils.isEmpty(updateFileDTO.getSourceID()) || ObjectUtils.isEmpty(updateFileDTO.getName())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        String originalFilename = updateFileDTO.getName();
        /*if (originalFilename.startsWith(".")) {
            // 文件名不符合规范, 请修改
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }*/

        CommonSource cs = ioSourceDao.getSourceInfo(updateFileDTO.getSourceID());
        if (ObjectUtils.isEmpty(cs)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, cs.getSourceID(), cs.getParentLevel(), "9", cs.getTargetType());

        String fileExtension = FileUtil.getFileExtension(originalFilename);
        if (ObjectUtils.isEmpty(fileExtension)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        List<String> sourceNameList = ioSourceDao.getSourceNameList(updateFileDTO.getSourceID());
        /*if (!CollectionUtils.isEmpty(sourceNameList) && sourceNameList.contains(updateFileDTO.getName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathExists.getCode());
        }*/
        originalFilename = this.checkRepeatName(originalFilename, originalFilename, fileExtension, sourceNameList, 1);
        CommonSource commonSource = new CommonSource();
        //设置默认值
        this.setDefault(commonSource, loginUser);
        String busType = BusTypeEnum.CLOUD.getBusType();
        commonSource.setSourceType(BusTypeEnum.CLOUD.getTypeCode());
        commonSource.setTenantId(loginUser.getTenantId());
        // 添加
        this.saveFileDetail(commonSource, updateFileDTO, busType, fileExtension, originalFilename, resultMap, "makeFile", loginUser);

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", commonSource.getSourceID());
        reMap.put("sourceParent", commonSource.getParentID());
        reMap.put("type", "mkfile");
        reMap.put("pathName", commonSource.getSourceName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileMkFile.getCode(), paramList, systemLogTool.getRequest());
    }

    public void saveFileDetail(CommonSource commonSource, CheckFileDTO updateFileDTO, String busType, String fileExtension
            , String originalFilename, Map<String, Object> resultMap, String pre, LoginUser loginUser) {

        long fileSize = 0;
        String defaultPath = storageService.getDefaultStorageDevicePath();
        //最终文件目录路径
        String finalTopPath = defaultPath + PropertiesUtil.getUpConfig(busType + ".savePath");
        //基础路径+日期路径
        String finalFolderPath = finalTopPath + FileUtil.getDatePath();
        LogUtil.info(pre + " doSaveDirect finalFolderPath=" + finalFolderPath);
        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("创建目录失败,makeFile  path:" + finalFolderPath);
//                return null;
            }
        }
        //文件后缀
        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + commonSource.getUserID() + "." + fileExtension;
        //最终文件
        File finalFile = new File(finalFilePath);
        String serverChecksum;
        FileInputStream fis = null;
        byte[] size = {0};
        boolean isNew = false;
        commonSource.setSize(0L);
        if (!ObjectUtils.isEmpty(updateFileDTO.getContent())) {

            if ("oexe".equals(fileExtension.toLowerCase())){
                commonSource.setOexeContent(updateFileDTO.getContent());
                String value = "";
                String type = "";
                boolean checkDefault = false;
                Map<String, Object> oexeMap = null;
                try {
                    // 图标
                    oexeMap = JsonUtils.jsonToMap(updateFileDTO.getContent());
                    value = (String) oexeMap.get("value");
                    type = (String) oexeMap.get("type");
                    if ("url".equals(type)) {
                        if (!oexeMap.containsKey("imgIco") || ObjectUtils.isEmpty(oexeMap.get("imgIco"))) {
                            checkDefault = true;
                        }
                    }
                }catch (Exception e){

                }
                if (checkDefault && !ObjectUtils.isEmpty(value)){
                    String requestRootUrl = HttpUtil.geUrlRootUrl(value);
                    String imgIco = requestRootUrl + "/favicon.ico";
                    boolean checkIoc = UrlDUtil.checkUrlConnection(imgIco);
                    if (checkIoc){
                        oexeMap.put("imgIco", imgIco);
                        updateFileDTO.setContent(JsonUtils.beanToJson(oexeMap));
                    }
                }
            }
            // 放入addCommonSource方法
            size = updateFileDTO.getContent().getBytes(StandardCharsets.UTF_8);
            commonSource.setSize((long) size.length);
        } else {
            isNew = true;
            updateFileDTO.setContent(" ");
        }
        try {
            if ("svg".equals(fileExtension.toLowerCase())) {
                FileUtil.writeSvgFile(finalFile, size);
            } else if (Arrays.asList("doc", "docx").contains(fileExtension.toLowerCase())) {
                FileUtil.writeDocFile(finalFile, updateFileDTO.getContent());
            } else if (Arrays.asList("ppt", "pptx").contains(fileExtension.toLowerCase())) {
                FileUtil.writePptFile(finalFile, updateFileDTO.getContent());
            } else if ("html".equals(fileExtension.toLowerCase())) {
                FileUtil.writeHTMLFile(finalFile, updateFileDTO.getContent());
            } else if (Arrays.asList("xls", "xlsx").contains(fileExtension.toLowerCase())) {
                FileUtil.writeExcelFile(finalFile);
            } else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(fileExtension.toLowerCase())) {
                //转成保存图片
                FileUtil.byte2image(size, finalFilePath);
            } else {
                if ("txt".equals(fileExtension.toLowerCase()) && isNew){
                    FileUtil.createNewFile(finalFile);
                }else {
                    FileUtil.writeFile(finalFile, size);
                }


            }
            /*****************************/
            fis = new FileInputStream(finalFile);
            serverChecksum = DigestUtils.md5DigestAsHex(fis);
            fis.close();
        } catch (IOException e) {
            LogUtil.error(e.getMessage(), " 创建文件失败");
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        }
        commonSource.setName(originalFilename);
        commonSource.setParentID(updateFileDTO.getSourceID());
        commonSource.setHashMd5(serverChecksum);
        commonSource.setPath(finalFilePath);
        commonSource.setFileType(fileExtension);
        commonSource.setTenantId(commonSource.getTenantId());

        if (!ObjectUtils.isEmpty(commonSource.getParentID()) && commonSource.getParentID() > 0) {
            CommonSource parentSource = this.getSourceInfo(commonSource.getParentID());
            if (ObjectUtils.isEmpty(parentSource)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            commonSource.setParentLevel(parentSource.getParentLevel() + commonSource.getParentID() + ",");
            commonSource.setTargetType(parentSource.getTargetType());
        } else {
            commonSource.setParentLevel(",0,");
            commonSource.setTargetType(1);
        }
        if (!ObjectUtils.isEmpty(commonSource.getSize()) && commonSource.getSize() > 0) {
            fileSize = commonSource.getSize();
        }
        try {
            this.addCommonSource(commonSource.getUserID(), commonSource, EventEnum.mkfile);
            resultMap.put("source", commonSource);
        } catch (Exception e) {
            LogUtil.error(e, "makeFile source入库失败" + JsonUtils.beanToJson(commonSource));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        //
        if (fileSize > 0 && "makeFile".equals(pre)) {
            this.updateMemory(fileSize, 0, loginUser.getUserID(), commonSource.getTargetType(), commonSource.getParentLevel());
        }
    }

    public void setDefault(CommonSource commonSource, LoginUser loginUser) {
        commonSource.setUserID(loginUser.getUserID());
        commonSource.setAppPreviewUrl("");
        commonSource.setAppPreview(0);
        commonSource.setHashMd5("");
        commonSource.setIsM3u8(0);
        commonSource.setIsPreview(0);
        commonSource.setSourceLength(0);
        commonSource.setName("");
        commonSource.setPath("");
        commonSource.setFileType("");
        commonSource.setPreviewUrl("");
        commonSource.setThumb("");
        if (Objects.isNull(commonSource.getSourceType())) {
            commonSource.setSourceType(0);
        }
        commonSource.setApprovalState("0");
        commonSource.setResolution("");
    }

    /**
     * 链接分享
     */
    public List<Long> checkUserIsShare(Long userID) {
        String key = GlobalConfig.my_share_key + userID;
        List<Long> shareList = null;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String value = operations.get(key);
        if (!ObjectUtils.isEmpty(value) && !"0".equals(value)) {
            try {
                shareList = Arrays.asList(value.split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
            } catch (Exception e) {
                LogUtil.error(e, "checkUserIsShare 缓存解析失败 key=" + key + "，value=" + value);
            }
        }
        if (ObjectUtils.isEmpty(value)) {
            shareList = shareDao.checkUserIsShare(userID);
            if (ObjectUtils.isEmpty(shareList)) {
                shareList = new ArrayList<>();
                shareList.add(0L);
            }
            String share = shareList.stream().map(String::valueOf).collect(Collectors.joining(","));
            operations.set(key, share, 30, TimeUnit.MINUTES);
        }

        return shareList;
    }

    /**
     * 收藏
     */
    public List<String> checkIsFavByUserId(Long userID) {

        String key = GlobalConfig.my_fav_key + userID;
        List<String> favList = null;
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String value = operations.get(key);
        if (!ObjectUtils.isEmpty(value) && !"0".equals(value)) {
            try {
                favList = Arrays.asList(value.split(",")).stream().map(String::valueOf).collect(Collectors.toList());
            } catch (Exception e) {
                LogUtil.error(e, "checkIsFavByUserId 缓存解析失败 key=" + key + "，value=" + value);
            }
        }
        if (ObjectUtils.isEmpty(value)) {
            favList = userFavDao.checkIsFavByUserId(userID);
            if (ObjectUtils.isEmpty(favList)) {
                favList = new ArrayList<>();
                favList.add("0");
            }
            operations.set(key, favList.stream().collect(Collectors.joining(",")), 30, TimeUnit.MINUTES);
        }
        return favList;
    }

    public void updateFileCover(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(updateFileDTO.getSourceID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        CommonSource cs = ioSourceDao.getSourceInfo(updateFileDTO.getSourceID());
        if (ObjectUtils.isEmpty(cs)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, cs.getSourceID(), cs.getParentLevel(), "8", cs.getTargetType());


        IOFileMeta meta = ioFileDao.getFileValue(updateFileDTO.getSourceID());
        Map<String, Object> map = null;
        if (!ObjectUtils.isEmpty(meta) && !ObjectUtils.isEmpty(meta.getValue())) {
            map = JsonUtils.jsonToMap(meta.getValue());
        }
        if (ObjectUtils.isEmpty(map)) {
            map = new HashMap<>(1);
        }
        map.put("cover", ObjectUtils.isEmpty(updateFileDTO.getCover()) ? "" : updateFileDTO.getCover());

        try {
            if (!ObjectUtils.isEmpty(meta)) {
                ioFileDao.updateOneFileUrlValue(meta.getFileID(), JsonUtils.beanToJson(map));
            } else {
                IOFileMeta fileMeta = new IOFileMeta();
                fileMeta.setFileID(meta.getFileID());
                fileMeta.setKey("fileInfoMore");
                fileMeta.setValue(JsonUtils.beanToJson(map));
                ioFileDao.insertMeta(fileMeta);
            }
        } catch (Exception e) {
            LogUtil.error(e, " 保存封面失败！");
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }

    /**
     * 标签
     */
    public Map<String, List<CommonLabelVo>> getSourceTagMap(Long userID, List<String> sourceIDs) {
        Map<String, List<CommonLabelVo>> map = null;
        List<UserFavVo> myTagList = userFavDaoImpl.getFileTagBySourceID(userID, sourceIDs);
        if (!CollectionUtils.isEmpty(myTagList)) {
            map = new HashMap<>();
            List<CommonLabelVo> labelList = null;
            for (UserFavVo userFavVo : myTagList){
                if (map.containsKey(userFavVo.getPath())){
                    labelList = map.get(userFavVo.getPath());
                }else {
                    labelList = new ArrayList<>();
                }
                labelList.add(new CommonLabelVo(userFavVo.getTagID().longValue(),userFavVo.getUserID(),userFavVo.getLabelName(), userFavVo.getStyle()));
                map.put(userFavVo.getPath(), labelList );
            }
        }
        return map;
    }

    public String checkRepeatName(String name, String newName, List<String> sourceNameList, int i) {
        if (!CollectionUtils.isEmpty(sourceNameList)) {
            sourceNameList = sourceNameList.stream().map(String::toLowerCase).collect(Collectors.toList());
            if (sourceNameList.contains(newName.toLowerCase())) {
                return checkRepeatName(name, name + "(" + i + ")", sourceNameList, i + 1);
            }
        }
        return newName;
    }

    public String checkRepeatName(String name, String newName, String fileType, List<String> sourceNameList, int i) {
        log.info("checkRepeatName  name => {}  newName => {} fileType => {}  sourceNameList => {}  i => {}",
                name, newName, fileType, sourceNameList, i);
        if (!CollectionUtils.isEmpty(sourceNameList)) {
            sourceNameList = sourceNameList.stream().map(String::toLowerCase).collect(Collectors.toList());
            if (sourceNameList.contains(newName.toLowerCase())) {
                if (!ObjectUtils.isEmpty(fileType)) {
                    return checkRepeatName(name, name.replaceAll("." + fileType, "") + "(" + i + ")." + fileType, fileType, sourceNameList, i + 1);
                }else {
                    return checkRepeatName(name, name + "(" + i + ")" , fileType, sourceNameList, i + 1);
                }
            }
        }
        return newName;
    }

    /**
     * @Description: 收藏夹重命名
     * @params: [updateFileDTO, paramMap]
     * @Return: boolean
     * @Modified:
     */
    public boolean favRenameFile(CheckFileDTO updateFileDTO, LoginUser loginUser) {
        if (ObjectUtils.isEmpty(updateFileDTO.getId()) || StringUtil.isEmpty(updateFileDTO.getName())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.selectFolderFile.getCode());
        }
        if (updateFileDTO.getName().length() > 200) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<String> nameList = userFavDao.getFavNameList(loginUser.getUserID());
        if (!CollectionUtils.isEmpty(nameList)) {
            nameList = nameList.stream().map(String::toLowerCase).collect(Collectors.toList());
            if (nameList.contains(updateFileDTO.getName().toLowerCase())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.pathExists.getCode());
            }
        }

        try {
            userFavDao.updateFavName(updateFileDTO.getId(), updateFileDTO.getName());
        } catch (Exception e) {
            LogUtil.error(e, " batchFileRename error operationsDTO=" + JsonUtils.beanToJson(updateFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        return true;
    }

    public String getPropertiesFilePathAll(String propertiesName) {
       return getPropertiesFilePathAll(propertiesName, null);
    }
    public String getPropertiesFilePathAll(String propertiesName, Long tenantId) {
        String defaultPath = storageService.getDefaultStorageDevicePath(tenantId, 0);
        String finalTopPath = PropertiesUtil.getUpConfig(propertiesName);
        return defaultPath + finalTopPath;
    }
}
