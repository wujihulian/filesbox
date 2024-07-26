package com.svnlan.home.utils;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.I18nUtils;
import com.svnlan.enums.AuthEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.HomeExplorerDao;
import com.svnlan.home.dao.IoSourceAuthDao;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.enums.CloudOperateEnum;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.IoSourceAuthVo;
import com.svnlan.home.vo.ParentPathDisplayVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.GroupSourceDao;
import com.svnlan.user.dao.UserDao;
import com.svnlan.user.dao.UserGroupDao;
import com.svnlan.user.domain.GroupSource;
import com.svnlan.user.vo.UserGroupVo;
import com.svnlan.user.vo.UserVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.StringUtil;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/17 10:20
 */
@Component
public class UserAuthTool {

    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    UserDao userDaoImpl;
    @Resource
    IoSourceAuthDao ioSourceAuthDao;
    @Resource
    UserGroupDao userGroupDao;
    @Resource
    GroupSourceDao groupSourceDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    HomeExplorerDao homeExplorerDao;

    /** 后台权限 */
    public static final String[] manager = {"/api/disk/msgWarning", "/api/disk/setMsgWarning", "/api/disk/pluginList" , "/api/disk/setPlugin"};
    /** 概览 admin.index.dashboard */
    public static final String[] dashboard = {"/api/overview/visitRecord/list", "/api/overview/diskInfo/overview", "/api/overview/fileAndDevice/proportion"};
    /** 基本设置 admin.index.setting */
    public static final String[] setting = {"/api/disk/setSetting"};
    /** 角色权限列表 admin.role.list */
    public static final String[] roleList = {"/api/disk/role/list"};
    /** 角色权限编辑 admin.role.edit */
    public static final String[] roleEdit = {"/api/disk/role/edit","/api/disk/role/sort","/api/disk/role/remove"};
    /** 文档权限列表 admin.auth.list */
    public static final String[] authList = {};
    /** 文档权限编辑 admin.auth.edit */
    public static final String[] authEdit = {"/api/disk/auth/edit","/api/disk/auth/sort","/api/disk/auth/remove"};
    /** 部门与成员列表 admin.member.list */
    public static final String[] userList = {"/api/disk/group/list", "/api/disk/user/list", "/api/disk/group/search"};
    /** 编辑用户 admin.member.userEdit */
    public static final String[] userEdit = {"/api/disk/user/set", "/api/disk/user/edit"};
    /** 部门与成员列表 admin.member.groupEdit */
    public static final String[] groupEdit = {"/api/disk/group/remove", "/api/disk/group/set", "/api/disk/group/edit", "/api/disk/group/sort"};
    /** 存储列表 admin.storage.list */
    public static final String[] storageList = {};
    /** 存储操作 admin.storage.edit */
    public static final String[] storageEdit = {};

    /** 文件夹 explorer.add */
    public static final String[] addDir = {"/api/disk/createFolder"};
    /** 上传 explorer.upload */
    public static final String[] upload = {"/api/disk/upload","/api/disk/upload/check"};
    /** 解压 explorer.unzip */
    public static final String[] unzip = {"/api/disk/unZip"};
    /** 压缩 explorer.zip */
    public static final String[] zip = {"/api/disk/zip"};
    /** 分享 explorer.share */
    public static final String[] share = {"/api/disk/userShare/save","/api/disk/userShare/cancel"};
    /** 查看资讯 explorer.informationView */
    public static final String[] informationView = {"/api/disk/getInfoList", "/api/disk/getHomepageDetail"};
    public static final String[] informationManage = {"/api/disk/saveInfoType","/api/disk/editInfoType","/api/disk/deleteInfoType"
            ,"/api/disk/saveCommonInfo","/api/disk/editCommonInfo", "/api/disk/deleteInfo"};
    /** 编辑保存 explorer.edit*/
    public static final String[] edit = {"/api/disk/fileSave","/api/disk/fav/moveTop","/api/disk/fav/moveBottom","/api/disk/comment/del","/api/disk/comment/save"};
    /** 预览 explorer.view */
    public static final String[] view = {"/api/disk/preview","/api/disk/unzipList","/api/disk/preview/get","/api/disk/comment/list", "api/disk/pathLog","/api/disk/comment/list"
    ,"/api/disk/pathLog"};
    /** 文档删除 explorer.remove */
    public static final String[] remove = {"/api/disk/setAuth","/api/disk/history/setVersion","/api/disk/history/delete","/api/disk/history/setDetail"};


    public void checkRoleAuthByTypeKey(LoginUser loginUser, String type) {
        /** 权限 */
        Map<String, Object> roleMap = null;
        if (loginUser.getUserType().intValue() == 1 ){
            return;
        }
        roleMap = this.getUserRoleMap(loginUser);
        if (ObjectUtils.isEmpty(roleMap)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
        }
        if (!roleMap.containsKey(type) || !"1".equals(roleMap.get(type).toString())){
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
        }
    }


    /** 标签权限跟随 收藏权限 */
    public void checkUserTagPermission(LoginUser loginUser){
        this.checkRoleAuthByTypeKey(loginUser, "user.fav");
    }


    public void checkUserPermission(LoginUser loginUser, CheckFileDTO updateFileDTO){
        Map<String, Object> roleMap = null;
        if (loginUser.getUserType().intValue() == 1 ){
            return;
        }
        roleMap = this.getUserRoleMap(loginUser);

        if (ObjectUtils.isEmpty(roleMap)) {
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
        }
        CloudOperateEnum operateEnum = CloudOperateEnum.getOperateEnum(updateFileDTO.getOperation());
        LogUtil.info("checkUserPermission roleMap=" + JsonUtils.beanToJson(roleMap) + "， operateEnum=" + operateEnum.getOperate());
        try {
            switch (operateEnum){
                case RENAME_FILE:
                case FILE_DESC:
                case ADD_TOP:
                case CANCEL_TOP:
                case FILE_COVER:
                    if (!roleMap.containsKey("explorer.edit") || !"1".equals(roleMap.get("explorer.edit").toString())){
                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
                    }
                    break;
                case COPY_FILE:
                case MOVE_FILE:
                    if (!roleMap.containsKey("explorer.move") || !"1".equals(roleMap.get("explorer.move").toString())){
                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
                    }
                    break;
                case DELETE_FILE:
                case DELETE_BIN:
                case DELETE_BIN_ALL:
                    if (!roleMap.containsKey("explorer.remove") || !"1".equals(roleMap.get("explorer.remove").toString())){
                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
                    }
                    break;
                case SHARE_FILE:
                    if (!roleMap.containsKey("explorer.share") || !"1".equals(roleMap.get("explorer.share").toString())){
                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
                    }
                    break;
                case FAV_FILE:
                case FAV_DEL:
                    if (!roleMap.containsKey("user.fav") || !"1".equals(roleMap.get("user.fav").toString())){
                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
                    }
                    break;
                case MAKE_FILE:
                    if (!roleMap.containsKey("explorer.add") || !"1".equals(roleMap.get("explorer.add").toString())){
                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
                    }
                    break;
                case RECYCLE_FILE_ALL:
                case RECYCLE_FILE:
                    break;
                default:
                    throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }catch (Exception e){
            LogUtil.error(e, "校验用户权限");
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
        }

    }

    public Map<String, Object> getUserRoleMap(LoginUser loginUser){
        Map<String, Object> roleMap = null;

        Long userID = loginUser.getUserID();
        // 权限
        String userRoleKey = GlobalConfig.userRoleAuth_key;
        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        String value = operations.get(userRoleKey, userID + "");
        if (!ObjectUtils.isEmpty(value)) {
            try {
                roleMap = JsonUtils.jsonToMap(value);
            }catch (Exception e){
                LogUtil.error(e, "updateFile 缓存解析失败 key=" + userRoleKey + "，userID=" + userID + "，value=" + value);
            }
        }
        if (ObjectUtils.isEmpty(roleMap)) {
            UserVo userVo = userDaoImpl.getUserInfo(userID);
            roleMap = ObjectUtils.isEmpty(userVo.getAuth()) ? null : AuthEnum.getUserAuthMap(userVo.getAuth());
        }
        return roleMap;
    }

    public Boolean checkManageAuth(LoginUser loginUser, String uri){
        if (loginUser.getUserType().intValue() == 1){
            return true;
        }
        Map<String, Object> roleMap = this.getUserRoleMap(loginUser);

        if (!ObjectUtils.isEmpty(roleMap)){
            String v = JsonUtils.beanToJson(roleMap);
            if (Arrays.asList(manager).contains(uri)){
                // 管理后台权限
                if (v.indexOf("admin") < 0){
                    throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
                }
            }
        }
        LogUtil.info("checkManageAuth uri= " + uri);
        if (Arrays.asList(dashboard).contains(uri)){
            if (!roleMap.containsKey("admin.index.dashboard") || !"1".equals(roleMap.get("admin.index.dashboard").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(setting).contains(uri)){
            if (!roleMap.containsKey("admin.index.setting") || !"1".equals(roleMap.get("admin.index.setting").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(roleList).contains(uri)){
            if (!roleMap.containsKey("admin.role.list") || !"1".equals(roleMap.get("admin.role.list").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(roleEdit).contains(uri)){
            if (!roleMap.containsKey("admin.role.edit") || !"1".equals(roleMap.get("admin.role.edit").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(authList).contains(uri)){
            if (!roleMap.containsKey("admin.auth.list") || !"1".equals(roleMap.get("admin.auth.list").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(authEdit).contains(uri)){
            if (!roleMap.containsKey("admin.auth.edit") || !"1".equals(roleMap.get("admin.auth.edit").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(userList).contains(uri)){
            if (!roleMap.containsKey("admin.member.list") || !"1".equals(roleMap.get("admin.member.list").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(userEdit).contains(uri)){
            if (!roleMap.containsKey("admin.member.userEdit") || !"1".equals(roleMap.get("admin.member.userEdit").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(groupEdit).contains(uri)){
            if (!roleMap.containsKey("admin.member.groupEdit") || !"1".equals(roleMap.get("admin.member.groupEdit").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(storageList).contains(uri)){
            if (!roleMap.containsKey("admin.storage.list") || !"1".equals(roleMap.get("admin.storage.list").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(storageEdit).contains(uri)){
            if (!roleMap.containsKey("admin.storage.edit") || !"1".equals(roleMap.get("admin.storage.edit").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(addDir).contains(uri)){
            if (!roleMap.containsKey("explorer.add") || !"1".equals(roleMap.get("explorer.add").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(upload).contains(uri)){
            if (!roleMap.containsKey("explorer.upload") || !"1".equals(roleMap.get("explorer.upload").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(unzip).contains(uri)){
            if (!roleMap.containsKey("explorer.unzip") || !"1".equals(roleMap.get("explorer.unzip").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(zip).contains(uri)){
            if (!roleMap.containsKey("explorer.zip") || !"1".equals(roleMap.get("explorer.zip").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(share).contains(uri)){
            if (!roleMap.containsKey("explorer.share") || !"1".equals(roleMap.get("explorer.share").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(informationManage).contains(uri)){
            if (!roleMap.containsKey("admin.index.information") || !"1".equals(roleMap.get("admin.index.information").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(informationView).contains(uri)){
            if (!roleMap.containsKey("explorer.informationView") || !"1".equals(roleMap.get("explorer.informationView").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(view).contains(uri)){
            if (!roleMap.containsKey("explorer.view") || !"1".equals(roleMap.get("explorer.view").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(edit).contains(uri)){
            if (!roleMap.containsKey("explorer.edit") || !"1".equals(roleMap.get("explorer.edit").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }else if (Arrays.asList(remove).contains(uri)){
            if (!roleMap.containsKey("explorer.remove") || !"1".equals(roleMap.get("explorer.remove").toString())){
                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
            }
        }

        return true;
    }

    /** 文档权限 1查看、2搜索、3预览、4下载、5上传、6压缩、7解压、8编辑、9新增、10删除、11分享、12评论、13动态、14管理权限、15自定义删除 **/
    public boolean checkGroupDocAuth(LoginUser loginUser, Long sourceId, String parentLevel, String checkAuth, Integer targetType){
        return checkGroupDocAuth(loginUser, sourceId, parentLevel, checkAuth, targetType, true);
    }
    public boolean checkGroupDocAuth(LoginUser loginUser, Long sourceId, String parentLevel, String checkAuth, Integer targetType, boolean checkParent){
        if (!ObjectUtils.isEmpty(targetType) && targetType.intValue() != 2){
            return true;
        }
        if (!ObjectUtils.isEmpty(loginUser) && loginUser.getUserType().intValue() == 1){
            return true;
        }
        Long userId = loginUser.getUserID();
        if ("10".equals(checkAuth)){
            // 用户可以对自己上传创建的文件文件夹做删除操作
            Long c =  ioSourceDao.checkSourceIsOwn(sourceId, loginUser.getUserID());
            if (!ObjectUtils.isEmpty(c) && c > 0){
                return true;
            }
        }

        List<Integer> checkAuthList = Arrays.asList(checkAuth.split(",")).stream().filter(n1 -> !ObjectUtils.isEmpty(n1) && !"0".equals(n1)).map(Integer::valueOf).collect(Collectors.toList());

        List<Long> sourceIds = new ArrayList<>();
        List<Long> parentIds = null;
        if (!",0,".equals(parentLevel)){
            parentIds = Arrays.asList(parentLevel.split(",")).stream().filter(n1 -> !ObjectUtils.isEmpty(n1) && !"0".equals(n1)).map(Long::parseLong).collect(Collectors.toList());
            sourceIds.addAll(parentIds);
        }
        if (!sourceIds.contains(sourceId)) {
            sourceIds.add(sourceId);
        }
        // 用户所在部门
        List<UserGroupVo> myGroupList = this.getUserGroupAuth(userId);
        // 文件及上级文件夹单独设置的权限
        List<IoSourceAuthVo> allReList = ioSourceAuthDao.getSourceAuthBySourceIDList(sourceIds, userId);
        if (!CollectionUtils.isEmpty(allReList)){
            for (int i = sourceIds.size(); i-- > 0; ) {
                Long sID = sourceIds.get(i);
                List<IoSourceAuthVo> userList = allReList.stream().filter(n-> n.getSourceID().equals(sID) && n.getTargetType().intValue() == 1 && n.getTargetID() > 0).collect(Collectors.toList());
                List<IoSourceAuthVo> groupList = allReList.stream().filter(n-> n.getSourceID().equals(sID) && n.getTargetType().intValue() == 2).collect(Collectors.toList());
                List<IoSourceAuthVo> otherUserList = allReList.stream().filter(n-> n.getSourceID().equals(sID) && n.getTargetType().intValue() == 1 && n.getTargetID().longValue() == 0).collect(Collectors.toList());
                // 用户权限
                if (!CollectionUtils.isEmpty(userList)){
                    userList.forEach(n->{
                        if (n.getTargetID().longValue() == userId) {
                            if (ObjectUtils.isEmpty(n.getAuth())) {
                                String parentPathDis = getSourceParentPath(sourceId, parentLevel);
                                LogUtil.error(String.format("权限不足：1:userId=%s，sourceId=%s，parentLevel=%s，checkAuth=%s，targetType=%s，parentPathDis=%s", userId, sourceId, parentLevel, checkAuth, targetType, parentPathDis));
                                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                            }
                            List<Integer> authList = Arrays.asList(n.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                            boolean c = false;
                            for (int a : checkAuthList) {
                                if (authList.contains(a)) {
                                    c = true;
                                }
                            }
                            if (!c) {
                                String parentPathDis = getSourceParentPath(sourceId, parentLevel);
                                LogUtil.error(String.format("权限不足：2:userId=%s，sourceId=%s，parentLevel=%s，checkAuth=%s，targetType=%s，parentPathDis=%s", userId, sourceId, parentLevel, checkAuth, targetType, parentPathDis));
                                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                            }
                        }
                    });
                    return true;
                }
                if (!CollectionUtils.isEmpty(groupList)){
                    boolean check = false;
                    if (!CollectionUtils.isEmpty(myGroupList)){
                        // 指定部门
                        for(IoSourceAuthVo n : groupList){
                            for (UserGroupVo vo : myGroupList){
                                if (n.getTargetID().longValue() == vo.getGroupID()){
                                    List<Integer> authList = Arrays.asList(n.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                                    boolean c = false;
                                    for (int a : checkAuthList){
                                        if (authList.contains(a)){
                                            c = true;
                                        }
                                    }
                                    if (!c){
                                        String parentPathDis = getSourceParentPath(sourceId, parentLevel);
                                        LogUtil.error(String.format("权限不足：3:userId=%s，sourceId=%s，parentLevel=%s，checkAuth=%s，targetType=%s，parentPathDis=%s",userId,sourceId,parentLevel,checkAuth,targetType, parentPathDis));
                                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                                    }
                                    check = true;
                                }
                            }
                        }
                        if (check){
                            return true;
                        }
                    }
                }
                // 其他人权限
                if (!CollectionUtils.isEmpty(otherUserList)){
                    for(IoSourceAuthVo n : otherUserList){
                        List<Integer> authList = Arrays.asList(n.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                        boolean c = false;
                        for (int a : checkAuthList){
                            if (authList.contains(a)){
                                c = true;
                            }
                        }
                        if (!c){
                            String parentPathDis = getSourceParentPath(sourceId, parentLevel);
                            LogUtil.error(String.format("权限不足：5:userId=%s，sourceId=%s，parentLevel=%s，checkAuth=%s，targetType=%s，parentPathDis=%s",userId,sourceId,parentLevel,checkAuth,targetType,parentPathDis));
                            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                        }
                    }
                }
                // 指定部门的上一级部门
                if (!CollectionUtils.isEmpty(groupList)){

                    boolean check = false;
                    boolean checkIn = false;
                    // 指定部门的上级部门
                    for(IoSourceAuthVo n : groupList){
                        for (UserGroupVo vo : myGroupList){
                            List<Long> parentIDLists = Arrays.asList(vo.getParentLevel().split(",")).stream().filter(n1 -> !ObjectUtils.isEmpty(n1) && !"0".equals(n1)).map(Long::parseLong).collect(Collectors.toList());
                            if (!CollectionUtils.isEmpty(parentIDLists) ){
                                if (parentIDLists.contains(n.getTargetID())){
                                    checkIn = true;
                                    List<Integer> authList = Arrays.asList(n.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                                    boolean c = false;
                                    for (int a : checkAuthList){
                                        if (authList.contains(a)){
                                            c = true;
                                        }
                                    }
                                    if (c){
                                        check = true;
                                    }
                                }
                            }
                        }
                    }
                    if (check){
                        return true;
                    }
                    // 设置了但是没有权限
                    if (checkIn){
                        String parentPathDis = getSourceParentPath(sourceId, parentLevel);
                        LogUtil.error(String.format("权限不足：4:userId=%s，sourceId=%s，parentLevel=%s，checkAuth=%s，targetType=%s，parentPathDis=%s",userId,sourceId,parentLevel,checkAuth,targetType,parentPathDis));
                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                    }
                }
            }
        }
        // 单独设置权限 end
        List<Long> searchSourceIds = new ArrayList();
        if (checkParent){
            searchSourceIds = sourceIds;
        }else {
            searchSourceIds.add(sourceId);
        }
        // 文件夹权限及上级文件夹权限
        List<GroupSource> allReturnList = groupSourceDao.getGroupSourceList(new ArrayList<>(searchSourceIds));
        if ((checkParent && CollectionUtils.isEmpty(allReturnList)) || CollectionUtils.isEmpty(myGroupList)){
            // 没有部门则没有权限
            String parentPathDis = getSourceParentPath(sourceId, parentLevel);
            LogUtil.error(String.format("权限不足：6:userId=%s，sourceId=%s，parentLevel=%s，checkAuth=%s，targetType=%s，parentPathDis=%s",userId,sourceId,parentLevel,checkAuth,targetType,parentPathDis));
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
        }
        Map<Long, UserGroupVo> AuthGroupMap = myGroupList.stream().collect(Collectors.toMap(UserGroupVo::getGroupID, Function.identity(), (v1, v2) -> v2));
        Map<Long, Long> sourceGroupMap = allReturnList.stream().collect(Collectors.toMap(GroupSource::getSourceID, GroupSource::getGroupID, (v1, v2) -> v2));
        for (int i = searchSourceIds.size(); i-- > 0; ) {
            Long sID = searchSourceIds.get(i);
            if (sourceGroupMap.containsKey(sID)){
                long groupId = sourceGroupMap.get(sID);
                if (AuthGroupMap.containsKey(groupId)){
                    UserGroupVo userGroupVo = AuthGroupMap.get(groupId);
                    List<Integer> authList = Arrays.asList(userGroupVo.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                    boolean c = false;
                    for (int a : checkAuthList){
                        if (authList.contains(a)){
                            c = true;
                        }
                    }
                    if (!c){
                        String parentPathDis = getSourceParentPath(sourceId, parentLevel);
                        LogUtil.error(String.format("权限不足：7:userId=%s，sourceId=%s，parentLevel=%s，checkAuth=%s，targetType=%s，parentPathDis=%s",userId,sourceId,parentLevel,checkAuth,targetType,parentPathDis));
                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                    }else {
                        return true;
                    }
                }
            }
        }

        // 文件夹权限及上级文件夹权限 end
        return true;
    }
    /** 用于校验 checkGroupDocAuth 后再校验 */
    public boolean checkGroupDocAuthOther(LoginUser loginUser, List<Long> sourceIds, String checkAuth, Integer targetType){
        if (!ObjectUtils.isEmpty(targetType) && targetType.intValue() != 2){
            return true;
        }
        if (CollectionUtils.isEmpty(sourceIds)){
            return true;
        }
        if (!ObjectUtils.isEmpty(loginUser) && loginUser.getUserType().intValue() == 1){
            return true;
        }
        Long userId = loginUser.getUserID();
        List<Integer> checkAuthList = Arrays.asList(checkAuth.split(",")).stream().filter(n1 -> !ObjectUtils.isEmpty(n1) && !"0".equals(n1)).map(Integer::valueOf).collect(Collectors.toList());

        Map<Long, Boolean> checkResultMap = new HashMap<>();
        // 用户所在部门
        List<UserGroupVo> myGroupList = this.getUserGroupAuth(userId);
        // 文件及上级文件夹单独设置的权限
        List<IoSourceAuthVo> allReList = ioSourceAuthDao.getSourceAuthBySourceIDList(sourceIds, userId);
        if (!CollectionUtils.isEmpty(allReList)){
            for (int i = sourceIds.size(); i-- > 0; ) {
                Long sID = sourceIds.get(i);
                List<IoSourceAuthVo> userList = allReList.stream().filter(n-> n.getSourceID().equals(sID) && n.getTargetType().intValue() == 1 && n.getTargetID() > 0).collect(Collectors.toList());
                List<IoSourceAuthVo> groupList = allReList.stream().filter(n-> n.getSourceID().equals(sID) && n.getTargetType().intValue() == 2).collect(Collectors.toList());
                List<IoSourceAuthVo> otherUserList = allReList.stream().filter(n-> n.getSourceID().equals(sID) && n.getTargetType().intValue() == 1 && n.getTargetID().longValue() == 0).collect(Collectors.toList());
                // 用户权限
                if (!CollectionUtils.isEmpty(userList)){
                    userList.forEach(n->{
                        if (n.getTargetID().longValue() == userId) {
                            if (ObjectUtils.isEmpty(n.getAuth())) {
                                String parentPathDis = getSourceParentPath(n.getSourceID(), null);
                                LogUtil.error(String.format("权限不足Other：1:userId=%s，sourceId=%s，parentPathDis=%s，checkAuth=%s", userId, n.getSourceID(), parentPathDis, checkAuth));
                                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                            }
                            List<Integer> authList = Arrays.asList(n.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                            boolean c = false;
                            for (int a : checkAuthList) {
                                if (authList.contains(a)) {
                                    c = true;
                                }
                            }
                            if (!c) {
                                String parentPathDis = getSourceParentPath(n.getSourceID(), null);
                                LogUtil.error(String.format("权限不足Other：2:userId=%s，sourceId=%s，parentPathDis=%s，checkAuth=%s", userId, n.getSourceID(), parentPathDis, checkAuth));
                                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                            }
                            checkResultMap.put(n.getSourceID(), true);
                        }
                    });

                }
                if (!CollectionUtils.isEmpty(groupList)){
                    boolean check = false;

                    if (!CollectionUtils.isEmpty(myGroupList)){
                        // 指定部门
                        for(IoSourceAuthVo n : groupList){
                            if (checkResultMap.containsKey(n.getSourceID())){
                                continue;
                            }
                            for (UserGroupVo vo : myGroupList){
                                if (n.getTargetID().longValue() == vo.getGroupID()){
                                    List<Integer> authList = Arrays.asList(n.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                                    boolean c = false;
                                    for (int a : checkAuthList){
                                        if (authList.contains(a)){
                                            c = true;
                                        }
                                    }
                                    if (!c){
                                        String parentPathDis = getSourceParentPath(n.getSourceID(), null);
                                        LogUtil.error(String.format("权限不足Other：3:userId=%s，sourceId=%s，parentPathDis=%s，checkAuth=%s",userId,n.getSourceID(), parentPathDis,checkAuth));
                                        throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                                    }
                                    check = true;
                                }
                            }
                            checkResultMap.put(n.getSourceID(),true);
                        }
                        boolean checkIn = false;
                        // 指定部门的上级部门
                        for(IoSourceAuthVo n : groupList){
                            if (checkResultMap.containsKey(n.getSourceID())){
                                continue;
                            }
                            for (UserGroupVo vo : myGroupList){
                                List<Long> parentIDLists = Arrays.asList(vo.getParentLevel().split(",")).stream().filter(n1 -> !ObjectUtils.isEmpty(n1) && !"0".equals(n1)).map(Long::parseLong).collect(Collectors.toList());
                                if (!CollectionUtils.isEmpty(parentIDLists) ){
                                    if (parentIDLists.contains(n.getTargetID())){
                                        checkIn = true;
                                        List<Integer> authList = Arrays.asList(n.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                                        boolean c = false;
                                        for (int a : checkAuthList){
                                            if (authList.contains(a)){
                                                c = true;
                                            }
                                        }
                                        if (c){
                                            check = true;
                                        }
                                    }
                                }
                            }
                            // 设置了但是没有权限
                            if (checkIn && !check){
                                String parentPathDis = getSourceParentPath(n.getSourceID(), null);
                                LogUtil.error(String.format("权限不足Other：4:userId=%s，sourceId=%s，parentPathDis=%s，checkAuth=%s",userId,n.getSourceID(), parentPathDis,checkAuth));
                                throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                            }
                            checkResultMap.put(n.getSourceID(),true);
                        }
                    }
                }
                // 其他人权限
                if (!CollectionUtils.isEmpty(otherUserList)){
                    for(IoSourceAuthVo n : otherUserList){
                        if (checkResultMap.containsKey(n.getSourceID())){
                            continue;
                        }
                        List<Integer> authList = Arrays.asList(n.getAuth().split(",")).stream().map(Integer::valueOf).collect(Collectors.toList());
                        boolean c = false;
                        for (int a : checkAuthList){
                            if (authList.contains(a)){
                                c = true;
                            }
                        }
                        if (!c){
                            String parentPathDis = getSourceParentPath(n.getSourceID(), null);
                            LogUtil.error(String.format("权限不足Other：5:userId=%s，sourceId=%s，parentPathDis=%s，checkAuth=%s",userId,n.getSourceID(), parentPathDis,checkAuth));
                            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAuthAll.getCode(), new Object[]{parentPathDis});
                        }
                    }
                }
            }
        }
        return true;
    }

    public List<UserGroupVo> getUserGroupAuth(Long userID){
        // 用户群组权限
        List<UserGroupVo> userGroupList = null;
        String key = GlobalConfig.REDIS_KEY_USER_GROUP_AUTH;

        HashOperations<String, String, String> operations = stringRedisTemplate.opsForHash();
        String value = operations.get(key, userID+"");
        if (!ObjectUtils.isEmpty(value)) {
            try {
                userGroupList = JsonUtils.stringToList(value, UserGroupVo.class);
                // JSON.parseArray(JSON.toJSONString(value), UserGroupVo.class);
            }catch (Exception e){
                LogUtil.error(e, "getUserGroupAuth 缓存解析失败 key=" + key + "，value=" + value);
            }
        }
        if (ObjectUtils.isEmpty(userGroupList)) {
            userGroupList = userGroupDao.getUserGroupInfoList(Arrays.asList(userID));
            operations.put(key, userID+"", JsonUtils.beanToJson(userGroupList));
            operations.getOperations().expire(key, 30, TimeUnit.MINUTES);
        }
        LogUtil.info("getUserGroupAuth******************** userGroupList=" + JsonUtils.beanToJson(userGroupList));
        return userGroupList;
    }

    /** 文件路径、文件夹路径、路径全称path */
    private String getSourceParentPath(Long sourceId, String parentLevel){

        List<Long> parentIDLists =  null;
        if (!ObjectUtils.isEmpty(parentLevel)){
            parentIDLists = Arrays.asList(parentLevel.split(",")).stream().filter(n1 -> !ObjectUtils.isEmpty(n1) && !"0".equals(n1)).map(Long::parseLong).collect(Collectors.toList());
        }else {
            HomeExplorerVO parentInfo = homeExplorerDao.getOneSourceInfo(sourceId);
            parentIDLists = Arrays.asList(parentInfo.getParentLevel().split(",")).stream().filter(n1 -> !ObjectUtils.isEmpty(n1) && !"0".equals(n1)).map(Long::parseLong).collect(Collectors.toList());
        }
        if (CollectionUtils.isEmpty(parentIDLists)){
            parentIDLists = new ArrayList<>();
        }
        parentIDLists.add(sourceId);
        Map<Long, String> map = new HashMap<>(parentIDLists.size());
        List<ParentPathDisplayVo> list = ioSourceDao.getParentPathDisplayByIds(parentIDLists);
        if (!CollectionUtils.isEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(ParentPathDisplayVo::getSourceID, ParentPathDisplayVo::getName, (v1, v2) -> v2));
        }
        List<String> nameList = new ArrayList<>();
        for (Long id : parentIDLists) {
            if (map.containsKey(id)) {
                nameList.add(map.get(id));
            }
        }
        return CollectionUtils.isEmpty(nameList) ? "" : StringUtil.joinString(nameList, "/");

    }
}
