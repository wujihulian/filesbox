package com.svnlan.user.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.user.domain.Group;
import com.svnlan.user.dto.GroupDTO;
import com.svnlan.user.vo.GroupParentPathDisplayVo;
import com.svnlan.user.vo.GroupSizeVo;
import com.svnlan.user.vo.GroupVo;
import com.svnlan.user.vo.UserGroupVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 15:45
 */
public interface GroupDao extends BaseMapper<Group> {

    int insert(Group group);
    int update(Group group);
    int updateGroupSourceName(Map<String, Object> map);
    List<Group> getGroupList(GroupDTO groupDTO);
    List<Group> searchGroupList(GroupDTO groupDTO);
    int updateGroupState(GroupDTO groupDTO);
    Integer updateGroupSort(List<Group> list);
    Group getGroupByID(Long groupID);
    Group getTopGroup();
    Integer getMaxSort(Long parentID);
    List<Group> getGroupInfoList(@Param("list") List<Long> list);
    GroupVo getGroupAndSourceByID(Long groupID);
    List<UserGroupVo> getGroupVoList(@Param("list") List<Long> list);
    List<UserGroupVo> getMainGroupVoList(HomeExplorerDTO shareDTO);

    @Select("SELECT SUM(sizeUse) FROM `group`")
    Long sumGroupSpaceUsed();

    Group getGroupInfoByID(Long groupID);
    List<Group> getGroupUserCountList(List<Long> list);

    List<JSONObject> getNameByIds(@Param("groupIds") List<Long> groupIds);

    List<Long> getUserIdByGroupId(@Param("deptIdList") List<Long> deptIdList);

    @Select("SELECT groupID FROM user_group WHERE userID = #{userId}")
    List<Long> getGroupIdByUserId(@Param("userId") Long userId);

    @Select("SELECT groupID AS gid, name n, sizeUse s FROM `group` WHERE status IN (0, 1)")
    List<JSONObject> selectGroupSpaceUsage();
    List<GroupParentPathDisplayVo> getGroupParentPathDisplay(List<String> list);

    int syncBatchUpdateGroupMemoryList(@Param("list") List<GroupSizeVo> list);
    List<GroupSizeVo> getGroupSizeList(@Param("groupID") Long groupID);
}
