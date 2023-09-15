package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.MyMenuEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.service.SourceHistoryService;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.SourceHistoryUtil;
import com.svnlan.home.utils.UserAuthTool;
import com.svnlan.home.vo.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SourceOperateTool;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.DateDUtil;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.VideoUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/31 11:28
 */
@Service
public class SourceHistoryServiceImpl implements SourceHistoryService {

    @Resource
    IoSourceHistoryDao ioSourceHistoryDao;
    @Resource
    SourceOperateTool sourceOperateTool;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    UserDao userDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    SourceHistoryUtil sourceHistoryUtil;
    @Resource
    UserAuthTool userAuthTool;
    @Resource
    LoginUserUtil loginUserUtil;

    @Override
    public HomeExplorerResult getSourceHistoryList(HomeExplorerDTO homeExplorerDTO){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getSourceID()) || homeExplorerDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        Map<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("sourceID", homeExplorerDTO.getSourceID());
        hashMap.put("startIndex", homeExplorerDTO.getStartIndex());
        hashMap.put("pageSize", homeExplorerDTO.getPageSize());
        CommonSource cloudFile = fileOptionTool.getFileAttachment(homeExplorerDTO.getSourceID(), 0L);

        if (cloudFile == null || ObjectUtils.isEmpty(cloudFile.getName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }

        Long total = ioSourceHistoryDao.getCountSourceHistoryBySourceID(hashMap);

        String downloadKey = FileUtil.getDownloadKey();
        List<HomeExplorerVO> list = null;
        List<HomeExplorerVO> fileList = new ArrayList<>();
        if(0 < total.longValue()) {
            FileMetaVo fileMetaVo = null;
            list = ioSourceHistoryDao.getSourceHistoryBySourceID(hashMap);
            for (HomeExplorerVO vo1 : list){
                if (vo1.getFileID().equals(cloudFile.getFileID())){
                    continue;
                }
                // FileMeta value 处理
                sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);
                // path 处理
                sourceOperateTool.opHistoryPath(vo1, downloadKey);
                fileList.add(vo1);
            }
        }
        HomeExplorerResult result = new HomeExplorerResult();
        result.setTotal(total);
        result.setFolderList(null);
        result.setFileList(fileList);

        String downloadUrl = "/api/disk/attachment/" + FileUtil.encodeDownloadUrl(cloudFile.getName())
                + fileOptionTool.getDownloadParam(cloudFile.getSourceID(), downloadKey, "") ;

        String m3u8Key = FileUtil.getM3u8Key();
        Map<String, Object> reMap = new HashMap<>(1);

        Long userId = cloudFile.getUserID();
        IoSourceHistory orgHistory = ioSourceHistoryDao.getHistoryInfoByFileId(cloudFile.getSourceID(), cloudFile.getFileID());
        if (!ObjectUtils.isEmpty(orgHistory)){
            userId = orgHistory.getUserID();
        }
        //String domain = HttpUtil.getRequestRootUrl(null);
        reMap.put("sourceID", cloudFile.getSourceID());
        reMap.put("fileID", cloudFile.getFileID());
        reMap.put("fileType", cloudFile.getFileType());
        reMap.put("h5Url", cloudFile.getAppPreviewUrl());
        reMap.put("createTime", cloudFile.getCreateTime());
        UserVo userVo =userDao.getUserBaseOneInfo(userId);
        reMap.put("nickname", !ObjectUtils.isEmpty(userVo.getNickname()) ? userVo.getNickname() : userVo.getName());
        reMap.put("avatar", userVo.getAvatar());

        reMap.put("resolution", ObjectUtils.isEmpty(cloudFile.getResolution()) ? "" : cloudFile.getResolution());
        reMap.put("isPreview", cloudFile.getIsPreview());
        reMap.put("previewUrl", "");
        reMap.put("isSwf", 0);
        reMap.put("swfUrl", "");
        Integer videoLength = ObjectUtils.isEmpty(cloudFile.getSourceLength()) ? 0 : cloudFile.getSourceLength();
        reMap.put("name", cloudFile.getName());
        reMap.put("size", cloudFile.getSize());
        reMap.put("length", videoLength);
        reMap.put("isM3u8", cloudFile.getIsM3u8());

        if (cloudFile.getIsM3u8().equals(1)){//转码完成的
            //文档类
            if (Arrays.asList(GlobalConfig.DOC_TYPE_ARR).contains(cloudFile.getFileType())){
                reMap.put("isSwf", 1);
                reMap.put("swfUrl", cloudFile.getPreviewUrl());
            } else {//视频
                String m3u8Url = "/api/disk/mu/getMyM3u8.m3u8" + fileOptionTool.getM3u8Param(cloudFile.getSourceID(), m3u8Key);
                reMap.put("previewUrl", m3u8Url);
            }
        }
        //未完成的. 视频转码进度
        else if (!cloudFile.getIsM3u8().equals(-1)
                && Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(cloudFile.getFileType())){
            String sourcePath = cloudFile.getPath();
            //获取视频长度

            if (videoLength.equals(0)){
                videoLength = VideoUtil.getVideoLength(sourcePath);
                if (videoLength.equals(0)){
                    //获取转码进度
                    int dotPosition = sourcePath.lastIndexOf(".");
                    String m3u8Path = sourcePath.substring(0, dotPosition) + ".m3u8";
                    Integer progress = FileUtil.getConvertedLength(m3u8Path, videoLength, true);
                    reMap.put("convertProgress", progress);

                }else {
                    reMap.put("convertProgress", videoLength);
                }
            }
        }
        reMap.put("isH5", cloudFile.getAppPreview());
        reMap.put("downloadUrl", downloadUrl );
        //到前端页面
        if (Arrays.asList(GlobalConfig.DOC_SHOW_TYPE_ARR).contains(cloudFile.getFileType())) {
            reMap.put("pdfPreviewUrl", fileOptionTool.getPptPdfPreview2(cloudFile.getFileType(), cloudFile.getIsH264Preview(), downloadUrl));
            String pptPreviewUrl = fileOptionTool.getPptPreviewUrl(downloadUrl, cloudFile.getHashMd5());
            reMap.put("pptPreviewUrl", pptPreviewUrl);// HttpUtil.getRequestRootUrl(null) + downloadUrl
        }
        reMap.put("yzViewData", "");
        /*if(!ObjectUtils.isEmpty(cloudFile.getYzViewData())) {
            reMap.put("yzViewData", cloudFile.getYzViewData());
        }*/

        // path 处理
        if (!ObjectUtils.isEmpty(cloudFile.getPath()) && Arrays.asList(GlobalConfig.list_path_source_type).contains(cloudFile.getFileType())){
            if (!"svg".equals(cloudFile.getFileType())){
                //String firstPath = FileUtil.getFirstStorageDevicePath(cloudFile.getPath());
                //reMap.put("path", cloudFile.getPath().replace(firstPath + "/private/cloud", firstPath + "/common/cloud"));
                reMap.put("path", FileUtil.getShowImageUrl(cloudFile.getPath(), ""+cloudFile.getSourceID()+"." + cloudFile.getFileType()));
            }
        }else if("oexe".equals(cloudFile.getFileType())){
            reMap.put("path", "");
            reMap.put("oexeContent", FileUtil.getFileContent(cloudFile.getPath(), StandardCharsets.UTF_8));
        }else {
            reMap.put("path", "");
        }
        reMap.put("thumb", "");
        if (!ObjectUtils.isEmpty(cloudFile.getThumb())){
            reMap.put("thumb", FileUtil.getShowImageUrl(cloudFile.getThumb(), "thumb"+cloudFile.getSourceID()+".jpg"));
        }

        result.setCurrent(reMap);
        return result;
    }

    @Override
    public void setHistoryDetail(HomeExplorerDTO homeExplorerDTO){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getId()) || homeExplorerDTO.getId() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        checkHistoryAuth(homeExplorerDTO.getId(), null);

        homeExplorerDTO.setDetail(ObjectUtils.isEmpty(homeExplorerDTO.getDetail()) ? "" : homeExplorerDTO.getDetail());
        try {
            ioSourceHistoryDao.updateDetail(homeExplorerDTO.getId(), homeExplorerDTO.getDetail());
        }catch (Exception e){
            LogUtil.error(e, "setHistoryDetail error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }

    }

    @Override
    public void setHistoryRevision(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getId()) || homeExplorerDTO.getId() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 获取历史记录
        IoSourceHistory history = ioSourceHistoryDao.getHistoryInfo(homeExplorerDTO.getId());
        if (ObjectUtils.isEmpty(history)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 获取原本source的file信息
        IoSourceHistory file = ioSourceHistoryDao.getFileInfoBySourceID(history.getSourceID());
        if (ObjectUtils.isEmpty(file) || file.getFileID().longValue() == history.getFileID().longValue()){
            return;
        }
        userAuthTool.checkGroupDocAuth(loginUser, history.getSourceID(), file.getParentLevel(), "14", file.getTargetType());

        Long userId = file.getUserID();
        IoSourceHistory orgHistory = ioSourceHistoryDao.getHistoryInfoByFileId(homeExplorerDTO.getSourceID(), file.getFileID());
        if (!ObjectUtils.isEmpty(orgHistory)){
            userId = orgHistory.getUserID();
        }

        // 添加历史记录 sourceID, `userID`,`fileID`, `size`, `detail`
        IoSourceHistory ioSourceHistory = new IoSourceHistory();
        ioSourceHistory.setSourceID(history.getSourceID());
        ioSourceHistory.setUserID(ObjectUtils.isEmpty(userId) ? loginUser.getUserID() : userId);
        ioSourceHistory.setFileID(file.getFileID());
        ioSourceHistory.setSize(file.getSize());
        ioSourceHistory.setDetail(I18nUtils.i18n("explorer.history.setCurrent"));

        history.setUserID(loginUser.getUserID());
        try {
            // 修改source的fileID、size
            ioSourceHistoryDao.updateVerSource(history);

            // 删除历史版本再添加一条最新的记录、不删，用于查询历史userId  getHistoryInfoByFileId
            //ioSourceHistoryDao.delByID(homeExplorerDTO.getId());

            // 判断是否存在并保证size正确
            sourceHistoryUtil.changeCheckSourceHistory(null, ioSourceHistory);


        }catch (Exception e){
            LogUtil.error(e, "setHistoryDetail error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }

        String time = DateDUtil.LongTimeToString(history.getCreateTime(),DateDUtil.yyyy_MM_dd_HH_mm_ss);
        fileOptionTool.addSourceEvent(history.getSourceID(), file.getParentID(), loginUser.getUserID(), file.getName(), EventEnum.rollBack, time);
    }

    @Override
    public void deleteHistory(HomeExplorerDTO homeExplorerDTO){

        checkHistoryAuth(homeExplorerDTO.getId(), null);

        // 删除当前版本记录
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getId()) && homeExplorerDTO.getId() > 0){
            ioSourceHistoryDao.delByID(homeExplorerDTO.getId());
        }
        // 删除所有版本记录
        else if(!ObjectUtils.isEmpty(homeExplorerDTO.getSourceID()) && homeExplorerDTO.getSourceID() > 0){
            ioSourceHistoryDao.delBySourceID(homeExplorerDTO.getSourceID());
        }else {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

    }

    @Override
    public List parentSourceList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getParentLevel())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        List<Long> parentIDs = Arrays.asList(homeExplorerDTO.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(parentIDs)){
            if (!ObjectUtils.isEmpty(homeExplorerDTO.getParentID())) {
                CommonSource commonSource = fileOptionTool.getSourceInfo(homeExplorerDTO.getParentID());
                parentIDs = Arrays.asList(commonSource.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                if (CollectionUtils.isEmpty(parentIDs)) {
                    parentIDs = new ArrayList<>();
                }
                parentIDs.add(homeExplorerDTO.getParentID());
            }else {
                return new ArrayList();
            }
        }



        List<IOSource> copyList = ioSourceDao.copySourceList(parentIDs);
        if (CollectionUtils.isEmpty(copyList)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.rptSelectTips.getCode());
        }

        boolean isSystem = false;
        String auth = "";
        if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
            auth = GlobalConfig.SYSTEM_GROUP_AUTH;
            isSystem = true;
        }
        Map<Long, String> gsAuthMap = null;
        Map<Long, UserGroupVo> gsAuthNameMap = null;
        if (!isSystem){
            List<Long> groupSourceId = copyList.stream().filter(n->n.getTargetType().intValue() == 2).map(IOSource::getSourceID).collect(Collectors.toList());

            if (!isSystem){
                gsAuthMap = new HashMap<>(1);
                gsAuthNameMap = new HashMap<>(1);
                // 文件上级权限
                if (!isSystem && !CollectionUtils.isEmpty(groupSourceId)){
                    sourceOperateTool.getUserAuthByLevel(loginUser.getUserID(), new ArrayList<>(groupSourceId), gsAuthMap, gsAuthNameMap);
                }
            }
        }
        Map<Long, IOSource> parentMap = copyList.stream().collect(Collectors.toMap(IOSource::getSourceID, Function.identity(), (v1, v2) -> v2));
        IOSource ioSource = null;
        List<IOSource> list = new ArrayList<>();
        for (Long sourceID : parentIDs){
            if (!ObjectUtils.isEmpty(parentMap) && parentMap.containsKey(sourceID)) {
                ioSource = parentMap.get(sourceID);
                if (ioSource.getTargetType().intValue() == 1){
                    if (ioSource.getParentID().longValue() == 0){
                        ioSource.setName(I18nUtils.tryI18n(MyMenuEnum.rootPath.getCode()));
                    }
                    ioSource.setIcon(MyMenuEnum.rootPath.getIcon());
                }else {
                    ioSource.setIcon("box");
                }
                ioSource.setAuth(auth);
                if (!isSystem && !ObjectUtils.isEmpty(gsAuthMap) && gsAuthMap.containsKey(sourceID)){
                    if (gsAuthMap.containsKey(sourceID)){
                        ioSource.setAuth(gsAuthMap.get(sourceID));
                    }
                }
                list.add(ioSource);
            }
        }
        return list;
    }

    private void checkHistoryAuth(Long id, LoginUser loginUser){
        // 获取历史记录
        IoSourceHistory history = ioSourceHistoryDao.getHistoryInfo(id);
        if (ObjectUtils.isEmpty(history)){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 获取原本source的file信息
        IoSourceHistory file = ioSourceHistoryDao.getFileInfoBySourceID(history.getSourceID());
        if (ObjectUtils.isEmpty(file) || file.getFileID().longValue() == history.getFileID().longValue()){
            return;
        }
        if (ObjectUtils.isEmpty(loginUser)){
            loginUser = loginUserUtil.getLoginUser();
        }
        userAuthTool.checkGroupDocAuth(loginUser, history.getSourceID(), file.getParentLevel(), "14", file.getTargetType());

    }
}
