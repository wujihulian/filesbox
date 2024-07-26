package com.svnlan.home.service.impl;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.*;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.*;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.dto.HomeSettingDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.service.HomeExplorerService;
import com.svnlan.home.utils.*;
import com.svnlan.home.vo.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SourceOperateTool;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.user.dao.*;
import com.svnlan.user.domain.GroupSource;
import com.svnlan.user.domain.Role;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.user.vo.OptionVo;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.*;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author KingMgg
 * @data 2023/2/6 11:48
 */
@Service
public class HomeExplorerServiceImpl implements HomeExplorerService {
    @Resource
    RoleDao roleDaoImpl;
    @Resource
    HomeExplorerDao homeExplorerDaoImpl;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    SourceOperateTool sourceOperateTool;
    @Resource
    SystemOptionDao systemOptionDaoImpl;
    @Resource
    SystemLogTool systemLogTool;
    @Resource
    CommonLabelDao commonLabelDaoImpl;
    @Resource
    SystemSortTool systemSortTool;
    @Resource
    UserOptionDao userOptionDao;
    @Resource
    IoSourceEventDao ioSourceEventDao;
    @Resource
    UserDao userDaoImpl;

    @Resource
    IoSourceMetaDao ioSourceMetaDao;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    RoleDao roleDao;
    @Resource
    OptionTool optionTool;
    @Resource
    ParamUtils paramUtils;
    @Resource
    GroupSourceDao groupSourceDao;
    @Resource
    StorageService storageService;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    AsyncCutImgUtil asyncCutImgUtil;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    UserAuthTool userAuthTool;


    @Override
    public HomeExplorerResult homeExplorer(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser) {
        // 默认创建目录
        String treeOpen = systemOptionDaoImpl.getSystemConfigByKey("treeOpen");

        // my 个人空间,myFav 收藏夹,rootGroup 企业云盘,recentDoc 最近文档,fileType 文件类型,fileTag 标签,shareLink 外链分享
        // my,myFav,rootGroup,recentDoc,fileType,fileTag,shareLink
        List<String> treeOpenList = ObjectUtils.isEmpty(treeOpen) ? new ArrayList<>() : Arrays.asList(treeOpen.split(",")).stream().map(String::valueOf).collect(Collectors.toList());

        HomeExplorerResult result = null;
        homeExplorerDTO.setBlock(ObjectUtils.isEmpty(homeExplorerDTO.getBlock()) ? "default" : homeExplorerDTO.getBlock());

        switch (homeExplorerDTO.getBlock()){
            case "root":
                result = this.getRootList(loginUser, treeOpenList);
                break;
            case "files":
                result = this.getMyMenuList(loginUser, treeOpenList);
                break;
            case "tools":
                result = this.getToolList(treeOpenList);
                break;
            case "fileType":
                if (!treeOpenList.contains(homeExplorerDTO.getBlock())){
                    throw new SvnlanRuntimeException(CodeMessageEnum.errorAdminAuth.getCode());
                }
                result = this.getFileTypeList();
                break;
            case "fileTag":
                if (!treeOpenList.contains(homeExplorerDTO.getBlock())){
                    throw new SvnlanRuntimeException(CodeMessageEnum.errorAdminAuth.getCode());
                }
                result = this.getFileTagList(loginUser);
                break;
            case "fav":
                // 收藏夹
                result = this.getUserFavList(homeExplorerDTO, loginUser);
                break;
            case "recycle":
                // 回收站
                result = this.getRecycleList(homeExplorerDTO, loginUser);
                break;
            case "userRencent":
                // 最近文档
                result = this.getUserRencentList(homeExplorerDTO, loginUser);
                break;
            case "default":
                result = this.getHomeExplorerList(homeExplorerDTO, loginUser);
                break;
            default:
                result = this.getHomeExplorerList(homeExplorerDTO, loginUser);
                break;
        }
        return result;
    }
    /** 收藏夹*/
    private HomeExplorerResult getUserFavList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        List<IoSourceAuthVo> roleList = null;
        boolean checkGet = false;
        String auth = "";
        if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
            auth = GlobalConfig.SYSTEM_GROUP_AUTH;
            checkGet = true;

            Role role = roleDaoImpl.getSystemRoleInfo();
            roleList = new ArrayList<>();
            roleList.add(new IoSourceAuthVo(role.getRoleID(), role.getRoleName(), role.getLabel(), auth));
        }

        Map<String, Object> hashMap = new HashMap<>(4);
        hashMap.put("userID", loginUser.getUserID());
        hashMap.put("opType", "userFav");
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getKeyword())) {
            hashMap.put("keyword", homeExplorerDTO.getKeyword().toLowerCase());
        }
        hashMap.put("sortType", SourceSortEnum.getSortType(""));
        hashMap.put("sortField", "createTime");

        hashMap.put("startIndex", homeExplorerDTO.getStartIndex());
        hashMap.put("pageSize", homeExplorerDTO.getPageSize());
        hashMap.put("tenantId", tenantUtil.getTenantIdByServerName());
//        PageHelper.startPage(homeExplorerDTO.getCurrentPage(), homeExplorerDTO.getPageSize());
        Long total = homeExplorerDao.getUserFavExplorerCount(hashMap);
        List<HomeExplorerVO> list = null;
        if (0 < total) {
            list = homeExplorerDao.getUserFavExplorer(hashMap);
        }
        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        List<HomeExplorerVO> fileVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {

            FileMetaVo fileMetaVo = null;
            String downloadKey = FileUtil.getDownloadKey();

            int isLnkAudio = 0;
            Set<Long> lnkSourceIDList = new HashSet<>();
            Set<Long> uidList = new HashSet<>();
            List<Long> idList = new ArrayList<>();
            List<String> sourceIDs = new ArrayList<>();
            Set<String> pLevelList = new HashSet<>();
            boolean isSelect = false;
            Set<Long> gpidList = new HashSet<>();
            Set<Long> sidList = new HashSet<>();
            long spaceId = 0;
            for (HomeExplorerVO vo2 : list){
                if (!ObjectUtils.isEmpty(vo2.getSourceID()) && vo2.getSourceID() > 0) {
                    isSelect = true;
                    if (vo2.getIsFolder().intValue() == 1) {
                        idList.add(vo2.getSourceID());
                    }
                    uidList.add(vo2.getCreateUser());
                    uidList.add(vo2.getModifyUser());
                    pLevelList.add(vo2.getParentLevel());
                    sourceIDs.add(vo2.getSourceID().toString());
                    if (!checkGet && vo2.getTargetType().intValue() == 2){
                        checkGet = true;
                        sidList.add(vo2.getSourceID());
                        if (!",0,".equals(vo2.getParentLevel())) {
                            Set<Long> aList = Arrays.asList(vo2.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toSet());
                            gpidList.addAll(aList);
                            sidList.addAll(aList);
                        }
                    }
                    if (spaceId == 0 && 1 == vo2.getTargetType() && !",0,".equals(vo2.getParentLevel())){
                        List<Long> list1 = Arrays.asList(vo2.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                        spaceId = list1.get(0);
                    }
                }
            }
            if (spaceId == 0){
                HomeExplorerVO space = homeExplorerDao.getHomeSpace(loginUser.getUserID(), 0L);
                spaceId = space.getSourceID();
            }
            Map<Long, List<IoSourceAuthVo>> gsFileAuthMap = new HashMap<>(1);
            // 文件权限
            if (!CollectionUtils.isEmpty(sidList)){
                sourceOperateTool.getFileAuthByLevel(loginUser.getUserID(), sidList, gsFileAuthMap);
            }

            Map<Long, String> gsAuthMap = new HashMap<>();
            Map<Long, UserGroupVo> gsAuthNameMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(gpidList)){
                sourceOperateTool.getUserAuthByLevel(loginUser.getUserID(), new ArrayList<>(gpidList), gsAuthMap, gsAuthNameMap);
            }

            // 标签
            Map<String, List<CommonLabelVo>> sourceTagMap = isSelect ? fileOptionTool.getSourceTagMap(loginUser.getUserID(), sourceIDs) : null;

            // 链接分享
            List<Long> shareList = isSelect ? fileOptionTool.checkUserIsShare(loginUser.getUserID()) : null;

            List<UserVo> userList = isSelect ? userDaoImpl.getUserBaseInfo(new ArrayList<>(uidList)) : null;
            Map<Long, UserVo> userMap = !CollectionUtils.isEmpty(userList) ? userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2)) : null;
            if (ObjectUtils.isEmpty(userMap)){
                userMap = new HashMap<>(1);
            }
            userMap.put(0L, sourceOperateTool.getDefaultSystemUser());
            // 父级文件名称、位置
            Map<Long, String> parentPathDisplayMap = null;
            // 文件位置
            if (!CollectionUtils.isEmpty(pLevelList)) {
                parentPathDisplayMap = sourceOperateTool.getParentPathDisplayMap(pLevelList);

            }
            List<HomeExplorerVO> countList = CollectionUtils.isEmpty(idList) ? null : homeExplorerDao.getSourceChileCont(idList);
            Map<String, Integer> countMap = new HashMap<>(1);
            if (!CollectionUtils.isEmpty(countList)) {
                for (HomeExplorerVO home : countList) {
                    countMap.put(home.getParentID() + (home.getIsFolder().intValue() == 1 ? "_folder" : "_file"), home.getFileCount());
                }
            }
            for (HomeExplorerVO vo1 : list){
                vo1.setAuth(auth);
                if (ObjectUtils.isEmpty(auth) && vo1.getTargetType().intValue() == 2 ){
                    if (!ObjectUtils.isEmpty(gsFileAuthMap) && gsFileAuthMap.containsKey(vo1.getSourceID())){
                        vo1.setRoleList(JsonUtils.beanToJson(gsFileAuthMap.get(vo1.getSourceID())));
                    }else {
                        // 上级部门权限
                        if (!ObjectUtils.isEmpty(gsAuthMap)) {
                            vo1.setAuth(sourceOperateTool.getAuthByLevel(vo1,vo1.getParentLevel(), gsAuthMap, gsAuthNameMap));
                        }
                    }
                    if (ObjectUtils.isEmpty(vo1.getAuth()) || "0".equals(vo1.getAuth())){
                        // 不可见
                        continue;
                    }
                    if (!Arrays.asList(vo1.getAuth().split(",")).stream().collect(Collectors.toList()).contains("1")){
                        // 无列表权限
                        continue;
                    }
                }
                sourceOperateTool.setOwnDeleteRole(vo1, loginUser.getUserID());
                vo1.setHasFile(0);
                vo1.setHasFolder(0);
                // 是否收藏
                vo1.setIsFav(1);
                vo1.setParentName("");
                vo1.setCreateUserJson("");
                vo1.setModifyUserJson("");
                vo1.setTagList(new ArrayList<>());
                if (!ObjectUtils.isEmpty(vo1.getSourceID()) && vo1.getSourceID() > 0) {
                    if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_folder")) {
                        vo1.setHasFolder(countMap.get(vo1.getSourceID() + "_folder"));
                    }
                    if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_file")) {
                        vo1.setHasFile(countMap.get(vo1.getSourceID() + "_file"));
                    }
                    if (1 == vo1.getTargetType().intValue() && 0 == vo1.getParentID().longValue()) {
                        // 个人空间
                        vo1.setName(I18nUtils.i18n("explorer.toolbar.rootPath"));
                    }

                    // 是否分享
                    vo1.setIsShare(!CollectionUtils.isEmpty(shareList) && shareList.contains(vo1.getSourceID()) ? 1 : 0);

                    if (vo1.getIsFolder() == 1) {
                        vo1.setIcon("folder");
                    } else {
                        vo1.setIcon("file");
                        sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);
                    }

                    // 用户信息
                    vo1.setCreateUserJson(JsonUtils.beanToJson(userMap.get(vo1.getCreateUser())));
                    vo1.setModifyUserJson(JsonUtils.beanToJson(userMap.get(vo1.getModifyUser())));
                    // 父级文件名称
                    if (!ObjectUtils.isEmpty(parentPathDisplayMap) && parentPathDisplayMap.containsKey(vo1.getParentID())) {
                        vo1.setParentName(parentPathDisplayMap.get(vo1.getParentID()));
                    }
                    vo1.setPathDisplay("");
                    if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
                        vo1.setPathDisplay(sourceOperateTool.setParentPathDisplay(parentPathDisplayMap, vo1.getParentLevel(), spaceId));
                    }
                    // path 处理
                    sourceOperateTool.opPath(vo1, downloadKey, vo1.getAuth());
                    if (!ObjectUtils.isEmpty(vo1.getIsM3u8()) && vo1.getIsM3u8() == 0 && !ObjectUtils.isEmpty(vo1.getIsH264Preview()) && vo1.getIsH264Preview() == 1){
                        vo1.setIsM3u8(1);
                    }
                    if (!ObjectUtils.isEmpty(vo1.getOexeSourceID())) {
                        lnkSourceIDList.add(vo1.getOexeSourceID());
                        if (!ObjectUtils.isEmpty(vo1.getOexeFileType()) && Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(vo1.getOexeFileType())) {
                            isLnkAudio = 1;
                        }
                    }
                    // 已选标签
                    if (!ObjectUtils.isEmpty(sourceTagMap) && sourceTagMap.containsKey(vo1.getSourceID().toString())) {
                        vo1.setTagList(sourceTagMap.get(vo1.getSourceID().toString()));
                    }
                }else {
                    vo1.setName(vo1.getFavName());
                    vo1.setPath("");
                    vo1.setTargetType(0);
                    vo1.setSourceID(0L);
                    vo1.setIsFolder(0);
                    vo1.setFileType("");
                    vo1.setParentID(0L);
                    vo1.setFileID(0L);
                    vo1.setParentLevel("");
                    vo1.setIsDelete(0);
                    vo1.setSize(0L);
                    vo1.setCreateTime(0L);
                    vo1.setModifyTime(0L);
                    vo1.setSize(0L);
                    vo1.setValue("");
                    vo1.setHashMd5("");
                    vo1.setDescription("");
                    vo1.setPathDisplay("");
                }

                folderVOList.add(vo1);
            }

            oexeFilePath(downloadKey, lnkSourceIDList, isLnkAudio, folderVOList);

        }

        long total2 = 0;
        if (!CollectionUtils.isEmpty(folderVOList)){
            total2 = folderVOList.size();
        }
        result.setTotal(total2);
        result.setFolderList(folderVOList);
        result.setFileList(fileVOList);

        return result;
    }


    private void oexeFilePath(String downloadKey, Set<Long> lnkSourceIDList, int isLnkAudio, List<HomeExplorerVO> fileVOList){
        if (!CollectionUtils.isEmpty(lnkSourceIDList)) {
            List<HomeExplorerVO> lnkFileList = homeExplorerDao.getFolderAndImgAndAudioHomeExplorer(new ArrayList<>(lnkSourceIDList), isLnkAudio);
            if (!CollectionUtils.isEmpty(lnkFileList)) {
                Map<Long, HomeExplorerVO> lnkFileMap = lnkFileList.stream().collect(Collectors.toMap(HomeExplorerVO::getSourceID, Function.identity(), (v1, v2) -> v2));
                for (HomeExplorerVO vo3 : fileVOList) {
                    if (lnkFileMap.containsKey(vo3.getOexeSourceID())) {
                        HomeExplorerVO vo4 = lnkFileMap.get(vo3.getOexeSourceID());
                        vo4.setFileType(vo3.getOexeFileType());
                        if (!ObjectUtils.isEmpty(vo3.getOexeFileType())){
                            if (Arrays.asList("dcm").contains(vo3.getOexeFileType())
                                    || Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(vo3.getOexeFileType())) {
                                vo4.setDownloadUrl("/api/disk/attachment/" + FileUtil.encodeDownloadUrl(vo4.getName())
                                        + "?busId=" + vo4.getSourceID() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey);
                                vo4.setPath("");
                            }else if (!ObjectUtils.isEmpty(vo3.getOexeFileType()) && "gif".equals(vo3.getOexeFileType())) {
                                vo4.setDownloadUrl("/api/disk/attachment/" + FileUtil.encodeDownloadUrl(vo4.getName())
                                        + "?busId=" + vo4.getSourceID() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey);
                                vo4.setPath(FileUtil.getShowImageUrl(vo4.getPath(), vo4.getSourceID() + ""));
                            }else if (!ObjectUtils.isEmpty(vo3.getOexeFileType()) && Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(vo3.getOexeFileType())) {
                                vo4.setPath(FileUtil.getShowImageUrl(vo4.getPath(), vo4.getSourceID() + ""));
                            }
                        }else {
                            vo4.setPath("");
                        }
                        vo3.setSourceInfo(vo4);
                    }
                }
            }
        }
    }

    /** 回收站*/
    private HomeExplorerResult getRecycleList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        Map<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("userID", loginUser.getUserID());
        hashMap.put("tenantId", tenantUtil.getTenantIdByServerName());
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getKeyword())) {
            hashMap.put("keyword", homeExplorerDTO.getKeyword().toLowerCase());
        }
        SystemSortVo systemSortVo = systemSortTool.getUserSort(loginUser.getUserID());
        String key = "userRecycle";
        hashMap = systemSortTool.setSortAboutMap(systemSortVo,hashMap, loginUser.getUserID(), homeExplorerDTO.getSortField()
                , homeExplorerDTO.getSortType(), key, "io_source.");

        Long total = homeExplorerDao.getUserRecycleExplorerCount(hashMap);
        hashMap.put("startIndex", homeExplorerDTO.getStartIndex());
        hashMap.put("pageSize", homeExplorerDTO.getPageSize());
//        PageHelper.startPage(homeExplorerDTO.getCurrentPage(), homeExplorerDTO.getPageSize());
        List<HomeExplorerVO> list = null;
        if (0 < total) {
            list = homeExplorerDao.getUserRecycleExplorer(hashMap);
        }
        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        List<HomeExplorerVO> fileVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            FileMetaVo fileMetaVo = null;
            String downloadKey = FileUtil.getDownloadKey();
            long spaceId = 0;

            Set<String> pLevelList = new HashSet<>();
            Set<Long> uidList = new HashSet<>();
            List<Long> idList = new ArrayList<>();
            List<String> sourceIDs = new ArrayList<>();
            for (HomeExplorerVO vo2 : list){
                if (vo2.getIsFolder().intValue() == 1){
                    idList.add(vo2.getSourceID());
                }
                sourceIDs.add(vo2.getSourceID().toString());
                uidList.add(vo2.getCreateUser());
                uidList.add(vo2.getModifyUser());
                pLevelList.add(vo2.getParentLevel());
                if (spaceId == 0 && 1 == vo2.getTargetType() && !",0,".equals(vo2.getParentLevel())){
                    List<Long> list1 = Arrays.asList(vo2.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    spaceId = list1.get(0);
                }
            }
            if (spaceId == 0){
                HomeExplorerVO space = homeExplorerDao.getHomeSpace(loginUser.getUserID(), 0L);
                spaceId = space.getSourceID();
            }

            List<UserVo> userList = userDaoImpl.getUserBaseInfo(new ArrayList<>(uidList));
            Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));
            if (ObjectUtils.isEmpty(userMap)){
                userMap = new HashMap<>(1);
            }
            userMap.put(0L, sourceOperateTool.getDefaultSystemUser());

            // 标签
            Map<String, List<CommonLabelVo>> sourceTagMap = fileOptionTool.getSourceTagMap(loginUser.getUserID(), sourceIDs);
            // 收藏
            List<String> myFavList = fileOptionTool.checkIsFavByUserId(loginUser.getUserID());
            // 链接分享
            List<Long> shareList = fileOptionTool.checkUserIsShare(loginUser.getUserID());
            Map<Long, String> parentPathDisplayMap = null;
            // 文件位置
            if (!CollectionUtils.isEmpty(pLevelList)) {
                parentPathDisplayMap = sourceOperateTool.getParentPathDisplayMap(pLevelList);

            }
            List<HomeExplorerVO> countList = CollectionUtils.isEmpty(idList) ? null : homeExplorerDao.getSourceChileCont(idList);
            Map<String, Integer> countMap = new HashMap<>(1);
            if (!CollectionUtils.isEmpty(countList)) {
                for (HomeExplorerVO home : countList) {
                    countMap.put(home.getParentID() + (home.getIsFolder().intValue() == 1 ? "_folder" : "_file"), home.getFileCount());
                }
            }
            for (HomeExplorerVO vo1 : list){
                vo1.setHasFile(0);
                vo1.setHasFolder(0);
                if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_folder")){
                    vo1.setHasFolder(countMap.get(vo1.getSourceID() + "_folder"));
                }
                if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_file")){
                    vo1.setHasFile(countMap.get(vo1.getSourceID() + "_file"));
                }
                // 是否收藏
                vo1.setIsFav(!CollectionUtils.isEmpty(myFavList) && myFavList.contains(vo1.getSourceID().toString()) ? 1 : 0);

                // 是否分享
                vo1.setIsShare(!CollectionUtils.isEmpty(shareList) && shareList.contains(vo1.getSourceID()) ? 1 : 0);
                // FileMeta value 处理
                sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);

                // 用户信息
                vo1.setCreateUserJson(JsonUtils.beanToJson(userMap.get(vo1.getCreateUser())));
                vo1.setModifyUserJson(JsonUtils.beanToJson(userMap.get(vo1.getModifyUser())));

                // path 处理
                sourceOperateTool.opPath( vo1, downloadKey, "");
                if (!ObjectUtils.isEmpty(vo1.getIsM3u8()) && vo1.getIsM3u8() == 0 && !ObjectUtils.isEmpty(vo1.getIsH264Preview()) && vo1.getIsH264Preview() == 1){
                    vo1.setIsM3u8(1);
                }
                // 已选标签
                vo1.setTags("");
                vo1.setTagList(new ArrayList<>());
                if (!ObjectUtils.isEmpty(sourceTagMap) && sourceTagMap.containsKey(vo1.getSourceID().toString())){
                    vo1.setTagList(sourceTagMap.get(vo1.getSourceID().toString()));
                }
                vo1.setPathDisplay("");
                if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
                    vo1.setPathDisplay(sourceOperateTool.setParentPathDisplay(parentPathDisplayMap, vo1.getParentLevel(), spaceId));
                }
                if (vo1.getIsFolder() == 1) {
                    vo1.setIcon("folder");
                    folderVOList.add(vo1);
                } else {
                    vo1.setIcon("file");
                    fileVOList.add(vo1);
                }
            }
        }
//        PageInfo<HomeExplorerVO> pageInfo = new PageInfo<>(list);
        result.setTotal(total);
        result.setFolderList(folderVOList);
        result.setFileList(fileVOList);

        return sourceOperateTool.setSystemReturn(result, systemSortVo, "userRecycle");
    }
    /** 最近文档*/
    private HomeExplorerResult getUserRencentList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        List<IoSourceAuthVo> roleList = null;
        boolean isSystem = false;
        String auth = "";
        if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
            auth = GlobalConfig.SYSTEM_GROUP_AUTH;
            isSystem = true;
            Role role = roleDaoImpl.getSystemRoleInfo();
            roleList = new ArrayList<>();
            roleList.add(new IoSourceAuthVo(role.getRoleID(), role.getRoleName(), role.getLabel(), auth));
        }
        Map<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("userID", loginUser.getUserID());
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getKeyword())) {
            hashMap.put("keyword", homeExplorerDTO.getKeyword().toLowerCase());
        }
        SystemSortVo systemSortVo = systemSortTool.getUserSort(loginUser.getUserID());

        String key = "recentDoc";
        hashMap = systemSortTool.setSortAboutMap(systemSortVo,hashMap, loginUser.getUserID(), homeExplorerDTO.getSortField()
                , homeExplorerDTO.getSortType(), key, "io_source.");


        hashMap.put("startIndex", homeExplorerDTO.getStartIndex());
        hashMap.put("pageSize", homeExplorerDTO.getPageSize());
//        PageHelper.startPage(homeExplorerDTO.getCurrentPage(), homeExplorerDTO.getPageSize());
        Long total = homeExplorerDao.getUserRecentExplorerCount(hashMap);
        List<HomeExplorerVO> list = null;
        if (0 < total) {
            list = homeExplorerDao.getUserRecentExplorer(hashMap);
        }
        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        List<HomeExplorerVO> fileVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            FileMetaVo fileMetaVo = null;
            String downloadKey = FileUtil.getDownloadKey();
            Set<Long> uidList = new HashSet<>();
            Set<Long> gpidList = new HashSet<>();
            Set<Long> sidList = new HashSet<>();
            Set<String> pLevelList = new HashSet<>();
            long spaceId = 0;
            for (HomeExplorerVO vo2 : list){
                uidList.add(vo2.getCreateUser());
                uidList.add(vo2.getModifyUser());
                if (!isSystem && vo2.getTargetType().intValue() == 2) {
                    sidList.add(vo2.getSourceID());
                    if (!",0,".equals(vo2.getParentLevel())) {
                        Set<Long> aList = Arrays.asList(vo2.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toSet());
                        gpidList.addAll(aList);
                        sidList.addAll(aList);
                    }
                }
                if (spaceId == 0 && 1 == vo2.getTargetType() && !",0,".equals(vo2.getParentLevel())){
                    List<Long> list1 = Arrays.asList(vo2.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                    spaceId = list1.get(0);
                }
                pLevelList.add(vo2.getParentLevel());
            }
            if (spaceId == 0){
                HomeExplorerVO space = homeExplorerDao.getHomeSpace(loginUser.getUserID(), 0L);
                spaceId = space.getSourceID();
            }
            Map<Long, List<IoSourceAuthVo>> gsFileAuthMap = new HashMap<>(1);
            // 文件权限
            if (!CollectionUtils.isEmpty(sidList)){
                sourceOperateTool.getFileAuthByLevel(loginUser.getUserID(), sidList, gsFileAuthMap);
            }

            Map<Long, String> gsAuthMap = new HashMap<>();
            Map<Long, UserGroupVo> gsAuthNameMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(gpidList)){
                sourceOperateTool.getUserAuthByLevel(loginUser.getUserID(), new ArrayList<>(gpidList), gsAuthMap, gsAuthNameMap);
            }

            // 链接分享
            List<Long> shareList = fileOptionTool.checkUserIsShare(loginUser.getUserID());
            // 收藏
            List<String> myFavList = fileOptionTool.checkIsFavByUserId(loginUser.getUserID());

            List<UserVo> userList = userDaoImpl.getUserBaseInfo(new ArrayList<>(uidList));
            Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));
            if (ObjectUtils.isEmpty(userMap)){
                userMap = new HashMap<>(1);
            }
            userMap.put(0L, sourceOperateTool.getDefaultSystemUser());
            Map<Long, String> parentPathDisplayMap = null;
            // 文件位置
            if (!CollectionUtils.isEmpty(pLevelList)) {
                parentPathDisplayMap = sourceOperateTool.getParentPathDisplayMap(pLevelList);

            }

            for (HomeExplorerVO vo1 : list){
                vo1.setAuth(auth);
                if (!isSystem && vo1.getTargetType().intValue() == 2 ){
                    if (!ObjectUtils.isEmpty(gsFileAuthMap) && gsFileAuthMap.containsKey(vo1.getSourceID())){
                        vo1.setRoleList(JsonUtils.beanToJson(gsFileAuthMap.get(vo1.getSourceID())));
                    }else {
                        // 上级部门权限
                        if (!ObjectUtils.isEmpty(gsAuthMap)) {
                            vo1.setAuth(sourceOperateTool.getAuthByLevel(vo1,vo1.getParentLevel(), gsAuthMap, gsAuthNameMap));
                        }
                    }
                    if (ObjectUtils.isEmpty(vo1.getAuth()) || "0".equals(vo1.getAuth())){
                        // 不可见
                        continue;
                    }
                    if (!Arrays.asList(vo1.getAuth().split(",")).stream().collect(Collectors.toList()).contains("1")){
                        // 无列表权限
                        continue;
                    }
                }
                sourceOperateTool.setOwnDeleteRole(vo1, loginUser.getUserID());
                vo1.setHasFile(0);
                vo1.setHasFolder(0);
                // 是否收藏
                vo1.setIsFav(!CollectionUtils.isEmpty(myFavList) && myFavList.contains(vo1.getSourceID().toString()) ? 1 : 0);
                // 是否分享
                vo1.setIsShare(!CollectionUtils.isEmpty(shareList) && shareList.contains(vo1.getSourceID()) ? 1 : 0);
                // FileMeta value 处理
                sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);

                // 用户信息
                vo1.setCreateUserJson(JsonUtils.beanToJson(userMap.get(vo1.getCreateUser())));
                vo1.setModifyUserJson(JsonUtils.beanToJson(userMap.get(vo1.getModifyUser())));
                vo1.setPathDisplay("");
                if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
                    vo1.setPathDisplay(sourceOperateTool.setParentPathDisplay(parentPathDisplayMap, vo1.getParentLevel(), spaceId));
                }
                // path 处理
                sourceOperateTool.opPath(vo1, downloadKey, vo1.getAuth());
                if (!ObjectUtils.isEmpty(vo1.getIsM3u8()) && vo1.getIsM3u8() == 0 && !ObjectUtils.isEmpty(vo1.getIsH264Preview()) && vo1.getIsH264Preview() == 1){
                    vo1.setIsM3u8(1);
                }
                vo1.setIcon("file");
                fileVOList.add(vo1);
            }
        }
//        PageInfo<HomeExplorerVO> pageInfo = new PageInfo<>(list);
        result.setTotal(total);
        result.setFolderList(folderVOList);
        fileVOList = fileVOList.stream().sorted(Comparator.comparing(HomeExplorerVO::getModifyTime).reversed()).collect(Collectors.toList());
        result.setFileList(fileVOList);

        return sourceOperateTool.setSystemReturn(result, systemSortVo, "userRecycle");
    }

    private Boolean searchNameRepeatParam(HomeExplorerDTO homeExplorerDTO, Map<String, Object> hashMap, LoginUser loginUser){
        boolean searchRepeat = false;
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getRepeatSourceID()) && homeExplorerDTO.getRepeatSourceID() > 0){
            CommonSource commonSource = fileOptionTool.getSourceInfo(homeExplorerDTO.getRepeatSourceID());
            userAuthTool.checkGroupDocAuth(loginUser, commonSource.getSourceID(), commonSource.getParentLevel(), "2", commonSource.getTargetType());
            if (!ObjectUtils.isEmpty(homeExplorerDTO.getRepeatName())){
                hashMap.put("repeatName", homeExplorerDTO.getRepeatName());
                searchRepeat = true;
            }else {
                if (ObjectUtils.isEmpty(homeExplorerDTO.getHashMd5())){

                    if (!ObjectUtils.isEmpty(commonSource) && !ObjectUtils.isEmpty(commonSource.getHashMd5())){
                        hashMap.put("repeatHashMd5", commonSource.getHashMd5());
                        searchRepeat = true;
                    }
                }else {
                    hashMap.put("repeatHashMd5", homeExplorerDTO.getHashMd5());
                    searchRepeat = true;
                }
            }
        }
        return searchRepeat;
    }
    private void searchAll(LoginUser loginUser, boolean isSystem, Map<String, Object> hashMap, HomeExplorerVO space){
            // 全局搜索
            List<HomeExplorerVO> diskList = null;
            if (isSystem){
                diskList = systemSortTool.getSystemGroupSourceList(space.getTenantId());
            }else {
                diskList = systemSortTool.getUserGroupSourceList(loginUser.getUserID());
            }
            List<String> parentLevelList = new ArrayList<>();
            parentLevelList.add("," + space.getSourceID() + ",");
            if (!CollectionUtils.isEmpty(diskList)){
                diskList.forEach(n->parentLevelList.add("," + n.getSourceID() + ","));
            }
        hashMap.put("parentLevelList", parentLevelList);

    }
    private HomeExplorerResult getHomeExplorerList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        HomeExplorerResult result = null;
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        String homeExplorerKey = GlobalConfig.homeExplorerRedisKey + loginUser.getUserID();

        Long tenantId = tenantUtil.getTenantIdByServerName();

        String secondKey = paramUtils.getHomeExplorerSecondKey(homeExplorerDTO);
        LogUtil.info("getHomeExplorerList secondKey=" + secondKey);
        String homeExplorerKeyValue = "";
        if (!ObjectUtils.isEmpty(secondKey)){
            homeExplorerKeyValue = operations.get(homeExplorerKey, secondKey);
            if (!ObjectUtils.isEmpty(homeExplorerKeyValue)){
                LogUtil.info("getHomeExplorerList secondKey=" + secondKey + "，value is not null" );
                try {
                    result = JsonUtils.jsonToBean(homeExplorerKeyValue, HomeExplorerResult.class);
                }catch (Exception e){
                    LogUtil.error(e, "getHomeExplorerList error secondKey=" + secondKey + "，homeExplorerKeyValue=" + homeExplorerKeyValue);
                }
            }
        }

        if (!ObjectUtils.isEmpty(result) && !ObjectUtils.isEmpty(result.getTotal())){
            return result;
        }

        result = new HomeExplorerResult();

        HomeExplorerVO space = homeExplorerDaoImpl.getHomeSpace(loginUser.getUserID(), 0L);
        Map<String, Object> systemConfigMap = optionTool.getSystemConfigMap();

        boolean searchRepeat = false;
        boolean showFileMd5 = true;
        if (!ObjectUtils.isEmpty(systemConfigMap) && !ObjectUtils.isEmpty(systemConfigMap.containsKey("showFileMd5")) && "0".equals(systemConfigMap.get("showFileMd5"))){
            showFileMd5 = false;
        }

        List<IoSourceAuthVo> roleList = null;
        boolean isGroup = false;
        boolean isSystem = false;
        String auth = "";
        if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
            roleList = new ArrayList<>();
            Role role = roleDaoImpl.getSystemRoleInfo();
            auth = GlobalConfig.SYSTEM_GROUP_AUTH;
            isSystem = true;
            roleList.add(new IoSourceAuthVo(role.getRoleID(), role.getRoleName(), role.getLabel(), auth));
        }

        boolean isGroupMenu = false;
        String key = "";
        String parentName = "";
        List<GroupSource> groupSourceIDs = null;
        Map<Long, Integer> groupSourceStatusMap = null;
        Map<String, Object> hashMap = new HashMap<>(3);
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getSourceID()) && homeExplorerDTO.getSourceID() > 0){
            hashMap.put("sourceID", homeExplorerDTO.getSourceID());
            key = homeExplorerDTO.getSourceID().toString();
            HomeExplorerVO parentInfo = homeExplorerDao.getOneSourceInfo(homeExplorerDTO.getSourceID());
            if (ObjectUtils.isEmpty(parentInfo)){
                homeExplorerDTO.setSourceID(space.getSourceID());
                parentInfo = homeExplorerDao.getOneSourceInfo(space.getSourceID());
            }
            parentName = parentInfo.getName();
            // 判断是否是私密保险箱
            if (parentInfo.getTargetType().intValue() == 1){
                if (!ObjectUtils.isEmpty(parentInfo.getSourceHash()) && GlobalConfig.safe_code.equals(parentInfo.getSourceHash())){
                    // 校验是否验证过密码
                    String safeCheckString = stringRedisTemplate.opsForValue().get(GlobalConfig.user_safe_redis_key + loginUser.getUserID());
                    if (ObjectUtils.isEmpty(safeCheckString)){
                        String pathSafe = null;
                        // 判断是否有设置密码
                        String mySafePwd = ioSourceMetaDao.getValueMetaByKey(parentInfo.getSourceID(), GlobalConfig.safe_meta_code);
                        if (ObjectUtils.isEmpty(mySafePwd)){
                            pathSafe = "isNotSetPwd";
                        }else {
                            pathSafe = "isNotLogin";
                        }
                        result.setFileList(new ArrayList());
                        result.setFolderList(new ArrayList());
                        Map<String, Object> currentMap = new HashMap<>(1);
                        currentMap.put("name", parentInfo.getName());
                        currentMap.put("pathSafe", pathSafe);
                        currentMap.put("type", "folder");
                        result.setCurrent(currentMap);
                        result.setPathSafeMsg("未登录私密保险箱");
                        result.setPathSafe(pathSafe);
                        return result;
                    }
                    result.setPathSafe("login");
                }
            }else if (parentInfo.getTargetType().intValue() == 2){
                if (!ObjectUtils.isEmpty(homeExplorerDTO.getFileType()) && "folder".equals(homeExplorerDTO.getFileType())){
                    groupSourceIDs = groupSourceDao.getGroupSourceIDs(homeExplorerDTO.getSourceID());
                    if (!CollectionUtils.isEmpty(groupSourceIDs)) {
                        groupSourceStatusMap = new HashMap<>();
                        for (GroupSource gs : groupSourceIDs){
                            if (!ObjectUtils.isEmpty(gs.getStatus())){
                                groupSourceStatusMap.put(gs.getSourceID(), gs.getStatus());
                            }
                        }
                        isGroupMenu = true;
                        hashMap.put("isGroupMenu", 1);
                    }
                }else {
                    // 排除左侧栏目录
                    hashMap.put("targetType", 2);
                }
                if(!isSystem){
                    auth = sourceOperateTool.getUserAuthByLevel(loginUser.getUserID(), parentInfo.getParentLevel() , homeExplorerDTO);
                    roleList = new ArrayList<>();
                    roleList.add(new IoSourceAuthVo(homeExplorerDTO.getAuthID(), homeExplorerDTO.getAuthName(), homeExplorerDTO.getLabel(), auth));
                    isGroup = true;
                }
            }
        }else {
            hashMap.put("isSafe", 0);
            // 全局搜索
            if (ObjectUtils.isEmpty(homeExplorerDTO.getTagID())){
                if(ObjectUtils.isEmpty(homeExplorerDTO.getFileType())){
                    LogUtil.info("getHomeExplorerList null homeExplorerDTO=" + JsonUtils.beanToJson(homeExplorerDTO));
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                if (!ObjectUtils.isEmpty(homeExplorerDTO.getFileType()) && !Arrays.asList("allFile","folder", "all").contains(homeExplorerDTO.getFileType())) {
                    //
                    if (homeExplorerDTO.getFileType().indexOf("txt,") >= 0){
                        key = "recentDoc";
                    }else if (homeExplorerDTO.getFileType().indexOf("jpg,") >= 0){
                        key = "image";
                    }else if (homeExplorerDTO.getFileType().indexOf("mp3,") >= 0){
                        key = "music";
                    }else if (homeExplorerDTO.getFileType().indexOf("mp4,") >= 0){
                        key = "movie";
                    }else if (homeExplorerDTO.getFileType().indexOf("zip,") >= 0){
                        key = "zip";
                    }else if (homeExplorerDTO.getFileType().indexOf("psd,") >= 0){
                        key = "other";
                    }
                }
            }
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getUserID()) && homeExplorerDTO.getUserID() > 0) {
            hashMap.put("userID", homeExplorerDTO.getUserID());
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getKeyword())) {
            hashMap.put("keyword", homeExplorerDTO.getKeyword().toLowerCase());
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getUserID())) {
            hashMap.put("userID", homeExplorerDTO.getUserID());
            hashMap.put("isSearch", 1);
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getTagID())) {
            hashMap.put("tagID", homeExplorerDTO.getTagID());
            key = "userFileTag:" + homeExplorerDTO.getTagID();
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getFileType())) {
            space.setTenantId(tenantId);
            if (ObjectUtils.isEmpty(homeExplorerDTO.getSourceID())) {
                this.searchAll(loginUser, isSystem, hashMap, space);
            }
            //
            switch (homeExplorerDTO.getFileType()){
                case "allFile":
                    hashMap.put("isFolder", 0);
                    break;
                case "folder":
                    hashMap.put("isFolder", 1);
                    break;
                case "all":
                    break;
                default:
                    hashMap.put("isFolder", 0);
                    hashMap.put("fileTypeList", Arrays.asList(homeExplorerDTO.getFileType().split(",")).stream().map(String::valueOf).collect(Collectors.toList()));
                    paramUtils.docSearchParam(homeExplorerDTO.getFileType(), hashMap);

                    // hashMap.put("targetType", 1); 个人空间 + 部门与我有关的
                    hashMap.put("userID", loginUser.getUserID());
                    hashMap.put("isSearch", 1);
                    break;
            }
            // 文件、文件名查重
            searchRepeat = this.searchNameRepeatParam(homeExplorerDTO, hashMap, loginUser);
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getFromType()) && homeExplorerDTO.getFromType()) {
            hashMap.put("isFolder", 1);
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getMinSize())) {
            hashMap.put("minSize", homeExplorerDTO.getMinSize());
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getMaxSize())) {
            hashMap.put("maxSize", homeExplorerDTO.getMaxSize());
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getTimeFrom())) {

            Date timeFrom = DateUtil.strToDate("yyyy-MM-dd", homeExplorerDTO.getTimeFrom());
            hashMap.put("minDate", timeFrom.getTime()/1000);
        }
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getTimeTo())) {

            Date timeTo = DateUtil.strToDate("yyyy-MM-dd HH:mm:ss", homeExplorerDTO.getTimeTo() + " 23:59:59");
            hashMap.put("maxDate", timeTo.getTime()/1000);
        }
        SystemSortVo systemSortVo = systemSortTool.getUserSort(loginUser.getUserID());
        hashMap = systemSortTool.setSortAboutMap(systemSortVo,hashMap, loginUser.getUserID(), homeExplorerDTO.getSortField()
                , homeExplorerDTO.getSortType(), key);

        FileMetaVo fileMetaVo = null;


        hashMap.put("startIndex", homeExplorerDTO.getStartIndex());
        hashMap.put("pageSize", homeExplorerDTO.getPageSize());
        hashMap.put("tenantId", tenantId);

        Long total = homeExplorerDaoImpl.getCountHomeExplorer(hashMap);
        List<HomeExplorerVO> list = null;
        //PageHelper.startPage(homeExplorerDTO.getCurrentPage(), homeExplorerDTO.getPageSize());
        if(0 < total.longValue()) {
            list = homeExplorerDaoImpl.getHomeExplorer(hashMap);
        }
        List<IoSourceAuthVo> rList = null;
        Set<Long> lnkSourceIDList = new HashSet<>();
        int isLnkAudio = 0;
        HomeExplorerVO safeSource = null;
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        List<HomeExplorerVO> fileVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            boolean safe = false;
            String downloadKey = FileUtil.getDownloadKey();
            Set<Long> gpidList = new HashSet<>();
            List<Long> idList = new ArrayList<>();
            Set<Long> sidList = new HashSet<>();
            List<String> sourceIDs = new ArrayList<>();
            Set<Long> uidList = new HashSet<>();
            //Set<Long> pidList = new HashSet<>();
            Set<String> pLevelList = new HashSet<>();
            for (HomeExplorerVO vo2 : list) {
                // 判断是否显示md5
                if (!showFileMd5) {
                    vo2.setHashMd5("");
                }
                if (vo2.getIsFolder().intValue() == 1) {
                    idList.add(vo2.getSourceID());
                }
                pLevelList.add(vo2.getParentLevel());
                sourceIDs.add(vo2.getSourceID().toString());
                uidList.add(vo2.getCreateUser());
                uidList.add(vo2.getModifyUser());
                //pidList.add(vo2.getParentID());
                if (!isSystem && vo2.getTargetType().intValue() == 2) {
                    sidList.add(vo2.getSourceID());
                    if (!",0,".equals(vo2.getParentLevel())) {
                        Set<Long> parentIdList = Arrays.asList(vo2.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toSet());
                        gpidList.addAll(parentIdList);
                        sidList.addAll(parentIdList);
                    }
                }
            }
            Map<Long, List<IoSourceAuthVo>> gsFileAuthMap = new HashMap<>(1);
            // 文件权限
            if (!CollectionUtils.isEmpty(sidList)) {
                sourceOperateTool.getFileAuthByLevel(loginUser.getUserID(), sidList, gsFileAuthMap);
                LogUtil.info("checkRoleAuth getFileAuthByLevel sidList=" + JsonUtils.beanToJson(sidList) + " gsFileAuthMap=" + JsonUtils.beanToJson(gsFileAuthMap));
            }


            Map<Long, String> gsAuthMap = new HashMap<>(1);
            Map<Long, UserGroupVo> gsAuthNameMap = new HashMap<>(1);
            // 文件上级权限
            if (!isGroup && !CollectionUtils.isEmpty(gpidList)) {
                sourceOperateTool.getUserAuthByLevel(loginUser.getUserID(), new ArrayList<>(gpidList), gsAuthMap, gsAuthNameMap);
            }
            // 标签
            Map<String, List<CommonLabelVo>> sourceTagMap = fileOptionTool.getSourceTagMap(loginUser.getUserID(), sourceIDs);
            // 收藏
            List<String> myFavList = fileOptionTool.checkIsFavByUserId(loginUser.getUserID());
            // 链接分享
            List<Long> shareList = fileOptionTool.checkUserIsShare(loginUser.getUserID());

            // 父级文件名称
            //List<HomeExplorerVO> parentNameList = homeExplorerDao.getParentNameList(new ArrayList<>(pidList));
            //Map<Long, String> parentNameMap = CollectionUtils.isEmpty(parentNameList) ? null : parentNameList.stream().collect(Collectors.toMap(HomeExplorerVO::getSourceID, HomeExplorerVO::getParentName, (v1, v2) -> v2));

            Map<Long, String> parentPathDisplayMap = null;
            // 文件位置
            if (!CollectionUtils.isEmpty(pLevelList)) {
                parentPathDisplayMap = sourceOperateTool.getParentPathDisplayMap(pLevelList);

            }
            List<UserVo> userList = userDaoImpl.getUserBaseInfo(new ArrayList<>(uidList));
            Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));
            if (ObjectUtils.isEmpty(userMap)){
                userMap = new HashMap<>(1);
            }
            userMap.put(0L, sourceOperateTool.getDefaultSystemUser());
            List<HomeExplorerVO> countList = CollectionUtils.isEmpty(idList) ? null : homeExplorerDao.getSourceChileCont(idList);
            Map<String, Integer> countMap = new HashMap<>(1);
            if (!CollectionUtils.isEmpty(countList)) {
                for (HomeExplorerVO home : countList) {
                    countMap.put(home.getParentID() + (home.getIsFolder().intValue() == 1 ? "_folder" : "_file"), home.getFileCount());
                }
            }
            // file info more
            for (HomeExplorerVO vo1 : list) {
                // 是否是部门文件（左侧栏）
                vo1.setIsGroup(0);
                if (isGroupMenu && !ObjectUtils.isEmpty(groupSourceStatusMap) && groupSourceStatusMap.containsKey(vo1.getSourceID())) {
                    int status = groupSourceStatusMap.get(vo1.getSourceID());
                    if (status != 1){
                        continue;
                    }
                    vo1.setIsGroup(1);
                }
                if (isSystem) {
                    vo1.setRoleList(JsonUtils.beanToJson(roleList));
                }
                vo1.setAuth(auth);
                if (!isSystem && vo1.getTargetType().intValue() == 2) {
                    boolean separately  = false;
                    // 单独设置权限
                    if (!ObjectUtils.isEmpty(gsFileAuthMap)) {
                        LogUtil.info("checkRoleAuth getFileAuthByLevel checkSeparately=" + (gsFileAuthMap.containsKey(vo1.getSourceID())) + " gsFileAuthMap=" + JsonUtils.beanToJson(gsFileAuthMap));
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
                                    LogUtil.info("checkRoleAuth getFileAuthByLevel p=" + p + " checkParentSeparately=" + (gsFileAuthMap.containsKey(p)) + " gsFileAuthMap=" + JsonUtils.beanToJson(gsFileAuthMap));
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
                    if (ObjectUtils.isEmpty(vo1.getAuth()) || "0".equals(vo1.getAuth())){
                        // 不可见
                        continue;
                    }
                    if (!Arrays.asList(vo1.getAuth().split(",")).stream().collect(Collectors.toList()).contains("1")){
                        // 无列表权限
                        continue;
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


                if (vo1.getIsFolder().intValue() == 0 ) {
                    sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);
                }
                // 用户信息
                vo1.setCreateUserJson(JsonUtils.beanToJson(userMap.get(vo1.getCreateUser())));
                vo1.setModifyUserJson(JsonUtils.beanToJson(userMap.get(vo1.getModifyUser())));

                // path 处理
                sourceOperateTool.opPath(vo1, downloadKey, vo1.getAuth());
                if (!ObjectUtils.isEmpty(vo1.getIsM3u8()) && vo1.getIsM3u8() == 0 && !ObjectUtils.isEmpty(vo1.getIsH264Preview()) && vo1.getIsH264Preview() == 1){
                    vo1.setIsM3u8(1);
                }
                if (!ObjectUtils.isEmpty(vo1.getOexeSourceID())) {
                    lnkSourceIDList.add(vo1.getOexeSourceID());
                    if (!ObjectUtils.isEmpty(vo1.getOexeFileType()) && Arrays.asList(GlobalConfig.AUDIO_SHOW_TYPE_ARR).contains(vo1.getOexeFileType())) {
                        isLnkAudio = 1;
                    }
                }

                // 是否收藏
                vo1.setIsFav(!CollectionUtils.isEmpty(myFavList) && myFavList.contains(vo1.getSourceID().toString()) ? 1 : 0);
                // 已选标签
                vo1.setTags("");
                vo1.setTagList(new ArrayList<>());
                if (!ObjectUtils.isEmpty(sourceTagMap) && sourceTagMap.containsKey(vo1.getSourceID().toString())) {
                    vo1.setTagList(sourceTagMap.get(vo1.getSourceID().toString()));
                }
                // 是否分享
                vo1.setIsShare(!CollectionUtils.isEmpty(shareList) && shareList.contains(vo1.getSourceID()) ? 1 : 0);

                vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? vo1.getThumb() : vo1.getPath());
                vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? "" : vo1.getPath());

                // 父级文件名称
                if (!ObjectUtils.isEmpty(parentName)) {
                    vo1.setParentName(parentName);
                } else if (!ObjectUtils.isEmpty(parentPathDisplayMap) && parentPathDisplayMap.containsKey(vo1.getParentID())) {
                    vo1.setParentName(parentPathDisplayMap.get(vo1.getParentID()));
                }

                vo1.setPathDisplay("");
                if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
                    vo1.setPathDisplay(sourceOperateTool.setParentPathDisplay(parentPathDisplayMap, vo1.getParentLevel(), space.getSourceID()));
                }
                if (vo1.getIsFolder() == 1) {
                    vo1.setIcon("folder");
                    if (!ObjectUtils.isEmpty(vo1.getSourceHash()) && GlobalConfig.safe_code.equals(vo1.getSourceHash())){
                        vo1.setAuth("1");
                        safeSource = vo1;
                    }else {
                        folderVOList.add(vo1);
                    }
                } else {
                    vo1.setIcon("file");
                    fileVOList.add(vo1);
                }
            }
            oexeFilePath(downloadKey, lnkSourceIDList, isLnkAudio, fileVOList);
        }
        if (isGroupMenu){
            folderVOList = folderVOList.stream().sorted(Comparator.comparing(HomeExplorerVO::getIsGroup).thenComparing(HomeExplorerVO::getName).thenComparing(HomeExplorerVO::getSourceID)).collect(Collectors.toList());
        }
        if (!ObjectUtils.isEmpty(safeSource)){
            folderVOList.add(0,safeSource);
        }

        result.setTotal(total);
        result.setFolderList(folderVOList);
        result.setFileList(fileVOList);
        // 查询回收站是否有文件
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getCheckRecycle()) && 1 == homeExplorerDTO.getCheckRecycle() ){
            Map<String, Object> currentMap = new HashMap<>(1);
            Integer count = homeExplorerDao.checkUserRecycleExplorer(loginUser.getUserID());
            currentMap.put("recycleCount", count);
            result.setCurrent(currentMap);
        }
        sourceOperateTool.setSystemReturn(result, systemSortVo, String.valueOf(homeExplorerDTO.getSourceID()));

        if (!ObjectUtils.isEmpty(secondKey)) {
            operations.put(homeExplorerKey, secondKey, JsonUtils.beanToJson(result));
            operations.getOperations().expire(homeExplorerKey, 5, TimeUnit.SECONDS);
        }
        return result;
    }

    @Override
    public IOSource createDir(HomeExplorerVO homeExplorerVO, LoginUser loginUser ) {
        if (ObjectUtils.isEmpty(homeExplorerVO.getName()) || homeExplorerVO.getName().length() > 256 ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (ObjectUtils.isEmpty(homeExplorerVO.getParentID()) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        HomeExplorerVO parent = homeExplorerDao.getOneSourceInfo(homeExplorerVO.getParentID());
        if (ObjectUtils.isEmpty(parent) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, parent.getSourceID(), parent.getParentLevel(), "9", parent.getTargetType());

        List<String> sourceNameList =  ioSourceDao.getSourceNameList(homeExplorerVO.getParentID());
        /*if (!CollectionUtils.isEmpty(sourceNameList) && sourceNameList.contains(homeExplorerVO.getName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathExists.getCode());
        }*/
        homeExplorerVO.setName(fileOptionTool.checkRepeatName(homeExplorerVO.getName(), homeExplorerVO.getName(), sourceNameList, 1));

        int isSafe = optionTool.getSourceIsSafe(loginUser.getUserID(),parent.getParentLevel() + parent.getSourceID() + ",");

        IOSource source = new IOSource();
        source.setName(homeExplorerVO.getName());
        source.setParentLevel(parent.getParentLevel() + homeExplorerVO.getParentID() + "," );
        source.setTargetId(homeExplorerVO.getTargetID());
        source.setCreateUser(homeExplorerVO.getCreateUser());
        source.setModifyUser(homeExplorerVO.getModifyUser());
        source.setIsFolder(homeExplorerVO.getIsFolder());
        source.setTargetType(parent.getTargetType());
        source.setFileType("");
        source.setFileId(0L);
        source.setParentId(homeExplorerVO.getParentID());
        source.setStorageId(storageService.getDefaultStorageDeviceId());
        source.setTenantId(tenantUtil.getTenantIdByServerName());
        source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
        source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
        source.setIsSafe(isSafe);

        if (source.getParentId() <= 0){
            LogUtil.error("创建文件夹失败，不可创建根目录文件");
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        try {
            homeExplorerDao.createDir(source);
        }catch (Exception e){
            LogUtil.error(e, " createDir error");
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        if (!ObjectUtils.isEmpty(source.getId()) && source.getId() > 0){
            sourceOperateTool.setSourcePinYin(source.getId(), homeExplorerVO.getName());

            if (parent.getTargetType().intValue() == 2) {
                String groupSource = ioSourceMetaDao.getValueMetaByKey(homeExplorerVO.getParentID(), "groupSource");
                if (!ObjectUtils.isEmpty(groupSource)) {
                    ioSourceMetaDao.insert(new IOSourceMeta(source.getId(), "groupSource", groupSource));
                }
            }
        }

        // 添加文档操作event
        fileOptionTool.addSourceEvent(source.getId(), source.getParentId(), loginUser.getUserID(), source.getName(), EventEnum.mkdir);


        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", source.getId());
        reMap.put("sourceParent", source.getParentId());
        reMap.put("type", "mkdir");
        reMap.put("pathName", source.getName());
        paramList.add(reMap);

        systemLogTool.setSysLog(loginUser, LogTypeEnum.fileMkDir.getCode(), paramList, systemLogTool.getRequest());
        return source;

    }


    @Override
    public HomeFileDetailVO getFileDetail(HomeExplorerVO homeExplorerVO) {
        if (ObjectUtils.isEmpty(homeExplorerVO.getFileID()) || homeExplorerVO.getFileID() <= 0 ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        HomeFileDetailVO homeFileDetailVO = homeExplorerDao.getFileDetail(homeExplorerVO.getFileID());
        if (!ObjectUtils.isEmpty(homeFileDetailVO) && !ObjectUtils.isEmpty(homeFileDetailVO.getPath())){
            String firstPath = FileUtil.getFirstStorageDevicePath(homeFileDetailVO.getPath());
            homeFileDetailVO.setPath(homeFileDetailVO.getPath().replace(firstPath + "/doc/", firstPath + "/common/doc/")
                    .replace(firstPath + "/attachment/", firstPath + "/common/attachment/")
                    .replace(firstPath + "/private/", firstPath + "/common/")

            );
        }
        return homeFileDetailVO;
    }

    /**
       * @Description: 位置
       * @params:  [homeExplorerDTO]
       * @Return:  com.svnlan.home.vo.HomeExplorerResult
       * @Author:  sulijuan
       * @Date:  2023/2/14 16:35
       * @Modified:
       */
    private HomeExplorerResult getMyMenuList(LoginUser loginUser, List<String> treeOpenList){
        Long diskDefaultSize = optionTool.getTotalSpace();
        Long tenantId = tenantUtil.getTenantIdByServerName();
        // 用户群组权限
        //List<UserGroupVo> userGroupList = sourceOperateTool.getUserGroupAuth(loginUser.getUserID());
        List<String> sourceIDs= new ArrayList<>();
        List<Long> sourceIDList= new ArrayList<>();
        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        // my 个人空间,myFav 收藏夹,rootGroup 企业云盘
        UserVo userVo = userDaoImpl.getUserInfo(loginUser.getUserID());
        HomeExplorerVO vo = null;
        for (MyMenuEnum typeEnum : MyMenuEnum.values()){
            if ("fav".equals(typeEnum.getIcon())){
                if (CollectionUtils.isEmpty(treeOpenList) || !treeOpenList.contains("myFav")){
                    continue;
                }
            }
            if ("space".equals(typeEnum.getIcon())){
                if (CollectionUtils.isEmpty(treeOpenList) ||  !treeOpenList.contains("my")){
                    continue;
                }
            }
            vo = new HomeExplorerVO();
            vo.setIsChildren(true);
            vo.setCanDownload(false);
            vo.setIsTruePath(false);
            vo.setName(I18nUtils.tryI18n(typeEnum.getCode()));
            vo.setPathDesc("");
            if (!ObjectUtils.isEmpty(typeEnum.getDesc())) {
                vo.setPathDesc(I18nUtils.tryI18n(typeEnum.getDesc()));
            }
            // 个人空间
            if ("1".equals(typeEnum.getIsRoot())){
                HomeExplorerVO space = homeExplorerDaoImpl.getHomeSpace(loginUser.getUserID(), 0L);
                vo.setSizeMax(userVo.getSizeMax() <= 0 ? diskDefaultSize.doubleValue() : userVo.getSizeMax());
                vo.setSizeUse(space.getSize());
                vo.setSourceID(space.getSourceID());
                sourceIDs.add(vo.getSourceID().toString());
                sourceIDList.add(vo.getSourceID());
                vo.setDescription(space.getDescription());
                vo.setCreateTime(space.getCreateTime());
                vo.setModifyTime(space.getModifyTime());
                vo.setParentID(space.getParentID());
                vo.setParentLevel(space.getParentLevel());
                vo.setTargetType(1);
            }
            vo.setPathDisplay("/" + vo.getName());
            vo.setType("folder");
            vo.setIcon(typeEnum.getIcon());
            folderVOList.add(vo);
        }
        loginUser.setUserType(ObjectUtils.isEmpty(loginUser.getUserType()) ? 2 :loginUser.getUserType());
        // 企业云盘
        boolean system = false;
        List roleList = new ArrayList<>();
        if (1 == loginUser.getUserType()){
            Integer val = homeExplorerDaoImpl.getUserIdentityInfo(loginUser.getUserID());
            if (!ObjectUtils.isEmpty(val) && val.intValue() == 1){
                system = true;
            }
        }
        List<HomeExplorerVO> diskList = null;
        if (system){
            diskList = systemSortTool.getSystemGroupSourceList(tenantId);
        }else {
            diskList = systemSortTool.getUserGroupSourceList(loginUser.getUserID());
        }
        if (!CollectionUtils.isEmpty(diskList)) {

            boolean isSystem = false;
            // 文件权限
            if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
                String auth = GlobalConfig.SYSTEM_GROUP_AUTH;
                Role role = roleDaoImpl.getSystemRoleInfo();
                isSystem = true;
                roleList.add(new IoSourceAuthVo(role.getRoleID(), role.getRoleName(), role.getLabel(), auth));
            }
            Map<Long, List<IoSourceAuthVo>> gsFileAuthMap = new HashMap<>(1);
            if (!isSystem){
                Set<Long> gpidList = diskList.stream().map(HomeExplorerVO::getSourceID).collect(Collectors.toSet());
                // 文件权限
                if (!CollectionUtils.isEmpty(gpidList)){
                    sourceOperateTool.getFileAuthByLevel(loginUser.getUserID(), gpidList, gsFileAuthMap);
                }

            }
            List<IoSourceAuthVo> rList = null;
            for (HomeExplorerVO disk : diskList) {
                if (isSystem){
                    disk.setAuth(GlobalConfig.SYSTEM_GROUP_AUTH);
                    disk.setRoleList(JsonUtils.beanToJson(roleList));
                }else {
                    if (!ObjectUtils.isEmpty(gsFileAuthMap) && gsFileAuthMap.containsKey(disk.getSourceID())){
                        rList = gsFileAuthMap.get(disk.getSourceID());
                        disk.setAuth(rList.stream().map(IoSourceAuthVo::getAuth).collect(Collectors.joining(",")));
                        disk.setRoleList(JsonUtils.beanToJson(rList));
                    }else {
                        disk.setRoleList(JsonUtils.beanToJson(Arrays.asList(new IoSourceAuthVo(disk.getAuthID(), disk.getAuthName(), disk.getLabel(), disk.getAuth()))));
                    }
                }

                if (("," + disk.getAuth() + ",").indexOf(",1,") >= 0) {
                    sourceIDs.add(disk.getSourceID().toString());
                    sourceIDList.add(disk.getSourceID());
                    disk.setIcon("box");
                    disk.setTargetType(2);
                    disk.setIsChildren(true);
                    disk.setSizeMax(disk.getSizeMax() <= 0 ? diskDefaultSize.doubleValue() : disk.getSizeMax());
                    folderVOList.add(disk);
                }
            }
        }
        if (!CollectionUtils.isEmpty(treeOpenList) && treeOpenList.contains("information")) {
            if (1 == loginUser.getUserType()
                    || (!ObjectUtils.isEmpty(userVo.getAuth()) && userVo.getAuth().indexOf("explorer.informationView") >= 0)){
                // 资讯
                folderVOList.add(systemSortTool.infoToMeMenu());
            }
        }
        // 与我协作
        // folderVOList.add(systemSortTool.shareToMeMenu());


        // 标签
        Map<String, List<CommonLabelVo>> sourceTagMap = CollectionUtils.isEmpty(sourceIDs) ? null : fileOptionTool.getSourceTagMap(loginUser.getUserID(), sourceIDs);
        // 收藏
        List<String> myFavList = fileOptionTool.checkIsFavByUserId(loginUser.getUserID());

        for (HomeExplorerVO forder : folderVOList){
            if (ObjectUtils.isEmpty(forder.getSourceID())){
                continue;
            }
            // 是否收藏
            forder.setIsFav(!CollectionUtils.isEmpty(myFavList) && myFavList.contains(forder.getSourceID().toString()) ? 1 : 0);
            // 已选标签
            forder.setTags("");
            forder.setTagList(new ArrayList<>());
            if (!ObjectUtils.isEmpty(sourceTagMap) && sourceTagMap.containsKey(forder.getSourceID().toString())){
                forder.setTagList(sourceTagMap.get(forder.getSourceID().toString()));
            }

            /** desc */
            forder.setDescription(ObjectUtils.isEmpty(forder.getDescription()) ? "" : forder.getDescription());
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("isChildren", true);
        map.put("canDownload", false);
        String name = I18nUtils.tryI18n(MenuEnum.position.getCode());
        map.put("name", name);
        map.put("pathDesc", "");
        map.put("type", "folder");
        map.put("pathDisplay","/" + name);
        map.put("icon",MenuEnum.position.getIcon());
        result.setCurrent(map);
        result.setFolderList(folderVOList);
        result.setFileList(new ArrayList<>());
        return result;
    }

    /**
     * @Description: 文件类型
     * @params:  [homeExplorerDTO]
     * @Return:  com.svnlan.home.vo.HomeExplorerResult
     * @Author:  sulijuan
     * @Date:  2023/2/14 14:48
     * @Modified:
     */
    private HomeExplorerResult getFileTypeList(){
        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        HomeExplorerVO vo = null;
        for (DocumentTypeEnum typeEnum : DocumentTypeEnum.values()){
            vo = new HomeExplorerVO();
            vo.setCanDownload(false);
            vo.setIsTruePath(false);
            vo.setName(I18nUtils.tryI18n(typeEnum.getValue()));
            vo.setExt(typeEnum.getExt());
            vo.setExtType(typeEnum.getCode());
            vo.setType("folder");
            vo.setIcon(typeEnum.getIcon());
            folderVOList.add(vo);
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("isChildren", true);
        map.put("canDownload", false);
        String name = I18nUtils.tryI18n(MenuEnum.fileType.getCode());
        map.put("name", name);
        map.put("pathDesc", I18nUtils.tryI18n(MenuEnum.fileType.getDesc()));
        map.put("type", "folder");
        map.put("pathDisplay","/" + name);
        map.put("icon",MenuEnum.fileType.getIcon());
        result.setCurrent(map);
        result.setFolderList(folderVOList);
        result.setFileList(new ArrayList<>());
        return result;
    }

    /**
     * @Description: 工具
     * @params:  [homeExplorerDTO]
     * @Return:  com.svnlan.home.vo.HomeExplorerResult
     * @Author:  sulijuan
     * @Date:  2023/2/14 14:48
     * @Modified:
     */
    private HomeExplorerResult getToolList(List<String> treeOpenList){
        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
                // // my 个人空间,myFav 收藏夹,rootGroup 企业云盘,recentDoc 最近文档,fileType 文件类型,fileTag 标签,shareLink 外链分享
        HomeExplorerVO vo = null;
        for (ToolsEnum toolsEnum : ToolsEnum.values()){
            if (Arrays.asList("recentDoc", "shareLink", "toolbox").contains(toolsEnum.getIcon())){
                if (!treeOpenList.contains(toolsEnum.getIcon())){
                    continue;
                }
            }
            vo = new HomeExplorerVO();
            vo.setCanDownload(false);
            vo.setIsTruePath(false);
            vo.setName(I18nUtils.tryI18n(toolsEnum.getCode()));
            vo.setPathDesc(I18nUtils.tryI18n(toolsEnum.getValue()));
            vo.setType("folder");
            vo.setIcon(toolsEnum.getIcon());
            folderVOList.add(vo);
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("isChildren", true);
        map.put("canDownload", false);
        String name = I18nUtils.tryI18n(MenuEnum.tools.getCode());
        map.put("name", name);
        map.put("pathDesc", "");
        map.put("type", "folder");
        map.put("pathDisplay","/" + name);
        map.put("icon",MenuEnum.tools.getIcon());
        result.setCurrent(map);
        result.setFolderList(folderVOList);
        result.setFileList(new ArrayList<>());
        return result;
    }


    /**
     * @Description: root
     * @params:  [homeExplorerDTO]
     * @Return:  com.svnlan.home.vo.HomeExplorerResult
     * @Author:  sulijuan
     * @Date:  2023/2/14 14:48
     * @Modified:
     */
    private HomeExplorerResult getRootList(LoginUser loginUser, List<String> treeOpenList){

        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        HomeExplorerVO vo = null;
        // // my 个人空间,myFav 收藏夹,rootGroup 企业云盘,recentDoc 最近文档,fileType 文件类型,fileTag 标签,shareLink 外链分享

        for (MenuEnum menuEnum : MenuEnum.values()){
            if (Arrays.asList("fileType", "fileTag").contains(menuEnum.getType())){
                if (CollectionUtils.isEmpty(treeOpenList) || !treeOpenList.contains(menuEnum.getType())){
                    continue;
                }
            }
            vo = new HomeExplorerVO();
            switch (menuEnum.getType()){
                case "files":
                    vo.setChildren(this.getMyMenuList(loginUser, treeOpenList));
                    break;
                case "tools":
                    vo.setChildren(this.getToolList(treeOpenList));
                    break;
                case "fileType":
                    vo.setChildren(this.getFileTypeList());
                    break;
                case "fileTag":
                    vo.setChildren(this.getFileTagList(loginUser));
                    break;
            }
            vo.setCanDownload(false);
            vo.setIsTruePath(false);
            vo.setIsParent(true);
            vo.setOpen(menuEnum.getOpen());
            vo.setType(menuEnum.getType());
            vo.setIcon(menuEnum.getIcon());
            vo.setName(I18nUtils.tryI18n(menuEnum.getCode()));
            vo.setPathDesc("");
            if (!ObjectUtils.isEmpty(menuEnum.getDesc())) {
                vo.setPathDesc(I18nUtils.tryI18n(menuEnum.getDesc()));
            }

            vo.setPathDisplay("/" + vo.getName());
            vo.setType("folder");
            folderVOList.add(vo);
        }
        Map<String, Object> map = new HashMap<>(2);
        map.put("isChildren", true);
        map.put("canDownload", false);
        map.put("isTruePath", false);
        map.put("isParent", true);
        map.put("name", "root");
        map.put("pathDesc", "");
        map.put("type", "folder");
        map.put("pathDisplay","/root" );
        result.setCurrent(map);
        result.setFolderList(folderVOList);
        result.setFileList(new ArrayList<>());
        return result;
    }


    /**
       * @Description: 标签
       * @params:  []
       * @Return:  com.svnlan.home.vo.HomeExplorerResult
       * @Author:  sulijuan
       * @Date:  2023/2/14 16:50
       * @Modified:
       */
    private HomeExplorerResult getFileTagList(LoginUser loginUser){
        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();

        List<CommonLabelVo> list = commonLabelDaoImpl.getUserLabelList(loginUser.getUserID());
        HomeExplorerVO vo = null;
        boolean isChildren = false;
        if (!CollectionUtils.isEmpty(list)){
            isChildren = true;
            for (CommonLabelVo labelVo : list){
                vo = new HomeExplorerVO();
                vo.setName(labelVo.getLabelName());
                vo.setLabelId(labelVo.getLabelId());
                vo.setStyle(labelVo.getStyle());
                folderVOList.add(vo);
            }
        }

        Map<String, Object> map = new HashMap<>(2);
        map.put("isChildren", isChildren);
        map.put("canDownload", false);
        map.put("isTruePath", false);
        map.put("isParent", true);
        map.put("pathDesc", "");
        map.put("type", "folder");
        String name = I18nUtils.tryI18n(MenuEnum.tag.getCode());
        map.put("name", name);
        map.put("pathDisplay","/" + name);
        map.put("icon",MenuEnum.tag.getIcon());
        result.setCurrent(map);
        result.setFolderList(folderVOList);
        result.setFileList(new ArrayList<>());
        return result;
    }

    @Override
    public Boolean folderSetting(HomeSettingDTO homeExplorerVO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(homeExplorerVO.getKey()) || ObjectUtils.isEmpty(homeExplorerVO.getSourceID())|| ObjectUtils.isEmpty(homeExplorerVO.getValue())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        switch (homeExplorerVO.getKey()){
            case "listType":
                // 列表类型
                this.listType(homeExplorerVO, loginUser);
                break;
            case "filePanel":
                // 详情板块
                this.updateSystemOption(homeExplorerVO, loginUser);
                break;
            case "listSortField":
                this.listOpSet(homeExplorerVO, loginUser);
                // 排序
                break;
            case "fileIconSize":
                this.listOpSet(homeExplorerVO, loginUser);
                // 图标大小
                break;
                default:
        }
        return true;
    }

    private void listType(HomeSettingDTO homeExplorerVO, LoginUser loginUser){
        this.updateSystemOption(homeExplorerVO, loginUser);
        if ("icon".equals(homeExplorerVO.getValue())){
            Map<String, Object> map = new HashMap<>(1);
            String value = userOptionDao.getUserOtherConfigByKey(loginUser.getUserID(), "folderInfo", homeExplorerVO.getKey());
            try {
                if (!ObjectUtils.isEmpty(value)){
                    map = JsonUtils.jsonToMap(value);
                }
                map.put(homeExplorerVO.getSourceID(), homeExplorerVO.getListViewValue());
                userOptionDao.updateOptionValueByKey(loginUser.getUserID(), homeExplorerVO.getKey(), JsonUtils.beanToJson(map),"folderInfo");
            }catch (Exception e){
                LogUtil.error(e, " folderSetting listType error " + JsonUtils.beanToJson(homeExplorerVO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            }
        }
    }
    private void updateSystemOption(HomeSettingDTO homeExplorerVO, LoginUser loginUser){
        try {
            userOptionDao.updateSystemOptionValueByKey(loginUser.getUserID(), homeExplorerVO.getKey(), ObjectUtils.isEmpty(homeExplorerVO.getValue()) ? "" : homeExplorerVO.getValue());
        }catch (Exception e){
            LogUtil.error(e, " folderSetting error " + JsonUtils.beanToJson(homeExplorerVO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
    }
    private void listOpSet(HomeSettingDTO homeExplorerVO, LoginUser loginUser){
        this.updateSystemOption(homeExplorerVO, loginUser);
        // update listSortOrder
        if(!ObjectUtils.isEmpty(homeExplorerVO.getListViewValue())){
            List<String> listView = Arrays.asList(homeExplorerVO.getListViewValue().split(":")).stream().map(String::valueOf).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(listView) && listView.size() > 1){
                String listSortOrder = listView.get(1);
                try {
                    userOptionDao.updateSystemOptionValueByKey(loginUser.getUserID(), "listSortOrder", listSortOrder);
                }catch (Exception e){
                    LogUtil.error(e, " folderSetting listOpSet listSortOrder error " + JsonUtils.beanToJson(homeExplorerVO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
                }
            }
        }

        Map<String, Object> map = new HashMap<>(1);
        String value = userOptionDao.getUserOtherConfigByKey(loginUser.getUserID(), "folderInfo", homeExplorerVO.getListViewKey());
        try {
            if (!ObjectUtils.isEmpty(value)){
                map = JsonUtils.jsonToMap(value);
            }
            map.put(homeExplorerVO.getSourceID(), homeExplorerVO.getListViewValue());
            userOptionDao.updateOptionValueByKey(loginUser.getUserID(), homeExplorerVO.getListViewKey(), JsonUtils.beanToJson(map),"folderInfo");
        }catch (Exception e){
            LogUtil.error(e, " folderSetting error " + JsonUtils.beanToJson(homeExplorerVO) + "，loginUser=" + JsonUtils.beanToJson(loginUser));
        }
    }
    @Override
    public PageResult getPathLog(HomeExplorerDTO homeExplorerVO){
        if (ObjectUtils.isEmpty(homeExplorerVO.getSourceID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        PageResult<IoSourceEventVo> pageResult = new PageResult<>();
        int isFolder = 0;
        if (!ObjectUtils.isEmpty(homeExplorerVO.getIsFolder()) && homeExplorerVO.getIsFolder().intValue() == 1){
            isFolder = 1;
        }

//        PageHelper.startPage(homeExplorerVO.getCurrentPage(), homeExplorerVO.getPageSize());
        Long total = ioSourceEventDao.getSourceEventBySourceIDCount(homeExplorerVO.getSourceID(), isFolder);
        List<IoSourceEventVo> list = ioSourceEventDao.getSourceEventBySourceID(homeExplorerVO.getSourceID(), isFolder);
        if (CollectionUtils.isEmpty(list)){
            pageResult.setList(new ArrayList<>());
            pageResult.setTotal(0L);
            return pageResult;
        }

        for (IoSourceEventVo vo : list){
            if (ObjectUtils.isEmpty(vo.getNickname())){
                vo.setNickname("已删除");
                vo.setSex(1);
                vo.setStatus(2);
                vo.setAvatar("");
            }
        }
//        PageInfo<IoSourceEventVo> pageInfo = new PageInfo<>(list);
        pageResult.setTotal(total);
        pageResult.setList(list);
        return pageResult;
    }

    @Override
    public Map<String, Object> systemOpenInfo(){
        Map<String, Object> reMap = new HashMap<>(6);

        List<OptionVo> list = systemOptionDaoImpl.getSystemConfigByKeyListBy(Arrays.asList("systemName","systemDesc","passwordRule","needCheckCode"
                , "seo", "seoDesc", "meta", "systemLogo", "browserLogo", "thirdLoginConfig", "registerConfig", "globalIcp","defaultHome"
                , "tenantTypeConfig", "dingConfig", "networkConfig","enWechatConfig","cubeConfig"));

        for (OptionVo vo : list){
            if (Arrays.asList("browserLogo","systemLogo").contains(vo.getKeyString()) && !ObjectUtils.isEmpty(vo.getValueText())){
                reMap.put(vo.getKeyString(), FileUtil.getShowImageUrl(vo.getValueText(), vo.getKeyString()+".png"));
            }else if ("dingConfig".equals(vo.getKeyString()) && !ObjectUtils.isEmpty(vo.getValueText()) && !"{}".equals(vo.getValueText())){
                reMap.put("dingClientId", "");
                try {
                    DingConfigVo dingConfigVo = JsonUtils.jsonToBean(vo.getValueText(), DingConfigVo.class);
                    if (!ObjectUtils.isEmpty(dingConfigVo.getAppKey())){
                        reMap.put("dingClientId", dingConfigVo.getAppKey());
                    }
                }catch (Exception e){
                    LogUtil.error(e, "dingConfig 解析失败");
                }
            }else if ("thirdLoginConfig".equals(vo.getKeyString()) && !ObjectUtils.isEmpty(vo.getValueText()) && !"[]".equals(vo.getValueText())){
                if (vo.getValueText().indexOf("eube") > 0){
                    vo.setValueText(vo.getValueText().replace("eube", "cube"));
                }
                reMap.put(vo.getKeyString(), ObjectUtils.isEmpty(vo.getValueText()) ? "" : vo.getValueText());
            }else {
                reMap.put(vo.getKeyString(), ObjectUtils.isEmpty(vo.getValueText()) ? "" : vo.getValueText());
            }

        }

        if (!reMap.containsKey("defaultHome")){
            reMap.put("defaultHome", 2);
        }
        // tenantTypeConfig  1 单租户模式 2 平台模式
        if (!reMap.containsKey("tenantTypeConfig")){
            Long tenantId = tenantUtil.getTenantIdByServerName();
            reMap.put("tenantTypeConfig", tenantId == 1 ? 1 : 0);
        }
        /** 插件列表 */
        reMap.put("pluginList", optionTool.pluginList());
        // 没有设置则默认
        optionTool.defaultNetworkConfig(reMap);
        return reMap;
    }
    @Override
    public HomeExplorerVO getHomeExplorer(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){

        if(ObjectUtils.isEmpty(homeExplorerDTO.getSourceID()) || homeExplorerDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        HashOperations<String, String, String> operationsOne = stringRedisTemplate.opsForHash();
        String homeExplorerKey = GlobalConfig.homeExplorerOneRedisKey + loginUser.getUserID();
        String secondKey = homeExplorerDTO.getSourceID() + "";
        HomeExplorerVO homeExplorerVO = null;
        String valueOne = operationsOne.get(homeExplorerKey, secondKey);
        if (!ObjectUtils.isEmpty(valueOne)) {

            try {
                homeExplorerVO = JsonUtils.jsonToBean(valueOne, HomeExplorerVO.class);
            }catch (Exception e){
                LogUtil.error(e, "getHomeExplorer 解析失败 valueOne=" +valueOne);
            }
        }
        if (!ObjectUtils.isEmpty(homeExplorerVO)){

            if (ObjectUtils.isEmpty(homeExplorerVO.getName())){
                homeExplorerVO.setName(null);
            }
            if (ObjectUtils.isEmpty(homeExplorerVO.getDescription())){
                homeExplorerVO.setDescription(null);
            }
            return homeExplorerVO;
        }

        CommonSource commonSource = fileOptionTool.getSourceInfo(homeExplorerDTO.getSourceID());
        if(ObjectUtils.isEmpty(commonSource) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        homeExplorerVO = new HomeExplorerVO();
        homeExplorerVO.setSourceID(commonSource.getSourceID());
        homeExplorerVO.setFileID(commonSource.getFileID());
        homeExplorerVO.setParentID(commonSource.getParentID());
        homeExplorerVO.setParentLevel(commonSource.getParentLevel());
        homeExplorerVO.setName(commonSource.getName());
        homeExplorerVO.setDescription(commonSource.getDescription());
        homeExplorerVO.setPathDisplay("");
        if (!",0,".equals(commonSource.getParentLevel())) {
            Long spaceID = 0L;
            if (commonSource.getTargetType().intValue() == 1) {
                List<Long> parentIDs = Arrays.asList(commonSource.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                spaceID = parentIDs.get(0);
            }
            Set<String> pList = new HashSet<>();
            pList.add(commonSource.getParentLevel());
            Map<Long, String> parentPathDisplayMap = systemLogTool.getParentPathDisplayMap(pList);

            if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
                homeExplorerVO.setPathDisplay(systemLogTool.setParentPathDisplay(parentPathDisplayMap, commonSource.getParentLevel(), spaceID));
            }
        }

        if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType())
                || Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSource.getFileType())
                || Arrays.asList(GlobalConfig.CAMERA_TYPE_ARR).contains(commonSource.getFileType())){
            HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
            String value = operations.get(GlobalConfig.temp_img_video_key, homeExplorerDTO.getSourceID() + "");
            if (ObjectUtils.isEmpty(value)) {
                operations.put(GlobalConfig.temp_img_video_key, commonSource.getSourceID()+"", "0");

                asyncCutImgUtil.AsyncExecVideoImgThumb(commonSource, stringRedisTemplate, true);

                operations.getOperations().expire(GlobalConfig.temp_img_video_key, 24, TimeUnit.HOURS);
            }
        }

        operationsOne.put(homeExplorerKey, secondKey, JsonUtils.beanToJson(homeExplorerVO));
        operationsOne.getOperations().expire(homeExplorerKey, 5, TimeUnit.MINUTES);

        if (ObjectUtils.isEmpty(homeExplorerVO.getName())){
            homeExplorerVO.setName(null);
        }
        if (ObjectUtils.isEmpty(homeExplorerVO.getDescription())){
            homeExplorerVO.setDescription(null);
        }
        return homeExplorerVO;
    }


    @Override
    public HomeExplorerVO execImgVideoThumb(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){

        if (1 != loginUser.getUserType()){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if(ObjectUtils.isEmpty(homeExplorerDTO.getSourceID()) || homeExplorerDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        CommonSource commonSource = fileOptionTool.getSourceInfo(homeExplorerDTO.getSourceID());
        if(ObjectUtils.isEmpty(commonSource) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

        HomeExplorerVO homeExplorerVO = new HomeExplorerVO();
        homeExplorerVO.setSourceID(commonSource.getSourceID());
        homeExplorerVO.setParentID(commonSource.getParentID());
        homeExplorerVO.setParentLevel(commonSource.getParentLevel());
        homeExplorerVO.setName(commonSource.getName());
        homeExplorerVO.setDescription(commonSource.getDescription());
        homeExplorerVO.setPathDisplay("");
        if (!",0,".equals(commonSource.getParentLevel())) {
            Long spaceID = 0L;
            if (commonSource.getTargetType().intValue() == 1) {
                List<Long> parentIDs = Arrays.asList(commonSource.getParentLevel().split(",")).stream().filter(n -> !ObjectUtils.isEmpty(n) && !"0".equals(n)).map(Long::parseLong).collect(Collectors.toList());
                spaceID = parentIDs.get(0);
            }
            Set<String> pList = new HashSet<>();
            pList.add(commonSource.getParentLevel());
            Map<Long, String> parentPathDisplayMap = systemLogTool.getParentPathDisplayMap(pList);

            if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
                homeExplorerVO.setPathDisplay(systemLogTool.setParentPathDisplay(parentPathDisplayMap, commonSource.getParentLevel(), spaceID));
            }
        }

        if (Arrays.asList(GlobalConfig.VIDEO_TYPE_ARR).contains(commonSource.getFileType())
                || Arrays.asList(GlobalConfig.CAMERA_TYPE_ARR).contains(commonSource.getFileType())
                || Arrays.asList(GlobalConfig.IMAGE_TYPE_ARR).contains(commonSource.getFileType())){
            asyncCutImgUtil.AsyncExecVideoImgThumb(commonSource, stringRedisTemplate, false);
        }
        homeExplorerVO.setPath(commonSource.getPath());
        return homeExplorerVO;
    }

}
