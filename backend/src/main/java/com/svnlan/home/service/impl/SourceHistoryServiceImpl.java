package com.svnlan.home.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.MyMenuEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceHistoryDao;
import com.svnlan.home.dao.impl.HomeExplorerDaoImpl;
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
import com.svnlan.user.dao.Impl.RoleDaoImpl;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.domain.Role;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    UserDao userDaoImpl;
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
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    RoleDaoImpl roleDaoImpl;
    @Resource
    HomeExplorerDaoImpl homeExplorerDaoImpl;

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
        UserVo userVo =userDaoImpl.getUserBaseOneInfo(cloudFile.getUserID());
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

        String time = DateUtil.LongTimeToString(history.getCreateTime(),DateUtil.yyyy_MM_dd_HH_mm_ss);
        fileOptionTool.addSourceEvent(history.getSourceID(), file.getParentID(), loginUser.getUserID(), file.getName(), EventEnum.rollBack, time);
    }

    @Override
    public void deleteHistory(HomeExplorerDTO homeExplorerDTO){

        // 删除当前版本记录
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getId()) && homeExplorerDTO.getId() > 0){
            checkHistoryAuth(homeExplorerDTO.getId(), null);
            ioSourceHistoryDao.delByID(homeExplorerDTO.getId());
        }
        // 删除所有版本记录
        else if(!ObjectUtils.isEmpty(homeExplorerDTO.getSourceID()) && homeExplorerDTO.getSourceID() > 0){
            checkHistoryAuthBySourceId(homeExplorerDTO.getSourceID(), null);
            ioSourceHistoryDao.delBySourceID(homeExplorerDTO.getSourceID());
        }else {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

    }

    @Override
    public List parentSourceList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        return parentSourceListByParent(homeExplorerDTO, loginUser);
    }

    @Override
    public JSONObject parentSourceList2(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        JSONObject reMap = new JSONObject();
        HomeExplorerVO vo = null;
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getSourceID())){
            vo = parentSourceListBySourceId(homeExplorerDTO, loginUser);
            reMap.put("source",vo);
            homeExplorerDTO.setParentID(vo.getParentID());
            homeExplorerDTO.setParentLevel(vo.getParentLevel());
        }
        List list = parentSourceListByParent(homeExplorerDTO, loginUser);
        reMap.put("parentList", list);
        return reMap;
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

    private void checkHistoryAuthBySourceId(Long sourceID, LoginUser loginUser){

        // 获取原本source的file信息
        IoSourceHistory file = ioSourceHistoryDao.getFileInfoBySourceID(sourceID);
        if (ObjectUtils.isEmpty(file)){
            return;
        }
        if (ObjectUtils.isEmpty(loginUser)){
            loginUser = loginUserUtil.getLoginUser();
        }
        userAuthTool.checkGroupDocAuth(loginUser, sourceID, file.getParentLevel(), "14", file.getTargetType());

    }

    private List parentSourceListByParent(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){

        if (ObjectUtils.isEmpty(homeExplorerDTO.getParentLevel())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (",0,".equals(homeExplorerDTO.getParentLevel())){
            return new ArrayList();
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
            List<Long> groupSourceId = copyList.stream().filter(n->n.getTargetType().intValue() == 2).map(IOSource::getId).collect(Collectors.toList());

            if (!isSystem){
                gsAuthMap = new HashMap<>(1);
                gsAuthNameMap = new HashMap<>(1);
                // 文件上级权限
                if (!isSystem && !CollectionUtils.isEmpty(groupSourceId)){
                    sourceOperateTool.getUserAuthByLevel(loginUser.getUserID(), new ArrayList<>(groupSourceId), gsAuthMap, gsAuthNameMap);
                }
            }
        }
        Map<Long, IOSource> parentMap = copyList.stream().collect(Collectors.toMap(IOSource::getId, Function.identity(), (v1, v2) -> v2));
        IOSource ioSource = null;
        List<IOSource> list = new ArrayList<>();
        for (Long sourceID : parentIDs){
            if (!ObjectUtils.isEmpty(parentMap) && parentMap.containsKey(sourceID)) {
                ioSource = parentMap.get(sourceID);
                if (ioSource.getTargetType().intValue() == 1){
                    if (ioSource.getParentId().longValue() == 0){
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
    private HomeExplorerVO parentSourceListBySourceId(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        boolean isSystem = false;
        String auth = "";
        List<IoSourceAuthVo> roleList = null;
        if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
            roleList = new ArrayList<>();
            Role role = roleDaoImpl.getSystemRoleInfo();
            auth = GlobalConfig.SYSTEM_GROUP_AUTH;
            isSystem = true;
            roleList.add(new IoSourceAuthVo(role.getRoleID(), role.getRoleName(), role.getLabel(), auth));
        }
        HomeExplorerVO vo1 = homeExplorerDao.getHomeExplorerOne(homeExplorerDTO.getSourceID());
        boolean isGroup = false;
        if (vo1.getTargetType().intValue() == 1 && vo1.getParentID().longValue() == 0){
            vo1.setName(I18nUtils.tryI18n(MyMenuEnum.rootPath.getCode()));
        }
        List<String> sourceIDs = new ArrayList<>();
        sourceIDs.add(vo1.getSourceID().toString());
        String downloadKey = FileUtil.getDownloadKey();
        Set<Long> gpidList = new HashSet<>();
        Set<Long> sidList = new HashSet<>();
        if (vo1.getTargetType().intValue() == 2) {
            if (!isSystem) {
                auth = sourceOperateTool.getUserAuthByLevel(loginUser.getUserID(), vo1.getParentLevel(), homeExplorerDTO);
                roleList = new ArrayList<>();
                roleList.add(new IoSourceAuthVo(homeExplorerDTO.getAuthID(), homeExplorerDTO.getAuthName(), homeExplorerDTO.getLabel(), auth));
                isGroup = true;

                sidList.add(vo1.getSourceID());
                if (!",0,".equals(vo1.getParentLevel())) {
                    Set<Long> parentIdList = Arrays.asList(vo1.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toSet());
                    gpidList.addAll(parentIdList);
                    sidList.addAll(parentIdList);
                }
            }
        }

        Map<Long, List<IoSourceAuthVo>> gsFileAuthMap = new HashMap<>(1);
        // 文件权限
        if (!CollectionUtils.isEmpty(sidList)) {
            sourceOperateTool.getFileAuthByLevel(loginUser.getUserID(), sidList, gsFileAuthMap);
            LogUtil.info("parentSourceListBySourceId getFileAuthByLevel sidList=" + JsonUtils.beanToJson(sidList) + " gsFileAuthMap=" + JsonUtils.beanToJson(gsFileAuthMap));
        }


        Map<Long, String> gsAuthMap = new HashMap<>(1);
        Map<Long, UserGroupVo> gsAuthNameMap = new HashMap<>(1);
        // 文件上级权限
        if (!isGroup && !CollectionUtils.isEmpty(gpidList)) {
            sourceOperateTool.getUserAuthByLevel(loginUser.getUserID(), new ArrayList<>(gpidList), gsAuthMap, gsAuthNameMap);
        }
        // 标签
        Map<String, List<CommonLabelVo>> sourceTagMap = fileOptionTool.getSourceTagMap(loginUser.getUserID(), sourceIDs);
        Map<Long, String> parentPathDisplayMap = null;
        Set<String> pLevelList = new HashSet<>();
        pLevelList.add(vo1.getParentLevel());
        // 文件位置
        if (!CollectionUtils.isEmpty(pLevelList)) {
            parentPathDisplayMap = sourceOperateTool.getParentPathDisplayMap(pLevelList);

        }

        Set<Long> uidList = new HashSet<>();
        uidList.add(vo1.getCreateUser());
        uidList.add(vo1.getModifyUser());
        List<UserVo> userList = userDaoImpl.getUserBaseInfo(new ArrayList<>(uidList));
        Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));
        if (ObjectUtils.isEmpty(userMap)){
            userMap = new HashMap<>(1);
        }
        userMap.put(0L, sourceOperateTool.getDefaultSystemUser());

        List<Long> idList = new ArrayList<>();
        if (vo1.getIsFolder().intValue() == 1) {
            idList.add(vo1.getSourceID());
        }
        List<HomeExplorerVO> countList = CollectionUtils.isEmpty(idList) ? null : homeExplorerDao.getSourceChileCont(idList);
        Map<String, Integer> countMap = new HashMap<>(1);
        if (!CollectionUtils.isEmpty(countList)) {
            for (HomeExplorerVO home : countList) {
                countMap.put(home.getParentID() + (home.getIsFolder().intValue() == 1 ? "_folder" : "_file"), home.getFileCount());
            }
        }
        // 是否是部门文件（左侧栏）
        vo1.setIsGroup(0);
        if (isSystem) {
            vo1.setRoleList(JsonUtils.beanToJson(roleList));
        }
        vo1.setAuth(auth);

        List<IoSourceAuthVo> rList = null;
        if (!isSystem && vo1.getTargetType().intValue() == 2) {
            boolean separately  = false;
            // 单独设置权限
            if (!ObjectUtils.isEmpty(gsFileAuthMap)) {
                LogUtil.info("parentSourceListBySourceId getFileAuthByLevel checkSeparately=" + (gsFileAuthMap.containsKey(vo1.getSourceID())) + " gsFileAuthMap=" + JsonUtils.beanToJson(gsFileAuthMap));
                if (gsFileAuthMap.containsKey(vo1.getSourceID())) {
                    separately  = true;
                    rList = gsFileAuthMap.get(vo1.getSourceID());
                    vo1.setRoleList(JsonUtils.beanToJson(rList));
                    vo1.setAuth(rList.get(0).getAuth());
                }else {
                    List<Long> pList = Arrays.asList(vo1.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    if (!CollectionUtils.isEmpty(pList)){
                        for (int i = pList.size(); i-- > 0; ) {
                            Long p = pList.get(i);
                            LogUtil.info("parentSourceListBySourceId getFileAuthByLevel p=" + p + " checkParentSeparately=" + (gsFileAuthMap.containsKey(p)) + " gsFileAuthMap=" + JsonUtils.beanToJson(gsFileAuthMap));
                            if (gsFileAuthMap.containsKey(p)) {
                                separately  = true;
                                rList = gsFileAuthMap.get(p);
                                vo1.setRoleList(JsonUtils.beanToJson(rList));
                                vo1.setAuth(rList.get(0).getAuth());
                                break;
                            }
                        }
                    }
                }
            }
            // 没有单独设置
            if (!separately) {
                if (isGroup) {
                    vo1.setRoleList(JsonUtils.beanToJson(roleList));
                } else {
                    // 上级部门权限
                    if (ObjectUtils.isEmpty(auth) && !ObjectUtils.isEmpty(gsAuthMap)) {
                        vo1.setAuth(sourceOperateTool.getAuthByLevel(vo1, vo1.getParentLevel(), gsAuthMap, gsAuthNameMap));
                    }
                }
            }
        }

        sourceOperateTool.setOwnDeleteRole(vo1, loginUser.getUserID());
        vo1.setDownloadUrl("");
        vo1.setHasFile(0);
        vo1.setHasFolder(0);
        vo1.setResolution("");
        vo1.setLength(0);
        vo1.setThumb("");
        vo1.setCover("");
        vo1.setParentName("");
        if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_folder")) {
            vo1.setHasFolder(countMap.get(vo1.getSourceID() + "_folder"));
        }
        if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_file")) {
            vo1.setHasFile(countMap.get(vo1.getSourceID() + "_file"));
            // vo1.setCreateTime(vo1.getFileCreateTime());
        }

        FileMetaVo fileMetaVo = null;
        sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);
        // 用户信息
        vo1.setCreateUserJson(JsonUtils.beanToJson(userMap.get(vo1.getCreateUser())));
        vo1.setModifyUserJson(JsonUtils.beanToJson(userMap.get(vo1.getModifyUser())));

        // path 处理
        sourceOperateTool.opPath(vo1, downloadKey, vo1.getAuth());
        if (!ObjectUtils.isEmpty(vo1.getIsM3u8()) && vo1.getIsM3u8() == 0 && !ObjectUtils.isEmpty(vo1.getIsH264Preview()) && vo1.getIsH264Preview() == 1){
            vo1.setIsM3u8(1);
        }

        /*int isLnkAudio = 0;
        Set<Long> lnkSourceIDList = new HashSet<>();
        if (!ObjectUtils.isEmpty(vo1.getOexeSourceID())) {
            lnkSourceIDList.add(vo1.getOexeSourceID());
            if (!ObjectUtils.isEmpty(vo1.getOexeFileType()) && Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(vo1.getOexeFileType())) {
                isLnkAudio = 1;
            }
        }*/

        // 是否收藏
        vo1.setIsFav(0);
        // 已选标签
        vo1.setTags("");
        vo1.setTagList(new ArrayList<>());
        if (!ObjectUtils.isEmpty(sourceTagMap) && sourceTagMap.containsKey(vo1.getSourceID().toString())) {
            vo1.setTagList(sourceTagMap.get(vo1.getSourceID().toString()));
        }
        // 是否分享
        vo1.setIsShare(0);

        vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? vo1.getThumb() : vo1.getPath());
        vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? "" : vo1.getPath());

        // 父级文件名称
        vo1.setParentName("");
        if (!ObjectUtils.isEmpty(parentPathDisplayMap) && parentPathDisplayMap.containsKey(vo1.getParentID())) {
            vo1.setParentName(parentPathDisplayMap.get(vo1.getParentID()));
        }
        HomeExplorerVO space = homeExplorerDaoImpl.getHomeSpace(loginUser.getUserID(), 0L);
        vo1.setPathDisplay("");
        if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
            vo1.setPathDisplay(sourceOperateTool.setParentPathDisplay(parentPathDisplayMap, vo1.getParentLevel(), space.getSourceID()));
        }
        return vo1;
    }
}
