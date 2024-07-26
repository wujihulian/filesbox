package com.svnlan.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.annotation.VisitRecord;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.*;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.service.*;
import com.svnlan.home.utils.ConvertUtil;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.UserAuthTool;
import com.svnlan.home.vo.CommonSourceVO;
import com.svnlan.home.vo.IoSourceAuthVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.utils.*;
import com.svnlan.webdav.MimeTypeEnum;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 9:03
 */

@RestController
@CrossOrigin
public class UploadController {
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    private UploadService uploadService;
    @Resource
    SystemSortTool systemSortTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    private UploadZipService uploadZipService;
    @Resource
    private FavService favService;
    @Resource
    AuthService authService;
    @Resource
    UserAuthTool userAuthTool;
    @Resource
    VideoGetService videoGetService;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    SourceFileService sourceFileService;


    @Value("${environment.type}")
    private String environmentType;


    @RequestMapping(value = "/api/disk/upload", method = RequestMethod.POST)
    public String upload(@Valid UploadDTO uploadDTO) {
        uploadDTO.setStartTime(System.currentTimeMillis());
        String uuid = RandomUtil.getuuid();

        String lockExecKey = String.format(GlobalConfig.lock_upload_md5_captcha, uploadDTO.getTenantId(),uploadDTO.getTaskID());
        stringRedisTemplate.opsForValue().setIfAbsent(lockExecKey, "1");
        try {
            String ip = IpUtil.getIp(HttpUtil.getRequest());
            LogUtil.info(String.format("/api/disk/upload 文件大小, " + uploadDTO.getSize() + ", ip," + ip + ", uuid" + uuid
                    + "，fileName=%s，chunks=%s，chunk=%s，hashMd5=%s ，sourceId=%s，busType=%s" ,uploadDTO.getName(), ObjectUtils.isEmpty(uploadDTO.getChunks()) ? 1 : uploadDTO.getChunks()
            , ObjectUtils.isEmpty(uploadDTO.getChunk()) ? 0 : uploadDTO.getChunk(), uploadDTO.getHashMd5(), uploadDTO.getSourceID()
                    , uploadDTO.getBusType()));
        } catch (Exception e) {
            LogUtil.error(e, "文件日志失败");
        }
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        if (ObjectUtils.isEmpty(loginUser)) {
            result = new Result(false, CodeMessageEnum.bindSignError.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        if (!ObjectUtils.isEmpty(uploadDTO.getFullPath())){
            uploadDTO.setSourceID(setSourceIdByFullPath(uploadDTO.getFullPath(), uploadDTO.getSourceID(), loginUser));
        }
        try {
            CommonSource commonSource = new CommonSource();
            String serverUrl = HttpUtil.getRequestRootUrl(null);
            commonSource.setDomain(serverUrl);
            if (ObjectUtils.isEmpty(uploadDTO.getTaskID())) {
                uploadDTO.setTaskID(uploadDTO.getHashMd5() + "_" + uploadDTO.getSourceID());
            }
            uploadDTO.setTenantId(loginUser.getTenantId());
            CommonSourceVO commonSourceVO = null;
            if (!ObjectUtils.isEmpty(uploadDTO.getPath()) && !"0".equals(uploadDTO.getPath())) {
                // 编辑上传覆盖文件
                commonSourceVO = uploadService.fileUpload(uploadDTO, loginUser, commonSource);
            } else {
                // 上传文件
                commonSourceVO = uploadService.upload(uploadDTO, loginUser, commonSource);
            }

            if (!ObjectUtils.isEmpty(commonSourceVO.getPath())){
                commonSourceVO.setPreviewPath(FileUtil.getShowImageUrl(commonSourceVO.getPath(), commonSourceVO.getName()));
            }

            boolean success =(!ObjectUtils.isEmpty(commonSourceVO.getState()) && commonSourceVO.getState().equals(1)) ? true : false;
            result = new Result(commonSourceVO.getState().equals(1),
                    success ? CodeMessageEnum.success.getCode() : CodeMessageEnum.explorerError.getCode(),
                    commonSourceVO);

            // 异步合并则不转码，转码放到异步执行
            if (ObjectUtils.isEmpty(commonSource.getCheckMerge()) || !commonSource.getCheckMerge()) {
                commonSource.setUserID(loginUser.getUserID());
                // 试着转码
                uploadService.tryToConvertFile(commonSourceVO, commonSource, uploadDTO.getBusType());
            }

        } catch (SvnlanRuntimeException e) {
            LogUtil.error("上传: " + e.getMessage());
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            uploadDTO.setFile(null);
            LogUtil.error(e, "上传失败, dto:" + JsonUtils.beanToJson(uploadDTO) + ", e:" + e.getMessage());
            result = new Result(false, CodeMessageEnum.uploadError.getCode(), null);
        } finally {
            stringRedisTemplate.delete(lockExecKey);
            stringRedisTemplate.delete(systemSortTool.getUserSpaceKey(loginUser.getUserID()));
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        }
        String resultStr = JsonUtils.beanToJson(result);
        LogUtil.info("返回, " + resultStr + ", uuid" + uuid );
        return resultStr;
    }

    private Long setSourceIdByFullPath(String fullPath, Long sourceID, LoginUser loginUser){
        if (!ObjectUtils.isEmpty(fullPath)){
            int lastSlashIndex = fullPath.lastIndexOf("/");
            String name = fullPath.substring(0, lastSlashIndex);
            AddCloudDirectoryDTO addCloudDirectoryDTO = new AddCloudDirectoryDTO();
            addCloudDirectoryDTO.setSourceID(sourceID);
            addCloudDirectoryDTO.setName(name);
            Long sourceId = sourceFileService.mkDir(addCloudDirectoryDTO, loginUser);
            return sourceId;
        }
        return sourceID;
    }

    /**
     * @Description: 查看文件是否存在, 秒传
     * @params: [checksum]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/upload/check", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Result check(@Valid CheckFileDTO checkFileDTO) {
        String path = ObjectUtils.isEmpty(checkFileDTO.getPath()) ? "file" : checkFileDTO.getPath();
        LoginUser loginUser = loginUserUtil.getLoginUser();
        if (!ObjectUtils.isEmpty(checkFileDTO.getFullPath())){
            checkFileDTO.setSourceID(setSourceIdByFullPath(checkFileDTO.getFullPath(), checkFileDTO.getSourceID(), loginUser));
        }
        switch (path) {
            case "chunk":
                return this.checkChunk(checkFileDTO, loginUser);
            case "file":
            default:
                return this.checkFile(checkFileDTO, loginUser);
        }
    }

    public Result checkFile(CheckFileDTO checkFileDTO, LoginUser loginUser) {
        Map<String, Object> resultMap = null;
        String name = null;
        if (!ObjectUtils.isEmpty(checkFileDTO.getFileName())){
            name = checkFileDTO.getFileName();
        }else  if (!ObjectUtils.isEmpty(checkFileDTO.getName())){
            name = checkFileDTO.getName();
        }
        String key = "checkFile_" + checkFileDTO.getSourceID() + "_" + checkFileDTO.getHashMd5() + "_" + name;
        if (!ObjectUtils.isEmpty(name)){
            final Boolean setFlag = this.stringRedisTemplate.opsForValue().setIfAbsent(key, "1");
            if (setFlag == null || !setFlag) {
                resultMap.put("fileExists", true);
                resultMap.put("sourceId", 0);
                return new Result(true, CodeMessageEnum.success.getCode(), resultMap);
            }
        }
        try {

            if (!ObjectUtils.isEmpty(checkFileDTO.getPath()) && !"0".equals(checkFileDTO.getPath())) {
                // 编辑上传覆盖文件
                resultMap = uploadService.checkFileReplace(checkFileDTO, loginUser);
            } else {
                resultMap = uploadService.checkFile(checkFileDTO, loginUser);
            }
            if (!ObjectUtils.isEmpty(resultMap)){
                LogUtil.info("上传：秒传：" + checkFileDTO.getTaskID() + " resultMap=" + JsonUtils.beanToJson(resultMap));
            }
            return new Result(true, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "上传：秒传：" + checkFileDTO.getTaskID() + "秒传失败");
        } finally {
            stringRedisTemplate.delete(key);
            stringRedisTemplate.delete(systemSortTool.getUserSpaceKey(loginUser.getUserID()));
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        }
        return new Result(false, CodeMessageEnum.uploadError.getCode(), null);
    }

    public Result checkChunk(CheckFileDTO checkChunkDTO, LoginUser loginUser) {
        try {
            Map<String, Object> resultMap = uploadService.checkChunk(checkChunkDTO, loginUser);
            return new Result(true, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {

        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
     * @Description: 预览
     * @params: [response, previewAttachDTO]
     * @Return: void
     * @Author: sulijuan
     * @Date: 2023/2/17 10:43
     * @Modified:
     */
    @VisitRecord(isAsync = true)
    @RequestMapping(value = "/api/disk/preview", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String preview(HttpServletRequest request ,HttpServletResponse response, CheckFileDTO checkFileDTO) {
        if (ObjectUtils.isEmpty(checkFileDTO.getShareCode())
                && ObjectUtils.isEmpty(checkFileDTO.getHash())) {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            if (ObjectUtils.isEmpty(loginUser)) {
                Result result = new Result(false, CodeMessageEnum.bindSignError.getCode(), null);
                return JsonUtils.beanToJson(result);
            }
            checkFileDTO.setLoginUser(loginUser);
        }
        String path = ObjectUtils.isEmpty(checkFileDTO.getPath()) ? "info" : checkFileDTO.getPath();
        switch (path) {
            case "attachment":
                this.previewAttach(response, checkFileDTO);
                break;
            case "info":
                return this.getPreviewInfo(checkFileDTO);
            case "edit":
                return this.getPreviewEditInfo(checkFileDTO);
            case "refresh":
                return this.getPreviewRefreshInfo(checkFileDTO);
            case "fileUrl":
                return this.getPreviewFileUrl(checkFileDTO);
            default:
                this.previewAttach(response, checkFileDTO);
                break;
        }
        return "";
    }

    /**
     * @Description: 预览
     * @params: [response, checkFileDTO]
     * @Return: void
     * @Author: sulijuan
     * @Date: 2023/2/17 11:24
     * @Modified:
     */
    public void previewAttach(HttpServletResponse response, CheckFileDTO checkFileDTO) {
        String resultStr = "";
        try {
            resultStr = uploadService.returnSource(checkFileDTO, null, response);
//            response.sendRedirect(resultStr);
        } catch (SvnlanRuntimeException e) {
            resultStr = e.getMessage();
        } catch (Exception e) {
            LogUtil.error(e, "预览失败" + JsonUtils.beanToJson(checkFileDTO));
        }
//        return resultStr;

    }

    @RequestMapping(value = "/api/disk/preview/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void previewAttachWithName(HttpServletResponse response, CheckFileDTO previewAttachDTO, @PathVariable("fileName") String passedFileName) {
        String resultStr = "";
        try {
            resultStr = uploadService.returnSource(previewAttachDTO, passedFileName, response);
//            response.sendRedirect(resultStr);
        } catch (SvnlanRuntimeException e) {
            resultStr = e.getMessage();
        } catch (Exception e) {
            LogUtil.error(e, "预览失败" + JsonUtils.beanToJson(previewAttachDTO));
        }
//        return resultStr;
    }

    /**
     * @Description: 预览信息
     * @params: [getCloudPreviewDTO]
     * @Return: java.lang.String
     * @Modified:
     */
    public String getPreviewInfo(CheckFileDTO checkFileDTO) {
        Result result;
        try {
            Map<String, Object> re = uploadService.getPreviewInfo(checkFileDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "云盘获取预览信息失败");
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    public String getPreviewEditInfo(CheckFileDTO checkFileDTO) {
        Result result;
        try {
            Map<String, Object> re = favService.getPreviewEditInfo(checkFileDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "云盘获取文档编辑链接信息失败");
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    public String getPreviewRefreshInfo(CheckFileDTO checkFileDTO) {
        Result result;
        try {
            Map<String, Object> re = favService.getPreviewRefreshInfo(checkFileDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "云盘获取文档预览信息");
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    public String getPreviewFileUrl(CheckFileDTO checkFileDTO) {
        return favService.getPreviewFileUrl(checkFileDTO);
    }

    /**
     * @Description: 文件操作
     * @params: [updateFileDTO]
     * @Return: java.lang.String
     * @Modified:
     */
    @VisitRecord(isAsync = true)
    @RequestMapping(value = "/api/disk/operation", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String copyFile(@Valid @RequestBody CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            boolean success = uploadService.updateFile(updateFileDTO, resultMap, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerOneRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, "operation操作失败，updateFileDTO=" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, e.getErrorCode(), e.getMessage(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "云盘文件复制失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        } finally {
            stringRedisTemplate.delete(systemSortTool.getUserSpaceKey(loginUser.getUserID()));
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * 预览/下载
     */
    @RequestMapping(value = "/api/disk/attachment/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getAttachmentWithName(HttpServletResponse response, @Valid GetAttachmentDTO getAttachmentDTO,
                                        @PathVariable("fileName") String passedFileName) {
        String resultStr = "";
        try {
            if ("dev".equals(environmentType)){
                getAttachmentDTO.setGetInfo(2);
            }
            Map<String, Object> resultMap = new HashMap<>();
            resultStr = uploadService.getAttachment(response, getAttachmentDTO, passedFileName, resultMap);
            LogUtil.info("resultStr=" + resultStr + "，attachment" + JsonUtils.beanToJson(getAttachmentDTO) + "，resultMap=" + JsonUtils.beanToJson(resultMap));
            if (!StringUtil.isEmpty(resultStr) && resultStr.indexOf("http") == 0) {
                //获取信息
                if (getAttachmentDTO.getGetInfo() == 1) {
                    Result result = Result.returnSuccess(resultMap);
                    LogUtil.info("attachment true");
                    return JsonUtils.beanToJson(result);
                }else {
                    response.sendRedirect(resultStr);
                }
            }else {
                outFile(response,resultStr,passedFileName, true);
                return "";
            }
        } catch (SvnlanRuntimeException e) {
            resultStr = e.getMessage();
        } catch (Exception e) {
            LogUtil.error(e, "预览失败" + JsonUtils.beanToJson(getAttachmentDTO));
        }
        LogUtil.info("resultStr=" + resultStr + "，attachment" + JsonUtils.beanToJson(getAttachmentDTO));

        return resultStr;
    }
    /**
     * 视频切图预览/下载
     */
    @RequestMapping(value = "/api/disk/video/img/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getVideoImgAttachmentWithName(HttpServletResponse response, HttpServletRequest request, @Valid VideoCommonDto videoCommonDto,
                                                @PathVariable("fileName") String passedFileName) {
        String resultStr = "";
        boolean check = true;
        try {
            Map<String, Object> resultMap = new HashMap<>();
            resultStr = videoGetService.getVideoShearImg(response, videoCommonDto, passedFileName, resultMap);
            /*if (!StringUtil.isEmpty(resultStr) && resultStr.indexOf("http") == 0) {
                check = true;
                //获取信息
                response.sendRedirect(resultStr);
            }*/
        } catch (SvnlanRuntimeException e) {
            resultStr = e.getMessage();
            check = false;
            LogUtil.error(e, "getVideoImgAttachmentWithName 预览失败" + JsonUtils.beanToJson(videoCommonDto) + "，resultStr=" + resultStr);
        } catch (Exception e) {
            check = false;
            LogUtil.error(e, "getVideoImgAttachmentWithName 预览失败" + JsonUtils.beanToJson(videoCommonDto) + "，resultStr=" + resultStr);
        }
        LogUtil.info(check + "-check，getVideoImgAttachmentWithName resultStr=" + resultStr + "，attachment" + JsonUtils.beanToJson(videoCommonDto));

        boolean isDown = false;
        if (!ObjectUtils.isEmpty(videoCommonDto.getIsDown()) && "1".equals(videoCommonDto.getIsDown())){
            isDown = true;
        }

        String ext = resultStr.substring(resultStr.lastIndexOf(".") + 1);
        if ("mp4".equals(ext)){
            outMp4File(resultStr, request, response,passedFileName);
            return;
        }

        if (check){
            outFile(response,resultStr,passedFileName, isDown);
        }
    }

    @RequestMapping(value = "/api/disk/fileOut/{fileName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String diskFileOut(HttpServletResponse response, HttpServletRequest request, @Valid VideoCommonDto videoCommonDto,
                              @PathVariable("fileName") String passedFileName) {
        String resultStr = "";
        try {
            resultStr = videoGetService.fileOutPath(videoCommonDto, passedFileName);
        } catch (SvnlanRuntimeException e) {
            resultStr = e.getMessage();
            LogUtil.error(e, "diskFileOut 预览失败" + JsonUtils.beanToJson(videoCommonDto) + "，resultStr=" + resultStr);
            return JsonUtils.beanToJson(new Result(false, e.getErrorCode(), null));
        } catch (Exception e) {
            LogUtil.error(e, "diskFileOut 预览失败" + JsonUtils.beanToJson(videoCommonDto) + "，resultStr=" + resultStr);
            return JsonUtils.beanToJson(new Result(false, CodeMessageEnum.explorerError.getCode(), null));
        }
        LogUtil.info("true-check，diskFileOut resultStr=" + resultStr + "，attachment" + JsonUtils.beanToJson(videoCommonDto));

        if (ObjectUtils.isEmpty(resultStr)){
            return JsonUtils.beanToJson(new Result(false, CodeMessageEnum.pathNotExists.getCode(), null));
        }

        String ext = resultStr.substring(resultStr.lastIndexOf(".") + 1);
        if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(ext)){
            outMp4File(resultStr, request, response,passedFileName);
            return JsonUtils.beanToJson(new Result(true, CodeMessageEnum.success.getCode(), null));
        }
        outFile(response,resultStr,passedFileName, true);
        return JsonUtils.beanToJson(new Result(true, CodeMessageEnum.success.getCode(), null));
    }

    private void outFile(HttpServletResponse response, String resultStr, String passedFileName, boolean isDown){

        File file = new File(resultStr);
        if (!file.exists()) {
            LogUtil.error("outFile  该资源不存在或不可预览");
            return;
        }
        // 获取文件后缀
        String ext = resultStr.substring(resultStr.lastIndexOf(".") + 1);

        try (FileInputStream fis = new FileInputStream(file); ServletOutputStream sos = response.getOutputStream()) {
            String mineType = MimeTypeEnum.getContentType(ext);
            response.setContentType(mineType);
            if (isDown) {
                response.setHeader("Content-Disposition", "attachment;filename="
                        // 解决中文名称乱码的问题
                        + new String(passedFileName.getBytes(StandardCharsets.UTF_8), StandardCharsets.ISO_8859_1));
            }
            try {
                IOUtils.copy(fis, sos);
            }catch (Exception e){
                LogUtil.error("outFile copy 拷贝文件流出错 resultStr=" + resultStr);
            }finally {
                if (sos != null){
                    try {
                        sos.close();
                    }catch (Exception e){
                    }
                }
                if (fis != null){
                    try {
                        fis.close();
                    }catch (Exception e){
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.error("outFile 拷贝文件流出错 resultStr=" + resultStr);
        }
    }


    //path为本地文件路劲
    public void outMp4File(String path, HttpServletRequest request, HttpServletResponse response, String passedFileName) {

        LogUtil.info("outMp4File path=" + path);
        RandomAccessFile targetFile = null;
        OutputStream outputStream = null;
        try {
            outputStream = response.getOutputStream();
            response.reset();
            //获取请求头中Range的值
            String rangeString = request.getHeader(HttpHeaders.RANGE);

            response.setHeader(HttpHeaders.ACCEPT_RANGES, "bytes");
            response.setHeader(HttpHeaders.CONTENT_TYPE, "video/mp4");
            //打开文件
            File file = new File(path);
            if (file.exists()) {
                //使用RandomAccessFile读取文件
                targetFile = new RandomAccessFile(file, "r");
                long fileLength = targetFile.length();
                long requestSize = (int) fileLength;
                //分段下载视频
                if (StringUtils.hasText(rangeString)) {
                    //从Range中提取需要获取数据的开始和结束位置
                    long requestStart = 0, requestEnd = 0;
                    String[] ranges = rangeString.split("=");
                    if (ranges.length > 1) {
                        String[] rangeDatas = ranges[1].split("-");
                        requestStart = Integer.parseInt(rangeDatas[0]);
                        if (rangeDatas.length > 1) {
                            requestEnd = Integer.parseInt(rangeDatas[1]);
                        }
                    }
                    if (requestEnd != 0 && requestEnd > requestStart) {
                        requestSize = requestEnd - requestStart + 1;
                    }
                    //根据协议设置请求头
                    if (!StringUtils.hasText(rangeString)) {
                        response.setHeader(HttpHeaders.CONTENT_LENGTH, fileLength + "");
                    } else {
                        long length;
                        if (requestEnd > 0) {
                            length = requestEnd - requestStart + 1;
                            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + length);
                            response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + requestStart + "-" + requestEnd + "/" + fileLength);
                        } else {
                            length = fileLength - requestStart;
                            response.setHeader(HttpHeaders.CONTENT_LENGTH, "" + length);
                            response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + requestStart + "-" + (fileLength - 1) + "/"
                                    + fileLength);
                        }
                    }
                    //断点传输下载视频返回206
                    response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                    //设置targetFile，从自定义位置开始读取数据
                    targetFile.seek(requestStart);
                } else {
                    //如果Range为空则下载整个视频
                    //response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + passedFileName);
                    //设置文件长度
                    response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileLength));
                    response.setHeader(HttpHeaders.CONTENT_RANGE, "bytes " + 0 + "-" + (fileLength - 1) + "/"
                            + fileLength);
                }

                //从磁盘读取数据流返回
                byte[] cache = new byte[4096];
                try {
                    while (requestSize > 0) {
                        int len = targetFile.read(cache);
                        if (requestSize < cache.length) {
                            outputStream.write(cache, 0, (int) requestSize);
                        } else {
                            outputStream.write(cache, 0, len);
                            if (len < cache.length) {
                                break;
                            }
                        }
                        requestSize -= cache.length;
                    }
                } catch (IOException e) {
                    // tomcat原话。写操作IO异常几乎总是由于客户端主动关闭连接导致，所以直接吃掉异常打日志
                    //比如使用video播放视频时经常会发送Range为0- 的范围只是为了获取视频大小，之后就中断连接了
                    LogUtil.error(e, "play error");
                }
            } else {
                throw new RuntimeException("文件路劲有误");
            }
            outputStream.flush();
        } catch (Exception e) {
            LogUtil.error(e,"文件传输错误");
            throw new RuntimeException("文件传输错误");
        }finally {
            if(outputStream != null){
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LogUtil.error(e,"流释放错误");
                }
            }
            if(targetFile != null){
                try {
                    targetFile.close();
                } catch (IOException e) {
                    LogUtil.error(e,"文件流释放错误");
                }
            }
        }
    }
    /**
     * @Description: txt 写入文件
     * @params: [uploadDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/1 13:29
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/fileSave", method = RequestMethod.POST)
    public String fileSave(@RequestBody SaveUploadDTO uploadDTO) {
        String uuid = RandomUtil.getuuid();
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        try {
            if (ObjectUtils.isEmpty(uploadDTO.getTaskID())) {
                uploadDTO.setTaskID(RandomUtil.getuuid());
            }
            CommonSource commonSourceVO = uploadService.fileSave(uploadDTO, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), commonSourceVO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error("上传fileSave: " + e.getMessage());
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            uploadDTO.setFile(null);
            LogUtil.error(e, "上传失败fileSave, dto:" + JsonUtils.beanToJson(uploadDTO) + ", e:" + e.getMessage());
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }finally {
            stringRedisTemplate.delete(systemSortTool.getUserSpaceKey(loginUser.getUserID()));
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        }
        String resultStr = JsonUtils.beanToJson(result);
        LogUtil.info("返回, " + resultStr + ", uuid" + uuid);
        return resultStr;
    }

    /**
     * @Description: 收藏夹置顶
     * @params: [updateFileDTO]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/fav/moveTop", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String moveTop(@Valid @RequestBody CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            boolean success = favService.moveTop(updateFileDTO, resultMap, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "收藏夹置顶失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 收藏夹置底
     * @params: [updateFileDTO]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/fav/moveBottom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String moveBottom(@Valid @RequestBody CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            boolean success = favService.moveBottom(updateFileDTO, resultMap, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "收藏夹置底失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * zip 压缩
     */
    @VisitRecord(isAsync = true)
    @RequestMapping(value = "/api/disk/zip", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String zip(@RequestBody CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            boolean success = uploadZipService.zipFile(updateFileDTO, resultMap, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
            if (!ObjectUtils.isEmpty(loginUser)){
                stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            }
        } catch (SvnlanRuntimeException e) {
            if (CodeMessageEnum.downLimitSizeError.getCode().equals(e.getErrorCode())){
                result = new Result(true, e.getErrorCode(), e.getMessage(), resultMap);
            }else {
                result = new Result(false, e.getErrorCode(), e.getMessage(), resultMap);
            }
        } catch (Exception e) {
            LogUtil.error(e, "压缩zip失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/zip/taskAction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String zipTaskAction(HttpServletResponse response, CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        boolean success = true;
        try {
            success = uploadZipService.taskAction(updateFileDTO, resultMap);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "taskAction压缩zip失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }

        if (!ObjectUtils.isEmpty(updateFileDTO.getOperation()) && "down".equals(updateFileDTO.getOperation())) {
            if (!ObjectUtils.isEmpty(resultMap) && resultMap.containsKey("finalFilePath")) {
                String cdnPath = systemSortTool.getCdnPath(resultMap.get("finalFilePath").toString(), resultMap.get("fileName").toString());

                //获取信息
                if (!ObjectUtils.isEmpty(updateFileDTO.getGetInfo()) && updateFileDTO.getGetInfo() == 1) {
                    resultMap.put("cdnPath", cdnPath);
                    result = Result.returnSuccess(resultMap);
                    return JsonUtils.beanToJson(result);
                } else {
                    resultMap.put("downloadUrl", cdnPath);
                    result = Result.returnSuccess(resultMap);
                    /*try {
                        response.sendRedirect(cdnPath);
                    }catch (Exception e){
                        LogUtil.error(e, "taskAction zip 下载失败");
                    }*/
                }
                LogUtil.info("taskAction  ，*** resultMap=" + JsonUtils.beanToJson(resultMap));
            }
        }else {
            if (!ObjectUtils.isEmpty(resultMap) && resultMap.containsKey("status") && "1".equals(resultMap.get("status").toString())
                    && !ObjectUtils.isEmpty(loginUser)) {
                stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            }
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 解压
     * @params: [sourceID]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/3/24 14:16
     * @Modified:
     */
    @VisitRecord(isAsync = true)
    @RequestMapping(value = "/api/disk/unZip", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String unZip(@RequestBody CheckFileDTO checkFileDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        List<CommonSource> list = null;
        try {
            if (ObjectUtils.isEmpty(checkFileDTO.getTaskID())){
                checkFileDTO.setTaskID(RandomUtil.getuuid());
            }
            list = uploadZipService.unZip(checkFileDTO, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), checkFileDTO);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage(), checkFileDTO);
        } catch (Exception e) {
            LogUtil.error(e, "解压失败 checkFileDTO=" + checkFileDTO);
            result = new Result(false, CodeMessageEnum.system_error.getCode(), checkFileDTO);
        }
        // 视频转码
        if (!CollectionUtils.isEmpty(list)) {
            for (CommonSource source : list) {
                ConvertDTO convertDTO = new ConvertDTO();
                convertDTO.setBusId(source.getSourceID());
                convertDTO.setBusType("cloud");
                convertDTO.setOtherType("unZip");
                String serverUrl = HttpUtil.getRequestRootUrl(null);
                source.setDomain(serverUrl);
                convertDTO.setDomain(serverUrl);
                convertDTO.setTenantId(tenantUtil.getTenantIdByServerName());
                convertUtil.doConvert(convertDTO, source);
            }
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/unZip/taskAction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String unZipTaskAction(HttpServletResponse response, CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        boolean success = true;
        try {
            success = uploadZipService.taskActionUnZip(updateFileDTO, resultMap, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "解压 taskAction失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }

        if (!ObjectUtils.isEmpty(resultMap) && resultMap.containsKey("status") && "1".equals(resultMap.get("status").toString())) {
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        }

        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 设置文档权限
     * @params: [checkFileDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/4/11 17:16
     * @Modified: 文档权限 1查看、2搜索、3预览、4下载、5上传、6压缩、7解压、8编辑、9新增、10删除、11分享、12评论、13动态、14管理权限、15自定义删除
     */
    @RequestMapping(value = "/api/disk/setAuth", method = RequestMethod.POST)
    public String setAuth(@RequestBody CheckFileDTO checkFileDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            authService.setAuth(checkFileDTO, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), checkFileDTO);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage(), checkFileDTO);
        } catch (Exception e) {
            LogUtil.error(e, "设置文档权限 checkFileDTO=" + checkFileDTO);
            result = new Result(false, CodeMessageEnum.system_error.getCode(), checkFileDTO);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 获取文档权限
     * @params: [checkFileDTO]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Modified:
     */
    @GetMapping(value = "/api/disk/getSourceAuth")
    public String getSourceAuth(CheckFileDTO checkFileDTO) {
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            List<IoSourceAuthVo> list = authService.getSourceAuth(checkFileDTO, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), list);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), checkFileDTO);
        } catch (Exception e) {
            LogUtil.error(e, "获取文档权限 checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), checkFileDTO);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/preview/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPreview(HttpServletResponse response, CheckFileDTO checkFileDTO) {

        Map<String, Object> re = null;

        Result result;
        try {
            re = uploadService.getFileContent(checkFileDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), checkFileDTO);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/preview/get checkFileDTO=" + checkFileDTO);
            result = new Result(false, CodeMessageEnum.system_error.getCode(), checkFileDTO);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/preview/blob", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPreviewBlob(HttpServletResponse response, CheckFileDTO checkFileDTO) {

        Map<String, Object> re = null;

        Result result;
        try {
            re = uploadService.getFileContentBlob(checkFileDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), checkFileDTO);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/preview/blob checkFileDTO=" + checkFileDTO);
            result = new Result(false, CodeMessageEnum.system_error.getCode(), checkFileDTO);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/preview/byte", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPreviewByte(HttpServletResponse response, CheckFileDTO checkFileDTO) {

        Map<String, Object> re = null;

        Result result;
        try {
            re = uploadService.getFileContentByte(checkFileDTO);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), checkFileDTO);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/preview/byte checkFileDTO=" + checkFileDTO);
            result = new Result(false, CodeMessageEnum.system_error.getCode(), checkFileDTO);
        }
        return JsonUtils.beanToJson(result);
    }
    /**
       * @Description: 校验url是否链接成功
       * @params:  [checkFileDTO]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/10/16 13:15
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/checkUrlConnection", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getPreviewByte(CheckFileDTO checkFileDTO) {
        Result result;
        try {
            boolean check = UrlDUtil.checkUrlConnection(checkFileDTO.getPath());
            result = new Result(check, check ? CodeMessageEnum.success.getCode() : CodeMessageEnum.sendFail.getCode() , null);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), checkFileDTO);
        } catch (Exception e) {
            LogUtil.error(e, "/api/disk/preview/byte checkFileDTO=" + checkFileDTO);
            result = new Result(false, CodeMessageEnum.system_error.getCode(), checkFileDTO);
        }
        return JsonUtils.beanToJson(result);
    }

}
