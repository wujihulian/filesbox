package com.svnlan.user.service.impl;

import com.svnlan.enums.GroupMetaEnum;
import com.svnlan.enums.MetaEnum;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dao.IoSourceDao;
import com.svnlan.home.dao.IoSourceMetaDao;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IOSourceMeta;
import com.svnlan.home.vo.HomeExplorerShareDetailVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dao.GroupDao;
import com.svnlan.user.dao.GroupMetaDao;
import com.svnlan.user.dao.GroupSourceDao;
import com.svnlan.user.dao.UserGroupDao;
import com.svnlan.user.domain.Group;
import com.svnlan.user.domain.GroupMeta;
import com.svnlan.user.domain.GroupSource;
import com.svnlan.user.dto.GroupDTO;
import com.svnlan.user.service.GroupService;
import com.svnlan.user.service.StorageService;
import com.svnlan.user.tools.GroupTool;
import com.svnlan.user.vo.GroupSizeVo;
import com.svnlan.user.vo.GroupVo;
import com.svnlan.utils.ChinesUtil;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.Partition;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 11:34
 */
@Service
public class GroupServiceImpl implements GroupService {

    @Resource
    GroupDao groupDao;
    @Resource
    GroupMetaDao groupMetaDao;
    @Resource
    IoSourceDao ioSourceDao;
    @Resource
    GroupSourceDao groupSourceDao;
    @Resource
    IoSourceMetaDao ioSourceMetaDao;
    @Resource
    UserGroupDao userGroupDao;
    @Resource
    StorageService storageService;
    @Resource
    GroupTool groupTool;

    @Override
    public List getGroupList(LoginUser loginUser, GroupDTO paramDto){
        paramDto.setParentID(ObjectUtils.isEmpty(paramDto.getParentID()) ? 0 : paramDto.getParentID());
        paramDto.setStatus(ObjectUtils.isEmpty(paramDto.getStatus()) ? null : paramDto.getStatus());

        long systemGroupID = 0;
        if (paramDto.getParentID().longValue() <= 0){
            systemGroupID = groupMetaDao.getSystemGroup();
        }

        List<Group> groupList = groupDao.getGroupList(paramDto);
        if (CollectionUtils.isEmpty(groupList)){
            return new ArrayList();
        }
        return getAuthShowList(groupList, systemGroupID);
    }

    @Override
    public List searchGroupList(LoginUser loginUser, GroupDTO paramDto){
        paramDto.setParentID(ObjectUtils.isEmpty(paramDto.getParentID()) ? 0 : paramDto.getParentID());
        List<Group> groupList = groupDao.searchGroupList(paramDto);
        if (CollectionUtils.isEmpty(groupList)){
            return new ArrayList();
        }
        long systemGroupID = 0;
        if (paramDto.getParentID().longValue() <= 0){
            systemGroupID = groupMetaDao.getSystemGroup();
        }
        return getAuthShowList(groupList, systemGroupID);
    }

    private List<Group> getAuthShowList(List<Group> groupList, long systemGroupID){
        List<Long> groupIdList = new ArrayList<>();
        Set<String> pLevelList = new HashSet<>();
        for (Group g : groupList){
            groupIdList.add(g.getGroupID());
            if (g.getParentID() > 0) {
                pLevelList.add(g.getParentLevel());
            }
        }
        Map<Long, String> parentPathDisplayMap = null;
        if (!CollectionUtils.isEmpty(pLevelList)){
            parentPathDisplayMap = groupTool.getParentPathDisplayMap(pLevelList);
        }
        List<GroupMeta> metaList = groupMetaDao.getGroupMetaList(groupIdList, Arrays.asList("authShowType", "authShowGroup"));
        Map<Long, Map<String, String>> map = new HashMap<>(groupIdList.size());
        if (!CollectionUtils.isEmpty(metaList)){
            Map<String, String> reMap = null;
            for (GroupMeta meta: metaList){
                if (!ObjectUtils.isEmpty(map) && map.containsKey(meta.getGroupID())){
                    reMap = map.get(meta.getGroupID());
                }else {
                    reMap = new HashMap<>(1);
                }
                reMap.put(meta.getKey(), meta.getValue());
                map.put(meta.getGroupID(), reMap);
            }

            for (Group group : groupList){
                if (!ObjectUtils.isEmpty(map) && map.containsKey(group.getGroupID())){
                    group.setAuthShowType(map.get(group.getGroupID()).get("authShowType"));
                    group.setAuthShowGroup(map.get(group.getGroupID()).get("authShowGroup"));
                }
                group.setPathDisplay("");
                if (!ObjectUtils.isEmpty(parentPathDisplayMap)) {
                    group.setPathDisplay(groupTool.setParentPathDisplay(parentPathDisplayMap, group.getParentLevel()));
                }
                group.setAuthShowType(ObjectUtils.isEmpty(group.getAuthShowType()) ? "" : group.getAuthShowType());
                group.setAuthShowGroup(ObjectUtils.isEmpty(group.getAuthShowGroup()) ? "" : group.getAuthShowGroup());
                group.setIsSystem(0);
                if (systemGroupID == group.getGroupID().longValue()){
                    group.setIsSystem(1);
                }
            }
        }
        return groupList;
    }

    @Override
    public void editGroup(String prefix, LoginUser loginUser, GroupDTO paramDto){
        /** 校验参数 */
        checkGroupParam(paramDto);
        paramDto.setParentID(!ObjectUtils.isEmpty(paramDto.getParentID()) ? paramDto.getParentID() : 0L);
        /** 转换数据*/
        Group group = changeGroupParam(paramDto);

        /** 添加部门 */
        if (ObjectUtils.isEmpty(paramDto.getGroupID()) || paramDto.getGroupID() <= 0 ){
            addGroup(prefix, group, paramDto, loginUser);
        }else {
            updateGroup(prefix, group, paramDto);
            // 先删除meta
            groupMetaDao.delMetaByGroupID(paramDto.getGroupID(), GroupMetaEnum.delKeyList());
        }

        if (!ObjectUtils.isEmpty(group.getGroupID())){
            // 删除原有的meta
            List<GroupMeta> paramList = new ArrayList<>();

            paramList.add(new GroupMeta(group.getGroupID(), GroupMetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(paramDto.getName())));
            paramList.add(new GroupMeta(group.getGroupID(), GroupMetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(paramDto.getName())));
            paramList.add(new GroupMeta(group.getGroupID(), GroupMetaEnum.authShowType.getValue(), ObjectUtils.isEmpty(paramDto.getAuthShowType()) ? "all" : paramDto.getAuthShowType()));
            if (ObjectUtils.isEmpty(paramDto.getAuthShowType()) || !"select".equals(paramDto.getAuthShowType())){
                paramDto.setAuthShowGroup("");
            }
            paramList.add(new GroupMeta(group.getGroupID(), GroupMetaEnum.authShowGroup.getValue(), ObjectUtils.isEmpty(paramDto.getAuthShowGroup()) ? "" : paramDto.getAuthShowGroup()));

            try {
                groupMetaDao.batchInsert(paramList);
            }catch (Exception e){
                LogUtil.error(e, prefix + " batchInsert error " + JsonUtils.beanToJson(paramDto));
            }
        }
    }

    public void updateGroup(String prefix, Group group, GroupDTO paramDto){
        // 查看是否更换上级部门
        GroupVo oldGroup = groupDao.getGroupAndSourceByID(paramDto.getGroupID());

        Map<String, Object> paramMap = new HashMap<>(2);
        if (oldGroup.getParentID().longValue() != paramDto.getParentID().longValue()){
            if (paramDto.getParentID() > 0){
                GroupVo parentGroup = groupDao.getGroupAndSourceByID(paramDto.getParentID());
                if (ObjectUtils.isEmpty(parentGroup)) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                group.setParentLevel(parentGroup.getParentLevel() + parentGroup.getGroupID() + ",");

                paramMap.put("parentID",parentGroup.getSourceID());
                paramMap.put("parentLevel",parentGroup.getSourceParentLevel() + parentGroup.getSourceID() + ",");
            }
            // parentSequenceLength = parentGroup.getGroupSequence().length();
            Integer sort = groupDao.getMaxSort(paramDto.getParentID());
            sort = ObjectUtils.isEmpty(sort) ? 0 : sort + 1;
            group.setSort(sort);
            group.setParentID(paramDto.getParentID());
        }

        try {
            groupDao.update(group);
        }catch (Exception e){
            LogUtil.error(e, prefix + " updateGroup error " + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        try {
            paramMap.put("name",group.getName());
            paramMap.put("sourceID",oldGroup.getSourceID());
            groupDao.updateGroupSourceName(paramMap);
        }catch (Exception e){
            LogUtil.error(e, prefix + " updateGroup updateGroupSourceName error " + JsonUtils.beanToJson(paramDto));
        }

    }
    public void addGroup(String prefix, Group group, GroupDTO paramDto, LoginUser loginUser){

       // int parentSequenceLength = 0;
        Long parentID = !ObjectUtils.isEmpty(paramDto.getParentID()) && paramDto.getParentID() > 0 ? paramDto.getParentID() : 0L ;
        group.setParentID(parentID);
        group.setParentLevel(",0,");

        IOSource source = new IOSource();
        source.setParentID(0L);
        source.setParentLevel(",0,");

        if (parentID > 0){
            GroupVo parentGroup = groupDao.getGroupAndSourceByID(parentID);
            if (ObjectUtils.isEmpty(parentGroup)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
           // parentSequenceLength = parentGroup.getGroupSequence().length();
            group.setParentLevel(parentGroup.getParentLevel() + parentGroup.getGroupID() + ",");

            source.setParentID(parentGroup.getSourceID());
            source.setParentLevel(parentGroup.getSourceParentLevel() + parentGroup.getSourceID() + ",");
        }
        Integer sort = groupDao.getMaxSort(parentID);
        sort = ObjectUtils.isEmpty(sort) ? 0 : sort + 1;

        group.setSort(sort);
        try {
            groupDao.insert(group);
        }catch (Exception e){
            LogUtil.error(e, prefix + " addGroup error " + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }

        // 新增部门时新增 source
        source.setName(group.getName());
        source.setCreateUser(loginUser.getUserID());
        source.setModifyUser(loginUser.getUserID());
        source.setTargetID(loginUser.getUserID());
        source.setIsFolder(1);
        source.setTargetType(2);
        source.setFileType("");
        source.setFileID(0L);
        source.setType(0);
        source.setThumbSize(0L);
        source.setConvertSize(0L);
        source.setStorageID(storageService.getDefaultStorageDeviceId());
        source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
        source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
        try {
            ioSourceDao.insert(source);

            groupSourceDao.insert(new GroupSource(group.getGroupID(), source.getSourceID()));
            ioSourceMetaDao.insert(new IOSourceMeta(source.getSourceID(), "groupSource", group.getGroupID() + ""));
        }catch (Exception e){
            LogUtil.error(e, " addGroup setGroupDefaultSource error");
        }

        if (!ObjectUtils.isEmpty(source.getSourceID()) && source.getSourceID() > 0) {
            List<IOSourceMeta> pyList = new ArrayList<>();
            pyList.add(new IOSourceMeta(source.getSourceID(), MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(source.getName())));
            pyList.add(new IOSourceMeta(source.getSourceID(), MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(source.getName())));

            if (!CollectionUtils.isEmpty(pyList)) {
                // 拼音
                try {
                    ioSourceMetaDao.batchInsert(pyList);
                } catch (Exception e) {
                    LogUtil.error(e, " addGroup setUserDefaultSource setSourcePinYin meta error pyList=" + JsonUtils.beanToJson(pyList));
                }
            }
        }
    }

    private Group changeGroupParam(GroupDTO paramDto){
        Group group = new Group();
        group.setGroupID(!ObjectUtils.isEmpty(paramDto.getGroupID()) ? paramDto.getGroupID() : null);
        group.setParentID(!ObjectUtils.isEmpty(paramDto.getParentID()) ? paramDto.getParentID() : 0L);
        group.setName(paramDto.getName());
        group.setSizeMax(!ObjectUtils.isEmpty(paramDto.getSizeMax()) ? paramDto.getSizeMax() : 0);
        group.setSizeMax(group.getSizeMax() < 0 ? 0 : group.getSizeMax());

        return group;
    }

    private void checkGroupParam(GroupDTO paramDto){
        //必填项目
        if (ObjectUtils.isEmpty(paramDto.getName())){
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerNotNull.getCode());
        }
        if (!ObjectUtils.isEmpty(paramDto.getName()) && paramDto.getName().length() > 255){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

    }
    @Override
    public void setGroupStatus(String prefix, LoginUser loginUser, GroupDTO paramDto){
        if (ObjectUtils.isEmpty(paramDto.getGroupID()) || ObjectUtils.isEmpty(paramDto.getStatus()) ){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!Arrays.asList(0,1,2).contains(paramDto.getStatus())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        long systemGroupID = groupMetaDao.getSystemGroup();
        if (systemGroupID == paramDto.getGroupID().longValue()){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            groupDao.updateGroupState(paramDto);
        }catch (Exception e){
            LogUtil.error(e, prefix + " 删除部门  error" + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        if (2 == paramDto.getStatus()){
            userGroupDao.delByGroupID(paramDto.getGroupID());
        }
    }
    @Override
    public void setGroupSort(String prefix, LoginUser loginUser, GroupDTO paramDto){
        if (ObjectUtils.isEmpty(paramDto.getIds())){
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<Long> groupIds = Arrays.asList(paramDto.getIds().split(",")).stream().map(Long::valueOf).collect(Collectors.toList());

        int sort = 0;
        List<Group> groupList = new ArrayList<>();
        for (Long id : groupIds){
            groupList.add(new Group(id, sort));
            sort ++ ;
        }
        try {
            groupDao.updateGroupSort(groupList);
        }catch (Exception e){
            LogUtil.error(e, prefix + " 权限排序 error " + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }

    @Override
    public Long sumGroupSpaceUsed() {
        return groupDao.sumGroupSpaceUsed();
    }

    @Override
    public void syncGroupSize(String prefix, LoginUser loginUser, GroupDTO paramDto){
        if (1 != loginUser.getUserType()){
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
        }
        Long groupID = (!ObjectUtils.isEmpty(paramDto.getGroupID()) && paramDto.getGroupID() > 0 ) ? paramDto.getGroupID() : 0L;
        List<GroupSizeVo> list = groupDao.getGroupSizeList(groupID);
        if (!CollectionUtils.isEmpty(list)){
            for (List<GroupSizeVo> subList : Partition.ofSize(list, 2000)) {
                try {
                    groupDao.syncBatchUpdateGroupMemoryList(subList);
                }catch (Exception e){
                    LogUtil.error(e, "syncGroupSize error");
                }
            }

        }

    }
}
