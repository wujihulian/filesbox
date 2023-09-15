package com.svnlan.tools;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.MetaEnum;
import com.svnlan.enums.SourceSortEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.*;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.home.domain.IoSourceEvent;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.UserAuthTool;
import com.svnlan.home.vo.*;
import com.svnlan.user.dao.GroupSourceDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dao.UserFavDao;
import com.svnlan.user.dao.UserGroupDao;
import com.svnlan.user.domain.GroupSource;
import com.svnlan.user.domain.User;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.ChinesUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 11:33
 */
@Component
public class SourceOperateTool {

    @Resource
    IoSourceMetaDao ioSourceMetaDao;
    @Resource
    IoSourceEventDao ioSourceEventDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    UserGroupDao userGroupDao;
    @Resource
    GroupSourceDao groupSourceDao;
    @Resource
    IoSourceAuthDao ioSourceAuthDao;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    UserDao userDao;
    @Resource
    StorageService storageService;
    @Resource
    UserFavDao userFavDao;
    @Resource
    UserAuthTool userAuthTool;


    public CommonSource getSourceInfo(Long sourceID){
        return ioSourceDao.getSourceInfo(sourceID);
    }

    public void delMetaBySourceID(Long sourceID){
        ioSourceMetaDao.delMetaBySourceID(sourceID, MetaEnum.delKeyList());
    }
    /**
       * @Description: 文件/文件夹拼音
       * @params:  [sourceID, name]
       * @Return:  void
       * @Author:  sulijuan
       * @Date:  2023/2/14 12:15
       * @Modified:
       */
    public void setSourcePinYin(Long sourceID, String name){
        setSourcePinYin(sourceID, name, false);
    }
    public void setSourcePinYin(Long sourceID, String name, boolean isDel){
        // 拼音
        try {
            if (isDel){
                ioSourceMetaDao.delMetaBySourceID(sourceID, MetaEnum.delKeyList());
            }
            // 删除原有的meta
            List<IOSourceMeta> paramList = new ArrayList<>();
            paramList.add(new IOSourceMeta(sourceID, MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(name)));
            paramList.add(new IOSourceMeta(sourceID, MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(name)));

            ioSourceMetaDao.batchInsert(paramList);
        }catch (Exception e){
            LogUtil.error(e, " setSourcePinYin meta error sourceID=" + sourceID + "，name=" + name);
        }
    }
    public void setSourcePinYinList(List<SourceOpDto> list, boolean isDel){
        // 拼音
        try {
            List<Long> sourceIDList = new ArrayList<>();

            List<IOSourceMeta> paramList = new ArrayList<>();
            for (SourceOpDto dto : list){
                sourceIDList.add(dto.getSourceID());

                paramList.add(new IOSourceMeta(dto.getSourceID(), MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(dto.getName())));
                paramList.add(new IOSourceMeta(dto.getSourceID(), MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(dto.getName())));
            }
            if (isDel){
                ioSourceMetaDao.delMetaBySourceIDList(sourceIDList, MetaEnum.delKeyList());
            }

            ioSourceMetaDao.batchInsert(paramList);
        }catch (Exception e){
            LogUtil.error(e, " setSourcePinYin meta error list=" + JsonUtils.beanToJson(list) );
        }
    }

    /**
       * @Description: 文件/文件夹操作event
       * @params:
       * @Return:
       * @Author:  sulijuan
       * @Date:  2023/2/14 12:15
       * @Modified:
       */
    public void setSourceEvent(Long sourceID, Long sourceParent, Long userID, String type, String desc) {
        IoSourceEvent event = new IoSourceEvent();
        try {
            event.setSourceID(sourceID);
            event.setSourceParent(sourceParent);
            event.setUserID(userID);
            event.setType(type);
            event.setDesc(desc);
            ioSourceEventDao.insert(event);
        }catch (Exception e){
            LogUtil.error(e, " setSourceEvent error event=" + JsonUtils.beanToJson(event));
        }
    }
    public void createDefaultSourceEventList(List<IOSource> sourceList, Long userID) {
        List<IoSourceEvent> paramList = new ArrayList<>();
        Map<String, String> reMap = null;
        if (!CollectionUtils.isEmpty(sourceList)){
            for (IOSource n : sourceList){
                reMap = new HashMap<>(2);
                reMap.put("createType", "mkdir");
                reMap.put("name", n.getName());
                paramList.add(new IoSourceEvent(n.getSourceID(), n.getParentID(), userID, EventEnum.mkdir.getCode(), JsonUtils.beanToJson(reMap)));
            }
        }
        try {
            ioSourceEventDao.batchInsert(paramList);
        }catch (Exception e){
            LogUtil.error(e, " createDefaultSourceEventList error paramList=" + JsonUtils.beanToJson(paramList));
        }
    }

    public IOSource setUserDefaultSource(User user, long opUserId, String folders) {
        IOSource source = new IOSource();
        source.setName(user.getName());
        source.setParentLevel(",0,");
        source.setCreateUser(opUserId);
        source.setModifyUser(opUserId);
        source.setTargetID(user.getUserID());
        source.setIsFolder(1);
        source.setTargetType(1);
        source.setFileType("");
        source.setFileID(0L);
        source.setParentID(0L);
        source.setThumbSize(0L);
        source.setConvertSize(0L);
        source.setStorageID(storageService.getDefaultStorageDeviceId());
        source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
        source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
        try {
            ioSourceDao.insert(source);
        }catch (Exception e){
            LogUtil.error(e, " setUserDefaultSource error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }

        if (!ObjectUtils.isEmpty(source.getSourceID())){

            List<IOSourceMeta> pyList = new ArrayList<>();
            pyList.add(new IOSourceMeta(source.getSourceID(), "mySpace", user.getUserID()+""));
            Integer storageID = storageService.getDefaultStorageDeviceId();
            List<String> folderList = ObjectUtils.isEmpty(folders) ? new ArrayList<>() : Arrays.asList(folders.split(",")).stream().map(String::valueOf).collect(Collectors.toList());
            folderList.add("桌面");
            List<IOSource> paramList = new ArrayList<>();
            if (!CollectionUtils.isEmpty(folderList)){
                IOSource sourceCh = null;
                for (String n : folderList){
                    sourceCh = new IOSource();
                    sourceCh.setName(n);
                    sourceCh.setParentLevel(",0," + source.getSourceID() + ",");
                    sourceCh.setParentID(source.getSourceID());
                    sourceCh.setCreateUser(opUserId);
                    sourceCh.setModifyUser(opUserId);
                    sourceCh.setTargetID(user.getUserID());
                    sourceCh.setIsFolder(1);
                    sourceCh.setTargetType(1);
                    sourceCh.setFileType("");
                    sourceCh.setFileID(0L);
                    sourceCh.setConvertSize(0L);
                    sourceCh.setThumbSize(0L);
                    sourceCh.setStorageID(storageID);
                    source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
                    source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
                    paramList.add(sourceCh);
                }
            }
            try {
                ioSourceDao.batchInsert(paramList);
            }catch (Exception e){
                LogUtil.error(e, " setUserDefaultSource batchInsert error");
                throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
            }

            if (!CollectionUtils.isEmpty(paramList)){
                for (IOSource sourceV : paramList){
                    if (ObjectUtils.isEmpty(sourceV.getSourceID())){
                        continue;
                    }
                    if ("桌面".equals(sourceV.getName())){
                        pyList.add(new IOSourceMeta(sourceV.getSourceID(), "myDesktop", user.getUserID()+""));
                        pyList.add(new IOSourceMeta(source.getSourceID(), "myDesktopSource", sourceV.getSourceID() + ""));
                    }
                    pyList.add(new IOSourceMeta(sourceV.getSourceID(), MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(sourceV.getName())));
                    pyList.add(new IOSourceMeta(sourceV.getSourceID(), MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(sourceV.getName())));
                }
            }
            if (!CollectionUtils.isEmpty(pyList)){
                // 拼音
                try {
                    ioSourceMetaDao.batchInsert(pyList);
                }catch (Exception e){
                    LogUtil.error(e, " setUserDefaultSource setSourcePinYin meta error pyList=" + JsonUtils.beanToJson(pyList));
                }
            }
            this.createDefaultSourceEventList(paramList, opUserId);
        }
        return source;
    }


    public HomeExplorerResult setSystemReturn(HomeExplorerResult result, SystemSortVo systemSortVo, String sourceID){

        result.setListType(systemSortVo.getListType());
        result.setListSortField(systemSortVo.getListSortField());
        result.setListSortOrder(systemSortVo.getListSortOrder());
        result.setListIconSize(ObjectUtils.isEmpty(systemSortVo.getFileIconSize()) ? "" : systemSortVo.getFileIconSize());
        if ("1".equals(systemSortVo.getListSortKeep()) && !CollectionUtils.isEmpty(systemSortVo.getListTypeList())){
            for (SystemSortVo vo : systemSortVo.getListTypeList()){
                if (vo.getSourceID().equals(sourceID)){
                    result.setListType(vo.getListType());
                    result.setListIconSize(vo.getFileIconSize());
                    break;
                }
            }
        }
        return result;
    }

    public void opPath(HomeExplorerVO vo1, String downloadKey, String auth){
        opPath(vo1, downloadKey, "", auth);
    }
    public void opPath(HomeExplorerVO vo1, String downloadKey, String shareCode, String auth){


        if (vo1.getIsFolder().intValue() ==1){
            return;
        }
        if (!ObjectUtils.isEmpty(shareCode)){
            try {
                vo1.setDownloadUrl("/api/disk/attachment/" + FileUtil.encodeDownloadUrl(vo1.getName())
                        + "?shareCode=" + URLEncoder.encode(shareCode,"UTF-8") + "&busId="+ vo1.getSourceID() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey);
            }catch (Exception e){
                LogUtil.error(e, "URLEncoder encode error shareCode=" + shareCode);
            }
        }else {
            if (vo1.getTargetType().intValue() == 1){
                // 推拽下载
                vo1.setDownloadUrl("/api/disk/attachment/" + FileUtil.encodeDownloadUrl(vo1.getName())
                        + "?busId=" + vo1.getSourceID() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey);
            }else {
                if (!ObjectUtils.isEmpty(auth)) {
                    List<String> aList = Arrays.asList(auth.split(",")).stream().map(String::valueOf).collect(Collectors.toList());
                    if (aList.contains("4")) {
                        // 推拽下载
                        vo1.setDownloadUrl("/api/disk/attachment/" + FileUtil.encodeDownloadUrl(vo1.getName())
                                + "?busId=" + vo1.getSourceID() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey);
                    }
                }
            }
        }
        // path 处理
        if (!ObjectUtils.isEmpty(vo1.getPath()) && Arrays.asList(GlobalConfig.list_path_source_type).contains(vo1.getFileType())){
            // 预览
            if (Arrays.asList("mp3","gif").contains(vo1.getFileType())){
                if (ObjectUtils.isEmpty(shareCode)){
                    vo1.setDownloadUrl("/api/disk/attachment/" + FileUtil.encodeDownloadUrl(vo1.getName())
                            + "?busId=" + vo1.getSourceID() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey);
                }
            }
            if(Arrays.asList("mp3","jpg", "jpeg", "png").contains(vo1.getFileType().toLowerCase())){
                String fileName = vo1.getPath().substring(vo1.getPath().lastIndexOf("/") + 1);
                String pUrl = vo1.getPath().substring(0, vo1.getPath().lastIndexOf("/") + 1);
                vo1.setpUrl(pUrl.replace("/private/", GlobalConfig.private_replace_key));
                vo1.setFileName(fileName);
            }
            String viewKey = FileUtil.getVideoImgDownloadKey(vo1.getPath(), vo1.getSourceID()+"");
            vo1.setPath("/api/disk/video/img/"+ vo1.getSourceID()+ "_" + vo1.getFileType() +".jpg?showPreview=1&key=" + viewKey);
            // gif返回下载链接

        }else if("oexe".equals(vo1.getFileType())){
            vo1.setOexeContent(FileUtil.getFileContent(vo1.getPath(), StandardCharsets.UTF_8));
            if (!ObjectUtils.isEmpty(vo1.getOexeContent()) && !"{}".equals(vo1.getOexeContent())){
                Map<String, Object> m = JsonUtils.jsonToMap(vo1.getOexeContent());
                if ("lnk".equals(m.get("type")) && m.containsKey("sourceID")){
                    vo1.setOexeSourceID(Long.valueOf(m.get("sourceID").toString()));
                    if (m.containsKey("fileType")){
                        vo1.setOexeFileType(m.get("fileType").toString());
                    }
                    if (m.containsKey("fileID")){
                        vo1.setOexeFileID(Long.valueOf(m.get("fileID").toString()));
                    }
                    if (m.containsKey("isFolder")){
                        vo1.setOexeIsFolder(Integer.valueOf(m.get("isFolder").toString()));
                    }
                }
            }
            vo1.setPath("");
        }else{
            vo1.setPath("");
        }
    }

    public void opHistoryPath(HomeExplorerVO vo1, String downloadKey){

        // 文件夹
        if (vo1.getIsFolder().intValue() ==1){
            return;
        }
        // 预览
        String viewKey = FileUtil.getVideoImgDownloadKey(vo1.getPath());
        vo1.setDownloadUrl("/api/disk/attachment/" + FileUtil.encodeDownloadUrl(vo1.getName())
                + "?busId=" + vo1.getSourceID()+ "&f=" + vo1.getId() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey);

        // path 处理
        if (!ObjectUtils.isEmpty(vo1.getPath()) && Arrays.asList(GlobalConfig.list_path_source_type).contains(vo1.getFileType())){
            if (!"svg".equals(vo1.getFileType())){
                vo1.setPath("/api/disk/video/img/"+ vo1.getSourceID()+ "." + vo1.getFileType() +"?showPreview=1&key=" + viewKey);
            }
        }else if("oexe".equals(vo1.getFileType())){
            vo1.setOexeContent(FileUtil.getFileContent(vo1.getPath(), StandardCharsets.UTF_8));
            vo1.setPath("");
        }else {
            vo1.setPath("");
        }
    }

    public void setFileMetaValue(FileMetaVo fileMetaVo, HomeExplorerVO vo1){
        if (!ObjectUtils.isEmpty(vo1.getValue())){
            fileMetaVo = JsonUtils.jsonToBean(vo1.getValue(), FileMetaVo.class);
            vo1.setLength(!ObjectUtils.isEmpty(fileMetaVo.getLength()) ? fileMetaVo.getLength() : 0);
            vo1.setThumb(!ObjectUtils.isEmpty(fileMetaVo.getThumb()) ? fileMetaVo.getThumb() : "");
            if (!ObjectUtils.isEmpty(vo1.getThumb()) && !Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(vo1.getFileType())){
                String viewKey = FileUtil.getVideoImgDownloadKey(vo1.getThumb(), vo1.getSourceID()+"");
                vo1.setThumb("/api/disk/video/img/"+ vo1.getSourceID()+ "_" + vo1.getFileType() +".jpg?showPreview=1&key=" + viewKey);
            }else {
                vo1.setThumb("");
            }
            vo1.setCover(!ObjectUtils.isEmpty(fileMetaVo.getCover()) ? fileMetaVo.getCover() : "");
            vo1.setResolution(!ObjectUtils.isEmpty(fileMetaVo.getResolution()) ? fileMetaVo.getResolution() : "");
            vo1.setCodecName(!ObjectUtils.isEmpty(fileMetaVo.getCodecName()) ? fileMetaVo.getCodecName() : "");
            vo1.setAvgFrameRate(!ObjectUtils.isEmpty(fileMetaVo.getAvgFrameRate()) ? fileMetaVo.getAvgFrameRate() : "");
            vo1.setFrameRate(!ObjectUtils.isEmpty(fileMetaVo.getFrameRate()) ? fileMetaVo.getFrameRate() : "");
            vo1.setSampleRate(!ObjectUtils.isEmpty(fileMetaVo.getSampleRate()) ? fileMetaVo.getSampleRate() : "");
            vo1.setChannels(!ObjectUtils.isEmpty(fileMetaVo.getChannels()) ? fileMetaVo.getChannels() : "");
            vo1.setAudioCodecName(!ObjectUtils.isEmpty(fileMetaVo.getAudioCodecName()) ? fileMetaVo.getAudioCodecName() : "");
            /*if (!ObjectUtils.isEmpty(vo1.getPreview()) && vo1.getPreview().intValue() == 0){
                vo1.setYzViewData("");
            }else {
                vo1.setYzViewData(!ObjectUtils.isEmpty(fileMetaVo.getYzViewData()) ? fileMetaVo.getYzViewData() : "");
            }*/
            vo1.setYzViewData("");
            vo1.setH264Path(!ObjectUtils.isEmpty(fileMetaVo.getH264Path()) ? fileMetaVo.getH264Path() : "");

        }
        vo1.setValue(null);
    }

    public String getUserAuthByLevel(Long userID, String parentLevel, HomeExplorerDTO homeExplorerDTO){
        String auth = "";
        Long sourceID = homeExplorerDTO.getSourceID();
        List<Long> pSourceIds = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(pSourceIds)){
            pSourceIds = new ArrayList<>();
        }
        pSourceIds.add(sourceID);

        List<GroupSource> allReList = groupSourceDao.getGroupSourceList(new ArrayList<>(pSourceIds));
        if (CollectionUtils.isEmpty(allReList)){
            return auth;
        }
        Map<Long, String> groupAuthMap = null;
        Map<Long, UserGroupVo> groupAuthNameMap = null;
        try {
            // 用户部门权限
            List<UserGroupVo> userGroupList = userAuthTool.getUserGroupAuth(userID);
            if (!CollectionUtils.isEmpty(userGroupList)){
                groupAuthMap = userGroupList.stream().collect(Collectors.toMap(UserGroupVo::getGroupID, UserGroupVo::getAuth, (v1, v2) -> v2));
                groupAuthNameMap = userGroupList.stream().collect(Collectors.toMap(UserGroupVo::getGroupID, Function.identity(), (v1, v2) -> v2));
                Map<Long, Long> gsMap = allReList.stream().collect(Collectors.toMap(GroupSource::getSourceID, GroupSource::getGroupID, (v1, v2) -> v2));
                if (gsMap.containsKey(sourceID) && groupAuthMap.containsKey(gsMap.get(sourceID))){
                    auth = groupAuthMap.get(gsMap.get(sourceID));
                    homeExplorerDTO.setAuthName(groupAuthNameMap.get(gsMap.get(sourceID)).getAuthName());
                    homeExplorerDTO.setAuthID(groupAuthNameMap.get(gsMap.get(sourceID)).getAuthID());
                    homeExplorerDTO.setLabel(groupAuthNameMap.get(gsMap.get(sourceID)).getLabel());
                }

                if (ObjectUtils.isEmpty(auth)){
                    for (GroupSource gs : allReList){
                        if (groupAuthMap.containsKey(gs.getGroupID())){
                            homeExplorerDTO.setAuthName(groupAuthNameMap.get(gs.getGroupID()).getAuthName());
                            homeExplorerDTO.setAuthID(groupAuthNameMap.get(gs.getGroupID()).getAuthID());
                            homeExplorerDTO.setLabel(groupAuthNameMap.get(gs.getGroupID()).getLabel());
                            auth = groupAuthMap.get(gs.getGroupID());
                        }
                    }
                }
            }
        }catch (Exception e){
            LogUtil.error(e, "getUserGroupAuth getUserAuthByLevel error userID=" + userID + "，sourceID=" + sourceID);
            LogUtil.info("getUserGroupAuth getUserAuthByLevel******************** pSourceIds=" + JsonUtils.beanToJson(pSourceIds));
            LogUtil.info("getUserGroupAuth getUserAuthByLevel******************** allReList=" + JsonUtils.beanToJson(allReList));
        }
        return ObjectUtils.isEmpty(auth) ? "" : auth;
    }

    public void getUserAuthByLevel(Long userID, List<Long> pSourceIds, Map<Long, String> gsAuthMap, Map<Long, UserGroupVo> gsAuthNameMap){
        List<GroupSource> allReList = groupSourceDao.getGroupSourceList(new ArrayList<>(pSourceIds));
        if (CollectionUtils.isEmpty(allReList)){
            return ;
        }
        try {
            // 用户部门权限
            List<UserGroupVo> userGroupList = userAuthTool.getUserGroupAuth(userID);
            if (!CollectionUtils.isEmpty(userGroupList)){
                Map<Long, String> groupAuthMap = userGroupList.stream().collect(Collectors.toMap(UserGroupVo::getGroupID, UserGroupVo::getAuth, (v1, v2) -> v2));
                Map<Long, UserGroupVo> groupAuthNameMap = userGroupList.stream().collect(Collectors.toMap(UserGroupVo::getGroupID, Function.identity(), (v1, v2) -> v2));
                for (GroupSource gs : allReList){
                    if (groupAuthMap.containsKey(gs.getGroupID())){
                        gsAuthMap.put(gs.getSourceID(), groupAuthMap.get(gs.getGroupID()));
                        gsAuthNameMap.put(gs.getSourceID(), groupAuthNameMap.get(gs.getGroupID()));
                    }
                }
            }
        }catch (Exception e){
            LogUtil.error(e, " getUserAuthByLevel error userID=" + userID + "，pSourceIds=" + JsonUtils.beanToJson(pSourceIds));
            LogUtil.info("getUserGroupAuth getUserAuthByLevel ******************** pSourceIds=" + JsonUtils.beanToJson(pSourceIds));
            LogUtil.info("getUserGroupAuth getUserAuthByLevel******************** allReList=" + JsonUtils.beanToJson(allReList));
            LogUtil.info("getUserGroupAuth getUserAuthByLevel******************** gsAuthMap=" + gsAuthMap);
        }
        return ;
    }

    public String getAuthByLevel(HomeExplorerVO vo1,String parentLevel, Map<Long, String> gsAuthMap, Map<Long, UserGroupVo> gsAuthNameMap){
        String auth = "";
        List<Long> pSourceIds = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(pSourceIds)){
            UserGroupVo vo = null;
            for (Long pid : pSourceIds){
                if (gsAuthMap.containsKey(pid)){
                    auth = gsAuthMap.get(pid);
                    vo = gsAuthNameMap.get(pid);
                    vo1.setAuthName(vo.getAuthName());
                    vo1.setRoleList(JsonUtils.beanToJson(Arrays.asList(new IoSourceAuthVo(vo.getAuthID(),vo.getAuthName(),vo.getLabel(), vo.getAuth()))) );
                }
            }
        }
        return auth;
    }

    /** 文件单独设置的权限 */
    public Set<Long> getFileAuthByLevel(Long userID, Set<Long> sourceIds, Map<Long, List<IoSourceAuthVo>> gsAuthMap){
        List<IoSourceAuthVo> allReList = ioSourceAuthDao.getSourceAuthBySourceIDList(new ArrayList<>(sourceIds), userID);
        if (CollectionUtils.isEmpty(allReList)){
            return new HashSet<>(sourceIds);
        }
        List<IoSourceAuthVo> userList = allReList.stream().filter(n-> n.getTargetType().intValue() == 1 && n.getTargetID() > 0).collect(Collectors.toList());
        List<IoSourceAuthVo> groupList = allReList.stream().filter(n-> n.getTargetType().intValue() == 2).collect(Collectors.toList());
        List<IoSourceAuthVo> otherUserList = allReList.stream().filter(n-> n.getTargetType().intValue() == 1 && n.getTargetID().longValue() == 0).collect(Collectors.toList());
        try {
            // 用户部门权限
            List<Long> groupIdList = userGroupDao.getMyGroupIDList(userID);
            List<Long> checkList = new ArrayList<>();
            // 用户权限
            if (!CollectionUtils.isEmpty(userList)){
                userList.forEach(n->{
                    if (n.getTargetID().longValue() == userID){
                        gsAuthMap.put(n.getSourceID(), Arrays.asList(n));
                        checkList.add(n.getSourceID());
                    }
                });
            }
            // 部门权限
            if (!CollectionUtils.isEmpty(groupList) && !CollectionUtils.isEmpty(groupIdList)){
                groupList.forEach(n->{
                    if (!checkList.contains(n.getSourceID())) {
                        if (groupIdList.contains(n.getTargetID().longValue()) ) {
                            if (!gsAuthMap.containsKey(n.getSourceID())){
                                gsAuthMap.put(n.getSourceID(), Arrays.asList(n));
                            }else {
                                List l = gsAuthMap.get(n.getSourceID());
                                l.add(n);
                                gsAuthMap.put(n.getSourceID(), l);
                            }
                        }
                    }
                });
            }
            if (!CollectionUtils.isEmpty(otherUserList)){
                otherUserList.forEach(n->{
                    if (!gsAuthMap.containsKey(n.getSourceID())){
                        gsAuthMap.put(n.getSourceID(), Arrays.asList(n));
                    }else if (!groupIdList.contains(n.getTargetID().longValue())){
                        gsAuthMap.put(n.getSourceID(), Arrays.asList(n));
                    }
                });
            }

        }catch (Exception e){
            LogUtil.info("getUserGroupAuth getFileAuthByLevel******************** allReList=" + JsonUtils.beanToJson(allReList));
            LogUtil.info("getUserGroupAuth getFileAuthByLevel******************** gsAuthMap=" + gsAuthMap);
        }

        Set<Long> idList = new HashSet<>();
        sourceIds.forEach(n->{
            if (!ObjectUtils.isEmpty(gsAuthMap) && !gsAuthMap.containsKey(n)){
                idList.add(n);
            }
        });

        return idList;
    }

    public Map<Long, String> getParentPathDisplayMap(Set<String> pList){
        Map<Long, String> map = null;
        if (!CollectionUtils.isEmpty(pList)) {

            List<Long> idSplitList = null;
            Set<Long> ids = new HashSet<>();
            for (String pId : pList){
                if (",0,".equals(pId)){
                    continue;
                }
                idSplitList = Arrays.asList(pId.split(",")).stream().filter(n-> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                ids.addAll(idSplitList);
            }
            if (CollectionUtils.isEmpty(ids)){
                return null;
            }

            List<ParentPathDisplayVo> list = ioSourceDao.getParentPathDisplayByIds(new ArrayList<>(ids));
            if (!CollectionUtils.isEmpty(list)) {
                map = list.stream().collect(Collectors.toMap(ParentPathDisplayVo::getSourceID, ParentPathDisplayVo::getName, (v1, v2) -> v2));
            }
        }
        return map;
    }

    public String setParentPathDisplay(Map<Long, String> map, String parentLevel, Long spaceID){

        List<Long> idList = Arrays.asList(parentLevel.split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(idList)){
            return "";
        }
        List<String> nameList = new ArrayList<>();
        for (Long id: idList){
            if (map.containsKey(id)){
                if (id.longValue() == spaceID){
                    nameList.add(I18nUtils.tryI18n("explorer.toolbar.rootPath"));
                }else {
                    nameList.add(map.get(id));
                }
            }
        }
        return CollectionUtils.isEmpty(nameList) ? "" : StringUtil.joinString(nameList,"/");
    }

    public HomeExplorerVO mySpaceDefault(Long userID) {

        HomeExplorerVO space = homeExplorerDao.getHomeSpace(userID, 0L);
        List<IOSourceMeta> pyList = new ArrayList<>();
        String spaceSourceIDStr = ioSourceMetaDao.getSourceIDMetaByKey(userID + "", "mySpace");
        // 个人空间
        if (ObjectUtils.isEmpty(spaceSourceIDStr)) {
            pyList.add(new IOSourceMeta(space.getSourceID(), "mySpace", userID + ""));
        }
        return space;
    }
    public HomeExplorerVO myDesktopDefault(Long userID) {
        IOSource sourceCh = null;
        List<IOSourceMeta> pyList = new ArrayList<>();
        Long sourceID = 0L;
        String desktopSourceIDStr = ioSourceMetaDao.getSourceIDMetaByKey(userID + "", "myDesktop");
        if (ObjectUtils.isEmpty(desktopSourceIDStr)){
            HomeExplorerVO space = mySpaceDefault(userID);

            List<IOSourceVo> desktopSourceList = ioSourceDao.getDesktopSourceList(space.getSourceID(),"桌面");

            if (!CollectionUtils.isEmpty(desktopSourceList)){
                IOSourceVo sourceChVo = desktopSourceList.get(0);
                sourceID = sourceChVo.getSourceID();
                pyList.add(new IOSourceMeta(sourceChVo.getSourceID(), "myDesktop", userID+""));
                pyList.add(new IOSourceMeta(space.getSourceID(), "myDesktopSource", sourceChVo.getSourceID() + ""));
                if (!CollectionUtils.isEmpty(pyList)){
                    // 拼音
                    try {
                        ioSourceMetaDao.batchInsert(pyList);
                    }catch (Exception e){
                        LogUtil.error(e, " setUserDefaultSource setSourcePinYin meta error pyList=" + JsonUtils.beanToJson(pyList));
                    }
                }
            }else {
                Integer storageID = storageService.getDefaultStorageDeviceId();
                List<String> folderList =  new ArrayList<>() ;
                folderList.add("桌面");
                List<IOSource> paramList = new ArrayList<>();
                if (!CollectionUtils.isEmpty(folderList)){
                    for (String n : folderList){
                        sourceCh = new IOSource();
                        sourceCh.setName(n);
                        sourceCh.setParentLevel(",0," + space.getSourceID() + ",");
                        sourceCh.setParentID(space.getSourceID());
                        sourceCh.setCreateUser(userID);
                        sourceCh.setModifyUser(userID);
                        sourceCh.setTargetID(userID);
                        sourceCh.setIsFolder(1);
                        sourceCh.setTargetType(1);
                        sourceCh.setFileType("");
                        sourceCh.setFileID(0L);
                        sourceCh.setConvertSize(0L);
                        sourceCh.setThumbSize(0L);
                        sourceCh.setStorageID(storageID);
                        sourceCh.setNamePinyin(ChinesUtil.getPingYin(sourceCh.getName()));
                        sourceCh.setNamePinyinSimple(ChinesUtil.getFirstSpell(sourceCh.getName()));
                        paramList.add(sourceCh);
                    }
                }
                try {
                    ioSourceDao.batchInsert(paramList);
                }catch (Exception e){
                    LogUtil.error(e, " setUserDefaultSource batchInsert error");
                    throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
                }

                if (!CollectionUtils.isEmpty(paramList)){
                    for (IOSource sourceV : paramList){
                        if (ObjectUtils.isEmpty(sourceV.getSourceID())){
                            continue;
                        }
                        sourceID =  sourceV.getSourceID();
                        if ("桌面".equals(sourceV.getName())){
                            pyList.add(new IOSourceMeta(sourceV.getSourceID(), "myDesktop", userID+""));
                            pyList.add(new IOSourceMeta(space.getSourceID(), "myDesktopSource", sourceV.getSourceID() + ""));
                        }
                        pyList.add(new IOSourceMeta(sourceV.getSourceID(), MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(sourceV.getName())));
                        pyList.add(new IOSourceMeta(sourceV.getSourceID(), MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(sourceV.getName())));
                    }
                }
                if (!CollectionUtils.isEmpty(pyList)){
                    // 拼音
                    try {
                        ioSourceMetaDao.batchInsert(pyList);
                    }catch (Exception e){
                        LogUtil.error(e, " setUserDefaultSource setSourcePinYin meta error pyList=" + JsonUtils.beanToJson(pyList));
                    }
                }
                this.createDefaultSourceEventList(paramList, userID);

            }

        }else {
            sourceID = Long.parseLong(desktopSourceIDStr);
        }
        HomeExplorerVO commonSource = null;
        if (!ObjectUtils.isEmpty(sourceID) && sourceID > 0){
            Map<String, Object> hashMap = new HashMap<>(5);
            hashMap.put("thisSourceID", sourceID);
            hashMap.put("startIndex", 0);
            hashMap.put("pageSize", 1);
            hashMap.put("sortType", SourceSortEnum.getSortType(""));
            hashMap.put("sortField", "createTime");


            List<HomeExplorerVO> list = homeExplorerDao.getHomeExplorer(hashMap);
            if (!CollectionUtils.isEmpty(list)){
                commonSource = list.get(0);
                Map<String, List<CommonLabelVo>> sourceTagMap = this.getSourceTagMap(userID, Arrays.asList(commonSource.getSourceID()+""));
                List<UserVo> userList = userDao.getUserBaseInfo(new ArrayList<>(Arrays.asList(commonSource.getCreateUser(), commonSource.getModifyUser())));
                Map<Long, UserVo> userMap = CollectionUtils.isEmpty(userList) ? null : userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));

                if (!ObjectUtils.isEmpty(userMap)) {
                    // 用户信息
                    commonSource.setCreateUserJson(JsonUtils.beanToJson(userMap.get(commonSource.getCreateUser())));
                    commonSource.setModifyUserJson(JsonUtils.beanToJson(userMap.get(commonSource.getModifyUser())));
                }
                // 已选标签
                commonSource.setTags("");
                commonSource.setTagList(new ArrayList<>());
                if (!ObjectUtils.isEmpty(sourceTagMap) && sourceTagMap.containsKey(commonSource.getSourceID().toString())){
                    commonSource.setTagList(sourceTagMap.get(commonSource.getSourceID().toString()));
                }
            }
        }
        return commonSource;
    }

    public Map<String, List<CommonLabelVo>> getSourceTagMap(Long userID, List<String> sourceIDs) {
        Map<String, List<CommonLabelVo>> map = null;
        List<UserFavVo> myTagList = userFavDao.getFileTagBySourceID(userID, sourceIDs);
        if (!CollectionUtils.isEmpty(myTagList)) {
            map = myTagList.stream().collect(Collectors.toMap(UserFavVo::getPath, UserFavVo::getFavList, (v1, v2) -> v2));
        }
        return map;
    }
}
