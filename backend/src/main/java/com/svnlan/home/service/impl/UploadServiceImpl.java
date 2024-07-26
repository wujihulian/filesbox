package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.dao.ShareDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.home.dto.*;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.enums.CloudOperateEnum;
import com.svnlan.home.enums.UploadEnum;
import com.svnlan.home.service.BusTypeHandleService;
import com.svnlan.home.service.ConvertFileService;
import com.svnlan.home.service.SourceFileService;
import com.svnlan.home.service.UploadService;
import com.svnlan.home.utils.*;
import com.svnlan.home.vo.CommonSourceVO;
import com.svnlan.home.vo.FileMetaVo;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.tools.UploadAboutTool;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.*;
import com.svnlan.webdav.FileProperties;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static com.svnlan.home.utils.FileUtil.specificExtList;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 9:07
 */
@Service
public class UploadServiceImpl implements UploadService {

    @Resource
    BusTypeHandleService busTypeHandleService;
    @Resource
    SystemSortTool systemSortTool;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    IoSourceHistoryDao ioSourceHistoryDao;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    ShareDao shareDao;
    @Resource
    ShareTool shareTool;
    @Resource
    UserAuthTool userAuthTool;

    @Resource
    FileContentTool fileContentTool;
    @Resource
    StorageService storageService;
    @Resource
    AsyncDoMergeFileUtil asyncDoMergeFileUtil;
    @Resource
    ConvertFileService convertFileService;
    @Resource
    SourceHistoryUtil sourceHistoryUtil;
    @Resource
    SourceFileService sourceFileService;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    UploadAboutTool uploadAboutTool;


    @Value("${cdn.domain}")
    private String cdnDomain;
    @Value("${environment.type}")
    private String environmentType;

    /**
     * 上传文件主流程
     *
     * @Description:
     * @params: [uploadDTO]
     * @Return: com.svnlan.upload.enumpack.UploadEnum
     * @Modified:
     */
    @Override
    public CommonSourceVO upload(UploadDTO uploadDTO, LoginUser loginUser, CommonSource commonSource) {

        // 参数错误
        if (ObjectUtils.isEmpty(uploadDTO) || ObjectUtils.isEmpty(uploadDTO.getBusType())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String prefix = "上传：" + uploadDTO.getTaskID()+ " ";
        String busType = uploadDTO.getBusType();
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(busType);
        if (busTypeEnum == null) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 上传前的操作
        HomeExplorerVO disk = beforeUpload(commonSource, uploadDTO, loginUser, busTypeEnum);

        String uuid = uploadDTO.getTaskID();
        String domain = commonSource.getDomain();
        Long sourceId = 0L;
        CommonSourceVO commonSourceVO = new CommonSourceVO();
        commonSourceVO.setState(0);
        uploadDTO.setTenantId(loginUser.getTenantId());
        MultipartFile file = uploadDTO.getFile();
        if (file == null) {
            commonSourceVO.setMsg(UploadEnum.fileEmpty.getMsg());
            return commonSourceVO;
        }
        if (ObjectUtils.isEmpty(uploadDTO.getSize())){
            uploadDTO.setSize(file.getSize());
        }
        commonSource.setSize(uploadDTO.getSize());
        // 判断容量是否足够
        fileOptionTool.checkMemory(disk, commonSource.getSize(), false, uploadDTO.getTaskID());
        //传参覆盖文件名 (小程序分片场景不能改文件名)
        if (!StringUtil.isEmpty(uploadDTO.getOverrideName())) {
            try {
                Field field = file.getClass().getDeclaredField("filename");
                field.setAccessible(true);
                field.set(file, uploadDTO.getOverrideName());
            } catch (Exception e) {
                LogUtil.error(e, prefix + "upload 设置名称出错, " + uploadDTO.getOverrideName());
            }
        }

        // 验证文件名及后缀
        validateUploadFileName(file.getOriginalFilename());
        // 对不同上传业务做验证
        doByBusType(uploadDTO, true, commonSource, busTypeEnum, disk);
        //是否是分段
        Boolean isDoChunk = true;
        Boolean isDone = false;
        Boolean isEndPart = false;
        String checksum = uploadDTO.getHashMd5();
        boolean update = false;
        boolean isRepeat = false;
        // 记录插入的sourceId
        String uploadSourceIdKey = String.format(GlobalConfig.upload_md5_captcha_sourceId,uploadDTO.getTenantId(),checksum);
        String lockRedisKey = String.format(GlobalConfig.upload_md5_captcha_merge, uploadDTO.getTenantId(),uploadDTO.getTaskID());
        String lockMergeCheckKey = String.format(GlobalConfig.upload_md5_captcha_merge_check, uploadDTO.getTenantId(),checksum);
        // 记录上传完成的分片
        String uploadKey = String.format(GlobalConfig.upload_md5_captcha, uploadDTO.getTenantId(), checksum);
        Boolean lockMergeSuccess = false;
        String md5SourceId = stringRedisTemplate.opsForValue().get(uploadSourceIdKey);
        CommonSource s = null;
        if (!ObjectUtils.isEmpty(md5SourceId) && !"0".equals(md5SourceId)){
            // 如果缓存的sourceId被删除
            s = fileOptionTool.getSourceInfo(Long.valueOf(md5SourceId));
            if (ObjectUtils.isEmpty(s)
                    || (!ObjectUtils.isEmpty(s.getIsDelete()) && s.getIsDelete().intValue() == 1)){
                md5SourceId = null;
                stringRedisTemplate.delete(uploadSourceIdKey);
                stringRedisTemplate.opsForHash().getOperations().expire(uploadKey, 1, TimeUnit.MILLISECONDS);
            }
        }
        UploadStateDTO uploadStateDTO = new UploadStateDTO();
        uploadStateDTO.setChecksum(checksum);
        //非分段，直接上传的类型
        if (uploadDTO.getChunk() == null || uploadDTO.getChunks() == null || uploadDTO.getChunks().equals(0) || uploadDTO.getChunks().equals(1)) {
            commonSource = this.doSaveDirect(uploadDTO, commonSource);
            isDoChunk = false;
            isDone = true;
        } else {
            // 是否是最后一个分片
            isEndPart = (uploadDTO.getChunk().intValue() >= (uploadDTO.getChunks() - 1)) ? true : false;
            //保存临时文件
            if (!file.isEmpty()) {
                uploadStateDTO = this.doSaveTemp(uploadDTO);
            }
            if (isEndPart){
                LogUtil.info(checksum + "，最后一个分片日志：uploadStateDTO=" + JsonUtils.beanToJson(uploadStateDTO) );
            }

            //全部分段完成，执行合并操作
            if (uploadStateDTO.getUpState() && uploadStateDTO.getPartAllDone()) {
                Boolean isSuccess = stringRedisTemplate.opsForValue().setIfAbsent(lockRedisKey, "1");
                LogUtil.info(prefix + "查询lockRedisKey="+ isSuccess + "，lockRedisKey"+lockRedisKey+ "，md5SourceId="+ md5SourceId );
                if(isSuccess) { //若成功
                    md5SourceId = stringRedisTemplate.opsForValue().get(uploadSourceIdKey);
                    // 合并锁
                    lockMergeSuccess = stringRedisTemplate.opsForValue().setIfAbsent(lockMergeCheckKey, uuid);
                    LogUtil.info(prefix + "，lockMergeSuccess="+ lockMergeSuccess  + "，md5SourceId="+ md5SourceId + "，uploadSourceIdKey=" + uploadSourceIdKey);
                    if (!lockMergeSuccess){
                        commonSourceVO.setMsg(UploadEnum.fileUploading.getMsg());
                        return commonSourceVO;
                        /*String oldUUID = stringRedisTemplate.opsForValue().get(lockMergeCheckKey);
                        String lockExecKey = String.format(GlobalConfig.lock_upload_md5_captcha, uploadDTO.getTenantId(),oldUUID);
                        String old = stringRedisTemplate.opsForValue().get(lockExecKey);
                        if (ObjectUtils.isEmpty(old)){
                            md5SourceId = stringRedisTemplate.opsForValue().get(uploadSourceIdKey);
                            if(!ObjectUtils.isEmpty(md5SourceId) && "0".equals(md5SourceId)){
                                md5SourceId = null;
                            }
                        }*/
                    }
                    stringRedisTemplate.expire(lockMergeCheckKey, 8, TimeUnit.MINUTES);
                    if (ObjectUtils.isEmpty(md5SourceId)) {
                        stringRedisTemplate.opsForValue().set(uploadSourceIdKey, "0", 24, TimeUnit.HOURS);
                        commonSource = this.doMergeFile(uploadStateDTO, commonSource, prefix);
                        isDone = true;
                        this.stringRedisTemplate.expire(lockRedisKey, 24, TimeUnit.HOURS);
                    }else {
                        if("0".equals(md5SourceId)){
                            isRepeat = true;
                            md5SourceId = sourceHistoryUtil.getRepeatMd5SourceId(uploadDTO.getStartTime(), stringRedisTemplate, uploadSourceIdKey);
                        }
                        if(!"0".equals(md5SourceId)){
                            commonSource = fileOptionTool.getSourceInfo(Long.valueOf(md5SourceId));
                            commonSource.setName(uploadDTO.getName());
                            commonSource.setParentID(ObjectUtils.isEmpty(uploadDTO.getSourceID()) ? 0L : uploadDTO.getSourceID());
                            commonSource.setDomain(domain);
                            update = true;
                            isDone = true;
                        }
                    }
                }
            }
        }

        //完成合并或直接上传，添加数据 && !StringUtil.isEmpty(commonSource.getHashMd5())
        if (isDone && commonSource != null) {
            // 同名处理
            // 资源相关信息写入数据库
            commonSource.setTenantId(loginUser.getTenantId());
            if (update){
                commonSource.setUserID(loginUser.getUserID());
                sourceId = recordSourceDataToDbUpdate(commonSource, uploadDTO, busTypeEnum);
            }else {
                sourceId = recordSourceDataToDb(commonSource, uploadDTO, busTypeEnum);
                stringRedisTemplate.opsForValue().set(uploadSourceIdKey, commonSource.getSourceID()+"", 24, TimeUnit.HOURS);
                md5SourceId = stringRedisTemplate.opsForValue().get(uploadSourceIdKey);
                LogUtil.info("000 md5SourceId="+ md5SourceId + ",uploadSourceIdKey=" + uploadSourceIdKey);
            }
        }

        boolean finalSuccess = false;
        //设置返回前端的值
        if (sourceId != null && !sourceId.equals(0L)) {
            finalSuccess = true;
            commonSourceVO.setSourceID(commonSource.getSourceID());
            commonSourceVO.setFileID(sourceId);
            commonSourceVO.setFileType(commonSource.getFileType());
            commonSourceVO.setName(commonSource.getName());
            commonSourceVO.setHashMd5(commonSource.getHashMd5());
            commonSourceVO.setPath(commonSource.getPath());
            commonSourceVO.setSourceLength(commonSource.getSourceLength());
            commonSourceVO.setIsM3u8(commonSource.getIsM3u8());
            //返回path
            commonSourceVO.setPath(FileUtil.returnPath(commonSourceVO.getPath(), uploadDTO.getBusType(), commonSource.getThumb()));
            LogUtil.info("上传成功等待返回, " + JsonUtils.beanToJson(commonSourceVO) + ", " + uploadDTO.getChunk());
            uploadDTO.setAsync(true);
            //根据业务类型，处理后续
            this.doByBusType(uploadDTO, false, commonSource, busTypeEnum, disk);

            //
            commonSourceVO.setThumb(commonSource.getThumb());
            commonSourceVO.setResolution(commonSource.getResolution());

            //处理返回预览URL
            // this.doPreviewUrlByBusType(commonSource, busTypeEnum, commonSourceVO);
        }
        //分段上传的
        if (isDoChunk && uploadStateDTO.getUpState() && !uploadStateDTO.getPartAllDone()) {
            commonSourceVO.setState(1);
        } else if (isDoChunk && uploadStateDTO.getUpState() && uploadStateDTO.getCheckExist()) {
            commonSourceVO.setState(1);
        } else {
            stringRedisTemplate.delete(uploadSourceIdKey);
            //合并或者直接上传的
            commonSourceVO.setState(sourceId == null || sourceId.equals(0L) ? 0 : 1);
            commonSourceVO.setRemark(sourceId == null || sourceId.equals(0L) ? "保存数据失败" : "");
            stringRedisTemplate.delete(GlobalConfig.checkMemory_key );
        }
        if (!isRepeat && lockMergeSuccess){
            // 分段上传完成后
            if (isDoChunk && isDone && !update) {
                if (ObjectUtils.isEmpty(sourceId) || sourceId <= 0) {
                    commonSourceVO.setState(0);
                    deleteChunkFile(busType, checksum, "");
                    String md5SourceId2 = stringRedisTemplate.opsForValue().get(uploadSourceIdKey);
                    if (!ObjectUtils.isEmpty(md5SourceId2) && "0".equals(md5SourceId2)) {
                        stringRedisTemplate.delete(uploadSourceIdKey);
                    }
                    stringRedisTemplate.opsForHash().getOperations().expire(uploadKey, 1, TimeUnit.MILLISECONDS);
                }
            }
        }
        if (finalSuccess) {
            LogUtil.info("上传再看次VO的数据" + JsonUtils.beanToJson(commonSourceVO));
        }
        // 图片内容安全校验
        /*if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSourceVO.getSourceSuffix())
                && BusTypeEnum.HOMEPAGE_IMAGE.getBusType().equals(busType)){
            Map<String, Object> paramMap = new HashMap<>(0);
            /** 只检查图片* /
            setImgSecCheckValue(commonSourceVO, paramMap);
            wxTool.imgSecCheck(" 资讯图片内容安全校验 ", paramMap);
        }*/

        if (isDone && !ObjectUtils.isEmpty(commonSource) && !ObjectUtils.isEmpty(commonSource.getSourceID())) {
            /** 操作日志 */
            Map<String, Object> reMap = null;
            List<Map<String, Object>> paramList = new ArrayList<>();
            reMap = new HashMap<>(4);
            reMap.put("sourceID", commonSource.getSourceID());
            reMap.put("sourceParent", commonSource.getParentID());
            if (!ObjectUtils.isEmpty(commonSource.getParentLevel())) {
                reMap.put("sourceParentLevel", commonSource.getParentLevel());
            }
            reMap.put("type", "upload");
            reMap.put("status", commonSourceVO.getState());
            reMap.put("remark", ObjectUtils.isEmpty(commonSourceVO.getRemark()) ? "" : commonSourceVO.getRemark());
            reMap.put("userID", loginUser.getUserID());
            reMap.put("pathName", commonSource.getName());
            paramList.add(reMap);
            systemLogTool.setSysLog(loginUser, LogTypeEnum.fileUpload.getCode(), paramList, systemLogTool.getRequest());
        }
        if (!ObjectUtils.isEmpty(commonSource.getCheckMerge()) && commonSource.getCheckMerge()) {

            String serverUrl = HttpUtil.getRequestRootUrl(null);
            commonSource.setDomain(serverUrl);
            commonSource.setUserID(loginUser.getUserID());
            asyncDoMergeFileUtil.asyncDoMergeFileUtil(uploadStateDTO, commonSource, commonSourceVO, uploadDTO.getBusType());
        }
        return commonSourceVO;
    }

    /**
     * 上传前的操作
     */
    @Override
    public HomeExplorerVO beforeUpload(CommonSource commonSource, UploadDTO uploadDTO, LoginUser loginUser, BusTypeEnum busTypeEnum) {

        //设置默认值
        fileOptionTool.setDefault(commonSource, loginUser);
        commonSource.setSourceType(busTypeEnum.getTypeCode());
        uploadDTO.setIgnoreFileSize(0.0);
        commonSource.setGroupID(0L);
        HomeExplorerVO disk = null;
        if (busTypeEnum.equals(BusTypeEnum.CLOUD)) {
            if (ObjectUtils.isEmpty(uploadDTO.getSourceID())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            /** 获取企业云盘 */
            disk = systemSortTool.getUserSpaceSize(loginUser.getUserID(), uploadDTO.getSourceID());
            uploadDTO.setIgnoreFileSize(disk.getIgnoreFileSize());
            commonSource.setGroupID(disk.getGroupID());
            commonSource.setTargetType(disk.getTargetType());
            // 权限校验
            userAuthTool.checkGroupDocAuth(loginUser, uploadDTO.getSourceID(), disk.getParentLevel(), "5", disk.getTargetType());
        }

        commonSource.setParentID(ObjectUtils.isEmpty(uploadDTO.getSourceID()) ? 0L : uploadDTO.getSourceID());
        commonSource.setUserID(loginUser.getUserID());
        return disk;
    }

    /**
     * 资源相关信息写入数据库
     */
    @Override
    public Long recordSourceDataToDb(CommonSource commonSource, UploadDTO uploadDTO, BusTypeEnum busTypeEnum) {
        List<String> sourceNameList = ioSourceDao.getSourceNameList(uploadDTO.getSourceID());
        commonSource.setName(fileOptionTool.checkRepeatName(commonSource.getName(), commonSource.getName(), commonSource.getFileType(), sourceNameList, 1));
        //验证学生文件大小
        this.doBeforeInsert(uploadDTO, commonSource);
        Long sourceId = this.addData(commonSource, busTypeEnum);
        LogUtil.info("上传入库完成, " + sourceId + "@" + JsonUtils.beanToJson(commonSource));
        return sourceId;
    }

    public Long recordSourceDataToDbUpdate(CommonSource commonSource, UploadDTO uploadDTO, BusTypeEnum busTypeEnum) {
        List<String> sourceNameList = ioSourceDao.getSourceNameList(uploadDTO.getSourceID());
        commonSource.setName(fileOptionTool.checkRepeatName(commonSource.getName(), commonSource.getName(), commonSource.getFileType(), sourceNameList, 1));
        //验证学生文件大小
        //this.doBeforeInsert(uploadDTO, commonSource);
        Long sourceId = this.addCheckData(commonSource, commonSource.getUserID());
        LogUtil.info("上传入库完成, " + sourceId + "@" + JsonUtils.beanToJson(commonSource));
        return sourceId;
    }

    /**
     * 验证文件名及后缀
     */
    @Override
    public void validateUploadFileName(String fileName) {
        if (fileName == null) {
            // 文件名不符合规范, 请修改
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        /*if (fileName.startsWith(".")) {
            // 文件名不符合规范, 请修改
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }*/
        //验证后缀
        //String suffix = FileUtil.getFileExtension(fileName);
        // 后缀无限制 20240221
        /*if (StringUtil.isEmpty(suffix)) {
            // 文件名不符合规范, 请修改
            //throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        } else {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]", Pattern.CASE_INSENSITIVE);
            // 文件名不符合规范, 请修改
            if (pattern.matcher(suffix).find()) {
                // 有可能是 tar.gz 这样的后缀
                if (specificExtList.stream().noneMatch(it -> it.equals(suffix))) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }
            }
        }*/
    }

    /**
     * 试着转码
     */
    @Override
    public void tryToConvertFile(CommonSourceVO commonSourceVO, CommonSource commonSource, String busType) {
        convertFileService.tryToConvertFile(commonSourceVO, commonSource, busType);
    }

    @Override
    public CommonSourceVO fileUpload(UploadDTO uploadDTO, LoginUser loginUser, CommonSource commonSourceRe) {
        // 参数错误
        if (ObjectUtils.isEmpty(uploadDTO) || ObjectUtils.isEmpty(uploadDTO.getBusType()) || !StringUtil.isNumeric(uploadDTO.getPath())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        String prefix = "fileUpload 上传：" + uploadDTO.getTaskID() + " ";
        String busType = uploadDTO.getBusType();
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(busType);
        if (busTypeEnum == null) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        MultipartFile file = uploadDTO.getFile();
        CommonSourceVO commonSourceVO = new CommonSourceVO();
        if (file == null || file.getSize() <= 0) {
            commonSourceVO.setMsg(UploadEnum.fileEmpty.getMsg());
            return commonSourceVO;
        }
        if (file.getOriginalFilename() == null) {
            // 文件名不符合规范, 请修改
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }
        long size = file.getSize();
        /*if (file.getOriginalFilename().startsWith(".")) {
            // 文件名不符合规范, 请修改
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        }*/
        CommonSource commonSource = fileOptionTool.getSourceInfo(Long.parseLong(uploadDTO.getPath()));
        if (ObjectUtils.isEmpty(commonSource)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, commonSource.getSourceID(), commonSource.getParentLevel(), "8", commonSource.getTargetType());

        commonSourceRe.setSourceType(busTypeEnum.getTypeCode());
        commonSource.setDomain(commonSourceRe.getDomain());
        commonSource.setSourceType(busTypeEnum.getTypeCode());
        commonSource.setIsM3u8(0);
        // 添加历史记录 sourceID, userID,fileID, size, detail
        Long userId = commonSource.getUserID();
        IoSourceHistory orgHistory = ioSourceHistoryDao.getHistoryInfoByFileId(commonSource.getSourceID(), commonSource.getFileID());
        if (!ObjectUtils.isEmpty(orgHistory)){
            userId = orgHistory.getUserID();
        }

        IoSourceHistory ioSourceHistory = new IoSourceHistory();
        ioSourceHistory.setSourceID(commonSource.getSourceID());
        ioSourceHistory.setUserID(ObjectUtils.isEmpty(userId) ? loginUser.getUserID() : userId);
        ioSourceHistory.setFileID(commonSource.getFileID());
        ioSourceHistory.setSize(commonSource.getSize());
        ioSourceHistory.setDetail("");

        commonSource.setHashMd5(null);
        String checksum = uploadDTO.getHashMd5();

        boolean isEndPart = false;

        commonSourceVO.setState(0);
        uploadDTO.setIgnoreFileSize(0.0);
        uploadDTO.setSourceID(commonSource.getParentID());
        commonSource.setGroupID(0L);
        commonSource.setUserID(loginUser.getUserID());
        HomeExplorerVO disk = null;
        if (busTypeEnum.equals(BusTypeEnum.CLOUD)) {
            if (ObjectUtils.isEmpty(uploadDTO.getSourceID())) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            /** 获取企业云盘 */
            disk = systemSortTool.getUserSpaceSize(loginUser.getUserID(), uploadDTO.getSourceID());
            uploadDTO.setIgnoreFileSize(disk.getIgnoreFileSize());
            commonSource.setGroupID(disk.getGroupID());
            commonSource.setTargetType(disk.getTargetType());
        }
        //验证
        fileOptionTool.checkMemory(disk, size);

        //验证后缀
        //String suffix = FileUtil.getFileExtension(file.getOriginalFilename());
        // 不限制文件后缀 20240221
        /*if (StringUtil.isEmpty(suffix)) {
            // 文件名不符合规范, 请修改
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
        } else {
            Pattern pattern = Pattern.compile("[^a-zA-Z0-9]", Pattern.CASE_INSENSITIVE);
            // 文件名不符合规范, 请修改
            if (pattern.matcher(suffix).find()) {
                throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
            }
        }*/
        /*if (!commonSource.getFileType().toLowerCase().equals(suffix.toLowerCase())){
            if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSource.getFileType().toLowerCase()) && Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(suffix.toLowerCase())){
            }else if (Arrays.asList("doc", "docx").contains(commonSource.getFileType().toLowerCase()) && Arrays.asList("doc", "docx").contains(suffix.toLowerCase())){
            }else if (Arrays.asList("ppt", "pptx").contains(commonSource.getFileType().toLowerCase()) && Arrays.asList("ppt", "pptx").contains(suffix.toLowerCase())){
            }else if (Arrays.asList("xls", "xlsx").contains(commonSource.getFileType().toLowerCase()) && Arrays.asList("xls", "xlsx").contains(suffix.toLowerCase())){
            }else {
                throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
            }
        }*/
        boolean isDone = false;
        Boolean isDoChunk = true;
        uploadDTO.setUpdateTmpPath("update/");
        uploadDTO.setTenantId(loginUser.getTenantId());


        UploadStateDTO uploadStateDTO = new UploadStateDTO();
        //非分段，直接上传的类型
        if (uploadDTO.getChunk() == null || uploadDTO.getChunks() == null || uploadDTO.getChunks().equals(0) || uploadDTO.getChunks().equals(1)) {
            isDone = true;
            isDoChunk = false;
            commonSource = this.doSaveDirect(uploadDTO, commonSource);
        } else {
            // 是否是最后一个分片
            isEndPart = (uploadDTO.getChunk().intValue() >= (uploadDTO.getChunks() - 1)) ? true : false;
            //保存临时文件
            if (!file.isEmpty()) {
                uploadStateDTO = this.doSaveTemp(uploadDTO);
            }
            if (isEndPart){
                LogUtil.info(checksum + "，最后一个分片日志 2：uploadStateDTO=" + JsonUtils.beanToJson(uploadStateDTO) );
            }
            //全部分段完成，执行合并操作
            if (uploadStateDTO.getUpState() && uploadStateDTO.getPartAllDone()) {
                boolean isDoMerge = stringRedisTemplate.opsForValue().setIfAbsent("fileUpload_doMerge_" + checksum, "1", 10, TimeUnit.MINUTES);
                if (isDoMerge) {
                    commonSource = this.doMergeFile(uploadStateDTO, commonSource, prefix);
                    stringRedisTemplate.expire("fileUpload_doMerge_" + checksum, 1, TimeUnit.MILLISECONDS);
                    isDone = true;
                }
            }
        }


        Long sourceId = 0L;
        //完成合并或直接上传，添加数据
        if (isDone && commonSource != null && !StringUtil.isEmpty(commonSource.getHashMd5())) {
            //验证文件大小
            this.doBeforeInsert(uploadDTO, commonSource);
            sourceId = this.updateData(commonSource);
            LogUtil.info("上传入库完成, " + sourceId + "@" + JsonUtils.beanToJson(commonSource));
        }

        boolean finalSuccess = false;
        //设置返回前端的值
        if (sourceId != null && !sourceId.equals(0L)) {

            commonSourceVO.setState(1);
            finalSuccess = true;
            commonSourceVO.setSourceID(commonSource.getSourceID());
            commonSourceVO.setFileID(sourceId);
            commonSourceVO.setFileType(commonSource.getFileType());
            commonSourceVO.setName(commonSource.getName());
            commonSourceVO.setHashMd5(commonSource.getHashMd5());
            commonSourceVO.setPath(commonSource.getPath());
            commonSourceVO.setSourceLength(commonSource.getSourceLength());
            commonSourceVO.setIsM3u8(commonSource.getIsM3u8());
            //返回path
            commonSourceVO.setPath(FileUtil.returnPath(commonSourceVO.getPath(), uploadDTO.getBusType(), commonSource.getThumb()));
            LogUtil.info("上传成功等待返回, " + JsonUtils.beanToJson(commonSourceVO) + ", " + uploadDTO.getChunk());
            uploadDTO.setAsync(true);
            //根据业务类型，处理后续
            this.doByBusType(uploadDTO, false, commonSource, busTypeEnum, disk);
            //
            commonSourceVO.setThumb(commonSource.getThumb());
            commonSourceVO.setResolution(commonSource.getResolution());
            // 删除
            String uploadKey = String.format(GlobalConfig.upload_md5_captcha, uploadDTO.getTenantId(), checksum);
            stringRedisTemplate.opsForHash().getOperations().expire(uploadKey, 1, TimeUnit.MILLISECONDS);

            if (isDone) {
                //  添加历史记录 保证size正确
                sourceHistoryUtil.changeCheckSourceHistory(new IoSourceHistory(commonSourceVO.getSourceID(),commonSourceVO.getFileID(),loginUser.getUserID(),size), ioSourceHistory);

            }
        }
        // 分段上传完成后
        if (isDoChunk && isDone){
            if (ObjectUtils.isEmpty(sourceId) || sourceId <= 0) {
                commonSourceVO.setState(0);
                deleteChunkFile(busType, checksum, "update/");
            }
        }
        if (finalSuccess) {
            LogUtil.info("fileUpload 上传再看次VO的数据" + JsonUtils.beanToJson(commonSourceVO));
        }
        if (isDone) {
            // 添加文档操作event
            fileOptionTool.addSourceEvent(commonSource.getSourceID(), commonSource.getParentID(), loginUser.getUserID(), commonSource.getName(), EventEnum.uploadNew);

            /** 操作日志 */
            Map<String, Object> reMap = null;
            List<Map<String, Object>> paramList = new ArrayList<>();
            reMap = new HashMap<>(4);
            reMap.put("sourceID", commonSource.getSourceID());
            reMap.put("sourceParent", commonSource.getParentID());
            reMap.put("type", "editFile");
            reMap.put("userID", loginUser.getUserID());
            reMap.put("pathName", commonSource.getName());
            paramList.add(reMap);
            systemLogTool.setSysLog(loginUser, LogTypeEnum.fileEdit.getCode(), paramList, systemLogTool.getRequest());
        }

        BeanUtils.copyProperties(commonSource, commonSourceRe);

        if (!ObjectUtils.isEmpty(commonSource.getCheckMerge()) && commonSource.getCheckMerge()) {
            commonSource.setUserID(loginUser.getUserID());
            asyncDoMergeFileUtil.asyncDoMergeFileUtil(uploadStateDTO, commonSource, commonSourceVO, uploadDTO.getBusType());
        }
        return commonSourceVO;
    }

    /**
     * @Description: 文件信息入库
     * @params: [uploadStateDTO]
     * @Return: java.lang.Boolean
     * @Modified:
     */
    private Long addData(CommonSource commonSource, BusTypeEnum busTypeEnum) {

        if (!ObjectUtils.isEmpty(commonSource.getParentID()) && commonSource.getParentID() > 0) {
            CommonSource parentSource = fileOptionTool.getSourceInfo(commonSource.getParentID());
            if (ObjectUtils.isEmpty(parentSource)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            commonSource.setParentLevel(parentSource.getParentLevel() + commonSource.getParentID() + ",");
            commonSource.setTargetType(parentSource.getTargetType());
        } else {
            commonSource.setParentLevel(",0,");
            commonSource.setTargetType(0);
            if (BusTypeEnum.checkIsInfoType(busTypeEnum.getBusType())) {
                commonSource.setTargetType(3);
            }
        }
        Long sourceId;
        try {
            fileOptionTool.addCommonSource(commonSource.getUserID(), commonSource, EventEnum.upload, commonSource.getFileID());
            sourceId = commonSource.getFileID();
        } catch (Exception e) {
            LogUtil.error(e, "source入库失败 1" + JsonUtils.beanToJson(commonSource));
            return 0L;
        }
        return sourceId;
    }

    private Long addCheckData(CommonSource commonSource, Long opUserId) {

        if (!ObjectUtils.isEmpty(commonSource.getParentID()) && commonSource.getParentID() > 0) {
            CommonSource parentSource = fileOptionTool.getSourceInfo(commonSource.getParentID());
            if (ObjectUtils.isEmpty(parentSource)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            commonSource.setParentLevel(parentSource.getParentLevel() + commonSource.getParentID() + ",");
            commonSource.setTargetType(parentSource.getTargetType());
        } else {
            commonSource.setParentLevel(",0,");
            commonSource.setTargetType(0);
        }
        Long sourceId;
        try {
            IOSource source = fileOptionTool.addIoSourceDetail(commonSource, opUserId, commonSource.getFileID(), EventEnum.upload);
            sourceId = commonSource.getFileID();
        } catch (Exception e) {
            LogUtil.error(e, "source入库失败" + JsonUtils.beanToJson(commonSource));
            return 0L;
        }
        return sourceId;
    }


    private Long updateData(CommonSource commonSource) {
        Long sourceId;
        try {
            fileOptionTool.updateCommonSource(commonSource.getUserID(), commonSource);
            sourceId = commonSource.getFileID();
        } catch (Exception e) {
            LogUtil.error(e, "source fileUpload updateData入库失败" + JsonUtils.beanToJson(commonSource));
            return 0L;
        }
        return sourceId;
    }


    /**
     * @Description: 入库前的一些处理
     * @params: [commonSource]
     * @Return: void
     * @Modified:
     */
    private void doBeforeInsert(UploadDTO uploadDTO, CommonSource commonSource) {
        //MP3长度
        if (commonSource.getFileType().equals("mp3")) {
            Integer length = VideoUtil.getVideoLength(commonSource.getPath());
            commonSource.setSourceLength(length);
        }
        //加上媒体号短视频
        if (StringUtil.isEmpty(commonSource.getThumb())
                || uploadDTO.getBusType().equals(BusTypeEnum.CLOUD.getTypeCode())
                || uploadDTO.getBusType().equals(BusTypeEnum.HOMEPAGE_VIDEO.getTypeCode())
        ) {
            busTypeHandleService.doForWareAndAttachment(uploadDTO, false, commonSource);
        }
        //图片
        if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSource.getFileType())) {
            //分辨率
            String resolution = ImageUtil.getResolution(commonSource.getPath());
            commonSource.setResolution(resolution);
        } else if (Arrays.asList(GlobalConfig.CAMERA_TYPE_ARR).contains(commonSource.getFileType())) {
            // 相机文件

        } else if (commonSource.getResolution() == null) {
            commonSource.setResolution("");
        }
    }

    @Resource
    private FileProperties fileProperties;

    @Resource
    private Environment environment;

    /**
     * @Description: 非分段，直接保存文件
     * @params: [uploadDTO]
     * @Return: com.svnlan.upload.domain.CommonSource
     * @Modified:
     */
    private CommonSource doSaveDirect(UploadDTO uploadDTO, CommonSource commonSource) {

        MultipartFile file = uploadDTO.getFile();
        if (file == null) {
            return null;
        }
        String busType = uploadDTO.getBusType();
        String finalTopPath = null;
        //最终文件目录路径
        if (Arrays.asList("image","avatar").contains(busType)){
            finalTopPath = PropertiesUtil.getUpConfig(busType + ".savePath");
        }else {
            String defaultPath = storageService.getDefaultStorageDevicePath();
            finalTopPath = defaultPath + PropertiesUtil.getUpConfig(busType + ".savePath");
        }

        if (Arrays.asList(environment.getActiveProfiles()).contains("local")) {
            // 表示是我本机环境的话
            finalTopPath = fileProperties.getFilePathRoot();
        }
        //基础路径+日期路径
        String finalFolderPath = finalTopPath + FileUtil.getDatePath();
        LogUtil.info("doSaveDirect finalFolderPath=" + finalFolderPath);
        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("创建目录失败, path:" + finalFolderPath);
//                return null;
            }
        }
        String originalFilename = file.getOriginalFilename();
        //文件后缀
        String fileExtension = "";
        if (!ObjectUtils.isEmpty(uploadDTO.getExt()) && "drawio".equals(uploadDTO.getExt())) {
            fileExtension = "drawio";
            originalFilename = originalFilename.replaceAll(".txt", ".drawio");
        } else {
            fileExtension = FileUtil.getFileExtension(FileUtil.removeIllegalSymbol(originalFilename));
        }
        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + RandomUtil.getuuid() + System.currentTimeMillis()
                + "_" + commonSource.getUserID() + "." + fileExtension;
        //最终文件
        File finalFile = new File(finalFilePath);
        String serverChecksum = "";
        FileInputStream fis = null;
        try {
            this.writeFile(finalFile, file.getBytes());
            fis = new FileInputStream(finalFile);
            if (!ObjectUtils.isEmpty(uploadDTO.getHashMd5())) {
                serverChecksum = uploadDTO.getHashMd5();
            } else if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(fileExtension)) {
                serverChecksum = DigestUtils.md5DigestAsHex(fis);
            } else {
                commonSource.setNeedHashMd5(1);
            }
            //serverChecksum = DigestUtils.md5DigestAsHex(fis);
            fis.close();
        } catch (IOException e) {
            LogUtil.error(e.getMessage(), " 上传文件失败");
            return null;
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        }
        //  手机拍照后缀，用于校验竖屏拍照上传照片旋转问题
        /*if (Arrays.asList(GlobalConfig.IMAGE_TYPE_PHOTOGRAPH).contains(fileExtension)) {
            String filePath = finalFolderPath + "rotate_"
                    + RandomUtil.getuuid() + System.currentTimeMillis()
                    + "_" + commonSource.getUserID() + "." + fileExtension;
            finalFilePath = ImageUtil.RotateImg(finalFilePath, filePath, true);
        }*/
        commonSource.setName(originalFilename);
        Integer typeCode = BusTypeEnum.getFromTypeName(busType).getTypeCode();
        commonSource.setSourceType(typeCode);
        commonSource.setSize(file.getSize());
        commonSource.setHashMd5(serverChecksum);
        //commonSource.setPath(StringUtil.replaceFirst(finalFilePath, finalTopPath, ""));
        commonSource.setPath(finalFilePath);
        //todo 文件后缀太长
        commonSource.setFileType(fileExtension);
        LogUtil.info("doSaveDirect commonSource=" + JsonUtils.beanToJson(commonSource));
        return commonSource;
    }

    /**
     * @Description: 根据业务类型做处理
     * @params: [uploadDTO, isBefore, commonSource, busTypeEnum]
     * @Return: void
     * @Author: sulijuan
     * @Date: 2023/2/16 10:14
     * @Modified:
     */
    @Override
    public void doByBusType(UploadDTO uploadDTO, boolean isBefore, CommonSource commonSource, BusTypeEnum busTypeEnum, HomeExplorerVO disk) {
        if (isBefore) {

        } else if (!isBefore) {
            /** 更新 企业云盘 memory */
            if (!ObjectUtils.isEmpty(disk)) {
                fileOptionTool.updateMemory(commonSource);
            }
        }

        switch (busTypeEnum) {
            case IMAGE:
            case AVATAR:
                busTypeHandleService.doForImage(uploadDTO, isBefore, commonSource);
                break;
            case CLOUD:
                busTypeHandleService.doForCloud(uploadDTO, isBefore, commonSource);
                break;
            default:
                break;
        }
        //额外的缩略图处理
        if (uploadDTO.getThumbSize() != null && !isBefore) {
            busTypeHandleService.doForExtraImage(uploadDTO, commonSource);
        }
    }

    /**
     * @Description: 文件是否存在
     * @params: [fileName]
     * @Return: java.lang.Boolean
     * @Modified:
     */
    private Boolean fileExists(String fileName) {
        return new File(fileName).exists();
    }

    /**
     * @Description: 写文件
     * @params: [file, fileBytes]
     * @Return: java.lang.Boolean
     * @Modified:
     */
    private Boolean writeFile(File file, byte[] fileBytes) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             BufferedOutputStream out = new BufferedOutputStream(fileOutputStream);) {
            out.write(fileBytes);
            out.flush();
        } catch (IOException e) {
            LogUtil.error(e, "写入文件失败, " + file.getAbsolutePath());
            return false;
        }
        return true;
    }

    /**
     * @Description: 分段路径 md5第一位/md5第二位/
     * @params: [checksum]
     * @Return: java.lang.String
     * @Modified:
     */
    private String getPartPath(String checksum) {
        return checksum.substring(0, 1) + "/" + checksum.substring(1, 2) + "/";
    }

    private String getTempFolderPath(String tempFolderPath, String checksum) {
        return tempFolderPath + getPartPath(checksum) + checksum + "/";
    }

    /**
     * @Description: 执行保存分段文件
     * @params: [uploadDTO]
     * @Return: boolean
     * @Modified:
     */
    private UploadStateDTO doSaveTemp(UploadDTO uploadDTO) {

        String prefix = "doSaveTemp 上传：" + uploadDTO.getTaskID()+ " ";
        UploadStateDTO uploadStateDTO = new UploadStateDTO();
        //完成状态false
        uploadStateDTO.setUpState(false);
        //分段全部完成false
        uploadStateDTO.setPartAllDone(false);
        //业务类型
        String busType = uploadDTO.getBusType();
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(busType);
        if (busTypeEnum == null) {
            return uploadStateDTO;
        }
        //原始文件md5
        String checksum = uploadDTO.getHashMd5();
        Long tenantId = uploadDTO.getTenantId();
        uploadStateDTO.setExt(uploadDTO.getExt());
        //读取临时文件路径配置
        uploadStateDTO.setBusType(busType);
        String defaultPath = storageService.getDefaultStorageDevicePath();
        String tempFolderPath = defaultPath + PropertiesUtil.getUpConfig(busType + ".savePath") + "tmp/"
                + ( !ObjectUtils.isEmpty(uploadDTO.getUpdateTmpPath()) ? uploadDTO.getUpdateTmpPath() : "");

        //临时文件目录：基础路径+分段路径   基础路径/md5第一位/md5第二位/
        tempFolderPath = this.getTempFolderPath(tempFolderPath, checksum);
        LogUtil.info(prefix + checksum + ", doSaveTemp tempFolderPath="+ tempFolderPath);
        try {
            File folder = new File(tempFolderPath);
//            System.out.println(tempFolderPath);
            //新建目录
            if (!folder.exists()) {
                if (!folder.mkdirs()) {
                    LogUtil.error(prefix + "创建目录失败 path:" + tempFolderPath);
//                    return uploadStateDTO;
                }
            }
            //当前分段号
            Integer curChunk = uploadDTO.getChunk();
            //md5_分段号.part
            String tempFileName = checksum + "_" + curChunk + ".part";

            File tempFile = new File(tempFolderPath + tempFileName);

            //todo 20240130 分片生成后记录到redis，当所有分片完成时再合并
            String uploadKey = String.format(GlobalConfig.upload_md5_captcha, tenantId, checksum);
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();

            uploadStateDTO.setCheckExist(false);
            String checkExist = operations.get(uploadKey, uploadDTO.getChunk() + "");
            // checkExits = 0 则分片正在写入
            LogUtil.info(prefix + "查看文件分片是否存在 chunks = " + uploadDTO.getChunks() + " checkExist="+checkExist+",uploadDTO.getChunk()"+uploadDTO.getChunk()+",path=" + tempFile.getPath());
            if (!ObjectUtils.isEmpty(checkExist) && Arrays.asList("0","1").contains(checkExist)){
                uploadStateDTO.setPartAllDone("1".equals(checkExist) ? checkClone(uploadDTO, uploadKey) : false);
                uploadStateDTO.setUpState(true);
                uploadStateDTO.setCheckExist("1".equals(checkExist) ? true : false);
                return uploadStateDTO;
            }
            //
            boolean c = operations.putIfAbsent(uploadKey, uploadDTO.getChunk() + "", "0");
            LogUtil.info(prefix + "文件分片不存在 c=" + c + "  checkExist="+checkExist+",uploadDTO.getChunk()"+uploadDTO.getChunk()+",path=" + tempFile.getPath());
            if (!c){
                uploadStateDTO.setPartAllDone(false);
                uploadStateDTO.setUpState(true);
                uploadStateDTO.setCheckExist(false);
                return uploadStateDTO;
            }

            MultipartFile file = uploadDTO.getFile();
            //写入文件
            Boolean writeSuccess = this.writeFile(tempFile, file.getBytes());

            if (!writeSuccess) {
                LogUtil.error(prefix + "写入文件失败");
                return uploadStateDTO;
            }
            operations.put(uploadKey, uploadDTO.getChunk() + "", "1");
            operations.getOperations().expire(uploadKey, 24, TimeUnit.HOURS);

            //分段数
            Integer chunks = uploadDTO.getChunks();
            //分段是否全部完成
            Boolean allDone = checkClone(uploadDTO, uploadKey);
            if (allDone) {
                String doneName = tempFolderPath + "done.txt";
                allDone = this.lockForDone(doneName);
            }
            LogUtil.info(prefix + checksum +"，allDone:" + allDone.toString() + ", chunks=" + uploadDTO.getChunks() + ", chunk=" + uploadDTO.getChunk());
            if (allDone) {
                uploadStateDTO.setChecksum(checksum);
                uploadStateDTO.setTempPath(tempFolderPath);
                uploadStateDTO.setChunks(chunks);
                uploadStateDTO.setFileName(FileUtil.removeIllegalSymbol(file.getOriginalFilename()));
            }
            uploadStateDTO.setPartAllDone(allDone);
            uploadStateDTO.setUpState(true);
            return uploadStateDTO;

        } catch (Exception e) {
            LogUtil.error(e, prefix + "保存temp失败");
        }
        return uploadStateDTO;
    }
    private Boolean checkClone(UploadDTO uploadDTO, String uploadKey){
        Set<Object> keyList = stringRedisTemplate.opsForHash().keys(uploadKey);
        if (!CollectionUtils.isEmpty(keyList)) {
            LogUtil.info("上传：" +uploadDTO.getTaskID() + " " + uploadDTO.getHashMd5() + ",查看分片是否都存在 chunks=" + uploadDTO.getChunks() + ", chunk=" + uploadDTO.getChunk()+
                    ",size=" + keyList.size() +":"+(keyList.size() == uploadDTO.getChunks())+"， checkClone keyList=" + JsonUtils.beanToJson(keyList));
            if (keyList.size() >= uploadDTO.getChunks().intValue()) {
                return true;
            }
        }
        return false;
    }

    /**
     * @Description: 文件锁，防止2次合并
     * @params: [fileName]
     * @Return: java.lang.Boolean
     * @Modified:
     */
    private Boolean lockForDone(String fileName) {
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                if (!file.createNewFile()) {
                    LogUtil.error("创建done文件失败");
                    return false;
                }
            }

            try (RandomAccessFile fis = new RandomAccessFile(file, "rw");
                 FileChannel fCin = fis.getChannel();
                 FileLock fLin = fCin.tryLock()) {
                if (fLin == null) {
                    LogUtil.error("获取锁失败, " + fileName);
                    return false;
                }
                byte[] buf = new byte[1024];
//            StringBuilder sb = new StringBuilder();
                String doneContent = "";
                if ((fis.read(buf)) != -1) {
                    doneContent = new String(buf, "utf-8");
                }
                boolean trueDone = false;
                if ("".equals(doneContent) || "done".equals(doneContent)) {
                    doneContent = "done";
                    fis.write(doneContent.getBytes("utf-8"));
                    trueDone = true;
                }
                return trueDone;
            }
        } catch (Exception e) {
            LogUtil.error(e, "done处理失败");
        }
        return false;
    }

    /**
     * @Description: 合并临时文件
     * @params: [uploadStateDTO]
     * @Return: com.svnlan.common.dto.UploadStateDTO
     * @Modified:
     */
    private CommonSource doMergeFile(UploadStateDTO uploadStateDTO, CommonSource commonSource, String prefix) {

        String checksum = uploadStateDTO.getChecksum();
        LogUtil.info(prefix + checksum + "，merge start. time:" + new Date());
        //业务类型
        String busType = uploadStateDTO.getBusType();
        Integer typeCode = BusTypeEnum.getFromTypeName(busType).getTypeCode();
        String tempPath = uploadStateDTO.getTempPath();
        Integer chunks = uploadStateDTO.getChunks();
        //合并状态失败
        if (StringUtil.isEmpty(checksum) || StringUtil.isEmpty(tempPath) || chunks.equals(0)) {
            uploadStateDTO.setMergeState(false);
            return null;
        }
        String defaultPath = storageService.getDefaultStorageDevicePath();
        //最终文件目录路径
        String finalTopPath = defaultPath + PropertiesUtil.getUpConfig(busType + ".savePath");

        //基础路径+平台网校路径+日期路径
        String finalFolderPath = finalTopPath + FileUtil.getDatePath();

        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error(prefix + "创建目录失败, path:" + finalFolderPath);
//                return null;
            }
        }
        //文件后缀
        String fileExtension = "";
        if (!ObjectUtils.isEmpty(uploadStateDTO.getExt()) && "drawio".equals(uploadStateDTO.getExt())) {
            fileExtension = "drawio";
            uploadStateDTO.setFileName(uploadStateDTO.getFileName().replaceAll(".txt", ".drawio"));
        } else {
            fileExtension = FileUtil.getFileExtension(uploadStateDTO.getFileName());
        }

        //最终文件路径  最终文件目录路径+毫秒+.后缀
        String finalFilePath = finalFolderPath
                + FileUtil.getRealFileName(commonSource.getUserID(), fileExtension);
        //最终文件  ---------------- 更改为异步合并 异步调用则放入库后：上一层

        if (!Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource)){
            //最终文件
            File finalFile = new File(finalFilePath);
            //临时文件数组
            File[] tempFiles = new File[chunks];
            try (FileChannel finalFileChannel = new FileOutputStream(finalFile, true).getChannel();) {
                StringBuilder tempFilePath;
                //将临时文件合并到最终文件
                LogUtil.info(checksum + "，merge break1. time:" + new Date());
                for (int i = 0; i < chunks; i++) {
                    //拼接临时文件路径
                    tempFilePath = new StringBuilder();
                    tempFilePath.append(tempPath);
                    tempFilePath.append(checksum);
                    tempFilePath.append("_");
                    tempFilePath.append(i);
                    tempFilePath.append(".part");
                    //通过FileChannel
                    tempFiles[i] = new File(tempFilePath.toString());
                    try (FileChannel tempFileChannel = new FileInputStream(tempFiles[i]).getChannel();) {
                        tempFileChannel.transferTo(0, tempFileChannel.size(), finalFileChannel);
                    }
                }
                LogUtil.info(prefix + checksum + "，merge break2. time:" + new Date());
                //设置合并状态为true
                uploadStateDTO.setMergeState(true);
            } catch (Exception e) {
                LogUtil.error(e, prefix + checksum + "，合并文件失败" + JsonUtils.beanToJson(commonSource));
                return null;
            }finally {
                LogUtil.info(prefix + checksum + "，合并文件 finally" + JsonUtils.beanToJson(commonSource) + "，uploadStateDTO=" + JsonUtils.beanToJson(uploadStateDTO));
            }

            LogUtil.info(prefix + checksum + "，merge break3. time:" + new Date());
            commonSource.setSize(finalFile.length());
            //  手机拍照后缀，用于校验竖屏拍照上传照片旋转问题
            /*if (Arrays.asList(GlobalConfig.IMAGE_TYPE_PHOTOGRAPH).contains(fileExtension)) {
                String filePath = finalFolderPath + "rotate_"
                        + RandomUtil.getuuid() + System.currentTimeMillis()
                        + "_" + commonSource.getUserID() + "." + fileExtension;
                finalFilePath = ImageUtil.RotateImg(finalFilePath, filePath, true);
            }*/
            //删除临时文件
            for (int i = 0; i < chunks; i++) {
                tempFiles[i].delete();
            }
            try {
                new File(tempPath + "done.txt").delete();
            } catch (Exception e){
                LogUtil.error(e, prefix + checksum + "，删除done文件失败");
            }
        }else {
            commonSource.setCheckMerge(true);
        }

        try {
            //设置合并状态为true
            uploadStateDTO.setMergeState(true);
            //最终合并文件md5
            LogUtil.info(prefix + checksum + "，merge break4.5time:" + new Date());
            commonSource.setHashMd5("");
            if (!ObjectUtils.isEmpty(uploadStateDTO.getChecksum())) {
                commonSource.setHashMd5(uploadStateDTO.getChecksum());
            } else {
                commonSource.setNeedHashMd5(1);
            }

            //todo 最终md5与前端传过来的对比,考虑是否要失败
            //todo 23-05-31 视频 md5通过前端入参，如果不入参则异步修改

            //设置文件名，相对路径，md5
            commonSource.setName(uploadStateDTO.getFileName());
            // commonSource.setPath(StringUtil.replaceFirst(finalFilePath, finalTopPath, ""));
            commonSource.setPath(finalFilePath);
            commonSource.setFileType(fileExtension);
            commonSource.setSourceType(typeCode);
            LogUtil.info(prefix + checksum + "，file info : " + JsonUtils.beanToJson(commonSource));

        } catch (Exception e) {
            LogUtil.error(e, prefix + checksum + "，合并文件失败" + JsonUtils.beanToJson(commonSource));
            return null;
        }
        LogUtil.info(prefix + checksum + "，merge finished. time:" + new Date());
        return commonSource;
    }

    /**
     *
     * @Description: 验证文件是否存在，通过md5
     * @params: [checksum]     * @Return: com.svnlan.upload.enumpack.UploadEnum
     * @Modified:
     */
    @Override
    public Map<String, Object> checkFile(CheckFileDTO checkFileDTO, LoginUser loginUser) {
        checkFileDTO.setTenantId(loginUser.getTenantId());
        Map<String, Object> resultMap = new HashMap<>(2);
        // 参数错误
        if (ObjectUtils.isEmpty(checkFileDTO) || ObjectUtils.isEmpty(checkFileDTO.getHashMd5()) || ObjectUtils.isEmpty(checkFileDTO.getSize())
                || ObjectUtils.isEmpty(checkFileDTO.getSourceID())) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        //验证业务类型
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(checkFileDTO.getBusType());
        if (busTypeEnum == null) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        if (Arrays.asList("image","avatar").contains(checkFileDTO.getBusType())){
            return this.reUploadMap(resultMap, checkFileDTO);
        }

        String busType = busTypeEnum.getBusType();
        /** 获取企业云盘 */
        HomeExplorerVO disk = systemSortTool.getUserSpaceSize(loginUser.getUserID(), checkFileDTO.getSourceID());
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, checkFileDTO.getSourceID(), disk.getParentLevel(), "5", disk.getTargetType());


        LocalDateTime time = null;
        if (Arrays.asList("dev", "itest").contains(environmentType) ) {
            time = LocalDateTime.ofInstant(Instant.ofEpochSecond(1678723200,0), ZoneId.systemDefault());
        }
        Map<String, Long> sourceNameMap =  ioSourceDao.getSourceNameListJson(checkFileDTO.getSourceID());

        List<String> sourceNameList = ObjectUtils.isEmpty(sourceNameMap) ? null : new ArrayList<>(sourceNameMap.keySet()) ;
        //  sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceID());
        if (!ObjectUtils.isEmpty(sourceNameMap) && sourceNameMap.containsKey(checkFileDTO.getName())){
            Long id = sourceNameMap.get(checkFileDTO.getName());
            // 获取文件同名上传设置类型，fileRepeat 1 覆盖 2 重命名 3 跳过 getUserUploadFileRepeatTypeRedis(userId)
            String uploadFileRepeat = systemSortTool.getUserUploadFileRepeatTypeRedis(loginUser.getUserID());
            if ("1".equals(uploadFileRepeat)){
                checkFileDTO.setPath(String.valueOf(id));
                return checkFileReplace(checkFileDTO, loginUser);
            }else if ("3".equals(uploadFileRepeat)){
                resultMap.put("fileExists", true);
                resultMap.put("chunkList", new ArrayList<>());
                resultMap.put("sourceID", id);
                // 3 跳过
                return resultMap;
            }
        }
        CommonSource commonSource = null;
        try {
            commonSource = fileOptionTool.selectByChecksum(checkFileDTO.getHashMd5(), checkFileDTO.getSize(), time);
        }catch (Exception e){
            LogUtil.error(e, "获取秒传数据失败");
        }
        if (commonSource == null) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        long createTime = commonSource.getCreateTime();
        Long d = System.currentTimeMillis();
        if (String.valueOf(createTime).length() == 10){
            createTime = createTime * 1000;
        }
        long hour = DateUtil.getHourDifference(createTime, d);
        LogUtil.info("秒传校验转码：hour=" + hour + "，name=" + checkFileDTO.getFileName()+ "，md5=" + checkFileDTO.getHashMd5());
        boolean checkHour = false;
        if (hour > 24){
            checkHour = true;
        }
        if (checkHour){
            int isM3u8 = ObjectUtils.isEmpty(commonSource.getIsM3u8()) ? 0 : commonSource.getIsM3u8();
            if ((isM3u8 <= 0 && Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType()))) {
                return this.reUploadMap(resultMap, checkFileDTO);
            }
            int isAudio = ObjectUtils.isEmpty(commonSource.getIsH264Preview()) ? 0 : commonSource.getIsH264Preview();
            if ((isAudio <= 0 && Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(commonSource.getFileType()))) {
                return this.reUploadMap(resultMap, checkFileDTO);
            }
        }
        if (Arrays.asList("dev", "itest").contains(environmentType) && commonSource.getCreateTime() < 1678723200) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        String finalFilePath = commonSource.getPath();
        if (!fileExists(finalFilePath)) {
            LogUtil.error("checkFile 下载文件不存在" + finalFilePath);
            return this.reUploadMap(resultMap, checkFileDTO);
        }

        fileOptionTool.checkMemory(disk, checkFileDTO.getSize());

        commonSource.setUserID(loginUser.getUserID());
        /** 获取企业云盘 */
        UploadDTO uploadDTO = new UploadDTO();
        checkFileDTO.setIgnoreFileSize(disk.getIgnoreFileSize());
        uploadDTO.setChunk(checkFileDTO.getChunk());
        uploadDTO.setChunks(checkFileDTO.getChunks());

        commonSource.setTargetType(disk.getTargetType());
        if (BusTypeEnum.checkIsInfoType(busTypeEnum.getBusType())) {
            commonSource.setTargetType(3);
        }
        resultMap.put("fileExists", true);
        resultMap.put("chunkList", new ArrayList<>());
        CommonSource newSource = new CommonSource();
        newSource.setGroupID(disk.getGroupID());
        newSource.setFileID(commonSource.getFileID());
        //设置默认值
        fileOptionTool.setDefault(newSource, loginUser);
        //云盘验证
        if (busTypeEnum.equals(BusTypeEnum.CLOUD)) {
            checkFileDTO.setBusTypeCode(BusTypeEnum.CLOUD.getTypeCode().toString());
        }
        commonSource.setSourceLength(0);
        if (!ObjectUtils.isEmpty(commonSource.getValue())) {
            try {
                FileMetaVo fileMetaVo = JsonUtils.jsonToBean(commonSource.getValue(), FileMetaVo.class);
                if (!ObjectUtils.isEmpty(fileMetaVo)) {
                    if (!ObjectUtils.isEmpty(fileMetaVo.getLength()) && fileMetaVo.getLength() > 0) {
                        commonSource.setSourceLength(fileMetaVo.getLength());
                    }
                    commonSource.setThumb(ObjectUtils.isEmpty(fileMetaVo.getThumb()) ? "" : fileMetaVo.getThumb());
                    commonSource.setAppPreviewUrl(ObjectUtils.isEmpty(fileMetaVo.getAppViewUrl()) ? "" : fileMetaVo.getAppViewUrl());
                    commonSource.setH264Path(ObjectUtils.isEmpty(fileMetaVo.getH264Path()) ? "" : fileMetaVo.getH264Path());
                    commonSource.setResolution(ObjectUtils.isEmpty(fileMetaVo.getResolution()) ? "" : fileMetaVo.getResolution());
                    commonSource.setPreviewUrl(ObjectUtils.isEmpty(fileMetaVo.getViewUrl()) ? "" : fileMetaVo.getViewUrl());
                    commonSource.setYzEditData(fileMetaVo.getYzEditData());
                    commonSource.setYzViewData(fileMetaVo.getYzViewData());
                    commonSource.setOexeContent(ObjectUtils.isEmpty(fileMetaVo.getOexeContent()) ? "" : fileMetaVo.getOexeContent());
                }
            } catch (Exception e) {
                LogUtil.error(e, " jsonToBean error fileMeta desc value=" + commonSource.getValue());
            }
        }
        try {
            //MP3长度
            if (!ObjectUtils.isEmpty(commonSource.getFileType()) && commonSource.getFileType().equals("mp3") && commonSource.getSourceLength().equals(0)) {
                Integer length = VideoUtil.getVideoLength(commonSource.getPath());
                newSource.setSourceLength(length);
            } else {
                newSource.setSourceLength(commonSource.getSourceLength());
            }
        } catch (Exception e) {
            newSource.setSourceLength(0);
            LogUtil.error(e, " mp3 value=" + commonSource.getValue());
        }

        newSource.setParentID(checkFileDTO.getSourceID());
        newSource.setParentLevel(disk.getParentLevel());
        newSource.setUserID(loginUser.getUserID());
        newSource.setHashMd5(commonSource.getHashMd5());
        newSource.setPath(commonSource.getPath());
        if (!ObjectUtils.isEmpty(checkFileDTO.getName())) {
            newSource.setName(checkFileDTO.getName());
            newSource.setFileType(FileUtil.getFileExtension(checkFileDTO.getName()));
        } else if (!ObjectUtils.isEmpty(checkFileDTO.getFileName())) {
            newSource.setName(checkFileDTO.getFileName());
            newSource.setFileType(FileUtil.getFileExtension(checkFileDTO.getFileName()));
        } else {
            newSource.setName(commonSource.getName());
            newSource.setFileType(commonSource.getFileType());
        }
        String firstPath = FileUtil.getFirstStorageDevicePath(newSource.getPath());
        String defaultFirstPath = storageService.getDefaultStorageDevicePath();

        newSource.setName(fileOptionTool.checkRepeatName(newSource.getName(), newSource.getName(), newSource.getFileType(), sourceNameList, 1));
        newSource.setSourceType(busTypeEnum.getTypeCode());
        newSource.setSize(commonSource.getSize());
        newSource.setIsPreview(commonSource.getIsPreview());
        newSource.setIsM3u8(commonSource.getIsM3u8());
        newSource.setAppPreview(commonSource.getAppPreview());
        newSource.setAppPreviewUrl(commonSource.getAppPreviewUrl());
        newSource.setPreviewUrl(commonSource.getPreviewUrl());
        newSource.setThumbSize(commonSource.getThumbSize());
        newSource.setConvertSize(commonSource.getConvertSize());
        //将原文件的视频转码信息直接拿来置下
        newSource.setH264Path(commonSource.getH264Path());
        newSource.setIsH264Preview(commonSource.getIsH264Preview());
        if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(newSource.getFileType())) {
            if (!ObjectUtils.isEmpty(commonSource.getResolution()) && !"0*0".equals(commonSource.getResolution())) {
                newSource.setResolution(commonSource.getResolution());
            } else {
                String resolution = ImageUtil.getResolution(newSource.getPath());
                newSource.setResolution(resolution);
            }
        } else {
            newSource.setResolution(commonSource.getResolution());
        }
        boolean checkPic = false;
        // 已改
        String prePath = PropertiesUtil.getUpConfig(busType + ".savePath");
        String audioPicPath = defaultFirstPath +  "/mu/images/"
                + newSource.getPath().replace("." + newSource.getFileType(), ".jpg").replace(firstPath + prePath, "");
        //缩略图
        newSource.setThumb(commonSource.getThumb());
        if (StringUtil.isEmpty(newSource.getThumb())) {
            if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(newSource.getFileType())) {
                busTypeHandleService.doForImage(null, false, newSource);
                newSource.setThumb((newSource.getPath())
                        .replace(firstPath + "/doc/", firstPath + "/common/doc/")
                        .replace(firstPath + "/attachment/", firstPath + "/common/attachment/")
                        .replace(firstPath + "/private/", firstPath + "/common/")
                );
            } else if (Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(newSource.getFileType())) {
                checkPic = VideoUtil.getAudioPic(newSource.getPath(), audioPicPath);
                if (checkPic) {
                    newSource.setThumb(audioPicPath);
                    File checkPicFile = new File(audioPicPath);
                    if (checkPicFile.exists()){
                        newSource.setThumbSize(ObjectUtils.isEmpty(newSource.getThumbSize()) ? 0L : newSource.getThumbSize());
                        newSource.setThumbSize(newSource.getThumbSize() + checkPicFile.length());
                    }
                }
            }


        }

        String filename = commonSource.getName();
        String suffix = FileUtil.getFileExtension(filename);
        String path = commonSource.getPath();
        //若是视频且分辨率为空时
        if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(suffix)
                && StringUtil.isEmpty(newSource.getResolution())) {
            int[] heightAndWidth = VideoUtil.getHeightAndWidth(path);
            String resolution = "1280*720";
            if (heightAndWidth[0] != 0 && heightAndWidth[1] != 0) {
                resolution = heightAndWidth[1] + "*" + heightAndWidth[0];
            }
            newSource.setResolution(resolution);
        }
        //
        Long sourceId = this.addCheckData(newSource, loginUser.getUserID());

        if (checkPic) {
            convertUtil.setCheckFileOther(checkFileDTO.getHashMd5(), checkFileDTO.getSize(), audioPicPath);
        }

        resultMap.put("sourceID", newSource.getSourceID());
        resultMap.put("fileID", sourceId);
        resultMap.put("resolution", newSource.getResolution());
        resultMap.put("sourcePath", FileUtil.returnPath(newSource.getPath(), busTypeEnum.getBusType(), newSource.getThumb()));
        resultMap.put("thumb", newSource.getThumb());
        //处理返回文档预览URL
        CommonSourceVO commonSourceVO = new CommonSourceVO();
        //
        // this.doPreviewUrlByBusType(newSource, busTypeEnum, commonSourceVO);
        String previewUrl = commonSourceVO.getPath();
        if (!StringUtil.isEmpty(previewUrl)) {
            resultMap.put("sourcePath", previewUrl);
        }
        //第三方预览地址
        String newPreviewUrl = commonSourceVO.getPreviewUrl();
        if (!StringUtil.isEmpty(newPreviewUrl)) {
            resultMap.put("previewUrl", newPreviewUrl);
        }
        // 更新容量
        fileOptionTool.updateMemory(newSource);

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", newSource.getSourceID());
        reMap.put("sourceParent", newSource.getParentID());
        if (!ObjectUtils.isEmpty(newSource.getParentLevel())) {
            reMap.put("sourceParentLevel", newSource.getParentLevel());
        }
        reMap.put("type", "upload");
        reMap.put("status", "1");
        reMap.put("userID", loginUser.getUserID());
        reMap.put("pathName", newSource.getName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileUpload.getCode(), paramList, systemLogTool.getRequest());

        return resultMap;
    }

    private Map<String, Object> reUploadMap(Map<String, Object> resultMap, CheckFileDTO checkFileDTO) {
        resultMap.put("fileExists", false);
        resultMap.put("sourceId", 0);
        resultMap.put("checkFile", JsonUtils.beanToJson(checkFileDTO));
        //获取已上传的分片序号
        List<Integer> chunkList = this.getChunkFileIndex(checkFileDTO);
        resultMap.put("chunkList", chunkList);
        return resultMap;
    }

    private List<Integer> getChunkFileIndex(CheckFileDTO checkFileDTO) {
        Long tenantId = ObjectUtils.isEmpty(checkFileDTO.getTenantId()) ? tenantUtil.getTenantIdByServerName() : checkFileDTO.getTenantId();
        String defaultPath = storageService.getDefaultStorageDevicePath();
        String tempFolderPath = defaultPath + PropertiesUtil.getUpConfig(checkFileDTO.getBusType() + ".savePath") + "tmp/";
        //原始文件md5
        String checksum = checkFileDTO.getHashMd5();
        //临时文件目录：基础路径+分段路径   基础路径/md5第一位/md5第二位/
        tempFolderPath = this.getTempFolderPath(tempFolderPath, checksum);
        LogUtil.info(checksum + ",getChunkFileIndex tempFolderPath="+ tempFolderPath);
        File tmpFileDir = new File(tempFolderPath);
        File[] chunkFiles = tmpFileDir.listFiles();
        List<Integer> chunkList = new ArrayList<>();
        if (chunkFiles == null || chunkFiles.length == 0) {
            return chunkList;
        }
        for (File f : chunkFiles) {
            if (f.length() != GlobalConfig.CHUNK_FILE_SIZE) {
                continue;
            }
            String fileName = f.getName();
            if (fileName.indexOf("done.txt") >= 0){
                try {
                    if (f.exists()){
                        f.delete();
                    }
                } catch (Exception e) {
                    LogUtil.error("秒传删除done.txt");
                }
                continue;
            }
            if (fileName.length() <= 7 || !fileName.substring(fileName.length() - 5).equals(".part")) {
                continue;
            }
            String indexStr = fileName.substring(fileName.indexOf("_") + 1, fileName.length() - 5);
            try {

                chunkList.add(Integer.parseInt(indexStr));
            } catch (Exception e) {
                LogUtil.error("分片上传, 文件名解析错误");
            }
        }

        if (!CollectionUtils.isEmpty(chunkList) && chunkList.size() > 0){
            try {
                String uploadKey = String.format(GlobalConfig.upload_md5_captcha, tenantId, checksum);
                for (Integer c : chunkList) {
                    stringRedisTemplate.opsForHash().putIfAbsent(uploadKey, String.valueOf(c), "1");
                }
            } catch (Exception e) {
                LogUtil.error("分片上传, redis put");
            }
        }

        return chunkList;
    }
    private void deleteChunkFile(String busType, String checksum, String tmp) {
        LogUtil.info(checksum + "分片上传, 未完成时删除");
        String defaultPath = storageService.getDefaultStorageDevicePath();
        String tempFolderPath = defaultPath + PropertiesUtil.getUpConfig(busType + ".savePath") + "tmp/" + tmp;
        //临时文件目录：基础路径+分段路径   基础路径/md5第一位/md5第二位/
        tempFolderPath = this.getTempFolderPath(tempFolderPath, checksum);
        File tmpFileDir = new File(tempFolderPath);
        File[] chunkFiles = tmpFileDir.listFiles();
        if (chunkFiles == null || chunkFiles.length == 0) {
            return;
        }
        for (File f : chunkFiles) {
            try {
                f.delete();
            } catch (Exception e) {
                LogUtil.error(checksum+"分片上传, 未完成时删除失败");
            }
        }
    }

    @Override
    public Map<String, Object> checkChunk(CheckFileDTO checkChunkDTO, LoginUser loginUser) {

        return null;
    }

    @Override
    public String returnSource(CheckFileDTO previewAttachDTO, String passedFileName, HttpServletResponse response) {

        if (previewAttachDTO.getBusType() == null || previewAttachDTO.getBusId() == null) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(previewAttachDTO.getBusType());
        if (busTypeEnum == null) {
            LogUtil.error("业务类型错误");
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        previewAttachDTO.setBusTypeCode(busTypeEnum.getTypeCode().toString());
        CommonSource commonSource = fileOptionTool.getFileAttachment(previewAttachDTO.getBusId());

        if (commonSource == null) {
            // 记录不存在
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String finalFilePath = commonSource.getPath();
        if (!fileExists(finalFilePath)) {
            LogUtil.error("下载文件不存在" + finalFilePath);
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String fileName = passedFileName == null ? commonSource.getName() : passedFileName;
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            fileName = fileName.replaceAll("\\+", "%20");
        } catch (Exception e) {
            LogUtil.error(e, "下载文件，编码失败");
        }
        boolean isSwf = "swf".equals(commonSource.getFileType()) && Boolean.TRUE.equals(previewAttachDTO.getViewSwf());

        try {
            FileUtil.responseFile(response, finalFilePath, fileName, isSwf);
        } catch (Exception e) {
            LogUtil.error(e, "资源获取失败");
        }
        return "";
    }

    @Override
    public Map<String, Object> getPreviewInfo(CheckFileDTO getCloudPreviewDTO) {

        String code = "";
        // 分享
        if (!ObjectUtils.isEmpty(getCloudPreviewDTO.getShareCode())) {
            List<ShareVo> list = shareDao.getShareByCode(getCloudPreviewDTO.getShareCode());
            if (CollectionUtils.isEmpty(list) || list.size() > 1) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorPathTips.getCode());
            }
            ShareVo vo = list.get(0);
            vo.setPassword(null);
            getCloudPreviewDTO.setBusType(BusTypeEnum.CLOUD.getBusType());
            try {
                code = "&shareCode=" + URLEncoder.encode(getCloudPreviewDTO.getShareCode(), "UTF-8");
                shareDao.updateNumView(vo.getShareID(), vo.getNumView() + 1);
            } catch (Exception e) {
                LogUtil.error("getPreviewInfo updateNumView error");
            }
        }else if (!ObjectUtils.isEmpty(getCloudPreviewDTO.getHash())) {
            String[] videoPathOrgArray = fileOptionTool.getArrayAttachmentToken(getCloudPreviewDTO.getHash());
            getCloudPreviewDTO.setSourceID(Long.valueOf(videoPathOrgArray[1]));
            getCloudPreviewDTO.setBusType(BusTypeEnum.CLOUD.getBusType());
        }
        if (ObjectUtils.isEmpty(getCloudPreviewDTO.getSourceID()) || getCloudPreviewDTO.getSourceID() <= 0) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        //验证业务类型
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(getCloudPreviewDTO.getBusType());
        if (busTypeEnum == null) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        getCloudPreviewDTO.setF(ObjectUtils.isEmpty(getCloudPreviewDTO.getF()) ? 0L : getCloudPreviewDTO.getF());
        CommonSource cloudFile = fileOptionTool.getFileAttachment(getCloudPreviewDTO.getSourceID(), getCloudPreviewDTO.getF());

        if (cloudFile == null || ObjectUtils.isEmpty(cloudFile.getName())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        String downloadKey = FileUtil.getDownloadKey();
        String downloadUrl = null;
        if (!ObjectUtils.isEmpty(getCloudPreviewDTO.getF()) && getCloudPreviewDTO.getF() > 0) {
            downloadUrl = "/api/disk/attachment/" + FileUtil.encodeDownloadUrl(cloudFile.getName())
                    + fileOptionTool.getDownloadParam(cloudFile.getSourceID(), downloadKey, code) + "&f=" + getCloudPreviewDTO.getF();
        } else {
            downloadUrl = "/api/disk/attachment/" + FileUtil.encodeDownloadUrl(cloudFile.getName())
                    + fileOptionTool.getDownloadParam(cloudFile.getSourceID(), downloadKey, code);
        }
        String m3u8Key = FileUtil.getM3u8Key();
        Map<String, Object> reMap = new HashMap<>(1);

        String domain = HttpUtil.getRequestRootUrl(null);
        reMap.put("sourceID", cloudFile.getSourceID());
        reMap.put("fileID", cloudFile.getFileID());
        reMap.put("fileType", cloudFile.getFileType());
        reMap.put("h5Url", cloudFile.getAppPreviewUrl());

        Map<String, Object> videoInfo = new HashMap<>();
        boolean checkGetVideo = false;
        if ( cloudFile.getIsM3u8().equals(1) && Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(cloudFile.getFileType())
                && (ObjectUtils.isEmpty(cloudFile.getFrame()) || ObjectUtils.isEmpty(cloudFile.getResolution()))
                ){
            videoInfo = VideoUtil.getVideoInfo(cloudFile.getPath());
            checkGetVideo = true;
        }
        /*  视频原文件、图片（用作选择水印）*/
        if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(cloudFile.getFileType())
                || Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(cloudFile.getFileType())
                // 字幕文件
                || Arrays.asList("srt","ass").contains(cloudFile.getFileType())
                ) {


            String fileName = cloudFile.getPath().substring(cloudFile.getPath().lastIndexOf("/") + 1);
            String pUrl = cloudFile.getPath().substring(0, cloudFile.getPath().lastIndexOf("/") + 1);

            reMap.put("pUrl", pUrl.replace("/private/", GlobalConfig.private_replace_key));
            reMap.put("fileName", fileName);
            if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(cloudFile.getFileType())){

                reMap.put("frame", cloudFile.getFrame());
                if (ObjectUtils.isEmpty(cloudFile.getFrame())){
                    if (!ObjectUtils.isEmpty(videoInfo)) {
                        int frame = VideoUtil.getVideoFrameRate(cloudFile.getPath(), videoInfo, false);
                        reMap.put("frame", frame);
                    }else {
                        reMap.put("frame", 24);
                    }
                }

            }
        }

        //若是视频且分辨率为空时
        if ((Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(cloudFile.getFileType()) || Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(cloudFile.getFileType()))
                && StringUtil.isEmpty(cloudFile.getResolution())) {

            String resolution = "1280*720";
            if (!ObjectUtils.isEmpty(videoInfo)) {
                int[] heightAndWidth = VideoUtil.getHeightAndWidth(cloudFile.getPath(), videoInfo, false);
                if (heightAndWidth[0] != 0 && heightAndWidth[1] != 0) {
                    resolution = heightAndWidth[1] + "*" + heightAndWidth[0];
                }
            }
            cloudFile.setResolution(resolution);
        }
        if(!ObjectUtils.isEmpty(cloudFile.getPath()) && "mp4".equals(cloudFile.getFileType().toLowerCase())) {
            String viewKey = FileUtil.getVideoImgDownloadKey(cloudFile.getPath());
            reMap.put("viewKey", viewKey);
            reMap.put("viewUrl", "/api/disk/video/img/" + cloudFile.getSourceID() + "." + cloudFile.getFileType() + "?showPreview=1&key=" + viewKey);
        }
        if (!ObjectUtils.isEmpty(getCloudPreviewDTO.getHash())) {
            String viewKey = FileUtil.getVideoImgDownloadKey(cloudFile.getPath(), String.valueOf(cloudFile.getSourceID() ));
            reMap.put("iframeUrl", "/api/disk/attachment/" + FileUtil.encodeDownloadUrl(cloudFile.getName())+ "?hash=" + viewKey);
        }
        reMap.put("resolution", ObjectUtils.isEmpty(cloudFile.getResolution()) ? "" : cloudFile.getResolution().replace("x","*"));
        reMap.put("isPreview", cloudFile.getIsPreview());
        reMap.put("previewUrl", "");
        reMap.put("isSwf", 0);
        reMap.put("swfUrl", "");
        Integer videoLength = ObjectUtils.isEmpty(cloudFile.getSourceLength()) ? 0 : cloudFile.getSourceLength();
        reMap.put("name", cloudFile.getName());
        reMap.put("size", cloudFile.getSize());
        reMap.put("length", videoLength);
        reMap.put("isM3u8", cloudFile.getIsM3u8());

        if (cloudFile.getIsM3u8().equals(1)) {//转码完成的
            //文档类
            if (Arrays.asList(GlobalConfig.DOC_TYPE_ARR).contains(cloudFile.getFileType())) {
                reMap.put("isSwf", 1);
                reMap.put("swfUrl", cloudFile.getPreviewUrl());
            } else {//视频
                String m3u8Url = "/api/disk/mu/getMyM3u8.m3u8" + fileOptionTool.getM3u8Param(cloudFile.getSourceID(), m3u8Key)
                        + ((!ObjectUtils.isEmpty(getCloudPreviewDTO.getF()) && getCloudPreviewDTO.getF().longValue() > 0) ? "&f=" + getCloudPreviewDTO.getF().longValue() : "");
                reMap.put("previewUrl", m3u8Url);
            }
        }
        //未完成的. 视频转码进度
        else if (!cloudFile.getIsM3u8().equals(-1)
                && Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(cloudFile.getFileType())) {
            String sourcePath = cloudFile.getPath();
            //获取视频长度

            if (videoLength.equals(0)) {
                if (checkGetVideo){
                    videoLength = VideoUtil.getVideoLength(sourcePath, videoInfo, false);
                }else {
                    videoLength = VideoUtil.getVideoLength(sourcePath);
                }

                if (videoLength.equals(0)) {
                    //获取转码进度
                    int dotPosition = sourcePath.lastIndexOf(".");
                    String m3u8Path = sourcePath.substring(0, dotPosition) + ".m3u8";
                    Integer progress = FileUtil.getConvertedLength(m3u8Path, videoLength, true);
                    reMap.put("convertProgress", progress);
                } else {
                    reMap.put("convertProgress", videoLength);
                }
            }
        }
        reMap.put("isH5", cloudFile.getAppPreview());
        reMap.put("downloadUrl", downloadUrl);
//        vo.setPptPreviewUrl(pptUtil.getPptPreviewUrl(downloadUrl, cloudFile.getChecksum()));
        //到前端页面
        if (Arrays.asList(GlobalConfig.DOC_SHOW_TYPE_ARR).contains(cloudFile.getFileType())) {
            reMap.put("pdfPreviewUrl", fileOptionTool.getPptPdfPreview2(cloudFile.getFileType(), cloudFile.getIsH264Preview(), downloadUrl));
            String pptPreviewUrl = fileOptionTool.getPptPreviewUrl(downloadUrl, cloudFile.getHashMd5());
            reMap.put("pptPreviewUrl", pptPreviewUrl);// HttpUtil.getRequestRootUrl(null) + downloadUrl
            /*if ("pdf".equals(cloudFile.getFileType())){
                //到前端页面
                String pptPreviewPath = domain + "/business/shareFolder.html?"
                        + "fileIds=" + cloudFile.getFileID()
                        + "&sourceID=" + cloudFile.getSourceID()
                        + "&sl=" + Base64.encodeBase64String(String.valueOf(System.currentTimeMillis() + 86400000 * 7).getBytes())
                        + "&isApp=1";
                reMap.put("pptPreviewPath", pptPreviewPath);
            }*/
        }
        /** .txt .js .json .css .sql .xml .java .cs(c#), 返回text */
        reMap.put("text", "");
        fileContentTool.getFileContent(cloudFile, reMap);
        if (!ObjectUtils.isEmpty(getCloudPreviewDTO.getPreview()) && "0".equals(getCloudPreviewDTO.getPreview())){
            cloudFile.setIsPreview(0);
        }
        /** ***/
        if (cloudFile.getIsPreview().intValue() == 0) {
            // 永中
            boolean isYongZhong = Arrays.asList(GlobalConfig.yongzhong_doc_excel_ppt_type).contains(cloudFile.getFileType());
            if (isYongZhong) {
                // 预览
                cloudFile.setDomain(domain);
                convertUtil.yongZhongPre(cloudFile, true);
                reMap.put("yzViewData", ObjectUtils.isEmpty(cloudFile.getYzViewData()) ? "" : cloudFile.getYzViewData());
                reMap.put("yzEditData", "");
            }
        } else {
            reMap.put("yzViewData", cloudFile.getYzViewData());
        }
        return reMap;
    }

    /**
     * @Description: 操作文件
     * @params: [updateFileDTO, resultMap]
     * @Return: boolean
     * @Author: sulijuan
     * @Date: 2023/2/17 15:26
     * @Modified:
     */
    @Override
    public boolean updateFile(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser) {

        CloudOperateEnum operateEnum = CloudOperateEnum.getOperateEnum(updateFileDTO.getOperation());
        if (ObjectUtils.isEmpty(operateEnum)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(updateFileDTO.getSourceLevel()) && !ObjectUtils.isEmpty(updateFileDTO.getSourceID())) {
            if (updateFileDTO.getSourceLevel().indexOf("," + updateFileDTO.getSourceID() + ",") < 0) {
                updateFileDTO.setSourceLevel(updateFileDTO.getSourceLevel() + updateFileDTO.getSourceID() + ",");
            }
        }
        if (!CollectionUtils.isEmpty(updateFileDTO.getDataArr())){
            HomeExplorerVO homeExplorerVO = ioSourceDao.getMySafeBoxSource(loginUser.getUserID());
            if (!ObjectUtils.isEmpty(homeExplorerVO)){
                for (SourceOpDto dto : updateFileDTO.getDataArr()){
                    if (!ObjectUtils.isEmpty(dto.getSourceID()) &&
                            homeExplorerVO.getSourceID().longValue() == dto.getSourceID().longValue()){
                        LogUtil.error("私密保险箱不支持此操作：updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
                        throw new SvnlanRuntimeException(CodeMessageEnum.pathNotSupport.getCode());
                    }
                }
            }
        }
        // 锁定
        fileOptionTool.getLock(updateFileDTO.getSourceID());

        /*if (Arrays.asList(environment.getActiveProfiles()).contains("pro")
                || Arrays.asList(environment.getActiveProfiles()).contains("pre")
                || Arrays.asList(environment.getActiveProfiles()).contains("test")) {
            // 这几个环境需要校验权限
            // 权限校验
            userAuthTool.checkUserPermission(loginUser, updateFileDTO);
        }*/
        // 权限校验
        userAuthTool.checkUserPermission(loginUser, updateFileDTO);

        try {
            LogUtil.info("云盘操作文件" + JsonUtils.beanToJson(updateFileDTO) + "," + JsonUtils.beanToJson(loginUser));
            switch (operateEnum) {
                case RENAME_FILE:
                    fileOptionTool.renameFile(updateFileDTO, loginUser);
                    // 移动、重命名和删除判断是否有删除刚刚上传的文件夹
                    sourceFileService.deleteUserUploadRedis(updateFileDTO,loginUser);
                    break;
                case FAV_RENAME_FILE:
                    fileOptionTool.favRenameFile(updateFileDTO, loginUser);
                    break;
                case COPY_FILE:
                    // 锁定
                    String concurrentKey = GlobalConfig.CLOUD_OPERATION_LOCK_KEY + updateFileDTO.getSourceID();
                    // 锁定
                    Boolean setSuccess = this.stringRedisTemplate.opsForValue().setIfAbsent(concurrentKey, "1", 2, TimeUnit.SECONDS);
                    if(!setSuccess) {
                        LogUtil.error( "频繁操作 updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
                        return true;
                    }
                    List<IOSource> list = fileOptionTool.copyFile(updateFileDTO, loginUser);
                    resultMap.put("sourceList", list);
                    this.stringRedisTemplate.delete(concurrentKey);
                    break;
                case MOVE_FILE:
                    fileOptionTool.moveFile(updateFileDTO, loginUser);
                    // 移动、重命名和删除判断是否有删除刚刚上传的文件夹
                    sourceFileService.deleteUserUploadRedis(updateFileDTO,loginUser);
                    break;
                case DELETE_FILE:
                    //
                    Integer removedFileCount = fileOptionTool.deleteFile(updateFileDTO, loginUser);
                    LogUtil.info("网校资源: 删除文件，数量：" + removedFileCount + ", " + JsonUtils.beanToJson(loginUser) + ", "
                            + JsonUtils.beanToJson(updateFileDTO) + "，" + JsonUtils.beanToJson(updateFileDTO));
                    // 移动、重命名和删除判断是否有删除刚刚上传的文件夹
                    sourceFileService.deleteUserUploadRedis(updateFileDTO,loginUser);
                    break;
                case RECYCLE_FILE:
                    fileOptionTool.recycleFile(updateFileDTO, loginUser, false);
                    LogUtil.info("网校资源: 还原文件, " + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case RECYCLE_FILE_ALL:
                    fileOptionTool.recycleFile(updateFileDTO, loginUser, true);
                    LogUtil.info("网校资源: 还原文件, " + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case DELETE_BIN:
                    fileOptionTool.deleteBin(updateFileDTO, loginUser, false);
                    LogUtil.info("网校资源: 彻底删除 删除回收站," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case DELETE_BIN_ALL:
                    fileOptionTool.deleteBin(updateFileDTO, loginUser, true);
                    LogUtil.info("网校资源: 彻底删除回收站所有 删除回收站," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case SHARE_FILE:
                    fileOptionTool.shareFile(updateFileDTO);
                    LogUtil.info("网校资源: 分享," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case FAV_FILE:
                    Long sourceID = !ObjectUtils.isEmpty(updateFileDTO.getPath()) ? Long.parseLong(updateFileDTO.getPath()) : updateFileDTO.getSourceID();
                    fileOptionTool.checkSafeOperate(updateFileDTO, loginUser, sourceID);
                    fileOptionTool.favFile(updateFileDTO, loginUser);
                    stringRedisTemplate.delete(GlobalConfig.my_fav_key + loginUser.getUserID());
                    LogUtil.info("网校资源: 添加收藏," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case FAV_DEL:
                    fileOptionTool.delFavFile(updateFileDTO, loginUser);
                    stringRedisTemplate.delete(GlobalConfig.my_fav_key + loginUser.getUserID());
                    LogUtil.info("网校资源: 取消收藏," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case FILE_DESC:
                    fileOptionTool.checkSafeOperate(updateFileDTO, loginUser, updateFileDTO.getSourceID());
                    fileOptionTool.saveDesc(updateFileDTO, loginUser);
                    LogUtil.info("网校资源: 添加描述," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case ADD_TOP:
                    fileOptionTool.checkSafeOperate(updateFileDTO, loginUser, updateFileDTO.getSourceID());
                    fileOptionTool.addTop(updateFileDTO, loginUser);
                    LogUtil.info("置顶," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case CANCEL_TOP:
                    fileOptionTool.checkSafeOperate(updateFileDTO, loginUser, updateFileDTO.getSourceID());
                    fileOptionTool.cancelTop(updateFileDTO, loginUser);
                    LogUtil.info("取消置顶," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                case MAKE_FILE:
                    String concurrentKey1 = GlobalConfig.CLOUD_OPERATION_LOCK_KEY + updateFileDTO.getSourceID() + "_" + updateFileDTO.getName();
                    // 锁定
                    Boolean setSuccess1 = this.stringRedisTemplate.opsForValue().setIfAbsent(concurrentKey1, "1", 1, TimeUnit.SECONDS);
                    if(!setSuccess1) {
                        LogUtil.error( "频繁操作 updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
                        return true;
                    }
                    fileOptionTool.makeFile(updateFileDTO, loginUser, resultMap);
                    LogUtil.info("新建文件," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    this.stringRedisTemplate.delete(concurrentKey1);
                    break;
                case FILE_COVER:
                    fileOptionTool.checkSafeOperate(updateFileDTO, loginUser, updateFileDTO.getSourceID());
                    fileOptionTool.updateFileCover(updateFileDTO, loginUser);
                    LogUtil.info("修改文件封面," + JsonUtils.beanToJson(loginUser) + ", " + JsonUtils.beanToJson(updateFileDTO));
                    break;
                default:
                    LogUtil.info(" default 云盘操作文件" + JsonUtils.beanToJson(updateFileDTO) + "," + JsonUtils.beanToJson(loginUser));
                    return false;
            }
        } catch (SvnlanRuntimeException e) {
            throw new SvnlanRuntimeException(e.getErrorCode(), e.getMessage(), true);
        } catch (Exception e) {
            LogUtil.error(e, "云盘操作失败" + JsonUtils.beanToJson(updateFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        } finally {
            //操作完成删除key
            fileOptionTool.delLock(updateFileDTO.getSourceID());
        }
        return true;
    }

    /**
     * @Description: 获取附件等文件资源
     * @params: [response, getAttachmentDTO, fileName]
     * @Return: java.lang.String
     * @Modified:
     */
    @Override
    public String getAttachment(HttpServletResponse response, GetAttachmentDTO getAttachmentDTO, String fileName, Map<String, Object> resultMap) {

        boolean isShare = false;
        // 分享
        if (!ObjectUtils.isEmpty(getAttachmentDTO.getShareCode())) {
            isShare = true;
            shareTool.checkShareLink(getAttachmentDTO);
        }else if (!ObjectUtils.isEmpty(getAttachmentDTO.getHash())) {
            getAttachmentDTO.setBusType("cloud");
            String[] videoPathOrgArray = fileOptionTool.getArrayAttachmentToken(getAttachmentDTO.getHash());
            getAttachmentDTO.setBusId(Long.valueOf(videoPathOrgArray[1]));
            getAttachmentDTO.setD("1");
        }
        //获取业务类型
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(getAttachmentDTO.getBusType());
        if (busTypeEnum == null) {
            LogUtil.error("下载文件业务类型错误, " + JsonUtils.beanToJson(getAttachmentDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        //权限
        Boolean hasPermission = true;
        LoginUser loginUser = null;
        CommonSource commonSourceAttach = null;
        if (!ObjectUtils.isEmpty(getAttachmentDTO.getF()) && getAttachmentDTO.getF() > 0) {
            commonSourceAttach = ioSourceHistoryDao.getHistorySourceInfo(getAttachmentDTO.getF());
        } else {
            commonSourceAttach = fileOptionTool.getSourceInfo(getAttachmentDTO.getBusId());
        }
        if (commonSourceAttach == null) {
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        boolean down = false;
        if (!ObjectUtils.isEmpty(getAttachmentDTO.getD()) && "1".equals(getAttachmentDTO.getD())) {
            down = true;
        }



        if (!isShare && !Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSourceAttach.getFileType())
                && !"mp3".equals(commonSourceAttach.getFileType())
                && !Arrays.asList(GlobalConfig.DOC_TYPE_ARR).contains(commonSourceAttach.getFileType())
                // && !pptUtil.checkIpWhiteList()
                && !"swf".equals(commonSourceAttach.getFileType()) && Boolean.TRUE.equals(getAttachmentDTO.getViewSwf())
        ) {
            hasPermission = false;
        }
        if (!ObjectUtils.isEmpty(getAttachmentDTO.getKey())) {
            String token = fileOptionTool.getAttachmentToken2(getAttachmentDTO.getKey());
            loginUser = loginUserUtil.getLoginUserByToken(HttpUtil.getRequest(), token);
        }
        if (!hasPermission) {
            //从key获取token
            if (loginUser == null || loginUser.getUserID() == null) {
                throw new SvnlanRuntimeException(CodeMessageEnum.errorAdminAuth.getCode());
            }
        }
        if (!ObjectUtils.isEmpty(loginUser) && down) {
            // 权限校验
            userAuthTool.checkGroupDocAuth(loginUser, commonSourceAttach.getSourceID(), commonSourceAttach.getParentLevel(), "4", commonSourceAttach.getTargetType());
        }
        fileName = ObjectUtils.isEmpty(fileName) ? commonSourceAttach.getName() : fileName;
        //是否swf
        boolean isSwf = "swf".equals(commonSourceAttach.getFileType()) && Boolean.TRUE.equals(getAttachmentDTO.getViewSwf());
        boolean isMp3 = "isMp3".equals(commonSourceAttach.getFileType());

        //String basePath = PropertiesUtil.getUpConfig(busTypeEnum.getBusType() + ".savePath");
        //文件实际路径
        String finalFilePath = commonSourceAttach.getPath();
        /*if (commonSourceAttach.getPath().indexOf(basePath) == 0) {
            finalFilePath = commonSourceAttach.getPath();
        } else {
            finalFilePath = basePath + commonSourceAttach.getPath();
        }*/
        //word看pdf
        if (getAttachmentDTO.getForwtop().equals(1) &&
                (commonSourceAttach.getFileType().equals("doc") || commonSourceAttach.getFileType().equals("docx"))) {
            int lastSlash = finalFilePath.lastIndexOf("/");
            finalFilePath = finalFilePath.substring(0, lastSlash) + "/pdf" + finalFilePath.substring(lastSlash);
            int dotPosition = finalFilePath.lastIndexOf(".");
            finalFilePath = finalFilePath.substring(0, dotPosition) + ".pdf";
        }

        boolean isDown = true;
        if (!ObjectUtils.isEmpty(getAttachmentDTO.getGetInfo()) && getAttachmentDTO.getGetInfo().intValue() == 1) {
            isDown = false;
        }
        if (isDown && !ObjectUtils.isEmpty(getAttachmentDTO.getView()) && getAttachmentDTO.getView().intValue() == 1) {
            isDown = false;
        }



        File finalFile = new File(finalFilePath);
        if (!finalFile.exists()) {
            LogUtil.error("下载文件不存在" + finalFilePath);
            this.setDownSystemLogInfo(commonSourceAttach, down, loginUser, 0, "下载文件不存在");
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }

        //替换非法字符
        fileName = FileUtil.replaceIllegalSymbol(fileName);
        String encodedFileName = fileName;
        //url encode
        try {
            encodedFileName = URLEncoder.encode(fileName, "UTF-8");
            encodedFileName = encodedFileName.replaceAll("\\+", "%20");
        } catch (Exception e) {
            LogUtil.error(e, "下载文件，编码失败");
        }
        //swf直接返回资源
        if (getAttachmentDTO.getGetInfo() != 2 && !isMp3 && isDown) {
            try {
                FileUtil.responseFile(response, finalFilePath, encodedFileName, isSwf, isDown);
            } catch (Exception e) {
                LogUtil.error(e, "资源获取失败");
            }
        }

        //有效时间12小时
        //Integer time = (int)(System.currentTimeMillis() / 1000) + 3600 * 12;
        //String hex = Integer.toHexString(time).toUpperCase();

        String firstPath = FileUtil.getFirstStorageDevicePath(finalFilePath);
        String defaultFirstPath = storageService.getDefaultStorageDevicePath();

        String hardLinkPath = null;
        if (getAttachmentDTO.getGetInfo() == 2) {
            // 添加日志
            this.setDownSystemLogInfo(commonSourceAttach, down, loginUser, 1, "");
            return finalFilePath;
        }else {
            //硬链接地址
            hardLinkPath = finalFilePath.replace(firstPath + "/private/", defaultFirstPath + "/common/down_temp/");

            File linkFile = new File(hardLinkPath);

            if (!linkFile.getParentFile().exists()) {
                linkFile.getParentFile().mkdirs();
            }
            try {//创建硬链
                if (!linkFile.exists()) {
                   // Files.createLink(Paths.get(hardLinkPath), Paths.get(finalFilePath));
                    Files.copy(Paths.get(finalFilePath), Paths.get(hardLinkPath),
                            StandardCopyOption.REPLACE_EXISTING);
                }
            } catch (FileAlreadyExistsException e) {//文件已存在, 忽略
                LogUtil.error(e, "创建硬链接失败");
            } catch (Exception e) {
                LogUtil.error(e, "创建硬链接失败");
                // 添加日志
                this.setDownSystemLogInfo(commonSourceAttach, down, loginUser, 0, "创建硬链接失败");
                throw new SvnlanRuntimeException(CodeMessageEnum.downError.getCode());
            }
            try {
                //路径缓存, 供定时任务删除使用
                stringRedisTemplate.opsForZSet().add(GlobalConfig.COMMON_DOWNLOAD_KEY_SET,
                        hardLinkPath, System.currentTimeMillis() + 86400000);
            } catch (Exception e) {
                LogUtil.error(e, "下载文件缓存失败");
            }
            //使用转义后的文件路径
        /*String encodedHardLinkPath = preHardLinkPath + "/" + encodedFileName;
        String key = org.apache.commons.codec.digest.DigestUtils.md5Hex(aliAuthMainKey + encodedHardLinkPath + hex);
*/
            //下载key不作为缓存key参数
            getAttachmentDTO.setKey(null);
        }

        String cdnPath = "";
        if (ObjectUtils.isEmpty(cdnDomain)) {
            cdnPath = HttpUtil.getRequestRootUrl(null) + hardLinkPath;
        } else {
            cdnPath = HttpUtil.getRequestRootUrl(null) + hardLinkPath;
        }
        resultMap.put("filePath", cdnPath);
        resultMap.put("fileSize", finalFile.length());

        // 添加日志
        this.setDownSystemLogInfo(commonSourceAttach, down, loginUser, 1, "");

        return cdnPath;
    }

    public void setDownSystemLogInfo(CommonSource commonSourceAttach, boolean down, LoginUser loginUser, Integer status, String remark) {
        if (down && !ObjectUtils.isEmpty(loginUser)) {
            /** 操作日志 */
            Map<String, Object> reMap = null;
            List<Map<String, Object>> paramList = new ArrayList<>();
            reMap = new HashMap<>(4);
            reMap.put("sourceID", commonSourceAttach.getSourceID());
            reMap.put("fileID", commonSourceAttach.getFileID());
            reMap.put("pathName", ObjectUtils.isEmpty(commonSourceAttach.getSourceName()) ? commonSourceAttach.getName() : commonSourceAttach.getSourceName());
            if (!ObjectUtils.isEmpty(commonSourceAttach.getParentLevel())) {
                reMap.put("sourceParentLevel", commonSourceAttach.getParentLevel());
            }
            reMap.put("type", "file");
            reMap.put("status", status);
            reMap.put("remark", remark);
            reMap.put("fromSourceID", commonSourceAttach.getParentID());
            reMap.put("fromName", "");
            paramList.add(reMap);
            systemLogTool.setSysLog(loginUser, LogTypeEnum.fileDownload.getCode(), paramList, systemLogTool.getRequest());
        }
    }

    @Override
    public CommonSource fileSave(SaveUploadDTO uploadDTO, LoginUser loginUser) {
        // 参数错误
        if (ObjectUtils.isEmpty(uploadDTO) || ObjectUtils.isEmpty(uploadDTO.getSourceID())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        CommonSource source = fileOptionTool.getSourceInfo(uploadDTO.getSourceID());
        if (source == null) {
            LogUtil.error("fileSave source is null");
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (source.getTargetType().intValue() == 2) {
            // 权限校验
            userAuthTool.checkGroupDocAuth(loginUser, source.getSourceID(), source.getParentLevel(), "8", source.getTargetType());
        }
        uploadDTO.setContent(ObjectUtils.isEmpty(uploadDTO.getContent()) ? "" : uploadDTO.getContent());
        Long fileID = source.getFileID();

        String key = GlobalConfig.file_edit_key + loginUser.getUserID() + "_" + uploadDTO.getSourceID() + "_" + uploadDTO.getTaskID();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String value = operations.get(key);
        boolean isOne = false;
        if (!ObjectUtils.isEmpty(value) && "1".equals(value)) {
            isOne = true;
        } else {
            operations.set(key, "1", 8, TimeUnit.HOURS);
        }
        String path = "";
        String fileExtension = source.getFileType();
        if (isOne) {
            path = source.getPath();
            File finalFile = new File(path);
            if ("svg".equals(source.getFileType().toLowerCase())) {
                byte[] size = uploadDTO.getContent().getBytes(StandardCharsets.UTF_8);
                FileUtil.writeSvgFile(finalFile, size);
            } else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(source.getFileType().toLowerCase())) {
                byte[] t = FileUtil.fromHexString(uploadDTO.getContent());
                //转成保存图片
                FileUtil.byte2image(t, path);
            } else if (Arrays.asList("doc", "docx").contains(fileExtension.toLowerCase())) {
                FileUtil.writeDocFile(finalFile, uploadDTO.getContent());
            } else if (Arrays.asList("ppt", "pptx").contains(fileExtension.toLowerCase())) {
                FileUtil.writePptFile(finalFile, uploadDTO.getContent());
            } else if ("html".equals(fileExtension.toLowerCase())) {
                FileUtil.writeHTMLFile(finalFile, uploadDTO.getContent());
            } else if (Arrays.asList("xls", "xlsx").contains(fileExtension.toLowerCase())) {
                // FileUtil.writeExcelFile(finalFile);
            } else {
                FileUtil.putFileContent(path, uploadDTO.getContent());
            }
        } else {
            /** 新增file记录，上条记录插入历史表 */
            CommonSource commonSource2 = new CommonSource();
            saveNewFile(uploadDTO, loginUser, source, commonSource2);
            path = commonSource2.getPath();
            fileID = commonSource2.getFileID();
        }

        File finalFile = new File(path);
        if (finalFile.exists()) {
            // 文件大小
            long size = finalFile.length();

            long updateSize = size;
            if (isOne){
                updateSize = source.getSize() - size;
            }
            if (size > 0) {
                Long groupID = 0L;
                if (2 == source.getTargetType().intValue()) {
                    /** 获取企业云盘 */
                    HomeExplorerVO disk = systemSortTool.getUserSpaceSize(loginUser.getUserID(), uploadDTO.getSourceID());
                    groupID = disk.getGroupID();
                }
                if (updateSize != 0) {
                    // 更新容量
                    fileOptionTool.updateMemory(size, groupID, loginUser.getUserID(), source.getTargetType(), source.getParentLevel());
                }
            }

            // 修改size
            if (!ObjectUtils.isEmpty(source.getFileThumbSize()) && source.getFileThumbSize() > 0 && size > source.getFileThumbSize()){
                size = size - source.getFileThumbSize();
            }
            IOSource sourceUpdate = new IOSource();
            sourceUpdate.setSize(size);
            sourceUpdate.setModifyUser(loginUser.getUserID());
            sourceUpdate.setId(uploadDTO.getSourceID());
            sourceUpdate.setFileId(fileID);

            // 修改文件source修改人、file文件大小
            try {
                ioSourceDao.updateFileSize(sourceUpdate);
            } catch (Exception e) {
                LogUtil.error(e, " fileSave updateFileSize error");
            }

            try {
                ioSourceDao.updateSourceModifyUser(sourceUpdate);
            } catch (Exception e) {
                LogUtil.error(e, " fileSave updateSourceModifyUser error");
            }
        }

        if (Arrays.asList(1, 2).contains(source.getTargetType())) {
            // 添加文档操作event
            fileOptionTool.addSourceEvent(source.getSourceID(), source.getParentID(), loginUser.getUserID(), source.getName(), EventEnum.edit);
        }

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", source.getSourceID());
        reMap.put("sourceParent", source.getParentID());
        reMap.put("type", "editFile");
        reMap.put("pathName", source.getName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileEdit.getCode(), paramList, systemLogTool.getRequest());

        return source;
    }

    private void saveNewFile(SaveUploadDTO uploadDTO, LoginUser loginUser, CommonSource source, CommonSource commonSource2) {

        /** 新增file记录，上条记录插入历史表 */

        //设置默认值
        fileOptionTool.setDefault(commonSource2, loginUser);
        String busType = BusTypeEnum.CLOUD.getBusType();
        commonSource2.setSourceType(BusTypeEnum.CLOUD.getTypeCode());
        commonSource2.setParentLevel(source.getParentLevel());
        commonSource2.setParentID(source.getParentID());
        commonSource2.setSourceID(source.getSourceID());
        commonSource2.setIsEdit("1");
        commonSource2.setTenantId(loginUser.getTenantId());


        Map<String, Object> resultMap = new HashMap<>(1);

        CheckFileDTO updateFileDTO = new CheckFileDTO();
        updateFileDTO.setContent(uploadDTO.getContent());
        updateFileDTO.setSourceID(uploadDTO.getSourceID());
        fileOptionTool.saveFileDetail(commonSource2, updateFileDTO, busType, source.getFileType(), source.getName(), resultMap, "fileSave", loginUser);

        Long userId = source.getUserID();
        IoSourceHistory orgHistory = ioSourceHistoryDao.getHistoryInfoByFileId(source.getSourceID(), source.getFileID());
        if (!ObjectUtils.isEmpty(orgHistory)){
            userId = orgHistory.getUserID();
        }

        // 添加历史记录 sourceID, userID,fileID, `size`, `detail`
        IoSourceHistory ioSourceHistory = new IoSourceHistory();
        ioSourceHistory.setSourceID(source.getSourceID());
        ioSourceHistory.setUserID(ObjectUtils.isEmpty(userId) ? loginUser.getUserID() : userId);
        ioSourceHistory.setFileID(source.getFileID());
        ioSourceHistory.setSize(source.getSize());
        ioSourceHistory.setDetail("");

        // 添加历史记录
        try {
            // 保证size正确
            sourceHistoryUtil.changeCheckSourceHistory(new IoSourceHistory(commonSource2.getSourceID(),commonSource2.getFileID(),loginUser.getUserID(),commonSource2.getSize()), ioSourceHistory);

        } catch (Exception e) {
            LogUtil.error(e, "添加历史记录失败 uploadDTO=" + JsonUtils.beanToJson(uploadDTO));
        }
    }

    @Override
    public Map<String, Object> checkFileReplace(CheckFileDTO checkFileDTO, LoginUser loginUser) {
        checkFileDTO.setTenantId(loginUser.getTenantId());
        Map<String, Object> resultMap = new HashMap<>(2);
        // 参数错误
        if (ObjectUtils.isEmpty(checkFileDTO) || ObjectUtils.isEmpty(checkFileDTO.getHashMd5())
                || ObjectUtils.isEmpty(checkFileDTO.getSize())
                || ObjectUtils.isEmpty(checkFileDTO.getSourceID())) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        // 参数错误
        if (!StringUtil.isNumeric(checkFileDTO.getPath())) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }

        //验证业务类型
        BusTypeEnum busTypeEnum = BusTypeEnum.getFromTypeName(checkFileDTO.getBusType());
        if (busTypeEnum == null) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }


        /** 获取企业云盘 */
        HomeExplorerVO disk = systemSortTool.getUserSpaceSize(loginUser.getUserID(), checkFileDTO.getSourceID());


        CommonSource replaceSource = fileOptionTool.getSourceInfo(Long.parseLong(checkFileDTO.getPath()));
        if (ObjectUtils.isEmpty(replaceSource)) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, disk.getSourceID(), disk.getParentLevel(), "8", disk.getTargetType());

        replaceSource.setSourceType(busTypeEnum.getTypeCode());

        LocalDateTime time = null;
        if (Arrays.asList("dev", "itest").contains(environmentType) ) {
            // 测试环境2023-03-14日之前的服务器文件清理过
            time = LocalDateTime.ofInstant(Instant.ofEpochSecond(1678723200,0), ZoneId.systemDefault());
        }

        CommonSource commonSource = null;
        try {
            commonSource = fileOptionTool.selectByChecksum(checkFileDTO.getHashMd5(), checkFileDTO.getSize(), time);
        }catch (Exception e){
            LogUtil.error(e, "获取秒传数据失败");
        }

        if (commonSource == null) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        if (replaceSource.getFileID().longValue() == commonSource.getFileID().longValue()){
            LogUtil.error("上传新版本与当前版本一致 replaceSource=" + JsonUtils.beanToJson(replaceSource));
            resultMap.put("fileExists", true);
            resultMap.put("chunkList", new ArrayList<>());
            resultMap.put("sourceID", replaceSource.getSourceID());
            resultMap.put("fileID", replaceSource.getFileID());
            resultMap.put("resolution", replaceSource.getResolution());
            return resultMap;
        }
        Long userId = replaceSource.getUserID();
        IoSourceHistory orgHistory = ioSourceHistoryDao.getHistoryInfoByFileId(replaceSource.getSourceID(), replaceSource.getFileID());
        if (!ObjectUtils.isEmpty(orgHistory)){
            userId = orgHistory.getUserID();
        }
        // 添加历史记录 sourceID, `userID`,`fileID`, `size`, `detail`
        IoSourceHistory ioSourceHistory = new IoSourceHistory();
        ioSourceHistory.setSourceID(replaceSource.getSourceID());
        ioSourceHistory.setUserID(ObjectUtils.isEmpty(userId) ? loginUser.getUserID() : userId);
        ioSourceHistory.setFileID(replaceSource.getFileID());
        ioSourceHistory.setSize(replaceSource.getSize());
        ioSourceHistory.setDetail("");

        // 格式不相同
       /*if (!commonSource.getFileType().equals(replaceSource.getFileType())){
            return this.reUploadMap(resultMap, checkFileDTO);
        }

        if (!commonSource.getFileType().toLowerCase().equals(replaceSource.getFileType().toLowerCase())){
            if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSource.getFileType().toLowerCase()) && Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(replaceSource.getFileType().toLowerCase())){
            }else if (Arrays.asList("doc", "docx").contains(commonSource.getFileType().toLowerCase()) && Arrays.asList("doc", "docx").contains(replaceSource.getFileType().toLowerCase())){
            }else if (Arrays.asList("ppt", "pptx").contains(commonSource.getFileType().toLowerCase()) && Arrays.asList("ppt", "pptx").contains(replaceSource.getFileType().toLowerCase())){
            }else if (Arrays.asList("xls", "xlsx").contains(commonSource.getFileType().toLowerCase()) && Arrays.asList("xls", "xlsx").contains(replaceSource.getFileType().toLowerCase())){
            }else {
                throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
            }
        }*/


        int isM3u8 = ObjectUtils.isEmpty(commonSource.getIsM3u8()) ? 0 : commonSource.getIsM3u8();
        if ((isM3u8 <= 0 && Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType()))) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        if (Arrays.asList("dev", "itest").contains(environmentType) && commonSource.getCreateTime() < 1678723200) {
            return this.reUploadMap(resultMap, checkFileDTO);
        }
        String finalFilePath = commonSource.getPath();
        if (!fileExists(finalFilePath)) {
            LogUtil.error("checkFile 下载文件不存在" + finalFilePath);
            return this.reUploadMap(resultMap, checkFileDTO);
        }

        fileOptionTool.checkMemory(disk, checkFileDTO.getSize());

        commonSource.setUserID(loginUser.getUserID());
        UploadDTO uploadDTO = new UploadDTO();
        checkFileDTO.setIgnoreFileSize(disk.getIgnoreFileSize());
        uploadDTO.setChunk(checkFileDTO.getChunk());
        uploadDTO.setChunks(checkFileDTO.getChunks());

        replaceSource.setGroupID(disk.getGroupID());
        replaceSource.setTargetType(disk.getTargetType());
        replaceSource.setUserID(loginUser.getUserID());

        resultMap.put("fileExists", true);
        resultMap.put("chunkList", new ArrayList<>());

        //云盘验证
        if (busTypeEnum.equals(BusTypeEnum.CLOUD)) {
            checkFileDTO.setBusTypeCode(BusTypeEnum.CLOUD.getTypeCode().toString());
        }


        replaceSource.setFileID(commonSource.getFileID());
        replaceSource.setSize(commonSource.getSize());

        resultMap.put("sourceID", replaceSource.getSourceID());
        resultMap.put("fileID", replaceSource.getFileID());
        resultMap.put("resolution", replaceSource.getResolution());
        resultMap.put("sourcePath", FileUtil.returnPath(commonSource.getPath(), busTypeEnum.getBusType(), commonSource.getThumb()));
        resultMap.put("thumb", commonSource.getThumb());
        String previewUrl = commonSource.getPath();
        if (!StringUtil.isEmpty(previewUrl)) {
            resultMap.put("sourcePath", previewUrl);
        }
        //第三方预览地址
        String newPreviewUrl = commonSource.getPreviewUrl();
        if (!StringUtil.isEmpty(newPreviewUrl)) {
            resultMap.put("previewUrl", newPreviewUrl);
        }

        // 添加历史记录
        try {
            sourceHistoryUtil.changeCheckSourceHistory(new IoSourceHistory(replaceSource.getSourceID(),replaceSource.getFileID(),loginUser.getUserID(),replaceSource.getSize()), ioSourceHistory);

        } catch (Exception e) {
            LogUtil.error(e, "添加历史记录失败 uploadDTO=" + JsonUtils.beanToJson(uploadDTO));
        }

        try {
            fileOptionTool.editIoSourceDetail(replaceSource, loginUser.getUserID());
        } catch (Exception e) {
            LogUtil.error(e, "添加历史记录失败 uploadDTO=" + JsonUtils.beanToJson(uploadDTO));
        }

        // 更新容量
        fileOptionTool.updateMemory(replaceSource);

        // 添加文档操作event
        fileOptionTool.addSourceEvent(replaceSource.getSourceID(), replaceSource.getParentID(), loginUser.getUserID(), replaceSource.getName(), EventEnum.uploadNew);

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", replaceSource.getSourceID());
        reMap.put("sourceParent", replaceSource.getParentID());
        reMap.put("type", "uploadNew");
        reMap.put("userID", loginUser.getUserID());
        reMap.put("pathName", replaceSource.getName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileUpload.getCode(), paramList, systemLogTool.getRequest());

        return resultMap;
    }

    @Override
    public Map<String, Object> getFileContent(CheckFileDTO getCloudPreviewDTO) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("fileData", "");
        if (ObjectUtils.isEmpty(getCloudPreviewDTO.getSourceID())) {
            return reMap;
        }
        getCloudPreviewDTO.setF(ObjectUtils.isEmpty(getCloudPreviewDTO.getF()) ? 0L : getCloudPreviewDTO.getF());
        CommonSource cloudFile = fileOptionTool.getFileAttachment(getCloudPreviewDTO.getSourceID(), getCloudPreviewDTO.getF());

        if (cloudFile == null || ObjectUtils.isEmpty(cloudFile.getPath())) {
            return reMap;
        }
        /** 返回十六进制 */
        reMap.put("fileData", FileUtil.getImageContent(cloudFile.getPath()));

        return reMap;
    }

    @Override
    public Map<String, Object> getFileContentBlob(CheckFileDTO getCloudPreviewDTO) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("fileData", "");
        if (ObjectUtils.isEmpty(getCloudPreviewDTO.getSourceID())) {
            return reMap;
        }
        getCloudPreviewDTO.setF(ObjectUtils.isEmpty(getCloudPreviewDTO.getF()) ? 0L : getCloudPreviewDTO.getF());
        CommonSource cloudFile = fileOptionTool.getFileAttachment(getCloudPreviewDTO.getSourceID(), getCloudPreviewDTO.getF());

        if (cloudFile == null || ObjectUtils.isEmpty(cloudFile.getPath())) {
            return reMap;
        }
        /** 返回Blob */
        reMap.put("fileData", FileUtil.getImageContentBlob(cloudFile.getPath()));

        return reMap;
    }

    @Override
    public Map<String, Object> getFileContentByte(CheckFileDTO getCloudPreviewDTO) {
        Map<String, Object> reMap = new HashMap<>();
        reMap.put("fileData", "");
        if (ObjectUtils.isEmpty(getCloudPreviewDTO.getSourceID())) {
            return reMap;
        }
        getCloudPreviewDTO.setF(ObjectUtils.isEmpty(getCloudPreviewDTO.getF()) ? 0L : getCloudPreviewDTO.getF());
        CommonSource cloudFile = fileOptionTool.getFileAttachment(getCloudPreviewDTO.getSourceID(), getCloudPreviewDTO.getF());

        if (cloudFile == null || ObjectUtils.isEmpty(cloudFile.getPath())) {
            return reMap;
        }
        /** 返回Byte */
        reMap.put("fileData", FileUtil.getFileByte(cloudFile.getPath()));

        return reMap;
    }
}
