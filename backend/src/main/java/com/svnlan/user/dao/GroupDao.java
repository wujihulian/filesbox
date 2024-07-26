package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.user.domain.Group;
import com.svnlan.user.dto.GroupDTO;
import com.svnlan.user.vo.GroupParentPathDisplayVo;
import com.svnlan.user.vo.GroupSizeVo;
import com.svnlan.user.vo.GroupVo;
import com.svnlan.user.vo.UserGroupVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 15:45
 */
public interface GroupDao {

    int insert(Group group, Long tenantId);

    int update(Group group);

    int updateGroupSourceName(Map<String, Object> map);

    List<Group> getGroupList(GroupDTO groupDTO);

    List<Group> searchGroupList(GroupDTO groupDTO);

    int updateGroupState(GroupDTO groupDTO);

    Integer updateGroupSort(List<Group> list);

    Group getTopGroup();

    Integer getMaxSort(Long parentID);

    List<Group> getGroupInfoList(List<Long> list);

    GroupVo getGroupAndSourceByID(Long groupID);

    List<UserGroupVo> getGroupVoList(List<Long> list);

    List<UserGroupVo> getMainGroupVoList(HomeExplorerDTO shareDTO);

    //    @Select("SELECT SUM(sizeUse) FROM groups")
    Long sumGroupSpaceUsed();

    Group getGroupInfoByID(Long groupID);

    List<Group> getGroupUserCountList(List<Long> list);


    List<Long> getUserIdByGroupId(List<Long> deptIdList);

    //    @Select("SELECT groupID FROM user_group WHERE userID = #{userId}")
    List<Long> getGroupIdByUserId(Long userId);

    //    @Select("SELECT groupID AS gid, name n, sizeUse s FROM groups WHERE status IN (0, 1)")
    List<JSONObject> selectGroupSpaceUsage(Long tenantId);

    List<GroupParentPathDisplayVo> getGroupParentPathDisplay(Map<String, Object> map);

    List<Object> selectParentLevel(List<Long> ids);

    void syncBatchUpdateGroupMemoryList(List<GroupSizeVo> list);
    List<GroupSizeVo> getGroupSizeList(Long groupID);
    List<Long> getDingDeptIds(Long tenantId, String tag);
    List<Group> getThirdGroupParent(String tag, Long groupId,String name);
    int updateGroupThirdTag(String tag, Long groupId);
    GroupVo getGroupAndSourceByDeptId(Long tenantId, Long deptId, String tag);
    GroupVo getTopGroupByTenantId(Long groupID);
    List<String> getThirdDeptIds(Long tenantId, String tag);
    GroupVo getGroupAndSourceByDingDeptId(Long tenantId, Long deptId, String tag);
}
