package com.svnlan.home.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.EventEnum;
import com.svnlan.enums.LogTypeEnum;
import com.svnlan.enums.SourceFieldEnum;
import com.svnlan.enums.SourceSortEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.ShareDao;
import com.svnlan.home.dao.ShareToDao;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IoSourceEvent;
import com.svnlan.home.domain.Share;
import com.svnlan.home.domain.ShareTo;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.dto.ShareDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.service.ShareService;
import com.svnlan.home.utils.FileOptionTool;
import com.svnlan.home.utils.FileUtil;
import com.svnlan.home.utils.UserAuthTool;
import com.svnlan.home.vo.*;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SourceOperateTool;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.tools.SystemSortTool;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dao.RoleDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dao.UserGroupDao;
import com.svnlan.user.domain.Group;
import com.svnlan.user.domain.Role;
import com.svnlan.user.dto.GroupDTO;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.user.service.ShareReportService;
import com.svnlan.user.service.UserManageService;
import com.svnlan.user.vo.SelectAuthVo;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PageResult;
import com.svnlan.utils.RandomUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/3 13:33
 */
@Service
public class ShareServiceImpl implements ShareService {

    @Resource
    ShareDao shareDao;
    @Resource
    SystemSortTool systemSortTool;
    @Resource
    SourceOperateTool sourceOperateTool;
    @Resource
    FileOptionTool fileOptionTool;
    @Resource
    UserDao userDao;
    @Resource
    UserGroupDao userGroupDao;
    @Resource
    HomeExplorerDao homeExplorerDao;
    @Resource
    ShareToDao shareToDao;
    @Resource
    GroupDao groupDao;

    @Resource
    SystemLogTool systemLogTool;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    UserManageService userManageService;
    @Resource
    RoleDao roleDao;
    @Resource
    UserAuthTool userAuthTool;

    @Override
    public void saveShare(ShareDTO shareDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(shareDTO.getSourceID())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!ObjectUtils.isEmpty(shareDTO.getTitle()) && shareDTO.getTitle().length() > 255){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        shareDTO.setIsLink(ObjectUtils.isEmpty(shareDTO.getIsLink()) ? 1 : shareDTO.getIsLink());
        shareDTO.setIsShareTo(ObjectUtils.isEmpty(shareDTO.getIsShareTo()) ? 0 : shareDTO.getIsShareTo());
        if (1 == shareDTO.getIsShareTo()){
            shareDTO.setIsLink(0);
        }

        CommonSource commonSource = fileOptionTool.getSourceInfo(shareDTO.getSourceID());
        if (ObjectUtils.isEmpty(commonSource)){
            throw new SvnlanRuntimeException(CodeMessageEnum.pathNotExists.getCode());
        }
        // 判断是否可以分享
        if (!Objects.equals(commonSource.getCanShare(), 1)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.sourceShareDisabled.getCode());
        }
        // 权限校验
        userAuthTool.checkGroupDocAuth(loginUser, commonSource.getSourceID(), commonSource.getParentLevel(), "11", commonSource.getTargetType());

        Share share = new Share();
        share.setUserID(loginUser.getUserID());
        share.setTitle(ObjectUtils.isEmpty(shareDTO.getTitle()) ? commonSource.getName() : shareDTO.getTitle());
        share.setUrl("");
        share.setSourceID(shareDTO.getSourceID());
        share.setOptions(ObjectUtils.isEmpty(shareDTO.getOptions()) ? "" : shareDTO.getOptions());
        share.setSourcePath(commonSource.getParentLevel());
        share.setIsLink(shareDTO.getIsLink());
        share.setIsShareTo(shareDTO.getIsShareTo());
        share.setTimeTo(ObjectUtils.isEmpty(shareDTO.getTimeTo()) ? 0L : shareDTO.getTimeTo());
        share.setNumDownload(0);
        share.setNumView(0);
        share.setPassword(ObjectUtils.isEmpty(shareDTO.getPassword()) ? "" : shareDTO.getPassword());
        String msg = (1 == shareDTO.getIsShareTo()) ? "内部协作" : "分享链接";

        String logCode = null;
        EventEnum eventEnum = null;

        ShareVo vo = shareDao.getShare(shareDTO.getSourceID(), loginUser.getUserID(), shareDTO.getIsShareTo(), shareDTO.getIsLink());
        if (!ObjectUtils.isEmpty(vo)){
            shareDTO.setShareID(vo.getShareID());
            share.setShareID(vo.getShareID());
        }

        if (!ObjectUtils.isEmpty(shareDTO.getShareID()) && shareDTO.getShareID() > 0){
            share.setShareID(shareDTO.getShareID());
            if (1 == shareDTO.getIsShareTo()) {
                logCode = LogTypeEnum.fileshareEdit.getCode();
                eventEnum = EventEnum.shareEdit;
            }else {
                logCode = LogTypeEnum.fileShareLinkEdit.getCode();
                eventEnum = EventEnum.shareLinkEdit;
            }
            try {
                // 可分享状态
                share.setStatus(1);
                shareDao.update(share);
            }catch (Exception e){
                LogUtil.error("编辑" + msg + "失败！shareDTO=" + JsonUtils.beanToJson(shareDTO));
                throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
            }
        }else {


            // 内部协作authTo
            if (1 == shareDTO.getIsShareTo()) {
                logCode = LogTypeEnum.fileShareToAdd.getCode();
                eventEnum = EventEnum.shareToAdd;
            }else {
                logCode = LogTypeEnum.fileShareLinkAdd.getCode();
                eventEnum = EventEnum.shareLinkAdd;
            }

            share.setShareHash(ObjectUtils.isEmpty(shareDTO.getShareHash()) ? RandomUtil.getStringAndNum(10) : shareDTO.getShareHash());
            try {
                shareDao.insert(share);
            }catch (Exception e){
                LogUtil.error("添加" + msg + "接失败！");
                throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
            }
        }

        // 内部协作authTo
        if (1 == shareDTO.getIsShareTo() && !ObjectUtils.isEmpty(share.getShareID())){
            // 删除后添加
            shareToDao.delete(share.getShareID());

            if (!CollectionUtils.isEmpty(shareDTO.getAuthTo())){
                List<ShareTo> toList = new ArrayList<>();
                ShareTo shareTo = null;
                for (ShareAuthDto authDto : shareDTO.getAuthTo()){
                    if (ObjectUtils.isEmpty(authDto.getAuthID()) || ObjectUtils.isEmpty(authDto.getTargetID()) || ObjectUtils.isEmpty(authDto.getTargetType())){
                        continue;
                    }
                    shareTo = new ShareTo();
                    shareTo.setShareID(share.getShareID());
                    shareTo.setAuthDefine(-1);
                    shareTo.setAuthID(authDto.getAuthID());
                    shareTo.setTargetID(authDto.getTargetID());
                    shareTo.setTargetType(authDto.getTargetType());
                    toList.add(shareTo);
                }

                if (!CollectionUtils.isEmpty(toList)){
                    try {
                        shareToDao.batchInsert(toList);
                    }catch (Exception e){
                        LogUtil.error("添加" + msg + " shareTo insert 失败！");

                    }
                }
            }

        }



        // 添加文档操作event
        fileOptionTool.addSourceEvent(commonSource.getSourceID(), commonSource.getParentID(), loginUser.getUserID(), commonSource.getName(), eventEnum);

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        reMap = new HashMap<>(4);
        reMap.put("sourceID", commonSource.getSourceID());
        reMap.put("sourceParent", commonSource.getParentID());
        reMap.put("type", "share");
        reMap.put("pathName", commonSource.getSourceName());
        paramList.add(reMap);
        systemLogTool.setSysLog(loginUser, logCode, paramList, systemLogTool.getRequest());

    }

    @Override
    public void cancelShare(ShareDTO shareDTO, LoginUser loginUser){

        List<Long> deleteList = null;
        if (!ObjectUtils.isEmpty(shareDTO.getShareID())){
            deleteList = Arrays.asList(shareDTO.getShareID());
        }
        if (!ObjectUtils.isEmpty(shareDTO.getShareIDStr())){
            deleteList = Arrays.asList(shareDTO.getShareIDStr().split(",")).stream().map(Long::parseLong).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(deleteList) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<ShareVo> list = shareDao.getShareByIdList(deleteList);
        if (CollectionUtils.isEmpty(list) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        ShareVo vo =  list.get(0);
        try {
            shareDao.deleteList(deleteList);
        }catch (Exception e){
            LogUtil.error("删除分享链接失败！shareDTO=" + JsonUtils.beanToJson(shareDTO));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        if (!ObjectUtils.isEmpty(vo) && !ObjectUtils.isEmpty(vo.getIsShareTo()) && 1 == vo.getIsShareTo()){
            // 删除分享关系
            shareToDao.deleteList(deleteList);
        }

        List<Long> sourceIDs = list.stream().map(ShareVo::getSourceID).collect(Collectors.toList());
        List<CommonSource> sourceList = ioSourceDao.getSourceInfoList(sourceIDs);
        if (CollectionUtils.isEmpty(sourceList)){
           return;
        }
        EventEnum eventEnum = EventEnum.shareLinkRemove;
        String logCode = LogTypeEnum.shareLinkRemove.getCode();
        // 内部协作authTo
        if (0 == vo.getIsLink()) {
            eventEnum = EventEnum.shareToRemove;
            logCode = LogTypeEnum.fileShareToRemove.getCode();
        }
        List<IoSourceEvent> eventList = new ArrayList<>();

        /** 操作日志 */
        Map<String, Object> reMap = null;
        List<Map<String, Object>> paramList = new ArrayList<>();
        for (CommonSource data : sourceList){
            eventList.add(new IoSourceEvent(data.getSourceID(), data.getParentID(), loginUser.getUserID(), eventEnum.getCode(), FileOptionTool.getSourceEventDesc(eventEnum, "", "")));

            reMap = new HashMap<>(4);
            reMap.put("sourceID", data.getSourceID());
            reMap.put("sourceParent", data.getParentID());
            reMap.put("type", "share");
            reMap.put("pathName", data.getSourceName());
            paramList.add(reMap);
        }
        // 添加文档操作event
        fileOptionTool.addSourceEventList(eventList);
        /** 操作日志 */
        systemLogTool.setSysLog(loginUser, logCode, paramList, systemLogTool.getRequest());
    }

    @Override
    public ShareVo getShare(ShareDTO shareDTO, LoginUser loginUser){
        ShareVo vo = null;
        shareDTO.setIsLink(ObjectUtils.isEmpty(shareDTO.getIsLink()) ? 1 : shareDTO.getIsLink());
        shareDTO.setIsShareTo(0);
        if (!ObjectUtils.isEmpty(shareDTO.getIsShareTo()) && 1 == shareDTO.getIsShareTo()){
            shareDTO.setIsLink(0);
            shareDTO.setIsShareTo(1);
        }
        if (!ObjectUtils.isEmpty(shareDTO.getSourceID()) && shareDTO.getSourceID() > 0){
            vo = shareDao.getShare(shareDTO.getSourceID(), loginUser.getUserID(), shareDTO.getIsShareTo(), shareDTO.getIsLink());
        }else if (!ObjectUtils.isEmpty(shareDTO.getShareID()) && shareDTO.getShareID() > 0){
            vo = shareDao.getShareById(shareDTO.getShareID());
        }
        if (ObjectUtils.isEmpty(vo)){
            return new ShareVo();
        }
        vo.setAuthTo(new ArrayList<>());
        // 获取被分享的部门或者个人
        if (1 == shareDTO.getIsShareTo()){
            List<ShareToVo> shareToList = shareToDao.getShareToList(vo.getShareID());
            if (!CollectionUtils.isEmpty(shareToList)){
                vo.setAuthTo(shareToList);
            }
        }
        return vo;
    }

    @Override
    public HomeExplorerResult getShareList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){
        boolean checkGet = false;
        List<IoSourceAuthVo> roleList = null;
        String auth = "";
        if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
            auth = GlobalConfig.SYSTEM_GROUP_AUTH;
            checkGet = true;

            Role role = roleDao.getSystemRoleInfo();
            roleList = new ArrayList<>();
            roleList.add(new IoSourceAuthVo(role.getRoleID(), role.getRoleName(), role.getLabel(), auth));
        }
        Map<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("userID", loginUser.getUserID());

        homeExplorerDTO.setIsLink(ObjectUtils.isEmpty(homeExplorerDTO.getIsLink()) ? 1 : homeExplorerDTO.getIsLink());
        homeExplorerDTO.setIsShareTo(0);
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getIsShareTo()) && 1 == homeExplorerDTO.getIsShareTo()){
            homeExplorerDTO.setIsLink(0);
            homeExplorerDTO.setIsShareTo(1);
        }
        hashMap.put("isShareTo", homeExplorerDTO.getIsShareTo());
        hashMap.put("isLink", homeExplorerDTO.getIsLink());
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getKeyword())){
            hashMap.put("keyword", homeExplorerDTO.getKeyword().toLowerCase());
        }

        SystemSortVo systemSortVo = systemSortTool.getUserSort(loginUser.getUserID());
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getSortField()) && !ObjectUtils.isEmpty(homeExplorerDTO.getSortType())){
            hashMap.put("sortType", SourceSortEnum.getSortType(homeExplorerDTO.getSortType()));
            hashMap.put("sortField", "io." + SourceFieldEnum.getSortField(homeExplorerDTO.getSortField()));
        }else {
            String sortType = systemSortVo.getListSortOrder();
            String sortField = systemSortVo.getListSortField();
            hashMap.put("sortType", SourceSortEnum.getSortType(sortType));
            hashMap.put("sortField", "io." + SourceFieldEnum.getSortField(sortField));
            if ("1".equals(systemSortVo.getListSortKeep()) && !CollectionUtils.isEmpty(systemSortVo.getListSortList())){
                for (SystemSortVo vo : systemSortVo.getListSortList()){
                    if (vo.getSourceID().equals(String.valueOf(homeExplorerDTO.getSourceID()))){
                        if (!ObjectUtils.isEmpty(vo.getListSortOrder())) {
                            hashMap.put("sortType", SourceSortEnum.getSortType(vo.getListSortOrder()));
                        }
                        if (!ObjectUtils.isEmpty(vo.getListSortField())) {
                            hashMap.put("sortField", "io." + SourceFieldEnum.getSortField(vo.getListSortField()));
                        }
                        break;
                    }
                }
            }
        }
        PageHelper.startPage(homeExplorerDTO.getCurrentPage(), homeExplorerDTO.getPageSize());
        List<HomeExplorerVO> list = this.shareDao.getShareList(hashMap);

        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        List<HomeExplorerVO> fileVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            FileMetaVo fileMetaVo = null;
            String downloadKey = FileUtil.getDownloadKey();
            List<Long> idList = new ArrayList<>();
            List<String> sourceIDs= new ArrayList<>();
            Set<Long> uidList = new HashSet<>();
            Set<Long> gpidList = new HashSet<>();
            Set<Long> sidList = new HashSet<>();
            Set<String> pLevelList = new HashSet<>();
            long spaceId = 0;
            for (HomeExplorerVO vo2 : list){
                if (vo2.getIsFolder().intValue() == 1){
                    idList.add(vo2.getSourceID());
                }
                sourceIDs.add(vo2.getSourceID().toString());
                uidList.add(vo2.getCreateUser());
                uidList.add(vo2.getModifyUser());

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
                pLevelList.add(vo2.getParentLevel());
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
            Map<Long, String> parentPathDisplayMap = null;
            // 文件位置
            if (!CollectionUtils.isEmpty(pLevelList)) {
                parentPathDisplayMap = sourceOperateTool.getParentPathDisplayMap(pLevelList);

            }
            if (spaceId == 0){
                HomeExplorerVO space = homeExplorerDao.getHomeSpace(loginUser.getUserID(), 0L);
                spaceId = space.getSourceID();
            }
            // 标签
            Map<String, List<CommonLabelVo>> sourceTagMap = fileOptionTool.getSourceTagMap(loginUser.getUserID(), sourceIDs);
            // 收藏
            List<String> myFavList = fileOptionTool.checkIsFavByUserId(loginUser.getUserID());

            List<UserVo> userList = userDao.getUserBaseInfo(new ArrayList<>(uidList));
            Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));
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
                vo1.setDownloadUrl("");
                vo1.setHasFile(0);
                vo1.setHasFolder(0);
                vo1.setResolution("");
                vo1.setLength(0);
                vo1.setThumb("");
                if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_folder")){
                    vo1.setHasFolder(countMap.get(vo1.getSourceID() + "_folder"));
                }
                if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_file")){
                    vo1.setHasFile(countMap.get(vo1.getSourceID() + "_file"));
                }

                sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);

                // 用户信息
                vo1.setCreateUserJson(JsonUtils.beanToJson(userMap.get(vo1.getCreateUser())));
                vo1.setModifyUserJson(JsonUtils.beanToJson(userMap.get(vo1.getModifyUser())));

                // path 处理
                sourceOperateTool.opPath( vo1, downloadKey, vo1.getShareHash(),"4");

                // 是否收藏
                vo1.setIsFav(!CollectionUtils.isEmpty(myFavList) && myFavList.contains(vo1.getSourceID().toString()) ? 1 : 0);
                // 已选标签
                vo1.setTags("");
                vo1.setTagList(new ArrayList<>());
                if (!ObjectUtils.isEmpty(sourceTagMap) && sourceTagMap.containsKey(vo1.getSourceID().toString())){
                    vo1.setTagList(sourceTagMap.get(vo1.getSourceID().toString()));
                }
                // 是否分享
                vo1.setIsShare(1);

                vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? vo1.getThumb() : vo1.getPath());
                vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? "" : vo1.getPath());
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
        PageInfo<HomeExplorerVO> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setFolderList(folderVOList);
        result.setFileList(fileVOList);

        return sourceOperateTool.setSystemReturn(result, systemSortVo, String.valueOf(homeExplorerDTO.getSourceID()));
    }

    @Resource
    private ShareReportService shareReportService;

    @Override
    public ShareVo getShareFile(ShareDTO shareDTO, LoginUser loginUser){
        if (ObjectUtils.isEmpty(shareDTO.getShareCode())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareNotExist.getCode());
        }
        List<ShareVo> list = shareDao.getShareByCode(shareDTO.getShareCode());
        if (CollectionUtils.isEmpty(list) || list.size() > 1){
            //throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorPathTips.getCode());
            return null;
        }
        ShareVo vo = list.get(0);
        String password = vo.getPassword();
        vo.setPassword(null);
        vo.setNeedPwd(0);
        if (ObjectUtils.isEmpty(vo.getNickname())){
            vo.setNickname(vo.getUserName());
        }
        // 密码
        if (!ObjectUtils.isEmpty(password)){
            if (ObjectUtils.isEmpty(shareDTO.getPassword()) || !password.equals(shareDTO.getPassword())){
                vo.setNeedPwd(1);
                vo.setSuccess(false);
                vo.setMessage(I18nUtils.i18n(CodeMessageEnum.ERROR_USER_PASSWORD_ERROR.getCode()));
                return vo;
            }
        }
        // 是否过期
        if (!ObjectUtils.isEmpty(vo.getTimeTo()) && vo.getTimeTo() > 0 && vo.getTimeTo() < System.currentTimeMillis() / 1000){
            vo.setSuccess(false);
            vo.setMessage(I18nUtils.i18n(CodeMessageEnum.shareExpiredTips.getCode()));
            return vo;
        }
        Map<String, Object> optionsMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(vo.getOptions())){
            optionsMap = JsonUtils.jsonToBean(vo.getOptions(), Map.class);
        }
        vo.setLogin(0);
        vo.setDown(0);
        vo.setPreview(0);
        vo.setDownNum(0);
        vo.setShareToTimeout(0L);
        boolean checkDown = false;

        // notView 是否禁用浏览
        if (optionsMap.containsKey("preview")) {
            String previewStr = optionsMap.get("preview").toString();
            if (!ObjectUtils.isEmpty(previewStr)) {
                vo.setPreview(Integer.parseInt(previewStr));
            }
        }
        // notDownload 是否禁用下载
        if (optionsMap.containsKey("down")) {
            String downStr = optionsMap.get("down").toString();
            if (!ObjectUtils.isEmpty(downStr)) {
                vo.setDown(Integer.parseInt(downStr));
            }
        }

        if (vo.getDown().intValue() == 1 && optionsMap.containsKey("downNum")){
            String downNumStr = optionsMap.get("downNum").toString();
            if (!ObjectUtils.isEmpty(downNumStr)){
                int downNum = Integer.parseInt(downNumStr);
                vo.setDownNum(downNum);
                if ("0".equals(downNumStr) || downNum > vo.getNumDownload()){
                    checkDown = true;
                }
            }else {
                checkDown = true;
            }
        }

        // 判断该分享链接是否有效
        shareReportService.checkIfShareLinkPermit(vo);

        // login 是否需要登录  1 是  0 否
        if (optionsMap.containsKey("login")) {
            String login = optionsMap.get("login").toString();
            if (!ObjectUtils.isEmpty(login) && "1".equals(login) && ObjectUtils.isEmpty(loginUser)) {
                vo.setLogin(1);
                vo.setSuccess(false);
                vo.setMessage(I18nUtils.i18n(CodeMessageEnum.shareLoginTips.getCode()));
                return vo;
            }
        }

        HomeExplorerVO homeExplorerVO = shareDao.getLinkShareInfo(vo.getSourceID());
        if (ObjectUtils.isEmpty(homeExplorerVO)){
            vo.setSuccess(false);
            vo.setMessage(I18nUtils.i18n(CodeMessageEnum.shareNotExist.getCode()));
            return vo;
        }
        Set<Long> uidList = new HashSet<>();
        uidList.add(homeExplorerVO.getCreateUser());
        uidList.add(homeExplorerVO.getModifyUser());

        List<UserVo> userList = userDao.getUserBaseInfo(new ArrayList<>(uidList));
        Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));

        FileMetaVo fileMetaVo = null;
        homeExplorerVO.setDownloadUrl("");
        homeExplorerVO.setHasFile(0);
        homeExplorerVO.setHasFolder(0);
        homeExplorerVO.setResolution("");
        homeExplorerVO.setLength(0);
        homeExplorerVO.setThumb("");
        sourceOperateTool.setFileMetaValue(fileMetaVo, homeExplorerVO);



        // 用户信息
        homeExplorerVO.setCreateUserJson(JsonUtils.beanToJson(userMap.get(homeExplorerVO.getCreateUser())));
        homeExplorerVO.setModifyUserJson(JsonUtils.beanToJson(userMap.get(homeExplorerVO.getModifyUser())));
        String downloadKey = FileUtil.getDownloadKey();
        sourceOperateTool.opPath(homeExplorerVO, downloadKey, shareDTO.getShareCode(), "4");
        // 是否分享
        homeExplorerVO.setIsShare(1);

        homeExplorerVO.setPath(ObjectUtils.isEmpty(homeExplorerVO.getPath()) ? homeExplorerVO.getThumb() : homeExplorerVO.getPath());
        homeExplorerVO.setPath(ObjectUtils.isEmpty(homeExplorerVO.getPath()) ? "" : homeExplorerVO.getPath());
        if (!checkDown){
            homeExplorerVO.setDownloadUrl("");
        }
        if (vo.getPreview().intValue() == 1){
            if (Arrays.asList(GlobalConfig.DOC_SHOW_TYPE_ARR).contains(homeExplorerVO.getFileType())) {
                String downloadUrl = "/api/disk/attachment/" + FileUtil.encodeDownloadUrl(homeExplorerVO.getName())
                        + "?busId=" + homeExplorerVO.getSourceID() + "&busType=" + BusTypeEnum.CLOUD.getBusType() + "&key=" + downloadKey;
                String pptPreviewUrl = fileOptionTool.getPptPreviewUrl(downloadUrl, homeExplorerVO.getHashMd5());
                homeExplorerVO.setPptPreviewUrl(pptPreviewUrl);
            }
        }
        vo.setShareFile(homeExplorerVO);
        // downNum 下载次数
        if (!checkDown){
            vo.setSuccess(false);
            // 抱歉，该分享下载次数超过分享者设置的上限
            vo.setMessage(I18nUtils.i18n(CodeMessageEnum.shareDownExceedTips.getCode()));
            return vo;
        }

        vo.setSuccess(true);
        return vo;
    }


    @Override
    public HomeExplorerResult getLinkShareList(HomeExplorerDTO homeExplorerDTO){
        if (ObjectUtils.isEmpty(homeExplorerDTO.getShareCode()) || ObjectUtils.isEmpty(homeExplorerDTO.getSourceID())
                || homeExplorerDTO.getSourceID() <= 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareNotExist.getCode());
        }
        List<ShareVo> shareList = shareDao.getShareByCode(homeExplorerDTO.getShareCode());
        if (CollectionUtils.isEmpty(shareList) || shareList.size() > 1){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorPathTips.getCode());
        }
        ShareVo vo = shareList.get(0);
        Map<String, Object> optionsMap = new HashMap<>();
        if (!ObjectUtils.isEmpty(vo.getOptions())){
            optionsMap = JsonUtils.jsonToBean(vo.getOptions(), Map.class);
        }
        try {
            shareDao.updateNumView(vo.getShareID(), vo.getNumView() + 1);
        }catch (Exception e){
            LogUtil.error("getLinkShareList updateNumView error");
        }

        vo.setDown(0);
        vo.setPreview(0);
        boolean checkDown = false;
        // notView 是否禁用浏览
        if (optionsMap.containsKey("preview")) {
            String previewStr = optionsMap.get("preview").toString();
            if (!ObjectUtils.isEmpty(previewStr)) {
                vo.setPreview(Integer.parseInt(previewStr));
            }
        }
        // notDownload 是否禁用下载
        if (optionsMap.containsKey("down")) {
            String downStr = optionsMap.get("down").toString();
            if (!ObjectUtils.isEmpty(downStr)) {
                vo.setDown(Integer.parseInt(downStr));
            }
        }
        if (vo.getDown().intValue() ==1){
            Object downNumStr = optionsMap.get("downNum");
            if (!ObjectUtils.isEmpty(downNumStr)){
                int downNum = Integer.parseInt(downNumStr.toString());
                vo.setDownNum(downNum);
                if ("0".equals(downNumStr) || downNum > vo.getNumDownload()){
                    checkDown = true;
                }
            }else {
                checkDown = true;
            }
        }

        Map<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("parentID", homeExplorerDTO.getSourceID());
        hashMap.put("sortType", SourceSortEnum.asc.getValue());
        hashMap.put("sortField", SourceFieldEnum.name.getValue());
        // 左边菜单时
        if (!ObjectUtils.isEmpty(homeExplorerDTO.getIsFolder())){
            hashMap.put("isFolder", homeExplorerDTO.getIsFolder());
        }

        PageHelper.startPage(homeExplorerDTO.getCurrentPage(), homeExplorerDTO.getPageSize());
        List<HomeExplorerVO> list = this.shareDao.getLinkShareList(hashMap);

        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        List<HomeExplorerVO> fileVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            FileMetaVo fileMetaVo = null;
            String downloadKey = FileUtil.getDownloadKey();
            List<Long> idList = new ArrayList<>();
            List<String> sourceIDs= new ArrayList<>();
            Set<Long> uidList = new HashSet<>();
            for (HomeExplorerVO vo2 : list){
                if (vo2.getIsFolder().intValue() == 1){
                    idList.add(vo2.getSourceID());
                }
                sourceIDs.add(vo2.getSourceID().toString());
                uidList.add(vo2.getCreateUser());
                uidList.add(vo2.getModifyUser());
            }
            List<UserVo> userList = userDao.getUserBaseInfo(new ArrayList<>(uidList));
            Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));
            List<HomeExplorerVO> countList = CollectionUtils.isEmpty(idList) ? null : homeExplorerDao.getSourceChileCont(idList);
            Map<String, Integer> countMap = new HashMap<>(1);
            if (!CollectionUtils.isEmpty(countList)) {
                for (HomeExplorerVO home : countList) {
                    countMap.put(home.getParentID() + (home.getIsFolder().intValue() == 1 ? "_folder" : "_file"), home.getFileCount());
                }
            }
            for (HomeExplorerVO vo1 : list){

                vo1.setDown(vo.getDown());
                vo1.setPreview(vo.getPreview());

                vo1.setDownloadUrl("");
                vo1.setHasFile(0);
                vo1.setHasFolder(0);
                vo1.setResolution("");
                vo1.setLength(0);
                vo1.setThumb("");
                if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_folder")){
                    vo1.setHasFolder(countMap.get(vo1.getSourceID() + "_folder"));
                }
                if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_file")){
                    vo1.setHasFile(countMap.get(vo1.getSourceID() + "_file"));
                }

                sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);
                vo1.setYzEditData("");
                if (vo1.getPreview().intValue() == 0){
                    vo1.setYzViewData("");
                }

                // 用户信息
                vo1.setCreateUserJson(JsonUtils.beanToJson(userMap.get(vo1.getCreateUser())));
                vo1.setModifyUserJson(JsonUtils.beanToJson(userMap.get(vo1.getModifyUser())));
                if (vo1.getIsFolder() == 0) {
                    // path 处理
                    sourceOperateTool.opPath(vo1, downloadKey, homeExplorerDTO.getShareCode(), "4");
                }
                // 是否分享
                vo1.setIsShare(1);

                vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? vo1.getThumb() : vo1.getPath());
                vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? "" : vo1.getPath());

                if (vo1.getIsFolder() == 1) {
                    vo1.setIcon("folder");
                    folderVOList.add(vo1);
                } else {

                    vo1.setIcon("file");
                    fileVOList.add(vo1);
                }

            }
        }
        PageInfo<HomeExplorerVO> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setFolderList(folderVOList);
        result.setFileList(fileVOList);

        return result;
    }

    @Override
    public HomeExplorerResult shareToMeList(HomeExplorerDTO homeExplorerDTO, LoginUser loginUser){

        Map<String, Object> hashMap = new HashMap<>(3);
        hashMap.put("userID", loginUser.getUserID());
        hashMap.put("isShareTo", 1);
        hashMap.put("isLink", 0);

        SystemSortVo systemSortVo = systemSortTool.getUserSort(loginUser.getUserID());
        systemSortTool.setSortAboutMap(systemSortVo,hashMap, loginUser.getUserID(), homeExplorerDTO.getSortField(), homeExplorerDTO.getSortType()
                , "shareToMe");

        /*
      该部门成员协作分享时,可以选择其他所有部门(及用户) all-所有 hide-仅支持选择当前部门及子部门(及用户) select-指定部门
        private String authShowType;
         authShowType 指定部门的id，多个部门用英文逗号隔开
        private String authShowGroup;
         */
        List<Long> groupIDList = null;
        // 用户部门权限
        List<UserGroupVo> userGroupList = userAuthTool.getUserGroupAuth(loginUser.getUserID());
        if (!CollectionUtils.isEmpty(userGroupList)){
            groupIDList = userGroupList.stream().map(UserGroupVo::getGroupID).collect(Collectors.toList());
            hashMap.put("list", groupIDList);
        }
        PageHelper.startPage(homeExplorerDTO.getCurrentPage(), homeExplorerDTO.getPageSize());
        List<HomeExplorerVO> list = this.shareDao.getShareToMeList(hashMap);

        HomeExplorerResult result = new HomeExplorerResult();
        List<HomeExplorerVO> folderVOList = new ArrayList<>();
        List<HomeExplorerVO> fileVOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)) {
            FileMetaVo fileMetaVo = null;
            String downloadKey = FileUtil.getDownloadKey();
            List<Long> idList = new ArrayList<>();
            List<String> sourceIDs= new ArrayList<>();
            Set<Long> uidList = new HashSet<>();
            for (HomeExplorerVO vo2 : list){
                if (vo2.getIsFolder().intValue() == 1){
                    idList.add(vo2.getSourceID());
                }
                sourceIDs.add(vo2.getSourceID().toString());
                uidList.add(vo2.getCreateUser());
                uidList.add(vo2.getModifyUser());
            }
            // 标签
            Map<String, List<CommonLabelVo>> sourceTagMap = fileOptionTool.getSourceTagMap(loginUser.getUserID(), sourceIDs);
            // 收藏
            List<String> myFavList = fileOptionTool.checkIsFavByUserId(loginUser.getUserID());

            List<UserVo> userList = userDao.getUserBaseInfo(new ArrayList<>(uidList));
            Map<Long, UserVo> userMap = userList.stream().collect(Collectors.toMap(UserVo::getUserID, Function.identity(), (v1, v2) -> v2));
            List<HomeExplorerVO> countList = CollectionUtils.isEmpty(idList) ? null : homeExplorerDao.getSourceChileCont(idList);
            Map<String, Integer> countMap = new HashMap<>(1);
            if (!CollectionUtils.isEmpty(countList)) {
                for (HomeExplorerVO home : countList) {
                    countMap.put(home.getParentID() + (home.getIsFolder().intValue() == 1 ? "_folder" : "_file"), home.getFileCount());
                }
            }
            for (HomeExplorerVO vo1 : list){
                vo1.setDownloadUrl("");
                vo1.setHasFile(0);
                vo1.setHasFolder(0);
                vo1.setResolution("");
                vo1.setLength(0);
                vo1.setThumb("");
                if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_folder")){
                    vo1.setHasFolder(countMap.get(vo1.getSourceID() + "_folder"));
                }
                if (!ObjectUtils.isEmpty(countMap) && countMap.containsKey(vo1.getSourceID() + "_file")){
                    vo1.setHasFile(countMap.get(vo1.getSourceID() + "_file"));
                }

                sourceOperateTool.setFileMetaValue(fileMetaVo, vo1);

                // 用户信息
                vo1.setCreateUserJson(JsonUtils.beanToJson(userMap.get(vo1.getCreateUser())));
                vo1.setModifyUserJson(JsonUtils.beanToJson(userMap.get(vo1.getModifyUser())));

                // path 处理
                sourceOperateTool.opPath( vo1, downloadKey, "4");

                // 是否收藏
                vo1.setIsFav(!CollectionUtils.isEmpty(myFavList) && myFavList.contains(vo1.getSourceID().toString()) ? 1 : 0);
                // 已选标签
                vo1.setTags("");
                vo1.setTagList(new ArrayList<>());
                if (!ObjectUtils.isEmpty(sourceTagMap) && sourceTagMap.containsKey(vo1.getSourceID().toString())){
                    vo1.setTagList(sourceTagMap.get(vo1.getSourceID().toString()));
                }
                // 是否分享
                vo1.setIsShare(0);

                vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? vo1.getThumb() : vo1.getPath());
                vo1.setPath(ObjectUtils.isEmpty(vo1.getPath()) ? "" : vo1.getPath());

                if (vo1.getIsFolder() == 1) {
                    vo1.setIcon("folder");
                    folderVOList.add(vo1);
                } else {
                    vo1.setIcon("file");
                    fileVOList.add(vo1);
                }

            }
        }
        PageInfo<HomeExplorerVO> pageInfo = new PageInfo<>(list);
        result.setTotal(pageInfo.getTotal());
        result.setFolderList(folderVOList);
        result.setFileList(fileVOList);

        return sourceOperateTool.setSystemReturn(result, systemSortVo, String.valueOf(homeExplorerDTO.getSourceID()));
    }

    @Override
    public SelectAuthVo userGroupList(HomeExplorerDTO shareDTO, LoginUser loginUser){

        if (!ObjectUtils.isEmpty(shareDTO.getGroupID()) && shareDTO.getGroupID() > 0){
            return userGroupListByGroup(shareDTO, loginUser);
        }

        // 获取列表
        List<UserGroupVo> list = userGroupListBySearch(shareDTO, loginUser);
        SelectAuthVo vo = new SelectAuthVo();
        vo.setGroupList(list);

        Map<String, Object> map = new HashMap<>(2);
        if (!ObjectUtils.isEmpty(shareDTO.getKeyword())) {
            map.put("keyword", shareDTO.getKeyword().toLowerCase());
        }
        // 判断是否是顶层group
        map.put("groupID", 0);

        List<UserVo> userList = this.shareDao.getNotGroupUserListByParam(map);

        vo.setUserList(CollectionUtils.isEmpty(userList) ? new ArrayList<>() : userList);
        return vo;
    }

    public List<UserGroupVo> userGroupListBySearch(HomeExplorerDTO shareDTO, LoginUser loginUser){
        // 系统管理员
        boolean isSystem = false;
        if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
            isSystem = true;
        }

        List<UserGroupVo> list = new ArrayList<>();
        // 用户部门权限
        List<UserGroupVo> userGroupList = null;
        if (!isSystem) {
            if (!ObjectUtils.isEmpty(shareDTO.getKeyword())) {
                userGroupList = userGroupDao.getUserGroupInfoListByParam(Arrays.asList(loginUser.getUserID()), shareDTO.getKeyword().toLowerCase());
            } else {
                userGroupList = userGroupDao.getUserGroupInfoList(Arrays.asList(loginUser.getUserID()));
            }
        }
        Set<Long> groupIDList = null;
        String authShowType = "";
        List<String> groupAuthList = null;
        if (!CollectionUtils.isEmpty(userGroupList)){
            for (UserGroupVo userGroupVo : userGroupList){
                // systemGroupSource:1__namePinyin:qiyeyunpan__namePinyinSimple:qyyp__authShowType:all__authShowGroup:
                if (!ObjectUtils.isEmpty(userGroupVo.getGroupAuth()) && userGroupVo.getGroupAuth().indexOf("authShowType:all__") >= 0){
                    authShowType = "all";
                }
            }
            if (!"all".equals(authShowType)){
                groupIDList = new HashSet<>();
                for (UserGroupVo userGroup : userGroupList){

                    list.add(userGroup);

                    groupAuthList = !ObjectUtils.isEmpty(userGroup.getGroupAuth()) ? Arrays.asList(userGroup.getGroupAuth().split("__")).stream().map(String::valueOf).collect(Collectors.toList()) : null;
                    if (!ObjectUtils.isEmpty(groupAuthList)){
                        for (String groupAuth : groupAuthList){
                            if (groupAuth.indexOf("authShowGroup:")>=0 && groupAuth.length() > 14 ){
                                groupIDList.addAll(Arrays.asList(groupAuth.substring(groupAuth.indexOf("authShowGroup:") + 14, groupAuth.length()).split(",")).stream().map(Long::valueOf).collect(Collectors.toSet()));
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(groupIDList)){
                    for (UserGroupVo userGroup : userGroupList){
                        if (groupIDList.contains(userGroup.getGroupID())){
                            groupIDList.remove(userGroup.getGroupID());
                        }
                    }
                    List<UserGroupVo> list2 = groupDao.getGroupVoList(new ArrayList<>(groupIDList));
                    if (!CollectionUtils.isEmpty(list2)){
                        list.addAll(list2);
                    }
                }
            }else {
                // 获取所有部门
                list = groupDao.getMainGroupVoList(shareDTO);
            }
        }else if (isSystem){
            list = groupDao.getMainGroupVoList(shareDTO);
        }

        Group g = groupDao.getTopGroup();

        if (!CollectionUtils.isEmpty(list)){
            List<Long> gidList = new ArrayList<>();
            for (UserGroupVo group : list){
                if (!ObjectUtils.isEmpty(group.isHasChildren()) && !group.isHasChildren()){
                    gidList.add(group.getGroupID());
                }
            }
            if (!CollectionUtils.isEmpty(gidList)){
                List<Group> countList = groupDao.getGroupUserCountList(gidList);
                Map<Long, Integer> countMap = CollectionUtils.isEmpty(countList) ? null : countList.stream().collect(Collectors.toMap(Group::getGroupID, Group::getHasChildren, (v1, v2) -> v2));
                if (!ObjectUtils.isEmpty(countMap)){
                    for (UserGroupVo group : list){
                        if (countMap.containsKey(group.getGroupID())){
                            group.setHasChildren(true);
                        }
                    }
                }
            }
            for (UserGroupVo vo : list){
                vo.setParentName(g.getName());
            }
        }else {
            list = new ArrayList<>();
        }
        return list;
    }
    public SelectAuthVo userGroupListByGroup(HomeExplorerDTO shareDTO, LoginUser loginUser){
        List<Group> list = null;
        List<UserVo> userList = null;
        Group g = groupDao.getGroupInfoByID(shareDTO.getGroupID());


        GroupDTO paramDto = new GroupDTO();
        paramDto.setStatus(1);
        paramDto.setParentID(shareDTO.getGroupID());

        if (!ObjectUtils.isEmpty(shareDTO.getKeyword())) {
            paramDto.setKeyword(shareDTO.getKeyword());
        }

        list = groupDao.getGroupList(paramDto);

        Map<String, Object> map = new HashMap<>(2);
        if (!ObjectUtils.isEmpty(shareDTO.getKeyword())) {
            map.put("keyword", shareDTO.getKeyword().toLowerCase());
        }
        // 判断是否是顶层group
        map.put("groupID", shareDTO.getGroupID());
        userList = this.shareDao.getSelectUserListByParam(map);
        List<UserGroupVo> groupList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(list)){
            List<Long> gidList = new ArrayList<>();
            for (Group group : list){
                boolean hasChildren = (group.getHasChildren().intValue() > 0 ? true : false);
                if (!hasChildren){
                    gidList.add(group.getGroupID());
                }
                groupList.add(new UserGroupVo(group.getGroupID(), group.getParentID(), group.getName(), g.getName(), hasChildren));
            }
            if (!CollectionUtils.isEmpty(gidList)){
                List<Group> countList = groupDao.getGroupUserCountList(gidList);
                Map<Long, Integer> countMap = CollectionUtils.isEmpty(countList) ? null : countList.stream().collect(Collectors.toMap(Group::getGroupID, Group::getHasChildren, (v1, v2) -> v2));
                if (!ObjectUtils.isEmpty(countMap)){
                    for (UserGroupVo group : groupList){
                        if (countMap.containsKey(group.getGroupID())){
                            group.setHasChildren(true);
                        }
                    }
                }
            }
        }

        SelectAuthVo vo = new SelectAuthVo();
        vo.setGroupList(groupList);
        vo.setUserList(userList);
        return vo;
    }


    @Override
    public Map<String, Object> previewNum(ShareDTO shareDTO){
        Map<String, Object> map = new HashMap<>(1);
        map.put("numView",0);
        if (!ObjectUtils.isEmpty(shareDTO.getShareCode())){
            List<ShareVo> list = shareDao.getShareByCode(shareDTO.getShareCode());
            if (CollectionUtils.isEmpty(list) || list.size() > 1){
                return map;
            }
            ShareVo vo = list.get(0);
            map.put("numView", vo.getNumView() + 1);
            vo.setPassword(null);
            try {
                shareDao.updateNumView(vo.getShareID(), vo.getNumView() + 1);
            }catch (Exception e){
                LogUtil.error("getPreviewInfo updateNumView error");
            }
        }
        return map;
    }

    @Override
    public PageResult getUserList(LoginUser loginUser, UserDTO userDTO){
        userDTO.setStatus(1);
        PageResult data = null;

        // 系统管理员
        boolean isSystem = false;
        if (!ObjectUtils.isEmpty(loginUser) && 1 == loginUser.getUserType()){
            isSystem = true;
        }

        List<UserGroupVo> list = new ArrayList<>();
        // 用户部门权限
        List<UserGroupVo> userGroupList = null;
        if (!isSystem) {
            if (!ObjectUtils.isEmpty(userDTO.getKeyword())) {
                userGroupList = userGroupDao.getUserGroupInfoListByParam(Arrays.asList(loginUser.getUserID()), userDTO.getKeyword().toLowerCase());
            } else {
                userGroupList = userGroupDao.getUserGroupInfoList(Arrays.asList(loginUser.getUserID()));
            }
        }
        Set<Long> groupIDList = null;
        String authShowType = "";
        List<String> groupAuthList = null;
        if (!CollectionUtils.isEmpty(userGroupList)){
            for (UserGroupVo userGroupVo : userGroupList){
                // systemGroupSource:1__namePinyin:qiyeyunpan__namePinyinSimple:qyyp__authShowType:all__authShowGroup:
                if (!ObjectUtils.isEmpty(userGroupVo.getGroupAuth()) && userGroupVo.getGroupAuth().indexOf("authShowType:all__") >= 0){
                    authShowType = "all";
                }
            }
            if (!"all".equals(authShowType)){
                groupIDList = new HashSet<>();
                for (UserGroupVo userGroup : userGroupList){
                    list.add(userGroup);
                    groupAuthList = !ObjectUtils.isEmpty(userGroup.getGroupAuth()) ? Arrays.asList(userGroup.getGroupAuth().split("__")).stream().map(String::valueOf).collect(Collectors.toList()) : null;
                    if (!ObjectUtils.isEmpty(groupAuthList)){
                        for (String groupAuth : groupAuthList){
                            if (groupAuth.indexOf("authShowGroup:")>=0 && groupAuth.length() > 14 ){
                                groupIDList.addAll(Arrays.asList(groupAuth.substring(groupAuth.indexOf("authShowGroup:") + 14, groupAuth.length()).split(",")).stream().map(Long::valueOf).collect(Collectors.toSet()));
                            }
                        }
                    }
                }
                if (!CollectionUtils.isEmpty(groupIDList)){
                    for (UserGroupVo userGroup : userGroupList){
                        if (groupIDList.contains(userGroup.getGroupID())){
                            groupIDList.remove(userGroup.getGroupID());
                        }
                    }
                    List<UserGroupVo> list2 = groupDao.getGroupVoList(new ArrayList<>(groupIDList));
                    if (!CollectionUtils.isEmpty(list2)){
                        list.addAll(list2);
                    }
                }
            }else {
                // 获取所有部门
                data = userManageService.getUserList(loginUser, userDTO);
            }
        }else if (isSystem){
            data = userManageService.getUserList(loginUser, userDTO);
        }

        if (!ObjectUtils.isEmpty(data)){
            return data;
        }

        if (!CollectionUtils.isEmpty(list)){
            List<String> groupLevelList = list.stream().map(UserGroupVo::getParentLevel).collect(Collectors.toList());
            userDTO.setGroupLevelList(groupLevelList);
            return userManageService.getUserList(loginUser, userDTO);
        }else {
            return new PageResult(0L, new ArrayList());
        }
    }
}
