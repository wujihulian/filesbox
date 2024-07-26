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
import com.svnlan.utils.TenantUtil;
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
    IoSourceMetaDao ioSourceMetaDaoImpl;

    @Resource
    GroupDao groupDaoImpl;
    @Resource
    GroupMetaDao groupMetaDao;

    @Resource
    GroupMetaDao groupMetaDaoImpl;
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
    @Resource
    TenantUtil tenantUtil;

    @Override
    public List getGroupList(LoginUser loginUser, GroupDTO paramDto) {
        paramDto.setParentID(ObjectUtils.isEmpty(paramDto.getParentID()) ? 0 : paramDto.getParentID());
        paramDto.setStatus(ObjectUtils.isEmpty(paramDto.getStatus()) ? null : paramDto.getStatus());

        long systemGroupID = 0;
        if (paramDto.getParentID().longValue() <= 0) {
            systemGroupID = groupMetaDaoImpl.getSystemGroup();
        }

        List<Group> groupList = groupDaoImpl.getGroupList(paramDto);
        if (CollectionUtils.isEmpty(groupList)) {
            return new ArrayList();
        }
        return getAuthShowList(groupList, systemGroupID);
    }

    @Override
    public List searchGroupList(LoginUser loginUser, GroupDTO paramDto) {
        paramDto.setParentID(ObjectUtils.isEmpty(paramDto.getParentID()) ? 0 : paramDto.getParentID());
        List<Group> groupList = groupDaoImpl.searchGroupList(paramDto);
        if (CollectionUtils.isEmpty(groupList)) {
            return new ArrayList();
        }
        long systemGroupID = 0;
        if (paramDto.getParentID().longValue() <= 0) {
            systemGroupID = groupMetaDaoImpl.getSystemGroup();
        }
        return getAuthShowList(groupList, systemGroupID);
    }

    private List<Group> getAuthShowList(List<Group> groupList, long systemGroupID) {
        List<Long> groupIdList = new ArrayList<>();
        Set<Long> pLevelList = new HashSet<>();
        List<String> parent_levels = new ArrayList<>();
        for (Group g : groupList) {
            groupIdList.add(g.getGroupID());
            if (g.getParentID() > 0) {
                String parent_level = g.getParentLevel();
                parent_levels.add(parent_level);
                String[] splits = parent_level.split(",");
                for (String split : splits) {
                    if (!split.equals("")) {
                        long groupId = Long.parseLong(split);
                        if (groupId != 0) {
                            pLevelList.add(groupId);
                        }
                    }
                }
            }
        }
        Map maps = new HashMap();
        maps.put("ids",new ArrayList<>(pLevelList));
        maps.put("parent_level",parent_levels);
        Map<Long, String> parentPathDisplayMap = null;
        if (!CollectionUtils.isEmpty(maps)) {
            parentPathDisplayMap = groupTool.getParentPathDisplayMap(maps);
        }
        List<GroupMeta> metaList = groupMetaDaoImpl.getGroupMetaList(groupIdList, Arrays.asList("authShowType", "authShowGroup"));
        Map<Long, Map<String, String>> map = new HashMap<>(groupIdList.size());
        if (!CollectionUtils.isEmpty(metaList)) {
            Map<String, String> reMap = null;
            for (GroupMeta meta : metaList) {
                if (!ObjectUtils.isEmpty(map) && map.containsKey(meta.getGroupID())) {
                    reMap = map.get(meta.getGroupID());
                } else {
                    reMap = new HashMap<>(1);
                }
                reMap.put(meta.getKey(), meta.getValue());
                map.put(meta.getGroupID(), reMap);
            }

            for (Group group : groupList) {
                if (!ObjectUtils.isEmpty(map) && map.containsKey(group.getGroupID())) {
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
                if (systemGroupID == group.getGroupID().longValue()) {
                    group.setIsSystem(1);
                }
            }
        }
        return groupList;
    }

    @Override
    public void editGroup(String prefix, LoginUser loginUser, GroupDTO paramDto) {
        /** 校验参数 */
        checkGroupParam(paramDto);

        /** 转换数据*/
        Group group = changeGroupParam(paramDto);

        Long tenantId = tenantUtil.getTenantIdByServerName();
        /** 添加部门 */
        if (ObjectUtils.isEmpty(paramDto.getGroupID()) || paramDto.getGroupID() <= 0 ){
            Long parentID = !ObjectUtils.isEmpty(paramDto.getParentID()) && paramDto.getParentID() > 0 ? paramDto.getParentID() : 0L;
            paramDto.setParentID(parentID);
            Integer sort = groupDaoImpl.getMaxSort(parentID);
            GroupVo parentGroup = null;
            if (parentID > 0) {
                parentGroup = groupDaoImpl.getGroupAndSourceByID(parentID);
            }
            sort = ObjectUtils.isEmpty(sort) ? 0 : sort + 1;
            addGroup(prefix, group, parentID, loginUser.getUserID(), tenantId, storageService.getDefaultStorageDeviceId(), sort, parentGroup);
        } else {
            updateGroup(prefix, group, paramDto);
            // 先删除meta
            groupMetaDao.delMetaByGroupID(paramDto.getGroupID(), GroupMetaEnum.delKeyList());
        }

        addGroupMetaDefault(prefix, group,tenantId);
    }

    public void updateGroup(String prefix, Group group, GroupDTO paramDto) {
        // 查看是否更换上级部门
        GroupVo oldGroup = groupDaoImpl.getGroupAndSourceByID(paramDto.getGroupID());

        Map<String, Object> paramMap = new HashMap<>(2);
        if (!ObjectUtils.isEmpty(paramDto.getParentID()) && oldGroup.getParentID().longValue() != paramDto.getParentID().longValue()) {
            if (paramDto.getParentID() > 0) {
                GroupVo parentGroup = groupDaoImpl.getGroupAndSourceByID(paramDto.getParentID());
                if (ObjectUtils.isEmpty(parentGroup)) {
                    throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
                }
                group.setParentLevel(parentGroup.getParentLevel() + parentGroup.getGroupID() + ",");

                paramMap.put("parentID", parentGroup.getSourceID());
                paramMap.put("parentLevel", parentGroup.getSourceParentLevel() + parentGroup.getSourceID() + ",");
            }else {
                group.setParentLevel(",0,");
            }
            // parentSequenceLength = parentGroup.getGroupSequence().length();
            Integer sort = groupDaoImpl.getMaxSort(paramDto.getParentID());
            sort = ObjectUtils.isEmpty(sort) ? 0 : sort + 1;
            group.setSort(sort);
            group.setParentID(paramDto.getParentID());
        }

        try {
            groupDaoImpl.update(group);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " updateGroup error " + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }
        try {
            paramMap.put("name", group.getName());
            paramMap.put("sourceID", oldGroup.getSourceID());
            groupDaoImpl.updateGroupSourceName(paramMap);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " updateGroup updateGroupSourceName error " + JsonUtils.beanToJson(paramDto));
        }

    }

    @Override
    public GroupVo addGroup(String prefix, Group group, Long parentID,Long targetId,Long tenantId,Integer storageId,Integer sort,GroupVo parentGroup) {
        // int parentSequenceLength = 0;

        group.setParentID(parentID);
        group.setParentLevel(",0,");
        IOSource source = new IOSource();
        source.setParentId(0L);
        source.setParentLevel(",0,");

        if (parentID > 0) {
            if (ObjectUtils.isEmpty(parentGroup)) {
                throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
            }
            // parentSequenceLength = parentGroup.getGroupSequence().length();
            group.setParentLevel(parentGroup.getParentLevel() + parentGroup.getGroupID() + ",");
            source.setParentId(parentGroup.getSourceID());
            source.setParentLevel(parentGroup.getSourceParentLevel() + parentGroup.getSourceID() + ",");
        }


        group.setSort(sort);
        try {
            groupDaoImpl.insert(group, null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " addGroup error " + JsonUtils.beanToJson(group));
            throw new SvnlanRuntimeException(CodeMessageEnum.saveError.getCode());
        }

        // 新增部门时新增 source
        source.setName(group.getName());
        source.setCreateUser(0L);
        source.setModifyUser(0L);
        source.setTargetId(targetId);
        source.setIsFolder(1);
        source.setTargetType(2);
        source.setFileType("");
        source.setFileId(0L);
        source.setType(0);
        source.setThumbSize(0L);
        source.setConvertSize(0L);
        source.setStorageId(storageId);
        source.setTenantId(tenantId);
        source.setNamePinyin(ChinesUtil.getPingYin(source.getName()));
        source.setNamePinyinSimple(ChinesUtil.getFirstSpell(source.getName()));
        try {
            ioSourceDao.insert(source);

            groupSourceDao.insert(new GroupSource(group.getGroupID(), source.getId(), tenantId));
            ioSourceMetaDao.insert(new IOSourceMeta(source.getId(), "groupSource", group.getGroupID() + ""));
        } catch (Exception e) {
            LogUtil.error(e, " addGroup setGroupDefaultSource error");
        }

        if (!ObjectUtils.isEmpty(source.getId()) && source.getId() > 0) {
            List<IOSourceMeta> pyList = new ArrayList<>();
            pyList.add(new IOSourceMeta(source.getId(), MetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(source.getName()), tenantId));
            pyList.add(new IOSourceMeta(source.getId(), MetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(source.getName()), tenantId));

            if (!CollectionUtils.isEmpty(pyList)) {
                // 拼音
                try {
                    ioSourceMetaDaoImpl.batchInsert(pyList);
                } catch (Exception e) {
                    LogUtil.error(e, " addGroup setUserDefaultSource setSourcePinYin meta error pyList=" + JsonUtils.beanToJson(pyList));
                }
            }
        }
        GroupVo groupVo = new GroupVo();
        groupVo.setGroupID(group.getGroupID());
        groupVo.setParentID(group.getParentID());
        groupVo.setSourceID(source.getId());
        groupVo.setParentLevel(group.getParentLevel());
        groupVo.setSourceParentID(source.getParentId());
        groupVo.setSourceParentLevel(source.getParentLevel());
        return groupVo;
    }

    private Group changeGroupParam(GroupDTO paramDto) {
        Group group = new Group();
        group.setGroupID(!ObjectUtils.isEmpty(paramDto.getGroupID()) ? paramDto.getGroupID() : null);

        group.setName(paramDto.getName());
        group.setSizeMax(!ObjectUtils.isEmpty(paramDto.getSizeMax()) ? paramDto.getSizeMax() : 0);
        group.setSizeMax(group.getSizeMax() < 0 ? 0 : group.getSizeMax());

        return group;
    }

    private void checkGroupParam(GroupDTO paramDto) {
        //必填项目
        if (ObjectUtils.isEmpty(paramDto.getName())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.explorerNotNull.getCode());
        }
        if (!ObjectUtils.isEmpty(paramDto.getName()) && paramDto.getName().length() > 255) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }

    }

    @Override
    public void setGroupStatus(String prefix, LoginUser loginUser, GroupDTO paramDto) {
        if (ObjectUtils.isEmpty(paramDto.getGroupID()) || ObjectUtils.isEmpty(paramDto.getStatus())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        if (!Arrays.asList(0, 1, 2).contains(paramDto.getStatus())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        long systemGroupID = groupMetaDaoImpl.getSystemGroup();
        if (systemGroupID == paramDto.getGroupID().longValue()) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        try {
            groupDaoImpl.updateGroupState(paramDto);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " 删除部门  error" + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
        if (2 == paramDto.getStatus()) {
            userGroupDao.delByGroupID(paramDto.getGroupID());
        }
    }

    @Override
    public void setGroupSort(String prefix, LoginUser loginUser, GroupDTO paramDto) {
        if (ObjectUtils.isEmpty(paramDto.getIds())) {
            throw new SvnlanRuntimeException(CodeMessageEnum.shareErrorParam.getCode());
        }
        List<Long> groupIds = Arrays.asList(paramDto.getIds().split(",")).stream().map(Long::valueOf).collect(Collectors.toList());

        int sort = 0;
        List<Group> groupList = new ArrayList<>();
        for (Long id : groupIds) {
            groupList.add(new Group(id, sort));
            sort++;
        }
        try {
            groupDaoImpl.updateGroupSort(groupList);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " 权限排序 error " + JsonUtils.beanToJson(paramDto));
            throw new SvnlanRuntimeException(CodeMessageEnum.system_error.getCode());
        }
    }

    @Override
    public Long sumGroupSpaceUsed() {
        return groupDaoImpl.sumGroupSpaceUsed();
    }

    @Override
    public void syncGroupSize(String prefix, LoginUser loginUser, GroupDTO paramDto){
        if (1 != loginUser.getUserType()){
            throw new SvnlanRuntimeException(CodeMessageEnum.noPermissionAction.getCode());
        }
        Long groupID = (!ObjectUtils.isEmpty(paramDto.getGroupID()) && paramDto.getGroupID() > 0 ) ? paramDto.getGroupID() : 0L;
        List<GroupSizeVo> list = groupDaoImpl.getGroupSizeList(groupID);
        if (!CollectionUtils.isEmpty(list)){
            for (List<GroupSizeVo> subList : Partition.ofSize(list, 2000)) {
                try {
                    groupDaoImpl.syncBatchUpdateGroupMemoryList(subList);
                }catch (Exception e){
                    LogUtil.error(e, "syncGroupSize error");
                }
            }

        }

    }


    @Override
    public GroupVo addGroupAll(String prefix, Group group, Long parentID,Long targetId,Long tenantId,Integer storageId
            ,Integer sort,GroupVo parentGroup) {
        GroupVo add = this.addGroup(prefix, group, parentID,targetId,tenantId,storageId,sort, parentGroup);
        addGroupMetaDefault(prefix, group,tenantId);
        return add;
    }

    public void addGroupMetaDefault(String prefix, Group group,Long tenantId) {
        if (!ObjectUtils.isEmpty(group.getGroupID())) {
            List<GroupMeta> paramList = new ArrayList<>();

            paramList.add(new GroupMeta(group.getGroupID(), GroupMetaEnum.namePinyin.getValue(), ChinesUtil.getPingYin(group.getName()), tenantId));
            paramList.add(new GroupMeta(group.getGroupID(), GroupMetaEnum.namePinyinSimple.getValue(), ChinesUtil.getFirstSpell(group.getName()), tenantId));
            paramList.add(new GroupMeta(group.getGroupID(), GroupMetaEnum.authShowType.getValue(), ObjectUtils.isEmpty(group.getAuthShowType()) ? "all" : group.getAuthShowType(), tenantId));
            if (ObjectUtils.isEmpty(group.getAuthShowType()) || !"select".equals(group.getAuthShowType())) {
                group.setAuthShowGroup("");
            }
            paramList.add(new GroupMeta(group.getGroupID(), GroupMetaEnum.authShowGroup.getValue(), ObjectUtils.isEmpty(group.getAuthShowGroup()) ? "" : group.getAuthShowGroup(), tenantId));

            try {
                groupMetaDao.batchInsert(paramList);
            } catch (Exception e) {
                LogUtil.error(e, prefix + " batchInsert error " + JsonUtils.beanToJson(group));
            }
        }
    }
}
