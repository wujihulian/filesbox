package com.svnlan.home.utils;

import com.alibaba.fastjson.JSONObject;
import com.aliyun.dingtalkstorage_1_0.models.GetDentryResponseBody;
import com.aliyun.dingtalkstorage_1_0.models.GetFileDownloadInfoResponseBody;
import com.aliyun.dingtalkstorage_1_0.models.GetSpaceResponseBody;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.DingSpaceTypeEnum;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.enums.SecurityTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.DingAboutDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.service.BusTypeHandleService;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.user.dao.Impl.GroupDaoImpl;
import com.svnlan.user.dao.Impl.SystemOptionDaoImpl;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.Group;
import com.svnlan.user.domain.ThirdUserInitializeConfig;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.service.UserManageService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import io.jsonwebtoken.lang.Assert;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/20 16:19
 */
@Component
public class AsyncDingFileUtil {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    DingAboutDao dingAboutDao;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    BusTypeHandleService busTypeHandleService;
    @Resource
    StorageService storageService;
    @Resource
    DingDownloadTool dingDownloadTool;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    IoSourceHistoryDao ioSourceHistoryDao;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    SourceHistoryUtil sourceHistoryUtil;
    @Resource
    UserDao userDao;
    @Resource
    GroupDaoImpl groupDaoImpl;
    @Resource
    UserManageService userManageService;
    @Resource
    SystemOptionDaoImpl systemOptionDaoImpl;
    @Resource
    OptionTool optionTool;
    @Resource
    IoSourceMetaDao ioSourceMetaDaoImpl;
    @Resource
    SystemSortTool systemSortTool;

    // 1 企业类型 2 家校通
    @Value("${enterprise.type}")
    String enterpriseType;


    /** 文件或文件夹添加事件 */
    @Transactional(rollbackFor = Exception.class)
    public void storageDentryCreate(String msg ,JSONObject eventJson, List<ThirdUserInitializeConfig.DingInfoDTO> dingInfoDTOList){

        String type = eventJson.getString("type");
        String dentryId = eventJson.getString("dentryId");
        String UnionId = null;
        if (eventJson.containsKey("unionId") ){
            UnionId = eventJson.getString("unionId");
        }else {
            UnionId = eventJson.getString("UnionId");
        }
        String spaceId = eventJson.getString("spaceId");
        // String eventScopeId = eventJson.getString("eventScopeId");


        // @TODO 需要根据 eventScopeId 获取是哪个租户id
        Long tenantId = 1L;
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig(tenantId);
        String appKey = dingConfigVo.getAppKey();
        String appSecret = dingConfigVo.getAppSecret();

        // 根据spaceId查询文件所在位置 org 团队文件 group 群文件 hideOrgEmp 个人文件
        GetSpaceResponseBody getSpaceResponseBody = DingUtil.getDingSpaceInfo(stringRedisTemplate,appKey,appSecret,UnionId,spaceId);
        if (ObjectUtils.isEmpty(getSpaceResponseBody)){
            LogUtil.error(msg + "storageDentryCreate 1 getSpaceResponseBody 获取空间信息失败 eventJson" + eventJson);
            return;
        }

        String scene = getSpaceResponseBody.getSpace().getScene();
        if (DingSpaceTypeEnum.group.getValue().equals(scene)){
            // 过滤群文件
            return;
        }
        long userId = 0L;

        String userName = null;
        String nickname = null;
        String avatar = "";
        Integer sex = 1;
        UserVo userVo = null;
        List<UserVo> userVoList = dingAboutDao.getUserInfoByUnionId(UnionId);

        // 删除之后不再新增用户
        if (!CollectionUtils.isEmpty(userVoList)){
            for (UserVo vo : userVoList){
                if (vo.getStatus().intValue() == 1){
                    userVo = vo;
                }
            }
            if (ObjectUtils.isEmpty(userVo) && userVoList.size() > 0){
                LogUtil.error("storageDentryCreate user 用户被删除过不再创建用户，请联系管理员查看 UnionId="
                        + UnionId + "， user=" + JsonUtils.beanToJson(userVoList));
                return;
            }
        }


        if (ObjectUtils.isEmpty(userVo)){
            if (checkGetUserError(UnionId)){
                LogUtil.error("storageDentryCreate user 获取用户钉钉信息失败，请联系管理员查看是否是限制用户 UnionId=" + UnionId);
                return;
            }
            userVo = this.createDingUser(UnionId, tenantId);
            if (ObjectUtils.isEmpty(userVo)){
                LogUtil.error("storageDentryCreate user 创建用户失败 eventJson=" + eventJson);
                return;
            }
        }
        userId = userVo.getUserID();
        userName = userVo.getName();
        nickname = userVo.getNickname();
        avatar = userVo.getAvatar();
        sex = userVo.getSex();


        // 配置的钉盘关联
        Long parentId = getParentIdBySpace(scene, dingInfoDTOList, userId, tenantId);
        LogUtil.info("storageDentryCreate parentId=" + parentId + "  getSpaceResponseBody" + JsonUtils.beanToJson(getSpaceResponseBody));

        if (ObjectUtils.isEmpty(parentId) || parentId <= 0){
            LogUtil.error("storageDentryCreate parentId= " + parentId + "，eventJson="+ eventJson);
            return;
        }

        // 获取文件、文件夹信息
        GetDentryResponseBody dingSourceBody = DingUtil.getDingSourceInfo(stringRedisTemplate,appKey,appSecret,UnionId,spaceId,dentryId);
        if (ObjectUtils.isEmpty(dingSourceBody)){
            LogUtil.error(msg + "storageDentryCreate 1 获取钉盘文件夹、文件信息失败 eventJson" + eventJson);
            return;
        }
        CommonSource parentSource = null;
        String dingParentIdStr = dingSourceBody.getDentry().getParentId();
        if (!ObjectUtils.isEmpty(dingParentIdStr) && !"0".equals(dingParentIdStr)){
            parentSource = fileOptionTool.getSourceInfoByDentryId(dingParentIdStr);
        }else if("0".equals(dingParentIdStr)){
            parentSource = fileOptionTool.getSourceInfo(parentId);
        }
        if (ObjectUtils.isEmpty(parentSource) || (!ObjectUtils.isEmpty(parentSource) && parentSource.getIsDelete().intValue() == 1)){
            List<CommonSource> newFolderList = new ArrayList<>();
            try {
                if ((!ObjectUtils.isEmpty(parentSource) && parentSource.getIsDelete().intValue() == 1)){
                    fileOptionTool.clearUserDentryIdByDentryId(dingParentIdStr);
                }
                // 创建上层文件夹
                boolean c = stringRedisTemplate.opsForValue().setIfAbsent(GlobalConfig.lock_dingParentId_key + dingParentIdStr, "1", 5, TimeUnit.MINUTES);
                if (!c){
                    LogUtil.info( "钉钉同步创建不存在的父文件夹 进行中。。。dingParentIdStr="+ dingParentIdStr);
                    int i = 0;
                    while (i < 13){
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                        }
                        parentSource = fileOptionTool.getSourceInfoByDentryId(dingParentIdStr);
                        if (!ObjectUtils.isEmpty(parentSource)){
                            LogUtil.info( "钉钉同步创建不存在的父文件夹 等待结束。。。dingParentIdStr="+ dingParentIdStr);
                            break;
                        }
                        LogUtil.info( "钉钉同步创建不存在的父文件夹 等待中。。。dingParentIdStr="+ dingParentIdStr);
                        i ++;
                    }
                } else {
                    parentSource = createParentFolder(parentId, dingParentIdStr, appKey, appSecret, UnionId, spaceId, newFolderList
                            , 0, userId, tenantId, scene, eventJson);
                }
            }catch (Exception e){
                LogUtil.error(e, "钉钉同步创建不存在的父文件夹error");
            }
        }

        if (ObjectUtils.isEmpty(parentSource)){
            LogUtil.error("钉钉同步创建不存在的父文件夹error");
            return;
        }

        List<CommonSource> convertList = new ArrayList<>();
        String name = dingSourceBody.getDentry().getName();

        List<String> sourceNameList = ioSourceDao.getSourceNameList(parentSource.getSourceID());

        if ("FOLDER".equals(type)){
            // 新建文件夹
            createDir(name, sourceNameList, parentSource, dentryId, UnionId, spaceId, eventJson
                    , userId, tenantId, scene);
        }else {
            // 文件
            String extension = eventJson.getString("extension");
            Long size = dingSourceBody.getDentry().getSize();

            // 企业网盘默认是1
            long checkSizeSpaceId = 1;
            // 个人空间
            if (DingSpaceTypeEnum.hideOrgEmp.getValue().equals(scene)){
                String spaceSourceIDStr = ioSourceMetaDaoImpl.getSourceIDMetaByKey(userId + "", "mySpace", null);
                checkSizeSpaceId = Long.parseLong(spaceSourceIDStr);
            }
            //判断是否超出容量
            /** 获取企业云盘 */
            HomeExplorerVO disk = systemSortTool.getUserSpaceSize(userId, checkSizeSpaceId);
            try {
                fileOptionTool.checkMemory(disk, size);
            }catch (SvnlanRuntimeException e){
                LogUtil.error(e, "容量校验：文件超出大小限制");
                return;
            }catch (Exception e){
                LogUtil.error(e, "容量校验 error");
                return;
            }


            // 获取文件下载信息
            GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody = DingUtil.getDingDownloadInfos(stringRedisTemplate, appKey, appSecret, UnionId, spaceId
                    , dentryId);
            if (ObjectUtils.isEmpty(getFileDownloadInfoResponseBody)){
                LogUtil.error(msg + "storageDentryCreate 2 获取文件下载信息失败 dingSourceBody=" + JsonUtils.beanToJson(dingSourceBody));
                return;
            }
            String finalTopPath = storageService.getDefaultStorageDevicePath() +  PropertiesUtil.getUpConfig("cloud.savePath");
            String finalFolderPath = finalTopPath + FileUtil.getDatePath();
            File folder = new File(finalFolderPath);
            //创建目录
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    LogUtil.error("storageDentryCreate 创建目录失败,makeFile  path:" + finalFolderPath);
                    return ;
                }
            }
            //文件后缀
            //最终文件路径  最终文件目录路径+毫秒+.后缀
            String fileName = RandomUtil.getuuid() + System.currentTimeMillis()
                    + "_" + userId ;

            String finalFilePath = finalFolderPath + fileName + "." + extension;
            //最终文件
            File finalFile = null;

            // 100M
            long rangeSize = 100*1024*1024;
            if (size > rangeSize){
                // 分片下载合并
                dingDownloadTool.dingDownloadAndMergeFile(getFileDownloadInfoResponseBody, finalFilePath, finalFolderPath
                        , size, fileName, extension,finalTopPath);

            }else {
                // 下载文件
                DingUtil.dingDownloadFile(getFileDownloadInfoResponseBody, finalFilePath);
            }

            // 下载完成后查看文件是否存在
            finalFile = new File(finalFilePath);
            if (!finalFile.exists()){
                LogUtil.error(msg + "storageDentryCreate 3 文件不存在，下载失败 dingSourceBody=" + JsonUtils.beanToJson(dingSourceBody));
                return;
            }

            String serverChecksum = "";
            FileInputStream fis = null;
            try {
                /*****************************/
                fis = new FileInputStream(finalFile);
                serverChecksum = DigestUtils.md5DigestAsHex(fis);
                fis.close();
            } catch (IOException e) {
                LogUtil.error(e.getMessage(), msg + " storageDentryCreate 4 获取 文件 md5 失败");
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
            UploadDTO uploadDTO = new UploadDTO();
            uploadDTO.setHasMark(false);

            CommonSource fileSource = new CommonSource();
            fileSource.setName(fileOptionTool.checkRepeatName(name, name, extension, sourceNameList, 1));
            fileSource.setParentID(parentSource.getSourceID());
            fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
            fileSource.setTargetType(parentSource.getTargetType());
            fileSource.setFileType(extension);
            fileSource.setSize(size);
            fileSource.setHashMd5(serverChecksum);
            fileSource.setTenantId(tenantId);
            fileSource.setPath(finalFilePath);
            fileSource.setDingEventJson(JsonUtils.beanToJson(eventJson));
            fileSource.setDingSpaceId(spaceId);
            fileSource.setDingUnionId(UnionId);
            fileSource.setDingDentryId(dentryId);
            fileSource.setSourceHash(scene);
            fileOptionTool.addCommonSource(userId, fileSource, EventEnum.mkfile);

            LogUtil.info(msg + "storageDentryUpdate 5 fileSource=" + JsonUtils.beanToJson(fileSource));
            if (Arrays.asList(GlobalConfig.VIDEO_AUDIO_TYPE_CONVERT).contains(extension)){
                // 转码、图片加缩略图
                convertList.add(fileSource);
            }else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(extension)){
                // "cloud"
                fileSource.setSourceType(2);
                busTypeHandleService.doForImage(uploadDTO, false, fileSource);
                if (!ObjectUtils.isEmpty(fileSource.getHashMd5())) {
                    convertList.add(fileSource);
                }
            } else {
                if (!ObjectUtils.isEmpty(fileSource.getHashMd5())) {
                    convertList.add(fileSource);
                }
            }


            // 更新容量
            fileOptionTool.updateMemory(fileSource.getSize(), 0, userId, fileSource.getTargetType(), fileSource.getParentLevel());


            /** 操作日志 */
            Map<String, Object> reMap = null;
            List<Map<String, Object>> paramList = new ArrayList<>();
            reMap = new HashMap<>(4);
            reMap.put("sourceID", fileSource.getSourceID());
            reMap.put("sourceParent", fileSource.getParentID());
            reMap.put("type", "uploadNew");
            reMap.put("userID", userId);
            reMap.put("pathName", fileSource.getName());
            paramList.add(reMap);
            LoginUser loginUser = new LoginUser();
            loginUser.setUserAgent(null);
            loginUser.setIp("");
            loginUser.setUserID(userId);
            loginUser.setName(userName);
            loginUser.setNickname(nickname);
            loginUser.setServerName("");
            loginUser.setAvatar(avatar);
            loginUser.setSex(sex);
            loginUser.setTenantId(tenantId);
            systemLogTool.setSysLog(loginUser, LogTypeEnum.fileUpload.getCode(), paramList, null,null,"钉转存");
        }
        // 文件转码
        if (!CollectionUtils.isEmpty(convertList)) {
            for (CommonSource source : convertList) {
                ConvertDTO convertDTO = new ConvertDTO();
                convertDTO.setBusId(source.getSourceID());
                convertDTO.setBusType("cloud");
                convertDTO.setOtherType("syncDing");
                source.setDomain("domain");
                convertDTO.setDomain("domain");
                convertDTO.setTenantId(tenantId);
                convertUtil.doConvert(convertDTO, source);
            }
        }
    }
    /** 文件或文件夹修改事件 */
    @Transactional(rollbackFor = Exception.class)
    public void storageDentryUpdate(JSONObject eventJson, List<ThirdUserInitializeConfig.DingInfoDTO> dingInfoDTOList){
        String type = eventJson.getString("type");
        String dentryId = eventJson.getString("dentryId");
        String UnionId = null;
        if (eventJson.containsKey("unionId") ){
            UnionId = eventJson.getString("unionId");
        }else {
            UnionId = eventJson.getString("UnionId");
        }
        String spaceId = eventJson.getString("spaceId");

        CommonSource detrySource = fileOptionTool.getSourceInfoByDentryId(dentryId);
        if (ObjectUtils.isEmpty(detrySource) || (!ObjectUtils.isEmpty(detrySource) && detrySource.getIsDelete().intValue() == 1)){
            if ((!ObjectUtils.isEmpty(detrySource) && detrySource.getIsDelete().intValue() == 1)){
                fileOptionTool.clearUserDentryIdByDentryId(dentryId);
            }
            // 走添加流程
            storageDentryCreate("storageDentryUpdate ", eventJson, dingInfoDTOList);
            return;
        }

        // 通过unionId获得 userId;
        long userId = 0L;

        String userName = null;
        String nickname = null;
        String avatar = "";
        Integer sex = 1;
        UserVo userVo = null;
        List<UserVo> userVoList = dingAboutDao.getUserInfoByUnionId(UnionId);

        // 删除之后不再新增用户
        if (!CollectionUtils.isEmpty(userVoList)){
            for (UserVo vo : userVoList){
                if (vo.getStatus().intValue() == 1){
                    userVo = vo;
                }
            }
            if (ObjectUtils.isEmpty(userVo) && userVoList.size() > 0){
                LogUtil.error("storageDentryUpdate user 用户被删除过不再创建用户，请联系管理员查看 UnionId="
                        + UnionId + "， user=" + JsonUtils.beanToJson(userVoList));
                return;
            }
        }
        if (ObjectUtils.isEmpty(userVo)){
            if (checkGetUserError(UnionId)){
                LogUtil.error("storageDentryUpdate user 获取用户钉钉信息失败，请联系管理员查看是否是限制用户 UnionId=" + UnionId);
                return;
            }
            userVo = this.createDingUser(UnionId, detrySource.getTenantId());
            if (ObjectUtils.isEmpty(userVo)){
                LogUtil.error("storageDentryUpdate user 创建用户失败 eventJson" + eventJson);
                return;
            }
        }
        userId = userVo.getUserID();
        userName = userVo.getName();
        nickname = userVo.getNickname();
        avatar = userVo.getAvatar();
        sex = userVo.getSex();


        DingConfigVo dingConfigVo = optionTool.getDingDingConfig(detrySource.getTenantId());
        String appKey = dingConfigVo.getAppKey();
        String appSecret = dingConfigVo.getAppSecret();

        GetDentryResponseBody dingSourceBody = DingUtil.getDingSourceInfo(stringRedisTemplate,appKey,appSecret,UnionId,spaceId,dentryId);

        if (ObjectUtils.isEmpty(dingSourceBody)){
            LogUtil.error("storageDentryUpdate 1 获取钉盘文件夹、文件信息失败 eventJson" + eventJson);
            return;
        }

        List<CommonSource> convertList = new ArrayList<>();
        String name = dingSourceBody.getDentry().getName();
        Long parentId = detrySource.getParentID();

        CommonSource parentSource = fileOptionTool.getSourceInfo(parentId);
        Long tenantId = parentSource.getTenantId();
        List<String> sourceNameList = ioSourceDao.getSourceNameList(parentId);

        if ("FOLDER".equals(type)){
            // 编辑文件夹
            List<SourceOpDto> renameList = new ArrayList<>();
            try {
                SourceOpDto opDto = new SourceOpDto();
                opDto.setName(fileOptionTool.checkRepeatName(name, name, sourceNameList, 1));
                opDto.setNamePinyin(ChinesUtil.getPingYin(opDto.getName()));
                opDto.setNamePinyinSimple(ChinesUtil.getFirstSpell(opDto.getName()));
                renameList.add(opDto);
                ioSourceDao.batchFileRename(renameList, userId);
            } catch (Exception e) {
                LogUtil.error(e, " batchFileRename error operationsDTO=" + JsonUtils.beanToJson(renameList));
                throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
            }
        }else {
            // 文件
            String extension = eventJson.getString("extension");
            Long size = dingSourceBody.getDentry().getSize();

            // 获取文件下载信息
            GetFileDownloadInfoResponseBody getFileDownloadInfoResponseBody = DingUtil.getDingDownloadInfos(stringRedisTemplate, appKey, appSecret, UnionId, spaceId
                    , dentryId);
            if (ObjectUtils.isEmpty(getFileDownloadInfoResponseBody)){
                LogUtil.error("storageDentryUpdate 2 获取文件下载信息失败 dingSourceBody=" + JsonUtils.beanToJson(dingSourceBody));
                return;
            }

            IoSourceHistory orgHistory = ioSourceHistoryDao.getHistoryInfoByFileId(detrySource.getSourceID(), detrySource.getFileID());
            if (!ObjectUtils.isEmpty(orgHistory)){
                userId = orgHistory.getUserID();
            }
            IoSourceHistory ioSourceHistory = new IoSourceHistory();
            ioSourceHistory.setSourceID(detrySource.getSourceID());
            ioSourceHistory.setUserID(userId);
            ioSourceHistory.setFileID(detrySource.getFileID());
            ioSourceHistory.setSize(detrySource.getSize());
            ioSourceHistory.setDetail("");


            String finalTopPath = storageService.getDefaultStorageDevicePath() +  PropertiesUtil.getUpConfig("cloud.savePath");
            String finalFolderPath = finalTopPath + FileUtil.getDatePath();
            File folder = new File(finalFolderPath);
            //创建目录
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    LogUtil.error("创建目录失败,makeFile  path:" + finalFolderPath);
                    return ;
                }
            }
            //文件后缀
            //最终文件路径  最终文件目录路径+毫秒+.后缀
            String fileName = RandomUtil.getuuid() + System.currentTimeMillis()
                    + "_" + userId ;

            String finalFilePath = finalFolderPath + fileName + "." + extension;
            //最终文件
            File finalFile = null;

            // 100M
            long rangeSize = 100*1024*1024;
            if (size > rangeSize){
                // 分片下载合并
                dingDownloadTool.dingDownloadAndMergeFile(getFileDownloadInfoResponseBody, finalFilePath, finalFolderPath
                        , size, fileName, extension,finalTopPath);

            }else {
                // 下载文件
                DingUtil.dingDownloadFile(getFileDownloadInfoResponseBody, finalFilePath);
            }

            // 下载完成后查看文件是否存在
            finalFile = new File(finalFilePath);
            if (!finalFile.exists()){
                LogUtil.error("storageDentryUpdate 3 文件不存在，下载失败 dingSourceBody=" + JsonUtils.beanToJson(dingSourceBody));
                return;
            }

            String serverChecksum = "";
            FileInputStream fis = null;
            try {
                /*****************************/
                fis = new FileInputStream(finalFile);
                serverChecksum = DigestUtils.md5DigestAsHex(fis);
                fis.close();
            } catch (IOException e) {
                LogUtil.error(e.getMessage(), " storageDentryUpdate 4 获取 文件 md5 失败");
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
            UploadDTO uploadDTO = new UploadDTO();
            uploadDTO.setHasMark(false);

            CommonSource fileSource = new CommonSource();
            fileSource.setName(fileOptionTool.checkRepeatName(name, name, extension, sourceNameList, 1));
            fileSource.setParentID(parentSource.getSourceID());
            fileSource.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + ",");
            fileSource.setTargetType(parentSource.getTargetType());
            fileSource.setFileType(extension);
            fileSource.setSize(size);
            fileSource.setHashMd5(serverChecksum);
            fileSource.setTenantId(tenantId);
            fileSource.setPath(finalFilePath);
            fileSource.setDingEventJson(JsonUtils.beanToJson(eventJson));
            fileSource.setDingSpaceId(spaceId);
            fileSource.setDingUnionId(UnionId);
            fileSource.setDingDentryId(dentryId);
            fileSource.setIsEdit("1");
            fileOptionTool.addCommonSource(userId, fileSource, EventEnum.mkfile);

            LogUtil.info("storageDentryUpdate 5 fileSource=" + JsonUtils.beanToJson(fileSource));
            if (Arrays.asList(GlobalConfig.VIDEO_AUDIO_TYPE_CONVERT).contains(extension)){
                // 转码、图片加缩略图
                convertList.add(fileSource);
            }else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(extension)){
                // "cloud"
                fileSource.setSourceType(2);
                busTypeHandleService.doForImage(uploadDTO, false, fileSource);
                if (!ObjectUtils.isEmpty(fileSource.getHashMd5())) {
                    convertList.add(fileSource);
                }
            } else {
                if (!ObjectUtils.isEmpty(fileSource.getHashMd5())) {
                    convertList.add(fileSource);
                }
            }

            // 添加历史记录
            try {
                sourceHistoryUtil.changeCheckSourceHistory(new IoSourceHistory(fileSource.getSourceID(),fileSource.getFileID(),fileSource.getUserID(),fileSource.getSize()), ioSourceHistory);

            } catch (Exception e) {
                LogUtil.error(e, "storageDentryUpdate 添加历史记录失败 uploadDTO=" + JsonUtils.beanToJson(uploadDTO));
            }

            // 更新容量
            fileOptionTool.updateMemory(fileSource.getSize(), 0, userId, fileSource.getTargetType(), fileSource.getParentLevel());

            // 添加文档操作event
            fileOptionTool.addSourceEvent(fileSource.getSourceID(), fileSource.getParentID(), userId, fileSource.getName(), EventEnum.uploadNew);

            /** 操作日志 */
            Map<String, Object> reMap = null;
            List<Map<String, Object>> paramList = new ArrayList<>();
            reMap = new HashMap<>(4);
            reMap.put("sourceID", fileSource.getSourceID());
            reMap.put("sourceParent", fileSource.getParentID());
            reMap.put("type", "uploadNew");
            reMap.put("userID", userId);
            reMap.put("pathName", fileSource.getName());
            paramList.add(reMap);
            LoginUser loginUser = new LoginUser();
            loginUser.setUserAgent(null);
            loginUser.setIp("");
            loginUser.setUserID(userId);
            loginUser.setName(userName);
            loginUser.setNickname(nickname);
            loginUser.setServerName("");
            loginUser.setAvatar(avatar);
            loginUser.setSex(sex);
            loginUser.setTenantId(tenantId);
            systemLogTool.setSysLog(loginUser, LogTypeEnum.fileUpload.getCode(), paramList, null);
        }
        // 文件转码
        if (!CollectionUtils.isEmpty(convertList)) {
            for (CommonSource source : convertList) {
                ConvertDTO convertDTO = new ConvertDTO();
                convertDTO.setBusId(source.getSourceID());
                convertDTO.setBusType("cloud");
                convertDTO.setOtherType("syncDing");
                source.setDomain("domain");
                convertDTO.setDomain("domain");
                convertDTO.setTenantId(tenantId);
                convertUtil.doConvert(convertDTO, source);
            }
        }



    }
    /** 文件或文件夹删除事件 */
    @Transactional(rollbackFor = Exception.class)
    public void storageDentryDelete(JSONObject eventJson, List<ThirdUserInitializeConfig.DingInfoDTO> dingInfoDTOList){

    }

    /** 刷新钉钉企业内部应用的accessToken*/
    public String refreshDingToken(Long tenantId) {
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig(tenantId);
        String appKey = dingConfigVo.getAppKey();
        String appSecret = dingConfigVo.getAppSecret();
        return DingUtil.refreshDingToken(stringRedisTemplate,appKey,appSecret);
    }

    /** 获取文件下载信息 */
    public String setStringRedisTemplate() {
        String url = "";

        return url;
    }

    public CommonSource createDir(String name, List<String> sourceNameList, CommonSource parentSource, String dentryId, String UnionId, String spaceId, JSONObject eventJson
    , Long userId, Long tenantId, String scene){

        // 文件夹
        CommonSource source = new CommonSource();
        source.setName(fileOptionTool.checkRepeatName(name, name, sourceNameList, 1));
        source.setParentID(parentSource.getSourceID());
        source.setParentLevel(parentSource.getParentLevel() + parentSource.getSourceID() + "," );
        source.setTargetType(parentSource.getTargetType());
        source.setFileType("");
        source.setSize(0L);
        source.setTenantId(tenantId);
        source.setDingDentryId(dentryId);
        source.setDingUnionId(UnionId);
        source.setDingSpaceId(spaceId);
        source.setDingEventJson(JsonUtils.beanToJson(eventJson));
        source.setSourceHash(scene);
        fileOptionTool.addIoSourceDetail(source, userId, 0L, EventEnum.mkdir);
        return source;
    }

    private UserVo createDingUser(String UnionId, Long tenantId){
        // UserDTO userDTO = DingUtil.getDingUserInfoByUnionid(UnionId, this.getAccessToken());
        String accessToken = this.getAccessToken(tenantId);
        String dingUserId = DingUtil.getUserIdByUnionid(UnionId, accessToken);
        if (ObjectUtils.isEmpty(dingUserId)){
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        UserDTO userDTO = getDingUserInfoByuserid(dingUserId, this.getAccessToken(tenantId), UnionId);
        if (ObjectUtils.isEmpty(userDTO)){
            LogUtil.error("createDingUser 钉转存 该用户获取为空，请联系管理员查看是否限制了此用户权限 dingUserId=" + dingUserId + "，unionId=" + UnionId);
            return null;
        }
        LogUtil.info("createDingUser 根据unionId获取钉钉userId dingUserId=" + dingUserId + "，userDTO=" + JsonUtils.beanToJson(userDTO));
        if (!ObjectUtils.isEmpty(enterpriseType) && "2".equals(enterpriseType)){
            // 家校通
            LogUtil.info("家校通校验用户是否是教师 userDTO=" + JsonUtils.beanToJson(userDTO));
            // TODO 判断是否是老师，如果是学生则返回
            if (!CollectionUtils.isEmpty(userDTO.getDeptIdList()) && userDTO.getDeptIdList().size() >= 1){
                try {
                    boolean check = DingUtil.checkDingUserIsTeacher(userDTO.getDeptIdList(), accessToken);
                    if (!check){
                        LogUtil.info("该用户非教师 userDTO=" + JsonUtils.beanToJson(userDTO));
                        // 非教师的用户放入redis排除
                        putDingUserErrRedis(UnionId);
                        return null;
                    }
                }catch (Exception e){
                    LogUtil.error(e, "获取家校通是否是老师失败");
                    // 非教师的用户放入redis排除
                    putDingUserErrRedis(UnionId);
                    return null;
                }

            }
        }

        SecurityTypeEnum thirdNameEnum = SecurityTypeEnum.DING_DING;
        UserVo userVo = null;
        userDTO.setDingUserId(dingUserId);
        try {
            String openId = userDTO.getOpenId();
            List<UserVo> userVoList = userDao.queryUserInfoByOpenIdOrMobile(openId, userDTO.getPhone(), thirdNameEnum.getCode(), UnionId, dingUserId);

            if (!CollectionUtils.isEmpty(userVoList)) {
                if (userVoList.size() == 1) {
                    userVo = userVoList.get(0);
                } else {
                    // 正常情况下， 这里的 userVoList size 为2 即 手机号 openId 分别查询出了一条
                    // 这种情况下属于 手机号是一个账号， openId 被绑定到另一个账号上的情况
                    // 找出这个 openId 所在的账号
                    userVo = userVoList.stream().filter(it -> Objects.equals(openId, it.getDingOpenId())).findAny()
                            .orElseThrow(() -> new SvnlanRuntimeException("未找到 openId =>" + openId + "的账号"));

                }
                // 关联用户
                if (!ObjectUtils.isEmpty(userVo)){
                    userVo.setDingUnionId(UnionId);
                    userVo.setDingOpenId(openId);
                    userVo.setDingUserId(dingUserId);
                    userVo.setPhone(ObjectUtils.isEmpty(userVo.getPhone()) ? userDTO.getPhone() : userVo.getPhone());
                    userManageService.associationUser(" createDingUser 钉盘同步关联用户", userVo);
                }
            }else {
                String k = GlobalConfig.ding_create_user_openId_phone_key + openId + "_" + userDTO.getPhone();
                boolean c = stringRedisTemplate.opsForValue().setIfAbsent(k, "1" ,10, TimeUnit.MINUTES);
                if (!c){
                    LogUtil.error("用户正在创建中 openId="+ openId + "  phone="+ userDTO.getPhone());
                    return null;
                }
                userVo = new UserVo();
                ThirdUserInitializeConfig initializeConfig = getInitializeConfig(thirdNameEnum);

                // {"groupID":1,"name":"sdfsdfsdf","nickname":"asdfadsf","password":"asdf23","sizeMax":1,"roleID":3,"groupInfo":[]}
                userDTO.setGroupID(1L);
                //String mobile = userDTO.getPhone();
                // 明文密码， 后面会加密
                String pwd = RandomStringUtils.randomAlphanumeric(6);
                userDTO.setPassword(pwd);
                //userDTO.setPassword(mobile.substring(mobile.length() - 6));
                // 用户的配额
                userDTO.setSizeMax(initializeConfig.getSizeMax());
                userDTO.setPwdState("0");
                // 用户角色
                userDTO.setRoleID(initializeConfig.getRoleID());
                userDTO.setSex(1);
                // 代表管理员
                userDTO.setCreateUser(1L);
                // 从配置里读取需要关联的用户部门
                List<Long> groupIds = initializeConfig.getGroupInfo().stream().map(ThirdUserInitializeConfig.GroupInfoDTO::getGroupID).collect(Collectors.toList());
                if (!CollectionUtils.isEmpty(groupIds)) {
                    // 查询 group
                    List<Group> groupInfoList = groupDaoImpl.getGroupInfoList(groupIds);
                    Assert.notEmpty(groupInfoList, "未查询到组织");
                    Assert.isTrue(Objects.equals(groupInfoList.size(), groupIds.size()), "存在未查询到的组织");

                    List<UserGroupVo> userGroupVoList = initializeConfig.getGroupInfo().stream().map(it -> {
                        UserGroupVo userGroupVo = new UserGroupVo();
                        userGroupVo.setGroupID(it.getGroupID());
                        userGroupVo.setAuthID(it.getAuthID());
                        return userGroupVo;
                    }).collect(Collectors.toList());

                    userDTO.setGroupInfo(userGroupVoList);
                }
                userDTO.setTenantId(tenantId);
                userDTO.setOpenIdType(Integer.parseInt(thirdNameEnum.getCode()));
                Long userId = userManageService.addThirdUser(" 钉盘同步创建用户", userDTO);
                userVo.setUserID(userId);
                userVo.setName(userDTO.getName());
                userVo.setNickname(userDTO.getNickname());
                userVo.setAvatar(userDTO.getAvatar());
                userVo.setSex(userDTO.getSex());
            }
        }catch (Exception e){
            LogUtil.error(e, " createDingUser 创建用户失败");
        }

        return userVo;
    }


    public String getAccessToken(Long tenantId){
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig(tenantId);
        String appKey = dingConfigVo.getAppKey();
        String appSecret = dingConfigVo.getAppSecret();
        return DingUtil.refreshDingToken(stringRedisTemplate, appKey, appSecret);
    }

    public DingConfigVo getDingConfig(Long tenantId){
        DingConfigVo dingConfigVo = optionTool.getDingDingConfig(tenantId);
        if(dingConfigVo == null){
            return null;
        }
        String appKey = dingConfigVo.getAppKey();
        String appSecret = dingConfigVo.getAppSecret();
        dingConfigVo.setAccessToken(DingUtil.refreshDingToken(stringRedisTemplate, appKey, appSecret));
        return dingConfigVo;
    }
    /**
     * 获取配置信息
     */
    public ThirdUserInitializeConfig getInitializeConfig(SecurityTypeEnum thirdNameEnum) {
        // 查询钉钉用户的配置
        String thirdLoginConfig = systemOptionDaoImpl.getSystemConfigByKey("thirdLoginConfig");
        if (!StringUtils.hasText(thirdLoginConfig)) {
            return null;
        }
        LogUtil.info("t钉盘同步创建用户 hirdLoginConfig => {}", thirdLoginConfig);
        List<ThirdUserInitializeConfig> thirdUserInitializeConfigList = JSONObject.parseArray(thirdLoginConfig, ThirdUserInitializeConfig.class);
        // 找出钉钉的配置
        return thirdUserInitializeConfigList.stream()
                .filter(it -> thirdNameEnum.getValue().equals(it.getThirdName()))
                .findFirst()
                .orElseThrow(() -> new SvnlanRuntimeException("钉盘同步创建用户 未查询到对应平台初始化配置 不能进行登录或相关操作"));
    }

    public Long getParentIdBySpace(String scene, List<ThirdUserInitializeConfig.DingInfoDTO> dingInfoDTOList, Long userId, Long tenantId){
        Long parentId = 0L;
        if (DingSpaceTypeEnum.org.getValue().equals(scene) || DingSpaceTypeEnum.co.getValue().equals(scene)){
            for (ThirdUserInitializeConfig.DingInfoDTO dingInfoConfig :dingInfoDTOList){
                if ("teamDoc".equals(dingInfoConfig.getTypeName())){
                    parentId = dingInfoConfig.getSourceId();
                }
            }
        }else if (DingSpaceTypeEnum.group.getValue().equals(scene)){
            for (ThirdUserInitializeConfig.DingInfoDTO dingInfoConfig :dingInfoDTOList){
                if ("groupFiles".equals(dingInfoConfig.getTypeName())){
                    parentId = dingInfoConfig.getSourceId();
                }
            }
        }else if (DingSpaceTypeEnum.hideOrgEmp.getValue().equals(scene)){
            for (ThirdUserInitializeConfig.DingInfoDTO dingInfoConfig :dingInfoDTOList){
                if ("myDoc".equals(dingInfoConfig.getTypeName())){
                    String spaceSourceIDStr = ioSourceMetaDaoImpl.getSourceIDMetaByKey(userId + "", "mySpace", null);

                    long mySpaceId = Long.parseLong(spaceSourceIDStr);
                    HomeExplorerVO homeExplorerVO = null;
                    Integer storageId = storageService.getDefaultStorageDeviceId();
                    homeExplorerVO = new HomeExplorerVO();
                    homeExplorerVO.setTargetID(userId);
                    homeExplorerVO.setParentID(mySpaceId);
                    homeExplorerVO.setName(dingInfoConfig.getPath());
                    homeExplorerVO.setIsSafe(0);
                    IOSource source = optionTool.addDir(homeExplorerVO, tenantId, storageId);
                    parentId = source.getId();
                }
            }
        }
        return parentId;
    }
    /**
     * @Description: 刷新储存订阅
     * @params:  []
     * @Return:  void
     * @Modified:
     */
    @Scheduled(cron = "${schedule.refreshSubscribe}")
    public void refreshSubscribe() throws Exception {
        Map<Long,List<UserVo>> tenantUsers = dingAboutDao.getDingUsers(null);
        for (Long tenantId:tenantUsers.keySet()) {
            enableSubscription(tenantId, tenantUsers);
        }
    }

    public void enableSubscription(Long tenantId, Map<Long,List<UserVo>> tenantUsers) throws Exception{
        DingConfigVo dingConfigVo = getDingConfig(tenantId);
        if(dingConfigVo != null && dingConfigVo.getAccessToken() != null){
            for (UserVo user:tenantUsers.get(tenantId)) {
                boolean success = DingUtil.dingtalkstorageSubscribeEvent(dingConfigVo.getAccessToken(),user.getDingUnionId(),dingConfigVo.getCorpId());
                if(success){
                    LogUtil.info("租户"+dingConfigVo.getCorpId()+"用户"+user.getDingUnionId()+"订阅存储事件成功");
                    break;
                }
            }
        }
    }

    public void subscribe(Long tenantId) throws Exception {
        Map<Long,List<UserVo>> tenantUsers = dingAboutDao.getDingUsers(tenantId);
        for (Long tenant:tenantUsers.keySet()) {
            DingConfigVo dingConfigVo = getDingConfig(tenant);
            if(dingConfigVo != null && dingConfigVo.getAccessToken() != null){
                for (UserVo user:tenantUsers.get(tenantId)) {
                    boolean success = DingUtil.dingtalkstorageSubscribeEvent(dingConfigVo.getAccessToken(),user.getDingUnionId(),dingConfigVo.getCorpId());
                    if(success){
                        LogUtil.info("租户"+dingConfigVo.getCorpId()+"用户"+user.getDingUnionId()+"订阅存储事件成功"+dingConfigVo.getAccessToken());
                        break;
                    }else{
                        LogUtil.info("租户"+dingConfigVo.getCorpId()+"用户"+user.getDingUnionId()+"订阅存储事件失败"+dingConfigVo.getAccessToken());
                    }
                }
            }
        }
    }
    public List<JSONObject> getDepartmentList(Long deptId, Long tenantId) {
        List<JSONObject> list = null;
        String accessToken = this.getAccessToken(tenantId);

        JSONObject jsonObj = null;
        try {
            jsonObj = DingUtil.getDepartmentList(accessToken, deptId);
        }catch (Exception e){
            LogUtil.error(e, "获取部门列表失败 deptId=" + deptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)) {
            LogUtil.info("获取部门列表getDepartmentList" + jsonObj);
            String listStr = jsonObj.getString("result");
            list = JsonUtils.jsonToList(listStr, JSONObject.class);

        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }

        return list;
    }

    // 获取企业部门下用户列表
    public JSONObject getDepartmentUserList(Long deptId, Long tenantId, long cursor, long size, String taskId) {
        JSONObject re = null;
        String accessToken = this.getAccessToken(tenantId);
        JSONObject jsonObj = null;
        try {
            jsonObj = DingUtil.getDepartmentUserList(accessToken, deptId, cursor, size);
        }catch (Exception e){
            LogUtil.error(e, "获取部门列表失败 deptId=" + deptId);
            return null;
            //throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }

        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)) {
            LogUtil.info("获取部门用户列表 getDepartmentUserList" + jsonObj);
            String result = jsonObj.getString("result");
            re = JSONObject.parseObject(result);
        } else {
            String error = jsonObj.getString("errmsg");
            stringRedisTemplate.opsForValue().set(GlobalConfig.pull_error_key + taskId, (ObjectUtils.isEmpty(error) ? "获取部门用户列表失败" : error), 1, TimeUnit.HOURS);

            return null;
            //throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return re;
    }

    public List<JSONObject> getDepartmentUserForeachList(Long deptId, Long tenantId, long cursor, long size, String taskId){
        JSONObject result = this.getDepartmentUserList(deptId, tenantId, cursor, size, taskId);
        if (ObjectUtils.isEmpty(result)){
            return null;
        }
        String listStr = result.getString("list");
        List<JSONObject> list = null;
        try {
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        }catch (Exception e){
        }
        if (CollectionUtils.isEmpty(list) || list.size() <= 0){
            return null;
        }
        Boolean hasMore = result.getBoolean("has_more");
        if (hasMore){
            Long subCursor = result.getLongValue("next_cursor");
            List<JSONObject> subList = getDepartmentUserForeachList(deptId, tenantId, subCursor, size, taskId);
            if (!CollectionUtils.isEmpty(subList)) {
                list.addAll(subList);
            }
        }
        return list;
    }

    public JSONObject getSchoolDepartmentList(Long deptId, Long pageSize, Long pageNo, Long tenantId) {
        JSONObject re = null;
        String accessToken = this.getAccessToken(tenantId);
        JSONObject jsonObj = null;
        try {
            jsonObj = DingUtil.getSchoolDepartmentList(accessToken, deptId, pageSize, pageNo);
        }catch (Exception e){
            LogUtil.error(e, "获取部门列表失败 deptId=" + deptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)) {
            LogUtil.info("获取家校通部门列表 getSchoolDepartmentList" + jsonObj);
            String listStr = jsonObj.getString("result");
            re = JSONObject.parseObject(listStr);
        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return re;
    }

    public List<JSONObject> getSchoolDepartmentForeachList(Long deptId, Long tenantId, Long pageNo, Long pageSize){
        JSONObject result = this.getSchoolDepartmentList(deptId, pageSize, pageNo, tenantId);
        if (ObjectUtils.isEmpty(result)){
            return null;
        }
        String listStr = result.getString("details");
        List<JSONObject> list = null;
        try {
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        }catch (Exception e){
        }
        if (CollectionUtils.isEmpty(list) || list.size() <= 0){
            return null;
        }
        Boolean hasMore = result.getBoolean("has_more");
        if (hasMore){
            List<JSONObject> subList = getSchoolDepartmentForeachList(deptId, tenantId, pageNo + 1, pageSize);
            if (!CollectionUtils.isEmpty(subList)) {
                list.addAll(subList);
            }
        }
        return list;
    }


    public List<JSONObject> getSchoolDeptUserForeachList(Long deptId, Long tenantId, Long pageNo, Long pageSize){
        JSONObject result = this.getSchoolDeptUserList(deptId, pageSize, pageNo, tenantId);
        if (ObjectUtils.isEmpty(result)){
            return null;
        }
        String listStr = result.getString("details");
        List<JSONObject> list = null;
        try {
            list = JsonUtils.jsonToList(listStr, JSONObject.class);
        }catch (Exception e){
        }
        if (CollectionUtils.isEmpty(list) || list.size() <= 0){
            return null;
        }
        Boolean hasMore = result.getBoolean("has_more");
        if (hasMore){
            List<JSONObject> subList = getSchoolDeptUserForeachList(deptId, tenantId, pageNo + 1, pageSize);
            if (!CollectionUtils.isEmpty(subList)) {
                list.addAll(subList);
            }
        }
        return list;
    }

    public JSONObject getSchoolDeptUserList(Long deptId, Long pageSize, Long pageNo, Long tenantId) {
        JSONObject re = null;
        String accessToken = this.getAccessToken(tenantId);
        JSONObject jsonObj = null;
        try {
            jsonObj = DingUtil.getDepartmentSchoolUserList(accessToken, deptId, "teacher", pageSize, pageNo);
        }catch (Exception e){
            LogUtil.error(e, "获取部门列表失败 deptId=" + deptId);
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
        }
        if (!ObjectUtils.isEmpty(jsonObj) && Objects.equals(jsonObj.getInteger("errcode"), 0)) {
            LogUtil.info("获取家校通部门列表 getDepartmentSchoolUserList" + jsonObj);
            String listStr = jsonObj.getString("result");
            re = JSONObject.parseObject(listStr);
        } else {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode(), jsonObj.getString("errmsg"), null);
        }
        return re;
    }


    private CommonSource createParentFolder(Long topParentId, String dingDentryIdStr, String appKey, String appSecret, String unionId
            , String spaceId,List<CommonSource> newFolderList, int i, Long userId, Long tenantId, String scene, JSONObject eventJson){
        GetDentryResponseBody dingSourceBody = DingUtil.getDingSourceInfo(stringRedisTemplate,appKey,appSecret,unionId,spaceId,dingDentryIdStr);

        if (ObjectUtils.isEmpty(dingSourceBody)){
            LogUtil.info(" createParentFolder dingSourceBody is null dingDentryIdStr=" + dingDentryIdStr );
            return fileOptionTool.getSourceInfo(topParentId);
        }
        LogUtil.info(" createParentFolder dingSourceBody=" + JsonUtils.beanToJson(dingSourceBody));
        CommonSource source = fileOptionTool.getSourceInfoByDentryId(dingDentryIdStr);
        if ((!ObjectUtils.isEmpty(source) && source.getIsDelete().intValue() == 1)){
            fileOptionTool.clearUserDentryIdByDentryId(dingDentryIdStr);
            source = null;
        }
        if (!ObjectUtils.isEmpty(source)){
            return addFolderList(source, unionId
                    , spaceId,newFolderList, userId, tenantId, scene, eventJson);
        }else {
            String dingParentIdStr = dingSourceBody.getDentry().getParentId();
            // 创建上层文件夹
            boolean c = stringRedisTemplate.opsForValue().setIfAbsent(GlobalConfig.lock_dingParentId_key + dingParentIdStr, "1", 5, TimeUnit.MINUTES);
            if (!c){
                LogUtil.error( "钉钉同步创建不存在的父文件夹 sub 进行中。。。dingParentIdStr="+ dingParentIdStr);
                return null;
            }
            String name = dingSourceBody.getDentry().getName();
            CommonSource newSource = new CommonSource();
            newSource.setName(name);
            newSource.setSort(i);
            newSource.setDingDentryId(dingDentryIdStr);
            newFolderList.add(newSource);
            if ("0".equals(dingParentIdStr)){
                source = fileOptionTool.getSourceInfo(topParentId);
                return addFolderList(source, unionId
                        , spaceId,newFolderList, userId, tenantId, scene, eventJson);
            }else {
                return createParentFolder(topParentId, dingParentIdStr, appKey, appSecret, unionId
                        , spaceId,newFolderList, i+1,userId, tenantId, scene,eventJson);
            }
        }
    }

    private CommonSource addFolderList(CommonSource parentSource, String unionId
            , String spaceId,List<CommonSource> newFolderList, Long userId, Long tenantId, String scene, JSONObject eventJson){
        if (!CollectionUtils.isEmpty(newFolderList) && newFolderList.size() > 0){
            List<String> sourceNameList = ioSourceDao.getSourceNameList(parentSource.getSourceID());
            newFolderList = newFolderList.stream().sorted(Comparator.comparing(CommonSource::getSort).reversed()).collect(Collectors.toList());
            int j = 0;
            CommonSource c = parentSource;
            for (CommonSource n : newFolderList){
                // 新建文件夹
                eventJson.put("dentryId",n.getDingDentryId());
                c = createDir(n.getName(), j == 0 ? sourceNameList : null, c, n.getDingDentryId(), unionId, spaceId, eventJson
                        , userId, tenantId, scene);
                j ++ ;
            }
            return c;
        }else {
            return parentSource;
        }
    }

    public UserDTO getDingUserInfoByuserid(String dingUserId, String accessToken, String UnionId) {
        UserDTO userDTO = DingUtil.getDingUserInfoByuserid(dingUserId, accessToken);
        if (ObjectUtils.isEmpty(userDTO)){
            // 查不到的用户放入redis排除
            putDingUserErrRedis(UnionId);
        }
        return userDTO;
    }
    public void putDingUserErrRedis(String UnionId){
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        Long hashLength = operations.size(GlobalConfig.getDingUserInfoByuserid_error_key);
        operations.put(GlobalConfig.getDingUserInfoByuserid_error_key, UnionId, UnionId);
        if (hashLength <= 0){
            long t = DateUtil.getTimeDiff();
            stringRedisTemplate.expire(GlobalConfig.getDingUserInfoByuserid_error_key, t, TimeUnit.MINUTES);
        }
    }
    public Boolean checkGetUserError(String UnionId) {
        return stringRedisTemplate.opsForHash().hasKey(GlobalConfig.getDingUserInfoByuserid_error_key, UnionId);
    }
}
