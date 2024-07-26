package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.domain.Group;
import com.svnlan.user.dto.GroupDTO;
import com.svnlan.user.vo.GroupVo;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/10 11:34
 */
public interface GroupService {

    List getGroupList(LoginUser loginUser, GroupDTO paramDto);
    List searchGroupList(LoginUser loginUser, GroupDTO paramDto);
    void editGroup(String prefix, LoginUser loginUser, GroupDTO paramDto);
    void setGroupStatus(String prefix, LoginUser loginUser, GroupDTO paramDto);
    void setGroupSort(String prefix, LoginUser loginUser, GroupDTO paramDto);
    void syncGroupSize(String prefix, LoginUser loginUser, GroupDTO paramDto);
    GroupVo addGroup(String prefix, Group group, Long parentID, Long targetId, Long tenantId, Integer storageId, Integer sort, GroupVo parentGroup);
    GroupVo addGroupAll(String prefix, Group group, Long parentID,Long targetId,Long tenantId,Integer storageId
            ,Integer sort,GroupVo parentGroup);
    Long sumGroupSpaceUsed();
}
