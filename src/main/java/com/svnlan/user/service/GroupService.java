package com.svnlan.user.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.user.dto.GroupDTO;

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

    Long sumGroupSpaceUsed();
}
