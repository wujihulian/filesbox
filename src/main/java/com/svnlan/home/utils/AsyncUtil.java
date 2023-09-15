package com.svnlan.home.utils;

import com.google.common.base.Joiner;
import com.svnlan.common.GlobalConfig;
import com.svnlan.enums.EventEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoFileDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.utils.video.VideoGetUtil;
import com.svnlan.home.utils.zip.ZipUtils;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dao.CommonInfoDao;
import com.svnlan.manage.domain.CommonSeo;
import com.svnlan.manage.dto.CommonInfoDto;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.user.dao.GroupSourceDao;
import com.svnlan.user.dao.SystemOptionDao;
import com.svnlan.user.domain.GroupSource;
import com.svnlan.user.vo.GroupSizeVo;
import com.svnlan.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/12 13:06
 */
@Component
public class AsyncUtil {

    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    SystemSortTool systemSortTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    GroupSourceDao groupSourceDao;
    @Resource
    IoSourceHistoryDao ioSourceHistoryDao;
    @Resource
    IoFileDao ioFileDao;
    @Resource
    CommonInfoDao commonInfoDao;
    @Resource
    SystemOptionDao systemOptionDao;
    @Resource
    ConvertUtil convertUtil;
    @Resource
    SourceHistoryUtil sourceHistoryUtil;

    @Value("${cdn.domain}")
    private String cdnDomain;
    @Value("${info.common.htmlPath.name}")
    private String infoHtmlPath;

    /**
     * @Description: 初始化装扮保存路径
     * @Modified:
     */
    private String designBasePath;
    private String pathDefault;
    private String templatePath;
    @PostConstruct
    public void setDesignBasePath(){
        // 已改
        try {
            Properties properties = PropertiesUtil.getConfig("upconfig.properties");
            pathDefault = properties.getProperty("home.path.default");
            designBasePath = properties.getProperty("home.savePath");
            templatePath = properties.getProperty("template.savePath");
        } catch (Exception e){
            designBasePath = "/uploads/design/home/";
            templatePath = "/uploads/design/template/";
            pathDefault = "/uploads";

        }
    }

    /**
     * @Description: 文件压缩
     */
    @Async(value = "asyncTaskExecutor")
    public void asyncZipFile(CheckFileDTO checkFileDTO, List<IOSourceVo> copyListByLevel) {
        long ms = System.currentTimeMillis();
        LogUtil.info("  asyncZipFile begin " + ms);
        if (!Arrays.asList(GlobalConfig.ZIP_SHOW_TYPE_ARR).contains(checkFileDTO.getFileType())){
            LogUtil.error("asyncZipFile 压缩类型不正确checkFileDTO=" + JsonUtils.beanToJson(checkFileDTO));
            return;
        }

        boolean isDown = false;
        if (!ObjectUtils.isEmpty(checkFileDTO.getOperation()) && "down".equals(checkFileDTO.getOperation())){
            isDown = true;
        }

        String finalFolderPath = checkFileDTO.getFinalFolderPath();
        // 存放复制文件的临时文件名
        String tempFolder = checkFileDTO.getTempFolder() ;
        // 压缩包的路径及名称
        String finalFilePath = checkFileDTO.getFinalFilePath();

        CommonSource commonSource = checkFileDTO.getCommonSource();

        // 获取需要打包压缩的文件列表
        List<Long> sourceIDList = new ArrayList<>();
        List<String> sourceLevelList = new ArrayList<>();
        for (SourceOpDto dto : checkFileDTO.getDataArr()) {
            sourceIDList.add(dto.getSourceID());
            if ("folder".equals(dto.getType())) {
                sourceLevelList.add(dto.getParentLevel() + dto.getSourceID() + ",");
            }
        }
        List<IOSourceVo> attachments = ioSourceDao.copySourcePathList(sourceIDList);
        if (CollectionUtils.isEmpty(attachments)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.rptSelectTips.getCode());
        }
        Map<Long, IOSourceVo> mainAttsMap = attachments.stream().collect(Collectors.toMap(IOSourceVo::getSourceID, Function.identity(), (v1, v2) -> v2));
        if (CollectionUtils.isEmpty(copyListByLevel) && !CollectionUtils.isEmpty(sourceLevelList)) {
            copyListByLevel = ioSourceDao.copySourcePathListByLevel(sourceLevelList);
        }
        if (!CollectionUtils.isEmpty(copyListByLevel)) {
            attachments.addAll(copyListByLevel);
        }
        // 按父类升序
        attachments = attachments.stream().sorted(Comparator.comparing(IOSourceVo::getParentLevel)).collect(Collectors.toList());

        LogUtil.info("zip  attachments=" + JsonUtils.beanToJson(attachments));
        // path结构
        List<String> levelList = null;
        List<Long> parentList = null;
        Map<Long, String> pathMap = new HashMap<>(0);
        pathMap.put(commonSource.getSourceID(), commonSource.getSourceName());
        for (IOSourceVo vo : attachments){
            if (1 == vo.getIsFolder()){
                pathMap.put(vo.getSourceID(), vo.getName());
            }
            // 填充目录
            vo.setPathDisplay(systemSortTool.getSourcePathDisplay(vo.getParentLevel().replace(commonSource.getParentLevel()+ commonSource.getSourceID() + ",", ""), pathMap, levelList, parentList));
        }

        LogUtil.info("asyncZipFile  attachments=" + JsonUtils.beanToJson(attachments));

        /* 优化zip 按目录结构创建并复制文件
        ZipUtils.addFile(attachments, finalFolderPath + tempFolder + "/");*/
        // 按目录结构创建
        ZipUtils.addFileNew(attachments, finalFolderPath + tempFolder + "/");

        long totalLength = 0;
        for (IOSourceVo vo : attachments){
            if (0 == vo.getIsFolder()){
                totalLength = totalLength + vo.getSize();
            }
        }
        LogUtil.info("zipFile finalFilePath=" + finalFilePath + ",attachments=" + JsonUtils.beanToJson(attachments));
        /**  打包 */
        try {

            /* 优化zip  根据fileType创建不同压缩包
            switch (checkFileDTO.getFileType()){
                case "zip":
                    ZipUtils.toZip(finalFolderPath + tempFolder + "/", finalFilePath, false, true, stringRedisTemplate
                            , checkFileDTO.getTaskID(), totalLength);
                    break;
                case "tar":
                    TarUtils.toZip(finalFolderPath + tempFolder + "/", finalFilePath, false, true);
                    break;
            }*/

            ZipUtils.toZipNewList(finalFolderPath + tempFolder + "/", finalFilePath, stringRedisTemplate
                    , checkFileDTO, true, attachments, mainAttsMap);

            stringRedisTemplate.opsForValue().set(GlobalConfig.async_key_zip_file + checkFileDTO.getTaskID(), "1", 20, TimeUnit.MINUTES);
        }catch (Exception e){
            LogUtil.error(e);
            stringRedisTemplate.delete(GlobalConfig.async_key_zip_file + checkFileDTO.getTaskID());
        }
        // 修改属性

        Long size = 0L;
        String serverChecksum = "";
        //最终文件
        File finalFile = new File(finalFilePath);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(finalFile);
            serverChecksum = DigestUtils.md5DigestAsHex(fis);
            size = finalFile.length();
            fis.close();
        } catch (IOException e) {
            LogUtil.error(e.getMessage(), " addSourceInfo 压缩文件失败");
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (Exception e) {
                    LogUtil.error(e);
                }
            }
        }
        if (size > 0 && !isDown){
            // 更新
            Map<String, Object> paramMap = new HashMap<>(5);
            paramMap.put("size", size);
            paramMap.put("fileSize", size);
            paramMap.put("modifyUser", checkFileDTO.getUserID());
            paramMap.put("sourceID", checkFileDTO.getBusId());
            paramMap.put("fileID", checkFileDTO.getFileID());
            paramMap.put("hashMd5", serverChecksum);


            IOSource sourceUpdate = new IOSource();
            sourceUpdate.setSize(size);
            sourceUpdate.setModifyUser(checkFileDTO.getUserID());
            sourceUpdate.setSourceID(checkFileDTO.getBusId());
            sourceUpdate.setFileID(checkFileDTO.getFileID());
            sourceUpdate.setHashMd5(serverChecksum);

            // 修改文件source修改人、file文件大小
            try {
                ioSourceDao.updateFileSize(sourceUpdate);
            }catch (Exception e){
                LogUtil.error(e, " asyncZipFile updateFileSize error paramMap=" + JsonUtils.beanToJson(paramMap));
            }
            try {
                ioSourceDao.updateSourceModifyUser(sourceUpdate);
            }catch (Exception e){
                LogUtil.error(e, " asyncZipFile updateSourceModifyUser error paramMap=" + JsonUtils.beanToJson(paramMap));
            }
            // 修改上层文件夹size
            updateMemory(size, 0L, checkFileDTO.getUserID(), checkFileDTO.getTargetType(), checkFileDTO.getParentLevel());
        }
        stringRedisTemplate.opsForValue().set(GlobalConfig.progress_key_zip_file + checkFileDTO.getTaskID(), String.valueOf(100), 60, TimeUnit.SECONDS);

        try {
            ZipUtils.deleteFile(new File(checkFileDTO.getTemp()));
        }catch (Exception e){
            LogUtil.error(e,"taskAction 删除临时文件夹失败 ");
        }
        LogUtil.info("  asyncZipFile end" + (System.currentTimeMillis() - ms) + " ms.  " + ms);
    }


    @Async(value = "asyncTaskExecutor")
    public void asyncDeleteFileUpdateMemory (LoginUser loginUser, List<CommonSource> copyList, List<Long> sourceIdList, List<IOSourceVo> dList ) {

        LogUtil.info("deleteFileUpdateMemory copyList " + JsonUtils.beanToJson(copyList) + "，sourceIdList=" + JsonUtils.beanToJson(sourceIdList));

        /** 获取企业云盘 */
        long uSize = 0;
        long userID = loginUser.getUserID();
        // 更新容量 筛选出是否已经包含的文件 sourceIdList
        List<Long> pList = null;
        Set<Long> gList = new HashSet<>();
        List<CommonSource> pSourceList = new ArrayList<>();
        Map<Long, Long> groupSizeMap = new HashMap<>(1);

        for (CommonSource vo : copyList){
            if (vo.getSize() <= 0){
                continue;
            }
            pList = Arrays.asList(vo.getParentLevel().split(",")).stream().filter(n->!ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(pList) && !Collections.disjoint(pList, sourceIdList)){

                LogUtil.info("deleteFileUpdateMemory CommonSource " + JsonUtils.beanToJson(vo) + "，isCheck=" + !Collections.disjoint(pList, sourceIdList) + "，pList=" + JsonUtils.beanToJson(pList));
                continue;
            }

            pSourceList.add(vo);

            if (1 == vo.getTargetType().intValue()){
                uSize = uSize + vo.getSize();
            }else {
                if (CollectionUtils.isEmpty(pList)){
                    gList.add(vo.getSourceID());
                    if (!ObjectUtils.isEmpty(groupSizeMap) && groupSizeMap.containsKey(vo.getSourceID())){
                        groupSizeMap.put(vo.getSourceID(), groupSizeMap.get(vo.getSourceID()) + vo.getSize().longValue());
                    }else {
                        groupSizeMap.put(vo.getSourceID(), vo.getSize());
                    }
                }else {

                    gList.addAll(pList);
                    for (Long p : pList){
                        if (!ObjectUtils.isEmpty(groupSizeMap) && groupSizeMap.containsKey(p)){
                            groupSizeMap.put(p, groupSizeMap.get(p) + vo.getSize().longValue());
                        }else {
                            groupSizeMap.put(p, vo.getSize());
                        }
                    }
                }
            }
        }
        // 根据sourceID获取groupID
        if (!ObjectUtils.isEmpty(groupSizeMap)) {
            List<GroupSource> allReList = groupSourceDao.getGroupSourceList(new ArrayList<>(gList));
            Map<Long, Long> gsMap = allReList.stream().collect(Collectors.toMap(GroupSource::getSourceID, GroupSource::getGroupID, (v1, v2) -> v2));

            try {
                List<GroupSizeVo> gSource = new ArrayList<>();
                groupSizeMap.forEach((key,value)->{
                    if (value > 0 && !ObjectUtils.isEmpty(gsMap) && gsMap.containsKey(key)) {
                        gSource.add(new GroupSizeVo(gsMap.get(key), value));
                    }
                });

                if (!CollectionUtils.isEmpty(gSource)) {
                    LogUtil.info("deleteFileUpdateMemory batchUpdateGroupMemoryList gSource=" + JsonUtils.beanToJson(gSource));
                    homeExplorerDao.batchUpdateGroupMemoryList(gSource);
                }
            } catch (Exception e) {
                LogUtil.error(e, "更新 deleteFileUpdateMemory batchUpdateGroupMemoryList企业云盘 sources memory失败");
            }
        }

        LogUtil.info("deleteFileUpdateMemory uSize=" + uSize+ "， copyList " + JsonUtils.beanToJson(copyList));
        if (uSize > 0) {
            Map<String, Object> paramMap = new HashMap<>(2);
            paramMap.put("memory", uSize);
            paramMap.put("userID", userID);
            LogUtil.info("deleteFileUpdateMemory updateSubtractUseUserMemory paramMap=" + JsonUtils.beanToJson(paramMap));
            try {
                homeExplorerDao.updateSubtractUseUserMemory(paramMap);
            } catch (Exception e) {
                LogUtil.error(e, "更新 deleteFileUpdateMemory updateSubtractUseUserMemory 企业云盘 sources memory失败");
            }
        }
        // source updateSourceMemoryList
        if (!CollectionUtils.isEmpty(pSourceList)) {
            try {
                List<IOSource> sourceList = new ArrayList<>();
                Map<Long, Long> sourceSizeMap = new HashMap<>();
                for (CommonSource source : pSourceList) {
                    List<Long> sourceIds = Arrays.asList(source.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(sourceIds)) {
                        for (Long id : sourceIds) {
                            long size = (!ObjectUtils.isEmpty(sourceSizeMap) && sourceSizeMap.containsKey(id) ) ? sourceSizeMap.get(id) + source.getSize() : source.getSize();
                            sourceSizeMap.put(id, size);
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
                    LogUtil.info("deleteFileUpdateMemory batchSubtractSourceMemoryList sourceList=" + JsonUtils.beanToJson(sourceList));
                    ioSourceDao.batchSubtractSourceMemoryList(sourceList);
                }
            } catch (Exception e) {
                LogUtil.error(e, " deleteFileUpdateMemory updateMemory error ");
            }
        }

        if (!CollectionUtils.isEmpty(dList)) {
            for (IOSourceVo vo : dList) {
                if (vo.getFileCount().intValue() == 1) {
                    try {
                        //删除文件
                        new File(vo.getPath()).delete();
                        LogUtil.info("deleteFileUpdateMemory path=" + vo.getPath());
                    } catch (Exception e) {
                        LogUtil.error(e, "deleteFileUpdateMemory 删除文件失败");
                    }
                }
            }
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void asyncUpdateFileMd5(String path) {
        File f = new File(path);
        //如果不是文件夹，数据流输出，生成文件
        String serverChecksum = "";
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(f);
            serverChecksum = DigestUtils.md5DigestAsHex(inputStream);
        }catch (Exception e){
            LogUtil.error(e, "asyncUpdateFileMd5 error");
        }finally {
            try {
                if (null != inputStream){
                    inputStream.close();
                }
            }catch (Exception e){
                LogUtil.error(e, "asyncUpdateFileMd5  inputStream close error");
            }
        }

        if (!ObjectUtils.isEmpty(serverChecksum)){
            ioFileDao.updateFileMd5ByPath(serverChecksum, path);
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void asyncAddSourceHistory(CommonSource commonSource, Long userID, Long fileID, Long size) {

        Long uid = userID;
        IoSourceHistory orgHistory = ioSourceHistoryDao.getHistoryInfoByFileId(commonSource.getSourceID(), commonSource.getFileID());
        if (!ObjectUtils.isEmpty(orgHistory)){
            uid = orgHistory.getUserID();
        }
        // 添加历史记录 sourceID, `userID`,`fileID`, `size`, `detail`
        IoSourceHistory ioSourceHistory = new IoSourceHistory();
        ioSourceHistory.setSourceID(commonSource.getSourceID());
        ioSourceHistory.setUserID(uid);
        ioSourceHistory.setFileID(fileID);
        ioSourceHistory.setSize(size);
        ioSourceHistory.setDetail("");
        // 添加历史记录
        try {//  保证size正确
            sourceHistoryUtil.changeCheckSourceHistory(new IoSourceHistory(commonSource.getSourceID(),commonSource.getFileID(),userID,commonSource.getSize()), ioSourceHistory);
        } catch (Exception e) {
            LogUtil.error(e, "添加历史记录失败 uploadDTO=" + JsonUtils.beanToJson(ioSourceHistory));
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void genInformationPage(CommonInfoDto infoDto, LoginUser loginUser) {
        List<CommonSeo> infoList = commonInfoDao.getSimpleInfoForSEO(infoDto.getInfoIdList());
        if (CollectionUtils.isEmpty(infoList)){
            LogUtil.error("资讯静态化, 资讯不存在, " + JsonUtils.beanToJson(infoDto) + "," + JsonUtils.beanToJson(loginUser));
            return;
        }
        this.handleSEO(infoList);

        //修改了detail
        if (infoDto.getInfoIdList().size() == 1 && !StringUtil.isEmpty(infoDto.getDetail())) {
            infoList.get(0).setDetail(infoDto.getDetail());
            infoList.get(0).setSingle(true);
        }
        LogUtil.info("资讯静态化 genInformationPage infoList=" + JsonUtils.beanToJson(infoList));


        this.saveForSEO(infoList);
        try {
            commonInfoDao.updateInfoGmtPage(infoList);
        } catch (Exception e){
            LogUtil.error(e, "更新文件生成时间失败");
        }
    }

    public void saveForSEO(List<CommonSeo> seoList) {
        // /uploads/design/pubinfo/pc/pubinfo/30035.shtml
        if (CollectionUtils.isEmpty(seoList)) {
            return;
        }
        CommonSeo first = seoList.get(0);

        //是否链接，如果是链接，则不生成具体的页面内容，只作为承载页
        boolean isUrlInfo = (null != first.getIsUrlInfo() && first.getIsUrlInfo() == 1);

        String tempLatePath =  templatePath + seoList.get(0).getTypeStr() +"/index.html";
        String mobileTempLatePath = templatePath + seoList.get(0).getTypeStr() + "/wap.html";
        String pcHtml = FileUtil.getFileContent(tempLatePath);
        String mobileHtml = FileUtil.getFileContent(mobileTempLatePath);
        String infoTip = "生成静态页," + JsonUtils.beanToJson(seoList) ;

        LogUtil.info(infoTip);
        boolean isInformation = seoList.get(0).getTypeStr().equals("information");
        //资讯, 使用网校的keyword
        String seoKeyword = "";
        if (isInformation){
            seoKeyword = systemOptionDao.getSystemConfigByKey("seo");
        }
        //域名循环
        long timeTotal = System.currentTimeMillis();
        String blackMobileHtml = null;

        //客户端类型循环
        for (String clientType : GlobalConfig.DESIGN_WEB_CLIENT_TYPE_ARR) {
            //String domainAndClientPath = infoHtmlPath + "/" + clientType;
            //循环
            for (CommonSeo seo : seoList) {

                long time = System.currentTimeMillis();
                String html ;
                //手机短视频， 背景黑色
                if (!ObjectUtils.isEmpty(seo.getInfoType()) && seo.getInfoType().equals("1") && clientType.equals("mb")){
                    if (blackMobileHtml == null){
                        blackMobileHtml = mobileHtml.replace("<html>", "<html style=\"background-color:#000\">");
                        blackMobileHtml = blackMobileHtml.replace("<body>", "<body style=\"background-color:#000\">");
                    }
                    html = blackMobileHtml;
                } else {
                    html = clientType.equals("pc") ? pcHtml : mobileHtml;
                }
                if (!isUrlInfo ) { //并且非动态的
                    if (!StringUtil.isEmpty(seo.getDetail())){
                        html = html.replace("<!-- /*article.content*/ -->", this.handleDetail(seo));
                    }
                    html = html.replace("<!-- /*article.introduce*/ -->", seo.getDescription());
                    html = html.replace("<!-- /*article.title*/ -->", seo.getTitle());
                }

                //替换标题
                html = html.replaceFirst("(?<=<title>)([\\s\\S]*?)(?=</title>)", seo.getTitle());
                //替换seo
                if (!seo.getKeyword().trim().equals("")) {
                    html = html.replaceFirst("(?<=<meta name=\"keywords\" content=\")([\\s\\S]*?)(?=\"/>)", StringUtil.isEmpty(seo.getKeyword()) ? seoKeyword : seo.getKeyword());
                }
                if (!seo.getDescription().trim().equals("")) {
                    html = html.replaceFirst("(?<=<meta name=\"description\" content=\")([\\s\\S]*?)(?=\"/>)", seo.getDescription());
                }

                if (isUrlInfo) { //并且非动态的
                    //如果是链接，将body标签中的内容替换为空
                    String[] list = html.split("<body>", 2);
                    if (list.length == 2 && list[0].length() > 0 && list[1].length() > 0) {
                        final int end = list[1].indexOf("</body>");
                        if (end >= 0) {
                            html = list[0] + "<body>" + list[1].substring(end);
                        }
                    }
                }
                String pubPath =  pathDefault + "/design/pubinfo/" + clientType + "/pubinfo/" + seo.getId() + ".shtml";

                File file = null;
                if (!ObjectUtils.isEmpty(pubPath)) {
                    file = new File(pubPath);
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    FileUtil.putFileContent(pubPath, html);
                }
                LogUtil.info("saveForSeo单次时长, " + (System.currentTimeMillis() - time));
            }
        }
        LogUtil.info("saveForSeo总时长, " + (System.currentTimeMillis() - timeTotal)
                + ", infoHtmlPath: " + infoHtmlPath
                + ", seoSize: " + seoList.size());
    }
    private void handleSEO(List<CommonSeo> seoList) {
        for (CommonSeo commonSeo : seoList){
            if (!commonSeo.getSeoJson().equals("")){
                try {
                    CommonSeo seo = JsonUtils.jsonToBean(commonSeo.getSeoJson(), CommonSeo.class);
                    if (!StringUtil.isEmpty(seo.getKeyword())){
                        commonSeo.setKeyword(seo.getKeyword());
                    } else if (!StringUtil.isEmpty(seo.getSeoKeyword())){
                        commonSeo.setKeyword(seo.getSeoKeyword());
                    }
                    if (!StringUtil.isEmpty(seo.getDescription())){
                        commonSeo.setDescription(seo.getDescription());
                    } else if (!StringUtil.isEmpty(seo.getDescription())) {
                        commonSeo.setDescription(seo.getSeoDescription());
                    }
                } catch (Exception e){
                    LogUtil.error(e, "seo 解析失败");
                }
            }
        }
    }
    private String handleDetail(CommonSeo seo) {
        String detail = "";
        //资讯detail 存到一个文件 /uploads/design/home/filesbox/pc/information/__detail_30035__.txt
        if (seo.getTypeStr().equals("information")) {
            String detailFilePath = designBasePath  + infoHtmlPath + "/pc/" + seo.getTypeStr() + "/__detail_" + seo.getId() + "__.txt";
            File file = new File(detailFilePath);
            LogUtil.info("handleDetail detailFilePath=" + detailFilePath);
            //如果是单个操作
            if (Boolean.TRUE.equals(seo.getSingle())){
                if (!file.getParentFile().exists()){
                    file.getParentFile().mkdirs();
                }
                FileUtil.putFileContent(detailFilePath, seo.getDetail());
            } else {
                detail = FileUtil.getFileContent(file);
            }
        }
        if (StringUtil.isEmpty(detail)){
            detail = seo.getDetail();
        }
        return  "\n<article style=\"display:none\">" + detail + "</article>";
    }


    /** 删除临时的文件/文件夹*/
    @Async(value = "asyncTaskExecutor")
    public void deleteDirOrFile(File file) {
        if (file.isDirectory()) {
            File[] subFiles = file.listFiles();
            if (subFiles != null) {
                for (File subFile : subFiles) {
                    deleteDirOrFile(subFile);
                }
            }
            if (file.exists())
                file.delete(); // 删除文件夹
        } else {
            if (file.exists())
                file.delete();
        }
    }

    /**
     * @Description: 异步合并剪切的视频
     * @params:  cutList 剪切的视频路径列表
     * @params:  finishPath 合并完成的最终视频
     * @params:  suffix 文件后缀
     * @params:  tempPath temp路径
     */
    @Async(value = "asyncTaskExecutor")
    public void cutVideoMerge(List<String> cutList, String finishPath, String suffix, String tempPath, List<CommonSource> convertList, String otherType
            , FileOptionTool fileOptionTool, long userID) {

        List<String> tsList = new ArrayList<>();
        boolean check = VideoGetUtil.cutVideoMerge(cutList, finishPath, suffix, tempPath, tsList);

        // 合并完再添加数据
        if ("cutVideo".equals(otherType) && !CollectionUtils.isEmpty(convertList) && convertList.size() == 1) {
            CommonSource s = convertList.get(0);
            s.setPath(finishPath);
            try {
                File f = new File(finishPath);
                if (f.exists()){
                    s.setSize(f.length());
                }
            }catch (Exception e){
            }
            fileOptionTool.addCommonSource(userID, s, EventEnum.mkfile);
            convertList = new ArrayList<>();
            convertList.add(s);
        }

        if (check && !CollectionUtils.isEmpty(convertList)){
            for (CommonSource source : convertList){
                ConvertDTO convertDTO = new ConvertDTO();
                convertDTO.setBusId(source.getSourceID());
                convertDTO.setBusType("cloud");
                convertDTO.setOtherType(otherType);
                convertDTO.setDomain(source.getDomain());
                if (ObjectUtils.isEmpty(source.getPath())){
                    source.setPath(finishPath);
                }
                convertUtil.doConvertMain(convertDTO, source);
            }
        }

        if (!ObjectUtils.isEmpty(tempPath)) {
            File destinationFolder = new File(tempPath);
            if (destinationFolder.exists()) {
                // 异步删除文件或者文件夹
                deleteDirOrFile(destinationFolder);
            }
        }

        if (!CollectionUtils.isEmpty(tsList)){
            for (String p : tsList) {
                File tsFile = new File(p);
                // 删除文件
                if (tsFile.exists()) {
                    tsFile.delete();
                }
            }
        }
    }

    /**
     * @Description: 转码
     * @params:  [path, finishPath,  resolution, frameRate]
     * @Return:  void
     * @Modified:
     */
    @Async(value = "asyncTaskExecutor")
    public void convertVideoMerge(VideoCommonDto videoCommonDto, List<CommonSource> convertList) {
        Boolean check = VideoGetUtil.convertVideoMerge(videoCommonDto.getVideoPathOrg(), videoCommonDto.getFinalFilePath(), videoCommonDto.getResolution(), videoCommonDto.getFrameRate());
        if (check && !CollectionUtils.isEmpty(convertList)){
            for (CommonSource source : convertList){
                ConvertDTO convertDTO = new ConvertDTO();
                convertDTO.setBusId(source.getSourceID());
                convertDTO.setBusType("cloud");
                convertDTO.setOtherType("convertVideo");
                source.setDomain(videoCommonDto.getServerUrl());
                convertDTO.setDomain(videoCommonDto.getServerUrl());
                convertUtil.doConvertMain(convertDTO, source);
            }
        }
    }

    @Async(value = "asyncTaskExecutor")
    public void settingVideo(VideoCommonDto videoCommonDto, List<CommonSource> convertList) {
        Boolean check = VideoGetUtil.configVideoMerge(videoCommonDto);
        if (check && !CollectionUtils.isEmpty(convertList)){
            for (CommonSource source : convertList){
                ConvertDTO convertDTO = new ConvertDTO();
                convertDTO.setBusId(source.getSourceID());
                convertDTO.setBusType("cloud");
                convertDTO.setOtherType("settingVideo");
                source.setDomain(videoCommonDto.getServerUrl());
                convertDTO.setDomain(videoCommonDto.getServerUrl());
                convertUtil.doConvertMain(convertDTO, source);
            }
        }
    }

    public void updateMemory(CommonSource commonSource) {
        updateMemory(commonSource.getSize(), commonSource.getGroupID(), commonSource.getUserID(), commonSource.getTargetType(), commonSource.getParentLevel());
    }

    public void updateMemory(long size, long groupID, long userID, Integer targetType, String parentLevel) {
        if (size > 0 && !ObjectUtils.isEmpty(targetType)) {
            Map<String, Object> paramMap = new HashMap<>(2);
            paramMap.put("groupID", groupID);
            paramMap.put("memory", size);
            paramMap.put("userID", userID);
            LogUtil.info("updateMemory paramMap=" + JsonUtils.beanToJson(paramMap) + ",targetType=" + targetType + "， parentLevel=" + parentLevel);
            try {
                if (targetType.intValue() == 1) {
                    homeExplorerDao.updateUserMemory(paramMap);
                } else {
                    if (!ObjectUtils.isEmpty(parentLevel)) {
                        this.updateGroupMemoryCopyBySearch(paramMap, parentLevel);
                    }else if(groupID > 0){
                        this.updateGroupMemoryCopy(paramMap, groupID);
                    }else {
                        LogUtil.error("更新企业云盘云盘memory 失败 groupID=" + groupID + "，parentLevel=" + parentLevel);
                    }
                }
            } catch (Exception e) {
                LogUtil.error(e, "更新 企业云盘 memory失败");
            }
            // source updateSourceMemoryList
            if (!ObjectUtils.isEmpty(parentLevel)) {
                try {
                    List<Long> sourceIds = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(sourceIds)) {
                        ioSourceDao.updateSourceMemoryList(sourceIds, size);
                    }
                } catch (Exception e) {
                    LogUtil.error(e, " updateMemory updateSourceMemoryList error ");
                }

            }
        }
    }

    public void updateGroupMemoryCopyBySearch(Map<String, Object> paramMap, String parentLevel) {
        List<Long> sourcePIDs = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n))
                .map(Long::parseLong).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(sourcePIDs)){
            return;
        }
        List<String> gpList = homeExplorerDao.getGroupParentLevelList(sourcePIDs);
        if (CollectionUtils.isEmpty(gpList)){
            return;
        }
        String groupParentLevel = Joiner.on(",").join(gpList);
        List<Long> groupIDs = Arrays.asList(groupParentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n))
                .map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupIDs)){
            return;
        }
        paramMap.put("list", groupIDs);
        homeExplorerDao.updateMemoryList(paramMap);
    }
    public void updateGroupMemoryCopy(Map<String, Object> paramMap, Long groupID) {
        String groupParentLevel = homeExplorerDao.getGroupParentLevel(groupID);
        List<Long> groupIDs = Arrays.asList(groupParentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n))
                .map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(groupIDs)) {
            groupIDs = new ArrayList<>();
        }
        groupIDs.add(groupID);
        paramMap.put("list", groupIDs);
        homeExplorerDao.updateMemoryList(paramMap);
    }

}
