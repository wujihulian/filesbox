package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.CompressFileDto;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.dto.GetAttachmentDTO;
import com.svnlan.home.dto.UploadDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.service.BusTypeHandleService;
import com.svnlan.home.service.ExplorerFileService;
import com.svnlan.home.service.UploadZipService;
import com.svnlan.home.utils.*;
import com.svnlan.home.utils.zip.*;
import com.svnlan.home.vo.ChangeSourceVo;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.user.service.StorageService;
import com.svnlan.utils.*;
import net.sf.sevenzipjbinding.IInArchive;
import net.sf.sevenzipjbinding.SevenZip;
import net.sf.sevenzipjbinding.SevenZipException;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/9 15:16
 */
@Service
public class UploadZipServiceImpl implements UploadZipService {
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    SystemSortTool systemSortTool;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    ExplorerFileService explorerFileService;
    @Resource
    BusTypeHandleService busTypeHandleService;
    @Resource
    AsyncUtil asyncUtil;
    @Resource
    StorageService storageService;
    @Resource
    AsyncUnZipFileUtil asyncUnZipFileUtil;
    @Resource
    UserAuthTool userAuthTool;
    @Resource
    ShareTool shareTool;
    @Resource
    TenantUtil tenantUtil;

    @Override
    public boolean zipFile(CheckFileDTO checkFileDTO, Map<String, Object> resultMap, LoginUser loginUser){

        if (ObjectUtils.isEmpty(checkFileDTO.getSourceID()) || checkFileDTO.getSourceID() <= 0
                || CollectionUtils.isEmpty(checkFileDTO.getDataArr())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long sourceID = checkFileDTO.getSourceID();
        CommonSource commonSource = fileOptionTool.getSourceInfo(sourceID);
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        boolean isDown = false;
        if (!ObjectUtils.isEmpty(checkFileDTO.getOperation()) && "down".equals(checkFileDTO.getOperation())){
            resultMap.put("operation", "down");
            isDown = true;
        }
        boolean isShare = false;
        // 分享
        if (!ObjectUtils.isEmpty(checkFileDTO.getShareCode())) {
            isShare = true;
            GetAttachmentDTO getAttachmentDTO = new GetAttachmentDTO();
            getAttachmentDTO.setShareCode(checkFileDTO.getShareCode());
            getAttachmentDTO.setD("1");
            shareTool.checkShareLink(getAttachmentDTO);
        }
        if (!isShare && ObjectUtils.isEmpty(loginUser)){
            throw new SvnlanRuntimeException(CodeMessageEnum.bindSignError.getCode());
        }
        // 压缩权限
        String auth = "6";
        if (isDown){
            auth = "4";
        }
        long userID = ObjectUtils.isEmpty(loginUser) || ObjectUtils.isEmpty(loginUser.getUserID()) ? 0 : loginUser.getUserID();
        List<IOSourceVo> copyListByLevel = null;
        if (!isShare && commonSource.getTargetType().intValue() == 2) {
            if (!isDown) {
                // 权限校验
                userAuthTool.checkGroupDocAuth(loginUser, commonSource.getSourceID(), commonSource.getParentLevel(), "9", commonSource.getTargetType());
            }

            // 校验被压缩的文件权限
            List<Long> sourceIDList = new ArrayList<>();
            List<String> sourceLevelList = new ArrayList<>();
            int i = 0;
            for (SourceOpDto dto : checkFileDTO.getDataArr()) {
                sourceIDList.add(dto.getSourceID());
                if ("folder".equals(dto.getType())) {
                    sourceLevelList.add(dto.getParentLevel() + dto.getSourceID() + ",");
                }

                userAuthTool.checkGroupDocAuth(loginUser, dto.getSourceID(), dto.getParentLevel(), auth, commonSource.getTargetType(), i==0 ? true : false);
                i ++;
            }
            List<Long> otherSourceIds = null;
            if (!CollectionUtils.isEmpty(sourceLevelList)) {
                copyListByLevel = ioSourceDao.copySourcePathListByLevel(sourceLevelList);
                if (!CollectionUtils.isEmpty(copyListByLevel)) {
                    otherSourceIds = copyListByLevel.stream().map(IOSourceVo::getSourceID).collect(Collectors.toList());
                    userAuthTool.checkGroupDocAuthOther(loginUser, otherSourceIds, auth, commonSource.getTargetType());
                }
            }

            // 校验被压缩的文件权限end
        }
        String suffix = ObjectUtils.isEmpty(checkFileDTO.getSuffix()) ? "zip" : checkFileDTO.getSuffix();
        if (!Arrays.asList(GlobalConfig.ZIP_SHOW_TYPE_ARR).contains(suffix)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        String uuid = RandomUtil.getuuid();
        checkFileDTO.setTaskID(ObjectUtils.isEmpty(checkFileDTO.getTaskID()) ? uuid : checkFileDTO.getTaskID());

        if (!isDown) {
            /** 获取企业云盘 */
            HomeExplorerVO disk = systemSortTool.getUserSpaceSize(userID, checkFileDTO.getSourceID());
            // 个人空间
            if (disk.getTargetType().intValue() == 1) {
                Long userSizeUse = disk.getUserSizeUse();
                // 0 为不限制容量
                if (disk.getUserSizeMax().doubleValue() > 0 && userSizeUse >= (disk.getUserSizeMax() * 1024 * 1024 * 1024)) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.spaceIsFull.getCode());
                }
            } else {
                // 部门
                Long sizeUse = disk.getSizeUse();
                // 0 为不限制容量
                if (disk.getSizeMax().doubleValue() > 0 && sizeUse >= (disk.getSizeMax() * 1024 * 1024 * 1024)) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.spaceIsFull.getCode());
                }
            }
        }else {

            List<Long> sList = new ArrayList<>();
            for (SourceOpDto dto : checkFileDTO.getDataArr()) {
                sList.add(dto.getSourceID());
            }
            List<IOSourceVo> attachments = ioSourceDao.copySourcePathList(sList);
            long totalSize = 0L;
            for (IOSourceVo vo : attachments){
                totalSize = totalSize + vo.getSize();
            }
            // 判断下载不可超过4G
            long checkSize = 4294967296L;
            if(totalSize > checkSize){
                throw new SvnlanRuntimeException(CodeMessageEnum.downLimitSizeError.getCode());
            }
        }
        String fileName = ObjectUtils.isEmpty(checkFileDTO.getName()) ? commonSource.getSourceName() + "." + suffix : checkFileDTO.getName();

        if (!fileName.endsWith("." + suffix)){
            fileName = fileName + "." + suffix;
        }
        // String zipSavePath = "/uploads/common/zip_task/" + FileUtil.getDatePath() + RandomUtil.getuuid() + "/";
        //基础路径+日期路径
        String finalFolderPath = storageService.getDefaultStorageDevicePath() +  PropertiesUtil.getUpConfig("cloud.savePath")  + FileUtil.getDatePath();

        if (!ObjectUtils.isEmpty(checkFileDTO.getOperation()) && "down".equals(checkFileDTO.getOperation())){
            // 只下载
            finalFolderPath = finalFolderPath.replace("/private/", "/common/down_temp/");
        }
        Long t = System.currentTimeMillis();
        // 存放复制文件的临时文件名
        String tempFolder = uuid + t + "_" + userID ;
        String finalFilePath = null;
        if (isDown){
            // 压缩包的路径及名称
            finalFilePath = finalFolderPath + (fileName.replace("." + suffix, t+"." + suffix));
            try {
                //路径缓存, 供定时任务删除使用
                stringRedisTemplate.opsForZSet().add(GlobalConfig.COMMON_DOWNLOAD_KEY_SET,
                        finalFilePath, System.currentTimeMillis() + 86400000);
            } catch (Exception e) {
                LogUtil.error(e, "下载文件缓存失败");
            }
        }else {
            // 压缩包的路径及名称
            finalFilePath = finalFolderPath + tempFolder + "." + suffix;
        }

        checkFileDTO.setFinalFolderPath(finalFolderPath);
        checkFileDTO.setCommonSource(commonSource);
        checkFileDTO.setUuid(uuid);
        checkFileDTO.setTempFolder(tempFolder);
        checkFileDTO.setFinalFilePath(finalFilePath);
        checkFileDTO.setTemp(finalFolderPath + tempFolder + "/");
        checkFileDTO.setFileName(fileName);
        checkFileDTO.setUserID(userID);
        checkFileDTO.setSourceID(sourceID);
        checkFileDTO.setStatus(1);
        checkFileDTO.setFileType(suffix);
        checkFileDTO.setTenantId(commonSource.getTenantId());

        resultMap.put("taskID", checkFileDTO.getTaskID());
        resultMap.put("finalFilePath", finalFilePath);
        resultMap.put("fileName", fileName);
        resultMap.put("userID", userID);
        resultMap.put("sourceID", sourceID);
        resultMap.put("status", 0);
        resultMap.put("temp", finalFolderPath + tempFolder + "/");
        resultMap.put("fileType", suffix);


        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_zip_file + checkFileDTO.getTaskID(), "0", 5, TimeUnit.HOURS);
        stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_zip_file_info + checkFileDTO.getTaskID(), JsonUtils.beanToJson(resultMap), 5, TimeUnit.HOURS);

        /** 异步 文件压缩*/
        checkFileDTO.setLoginUser(loginUser);
        checkFileDTO.setContent(JsonUtils.beanToJson(resultMap));

        if (!isDown && !ObjectUtils.isEmpty(loginUser)) {
            // 完成后操作 入库
            this.addSourceInfo(checkFileDTO, loginUser, resultMap);
        }
        long ms = System.currentTimeMillis();
        LogUtil.info("zipFile asyncZipFile begin " + ms);
        // 压缩
        asyncUtil.asyncZipFile(checkFileDTO, copyListByLevel);

        String key = GlobalConfig.async_key_zip_file + checkFileDTO.getTaskID();
        String denyString = stringRedisTemplate.opsForValue().get(key);
        if ("1".equals(denyString)) {
            // 完成
            resultMap.put("status", 1);
        }
        if (isDown && !ObjectUtils.isEmpty(loginUser)){
            // 下载文件夹日志
            /** 操作日志 */
            Map<String, Object> reMap = null;
            List<Map<String, Object>> paramList = new ArrayList<>();
            reMap = new HashMap<>(4);
            reMap.put("sourceID", commonSource.getSourceID());
            reMap.put("fileID", commonSource.getFileID());
            reMap.put("pathName", fileName);
            if (!ObjectUtils.isEmpty(commonSource.getSourceID())){
                reMap.put("sourceParentLevel", commonSource.getParentLevel() + "," + commonSource.getSourceID());
            }
            reMap.put("status", "1");
            reMap.put("type", "folder");
            reMap.put("fromSourceID", commonSource.getSourceID());
            reMap.put("fromName", commonSource.getSourceName());
            paramList.add(reMap);
            systemLogTool.setSysLog(loginUser, LogTypeEnum.zipDownload.getCode(), paramList, systemLogTool.getRequest());
        }
        LogUtil.info("zipFile asyncZipFile end" + (System.currentTimeMillis() - ms) + " ms.  " + ms);
        return true;
    }


    @Override
    public boolean taskAction(CheckFileDTO checkFileDTO, Map<String, Object> resultMap){
        if (ObjectUtils.isEmpty(checkFileDTO.getTaskID())){
            resultMap.put("status", 0);
            resultMap.put("taskID", checkFileDTO.getTaskID());
            resultMap.put("zipProgress", 0);
            return false;
        }
        resultMap.put("status", 0);
        resultMap.put("taskID", checkFileDTO.getTaskID());
        String key = GlobalConfig.async_key_zip_file + checkFileDTO.getTaskID();
        String denyString = stringRedisTemplate.opsForValue().get(key);

        // 压缩进度
        String progressKey = GlobalConfig.progress_key_zip_file +  checkFileDTO.getTaskID();
        String progressString = stringRedisTemplate.opsForValue().get(progressKey);
        resultMap.put("zipProgress", 0);
        if (!ObjectUtils.isEmpty(progressString)){
            resultMap.put("zipProgress", progressString);
        }

        LogUtil.info("taskAction key=" + key +"，denyString=" + denyString + "，progressString=" + progressString);
        if (ObjectUtils.isEmpty(denyString)){
            resultMap.put("status", 1);
            return true;
        }

        if ("1".equals(denyString)) {
            resultMap.put("status", 1);
            resultMap.put("zipProgress", 100);

            String infoKey = GlobalConfig.async_key_zip_file_info + checkFileDTO.getTaskID();
            String info = stringRedisTemplate.opsForValue().get(infoKey);
            if (ObjectUtils.isEmpty(info)){
                throw new SvnlanRuntimeException(CodeMessageEnum.explorerError.getCode());
            }
            LogUtil.info("taskAction key=" + key +"，info=" + info);
            CheckFileDTO paramVo = JsonUtils.jsonToBean(info, CheckFileDTO.class);
            if (ObjectUtils.isEmpty(checkFileDTO.getOperation())){
                if (!ObjectUtils.isEmpty(checkFileDTO.getOperation()) && "down".equals(checkFileDTO.getOperation())){
                    paramVo.setOperation("down");
                }
            }
            if (!ObjectUtils.isEmpty(checkFileDTO.getOperation()) && "down".equals(checkFileDTO.getOperation())){
                // 只下载
                resultMap.put("taskID", paramVo.getTaskID());
                resultMap.put("finalFilePath", paramVo.getFinalFilePath());
                resultMap.put("fileName", paramVo.getFileName());
                resultMap.put("userID", paramVo.getUserID());
                resultMap.put("sourceID", paramVo.getSourceID());
                resultMap.put("temp", paramVo.getTemp());
                resultMap.put("fileType", paramVo.getFileType());
            }else {
                // 入库
                //this.addSourceInfo(paramVo, loginUser, resultMap);
            }

            try {
                ZipUtils.deleteFile(new File(paramVo.getTemp()));
            }catch (Exception e){
                LogUtil.error(e,"taskAction 删除临时文件夹失败 ");
            }

            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete(infoKey);
            stringRedisTemplate.delete(progressKey);
        }else {
            // 转码进度
            //获取转码进度
            /*int dotPosition = sourcePath.lastIndexOf(".");
            String m3u8Path = sourcePath.substring(0, dotPosition) + ".m3u8";
            Integer progress = FileUtil.getConvertedLength(m3u8Path, videoLength, true);
            reMap.put("convertProgress", progress);*/
        }
        return true;
    }

    private void addSourceInfo(CheckFileDTO paramVo, LoginUser loginUser, Map<String, Object> resultMap){

        CommonSource commonSource = new CommonSource();
        //设置默认值
        fileOptionTool.setDefault(commonSource, loginUser);
        commonSource.setSourceType(BusTypeEnum.CLOUD.getTypeCode());
        // 入库
        commonSource.setUserID(paramVo.getUserID());
        commonSource.setName(paramVo.getFileName());
        commonSource.setParentID(paramVo.getSourceID());
        commonSource.setSize(0L);
        commonSource.setPath(paramVo.getFinalFilePath());
        commonSource.setFileType(paramVo.getFileType());
        commonSource.setTenantId(paramVo.getTenantId());
        if (!ObjectUtils.isEmpty(commonSource.getParentID()) && commonSource.getParentID() > 0) {
            CommonSource parentSource = fileOptionTool.getSourceInfo(commonSource.getParentID());
            if (ObjectUtils.isEmpty(parentSource)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            commonSource.setParentLevel(parentSource.getParentLevel() + commonSource.getParentID() + ",");
            commonSource.setTargetType(parentSource.getTargetType());
        } else {
            commonSource.setParentLevel(",0,");
            commonSource.setTargetType(1);
        }
        commonSource.setSize(0L);
        commonSource.setHashMd5("");

        try {
            fileOptionTool.addCommonSource(loginUser.getUserID(), commonSource, EventEnum.upload);
            resultMap.put("source", commonSource);
            paramVo.setBusId(commonSource.getSourceID());
            paramVo.setFileID(commonSource.getFileID());
            paramVo.setTargetType(commonSource.getTargetType());
            paramVo.setParentLevel(commonSource.getParentLevel());
        } catch (Exception e) {
            LogUtil.error(e, "addSourceInfo source入库失败" + JsonUtils.beanToJson(commonSource));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", commonSource.getSourceID());
        reMap.put("sourceParent", commonSource.getParentID());
        reMap.put("type", "editFile");
        reMap.put("pathName", commonSource.getName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileEdit.getCode(), paramList, systemLogTool.getRequest());
    }

    @Override
    public List<CommonSource> unZip(CheckFileDTO checkFileDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceID()) || checkFileDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo()) && ObjectUtils.isEmpty(checkFileDTO.getPathTo())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Long sourceID = checkFileDTO.getSourceID();
        CommonSource commonSource = fileOptionTool.getSourceInfo(sourceID);
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, commonSource.getSourceID(), commonSource.getParentLevel(), "7", commonSource.getTargetType());

        if (!Arrays.asList(GlobalConfig.UNZIP_SHOW_TYPE_ARR).contains(commonSource.getFileType())){
            LogUtil.error("unZip 解压缩类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        Long tenantId = commonSource.getTenantId();

        UploadDTO uploadDTO = new UploadDTO();
        /*String needMark = systemOptionDao.getSystemConfigByKey("needMark");
        uploadDTO.setHasMark((!ObjectUtils.isEmpty(needMark) && "1".equals(needMark)) ? true : false);*/
        uploadDTO.setHasMark(false);
        List<CommonSource> convertList = new ArrayList<>();
        Integer targetType = commonSource.getTargetType();

        List<String> sourceNameList = null;
        CommonSource parentSource = null;
        if (!ObjectUtils.isEmpty(checkFileDTO.getPathTo()) ){
           /* if (StringUtil.isSpecialChar(checkFileDTO.getPathTo())){
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }*/
            // 权限校验
            userAuthTool.checkGroupDocAuth(loginUser, commonSource.getParentID(), commonSource.getParentLevel(), "9", commonSource.getTargetType());
            parentSource = new CommonSource();
            parentSource.setName(checkFileDTO.getPathTo());
            parentSource.setParentID(commonSource.getParentID());
            parentSource.setParentLevel(commonSource.getParentLevel());
            parentSource.setTargetType(targetType);
            parentSource.setFileType("");
            parentSource.setSize(0L);
            parentSource.setTenantId(tenantId);
            fileOptionTool.addIoSourceDetail(parentSource, loginUser.getUserID(), 0L, EventEnum.mkdir);

        }else if(!ObjectUtils.isEmpty(checkFileDTO.getSourceIDTo()) && checkFileDTO.getSourceIDTo() > 0){
            parentSource = fileOptionTool.getSourceInfo(checkFileDTO.getSourceIDTo());
            userAuthTool.checkGroupDocAuth(loginUser, checkFileDTO.getSourceIDTo(), parentSource.getParentLevel(), "9", parentSource.getTargetType());
            // 查询文件夹下的文件、解析是否要重命名
            sourceNameList = ioSourceDao.getSourceNameList(checkFileDTO.getSourceIDTo());
            targetType = parentSource.getTargetType();

        }else {
            LogUtil.error("unZip 目标目录不存在 checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        String finalTopPath = storageService.getDefaultStorageDevicePath() +  PropertiesUtil.getUpConfig("cloud.savePath");
        //基础路径+日期路径
        String finalFolderPath = finalTopPath + FileUtil.getDatePath() + parentSource.getSourceID() + "/";
        File folder = new File(finalFolderPath);
        //创建目录
        if (!folder.exists()) {
            if (!folder.mkdirs()) {
                LogUtil.error("创建目录失败, path:" + finalFolderPath);
//                return null;
            }
        }

        List<ChangeSourceVo> fileList = new ArrayList<>();


        if (ObjectUtils.isEmpty(checkFileDTO.getFullName())){
            stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_unzip_file + checkFileDTO.getTaskID(), "0", 1, TimeUnit.HOURS);
            stringRedisTemplate.opsForValue().set(GlobalConfig.progress_key_unzip_file + checkFileDTO.getTaskID(), "0", 1, TimeUnit.HOURS);
            HttpServletRequest request = HttpUtil.getRequest();
            String serverUrl = HttpUtil.getRequestRootUrl(request);
            String serverName = HttpUtil.getServerName(request);
            loginUser.setServerName(serverName);
            loginUser.setUserAgent(HttpUtil.getUA(request));
            asyncUnZipFileUtil.asyncUnZipFile(commonSource, checkFileDTO, loginUser, stringRedisTemplate, finalFolderPath, fileList
            , systemLogTool,fileOptionTool, busTypeHandleService, uploadDTO, parentSource, sourceNameList, targetType, serverUrl, tenantId);

            return null;
            /** 解压整个文件 流程放异步进行 */
           /* CompressFileDto dto = CompressFileUtil.unzipFilePassword(commonSource.getPath(), finalFolderPath, fileList, loginUser.getUserID()
                    , checkFileDTO.getPassword(), checkFileDTO.getTaskID(), true, stringRedisTemplate );
            if (ObjectUtils.isEmpty(dto) || !dto.getSuccess()){
                if (!ObjectUtils.isEmpty(checkFileDTO.getPassword())){
                    throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
                }else {
                    throw new SvnlanRuntimeException(CodeMessageEnum.unzipErrorTips.getCode());
                }
            }*/
           // CompressFileUtil.compressFileBySource(commonSource, finalFolderPath, fileList, loginUser, checkFileDTO.getPassword());
        }else {
            // 解压单个文件或者文件夹
            this.compressFileByFile(commonSource, finalFolderPath, fileList, loginUser, checkFileDTO);

        }


        LogUtil.info("unZip parentSource=" + JsonUtils.beanToJson(parentSource));
        LogUtil.info("unZip fileList=" + JsonUtils.beanToJson(fileList));

        // 返回前端
        List<Long> sourceIds = new ArrayList<>();
        if (!ObjectUtils.isEmpty(parentSource) && !CollectionUtils.isEmpty(fileList)){
            List<ChangeSourceVo> sourceFileVos = new ArrayList<>();
            List<ChangeSourceVo> sourceDirVos = new ArrayList<>();
            for (ChangeSourceVo entry: fileList) {
                if (entry.getIsFolder().intValue() == 1){
                    if (entry.getFilePath().indexOf("//") >= 0){
                        entry.setFilePath(entry.getFilePath().replaceAll("//","/"));
                    }
                    sourceDirVos.add(entry);
                }else {
                    sourceFileVos.add(entry);
                }
            }

            Map<String, CommonSource> parentMap = new HashMap<>(1);
            parentMap.put(finalFolderPath, parentSource);
            // 文件夹
            if (!CollectionUtils.isEmpty(sourceDirVos)){
                //finalFolderPath
                sourceDirVos = sourceDirVos.stream().sorted(Comparator.comparing(ChangeSourceVo::getPathLength)).collect(Collectors.toList());
                for (ChangeSourceVo changeVo : sourceDirVos){
                    String parentPath = changeVo.getFilePath().substring(0, changeVo.getFilePath().indexOf(changeVo.getName()));
                    if (parentMap.containsKey(parentPath)) {
                        CommonSource pSource = parentMap.get(parentPath);
                        CommonSource dir = new CommonSource();
                        if (parentPath.equals(finalFolderPath)) {
                            dir.setName(fileOptionTool.checkRepeatName(changeVo.getName(), changeVo.getName(), sourceNameList, 1));
                        }else {
                            dir.setName(changeVo.getName());
                        }
                        dir.setParentID(pSource.getSourceID());
                        dir.setParentLevel(pSource.getParentLevel() + pSource.getSourceID() + ",");
                        dir.setTargetType(targetType);
                        dir.setFileType("");
                        dir.setSize(0L);
                        dir.setTenantId(tenantId);
                        fileOptionTool.addIoSourceDetail(dir, loginUser.getUserID(), 0L, EventEnum.mkdir);
                        if (parentPath.equals(finalFolderPath)){
                            sourceIds.add(dir.getSourceID());
                        }
                        parentMap.put(changeVo.getFilePath(), dir);
                    }
                }
            }

            LogUtil.info("unZip parentMap=" + JsonUtils.beanToJson(parentMap));
            LogUtil.info("unZip sourceDirVos=" + JsonUtils.beanToJson(sourceDirVos));
            LogUtil.info("unZip sourceFileVos=" + JsonUtils.beanToJson(sourceFileVos));

            // 文件
            if (!CollectionUtils.isEmpty(sourceFileVos)){

                Map<Long, Long> sourceSizeMap = new HashMap<>();

                List<Long> pIdSizeList = null;

                for (ChangeSourceVo changeVo : sourceFileVos){
                    String parentPath ;
                    if (changeVo.getFilePath().equals(changeVo.getName())){
                        parentPath = finalFolderPath;
                    }else {
                        parentPath = finalFolderPath + changeVo.getFilePath().substring(0, changeVo.getFilePath().indexOf(changeVo.getName()));
                    }
                    LogUtil.info("unZip check=" + parentMap.containsKey(parentPath) + " parentPath=" + parentPath);
                    if (parentMap.containsKey(parentPath)) {
                        CommonSource pSource = parentMap.get(parentPath);
                        CommonSource fileSource = new CommonSource();
                        if (parentPath.equals(finalFolderPath)) {
                            fileSource.setName(fileOptionTool.checkRepeatName(changeVo.getName(), changeVo.getName(), changeVo.getFileType(), sourceNameList, 1));
                        }else {
                            fileSource.setName(changeVo.getName());
                        }
                        fileSource.setParentID(pSource.getSourceID());
                        fileSource.setParentLevel(pSource.getParentLevel() + pSource.getSourceID() + ",");
                        fileSource.setTargetType(targetType);
                        fileSource.setFileType(changeVo.getFileType());
                        fileSource.setSize(changeVo.getSize());
                        fileSource.setHashMd5(changeVo.getHashMd5());
                        fileSource.setPath(changeVo.getPath());

                        /*pIdSizeList = Arrays.asList(fileSource.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                        for (Long pId : pIdSizeList) {
                            if (sourceSizeMap.containsKey(pId)) {
                                sourceSizeMap.put(pId, sourceSizeMap.get(pId) + fileSource.getSize());
                            } else {
                                sourceSizeMap.put(pId, fileSource.getSize());
                            }
                        }*/
                        fileSource.setTenantId(tenantId);
                        fileOptionTool.addCommonSource(loginUser.getUserID(), fileSource, EventEnum.mkfile);
                        if (parentPath.equals(finalFolderPath)){
                            sourceIds.add(fileSource.getSourceID());
                        }

                        if (!ObjectUtils.isEmpty(checkFileDTO.getFullName())) {
                            fileSource.setNeedHashMd5(1);
                        }
                        LogUtil.info("unZip fileSource=" + JsonUtils.beanToJson(fileSource));
                        if (Arrays.asList(GlobalConfig.VIDEO_AUDIO_TYPE_CONVERT).contains(changeVo.getFileType())){
                            // 转码、图片加缩略图
                            convertList.add(fileSource);
                        }else if (Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(changeVo.getFileType())){
                            // "cloud"
                            fileSource.setSourceType(2);
                            busTypeHandleService.doForImage(uploadDTO, false, fileSource);
                            if (!ObjectUtils.isEmpty(checkFileDTO.getFullName())) {
                                convertList.add(fileSource);
                            }
                        } else {
                            if (!ObjectUtils.isEmpty(checkFileDTO.getFullName())) {
                                convertList.add(fileSource);
                            }
                        }
                    }
                }
                /*if (!ObjectUtils.isEmpty(sourceSizeMap)){
                    List<IOSource> sourceList = new ArrayList<>();
                    IOSource vo = null;
                    for (Map.Entry<Long, Long> entry : sourceSizeMap.entrySet()) {
                        if (!ObjectUtils.isEmpty(entry.getKey())){
                            vo = new IOSource(entry.getKey(), entry.getValue());
                            sourceList.add(vo);
                        }
                    }
                    try {
                        if (!CollectionUtils.isEmpty(sourceList)) {
                            LogUtil.info("解压缩 batchUpdateSourceMemoryList sourceSizeMap=" + JsonUtils.beanToJson(sourceSizeMap));
                            ioSourceDao.batchUpdateSourceMemoryList(sourceList);
                        }
                    }catch (Exception e){
                        LogUtil.error(e,"解压缩 error batchUpdateSourceMemoryList sourceSizeMap=" + JsonUtils.beanToJson(sourceSizeMap));
                    }
                }*/
            }
        }

        checkFileDTO.setSourceIds(sourceIds);

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", commonSource.getSourceID());
        reMap.put("sourceParent", commonSource.getParentID());
        reMap.put("type", "upload");
        reMap.put("pathName", commonSource.getName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileUpload.getCode(), paramList, systemLogTool.getRequest());
        return convertList;
    }

    /** 解压单个文件或者文件夹 */
    public void compressFileByFile(CommonSource commonSource, String finalFolderPath, List<ChangeSourceVo> fileList, LoginUser loginUser, CheckFileDTO checkFileDTO){
        String compressKey = GlobalConfig.FILE_PREVIEW_COMPRESS_KEY + commonSource.getPath();
        String value = stringRedisTemplate.opsForValue().get(compressKey);
        LogUtil.info("compressFileByFile compressKey=" + compressKey + " ，value=" + value);
        CompressFileReader.FileNode fileNode = null;
        if (!ObjectUtils.isEmpty(value)){
            try {
                fileNode = JsonUtils.jsonToBean(value, CompressFileReader.FileNode.class);
            }catch (Exception e){
                LogUtil.error(e, "unzipList error value=" + value);
            }
        }
        if (ObjectUtils.isEmpty(fileNode)){
            fileNode = explorerFileService.getFileNode(commonSource, compressKey, fileNode);
        }
        if (ObjectUtils.isEmpty(fileNode) || CollectionUtils.isEmpty(fileNode.getChildList())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (fileNode.getEncrypted() && ObjectUtils.isEmpty(checkFileDTO.getPassword())){
            throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
        }
        String extractPath = commonSource.getPath().substring(0, commonSource.getPath().lastIndexOf(".") ) + GlobalConfig.separatorTO;
        String firstPath = FileUtil.getFirstStorageDevicePath(commonSource.getPath());
        extractPath = extractPath.replace(firstPath + "/private", firstPath + "/common/down_temp");
        int i = 0;
        CompressFileDto dto = null;

        RandomAccessFile randomAccessFile = null;
        IInArchive inArchive = null;
        try {
            randomAccessFile = new RandomAccessFile(commonSource.getPath(), "r");
            if (!ObjectUtils.isEmpty(checkFileDTO.getPassword()) && checkFileDTO.getPassword().length() > 0) {
                inArchive = SevenZip.openInArchive(null,
                        new RandomAccessFileInStream(randomAccessFile), checkFileDTO.getPassword());
            }else {
                inArchive = SevenZip.openInArchive(null,
                        new RandomAccessFileInStream(randomAccessFile));
            }
            ISimpleInArchive iSimpleInArchive =  inArchive.getSimpleInterface();

            //验证后缀
            String suffix = FileUtil.getFileExtension(checkFileDTO.getFullName());
            boolean directory = false;
            if (StringUtil.isEmpty(suffix)){
                // 文件夹
                directory = true;
            } else {
                /*Pattern pattern = Pattern.compile("[^a-zA-Z0-9]", Pattern.CASE_INSENSITIVE);
                // 文件名不符合规范, 请修改
                if (pattern.matcher(suffix).find()){
                    throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode());
                }*/
            }

            // 解压单个文件
            dto = new CompressFileDto();
            dto.setSuccess(false);
            dto.setStatus(0);
            // 解压文件夹
            this.compressFileCopy(fileNode.getChildList(), finalFolderPath, fileList, loginUser, checkFileDTO, extractPath, i, iSimpleInArchive, dto);

            LogUtil.info("fileList=" + JsonUtils.beanToJson(fileList));
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error(e, " getSimpleInArchiveByPath Exception : " + e);
        } finally {
            if (inArchive != null) {
                try {
                    inArchive.close();
                } catch (SevenZipException e) {
                    LogUtil.error(e, "Error closing archive: " + e);
                    e.printStackTrace();
                }
            }
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    LogUtil.error(e, "Error closing file " );
                    e.printStackTrace();
                }
            }
        }

        if (ObjectUtils.isEmpty(dto) || !dto.getSuccess()){
            if (!ObjectUtils.isEmpty(checkFileDTO.getPassword())){
                throw new SvnlanRuntimeException(CodeMessageEnum.errorPwd.getCode());
            }else {
                throw new SvnlanRuntimeException(CodeMessageEnum.unzipErrorTips.getCode());
            }
        }

    }

    private void compressFileCopy(List<CompressFileReader.FileNode> fileNodeList, String finalFolderPath, List<ChangeSourceVo> fileList, LoginUser loginUser, CheckFileDTO checkFileDTO
            , String extractPath, int i, ISimpleInArchive iSimpleInArchive, CompressFileDto dto){
        if (!CollectionUtils.isEmpty(fileList)){
            return;
        }
        fileNodeList = fileNodeList.stream().sorted(Comparator.comparing(CompressFileReader.FileNode::isDirectory)).collect(Collectors.toList());

        for (CompressFileReader.FileNode node : fileNodeList){
            i ++;
            LogUtil.info(checkFileDTO.getFullName() + "-----compressFileCopy-------" + node.getFileName());
            if (checkFileDTO.getFullName().equals(node.getFileName())){
                if (node.isDirectory()){
                    fileList.add(new ChangeSourceVo(node.getOriginName().endsWith("/") ? node.getOriginName().substring(0,node.getOriginName().length() -1) : node.getOriginName(),
                            1, finalFolderPath + node.getFullName() + "/"));
                    this.compressFileCopyChangeList(node.getChildList(), finalFolderPath, fileList, loginUser, checkFileDTO, extractPath, i, iSimpleInArchive, dto);
                    return;
                }else {
                    String suffix = FileUtil.getFileExtension(node.getOriginName());
                    String finalFilePath = finalFolderPath + RandomUtil.getuuid() +  System.currentTimeMillis()
                            + "_" + i +  "_" + loginUser.getUserID()+ "." + suffix;

                    // 单个解压
                    CompressFileDto dto1 = CompressFileUtil.unzipOneFilePassword(iSimpleInArchive, finalFilePath, checkFileDTO.getPassword(), node.getIndex());


                    fileList.add(new ChangeSourceVo(node.getOriginName(), 0, suffix
                            , finalFilePath , node.getOriginName()
                            , node.getSize(), ""));

                    dto.setStatus(dto1.getStatus());
                    dto.setSuccess(dto1.getSuccess());
                    dto.setResult(dto1.getResult());

                    LogUtil.info("compressFileCopy dto=" + JsonUtils.beanToJson(dto));
                    /*File linkFile = new File(finalFilePath);
                    try {//创建硬链
                        if (!linkFile.exists()){
                            Files.createLink(Paths.get(finalFilePath), Paths.get(extractPath + node.getFileName()));
                            fileList.add(new ChangeSourceVo(node.getOriginName(), 0, suffix
                                    , finalFilePath , node.getFullName().replaceAll(GlobalConfig.separator, GlobalConfig.separatorTO)
                                    , node.getSize(), ""));
                        }
                    } catch (FileAlreadyExistsException e){//文件已存在, 忽略
                        LogUtil.error(e, "compressFileCopy 创建硬链接失败 finalFilePath=" + finalFilePath + ", orgPath=" + extractPath + node.getFileName());
                    } catch (Exception e){
                        LogUtil.error(e, "compressFileCopy error  创建硬链接失败 finalFilePath="+ finalFilePath + ", orgPath=" + extractPath + node.getFileName());
                    }*/
                    return;
                }
            }else if (!CollectionUtils.isEmpty(node.getChildList())){
                compressFileCopy(node.getChildList(), finalFolderPath, fileList, loginUser, checkFileDTO
                        , extractPath, i, iSimpleInArchive, dto);
            }
        }
    }


    private void compressFileCopyChangeList(List<CompressFileReader.FileNode> fileNodeList, String finalFolderPath, List<ChangeSourceVo> fileList, LoginUser loginUser, CheckFileDTO checkFileDTO
            , String extractPath, int i, ISimpleInArchive iSimpleInArchive, CompressFileDto dto){
        fileNodeList = fileNodeList.stream().sorted(Comparator.comparing(CompressFileReader.FileNode::isDirectory)).collect(Collectors.toList());

        for (CompressFileReader.FileNode node : fileNodeList) {
            if (node.isDirectory()) {
                fileList.add(new ChangeSourceVo(node.getOriginName().endsWith("/") ? node.getOriginName().substring(0, node.getOriginName().length() - 1) : node.getOriginName(),
                        1, finalFolderPath + node.getFullName() + "/"));
                if (!CollectionUtils.isEmpty(node.getChildList())) {
                    compressFileCopyChangeList(node.getChildList(), finalFolderPath, fileList, loginUser, checkFileDTO
                            , extractPath, i, iSimpleInArchive, dto);
                }
            } else {
                String suffix = FileUtil.getFileExtension(node.getOriginName());
                String finalFilePath = finalFolderPath + RandomUtil.getuuid() + System.currentTimeMillis()
                        + "_" + i + "_" + loginUser.getUserID() + "." + suffix;

                fileList.add(new ChangeSourceVo(node.getOriginName(), 0, suffix
                        , finalFilePath, node.getFullName().replaceAll(GlobalConfig.separator, GlobalConfig.separatorTO)
                        , node.getSize(), ""));
                // 单个解压
                CompressFileDto dto1 = CompressFileUtil.unzipOneFilePassword(iSimpleInArchive, finalFilePath, checkFileDTO.getPassword(), node.getIndex());

                dto.setStatus(dto1.getStatus());
                dto.setSuccess(dto1.getSuccess());
                dto.setResult(dto1.getResult());
                LogUtil.info("compressFileCopyChangeList dto=" + JsonUtils.beanToJson(dto));

                /*File linkFile = new File(finalFilePath);
                try {//创建硬链
                    if (!linkFile.exists()) {
                        Files.createLink(Paths.get(finalFilePath), Paths.get(extractPath + node.getFileName()));
                        fileList.add(new ChangeSourceVo(node.getOriginName(), 0, suffix
                                , finalFilePath, node.getFullName().replaceAll(GlobalConfig.separator, GlobalConfig.separatorTO)
                                , node.getSize(), ""));
                    }
                } catch (FileAlreadyExistsException e) {//文件已存在, 忽略
                    LogUtil.error(e, "compressFileCopy 创建硬链接失败 finalFilePath=" + finalFilePath + ", orgPath=" + extractPath + node.getFileName());
                } catch (Exception e) {
                    LogUtil.error(e, "compressFileCopy error  创建硬链接失败 finalFilePath" + finalFilePath + ", orgPath=" + extractPath + node.getFileName());

                }*/
            }
        }
    }

    @Override
    public boolean taskActionUnZip(CheckFileDTO checkFileDTO, Map<String, Object> resultMap, LoginUser loginUser){
        if (ObjectUtils.isEmpty(checkFileDTO.getTaskID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        resultMap.put("status", 0);
        resultMap.put("taskID", checkFileDTO.getTaskID());
        String key = GlobalConfig.async_key_unzip_file + checkFileDTO.getTaskID();
        String denyString = stringRedisTemplate.opsForValue().get(key);
        // 进度
        String progressKey = GlobalConfig.progress_key_unzip_file +  checkFileDTO.getTaskID();
        String progressString = stringRedisTemplate.opsForValue().get(progressKey);
        resultMap.put("progress", 0);
        resultMap.put("zipProgress", 0);
        if (!ObjectUtils.isEmpty(progressString)){
            resultMap.put("progress", progressString);
            resultMap.put("zipProgress", progressString);
        }
        LogUtil.info("taskActionUnZip key=" + key +"，denyString=" + denyString + "，progressString=" + progressString);
        if (ObjectUtils.isEmpty(denyString)){
            resultMap.put("status", 1);
            resultMap.put("progress", 100);
            resultMap.put("zipProgress", 100);
            return true;
        }
        if ("1".equals(denyString)) {
            resultMap.put("status", 1);
            resultMap.put("progress", 100);
            resultMap.put("zipProgress", 100);
            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete(progressKey);
        }
        return true;
    }
}


