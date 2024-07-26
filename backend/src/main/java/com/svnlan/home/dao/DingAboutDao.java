package com.svnlan.home.dao;

import com.svnlan.user.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/10/20 13:30
 */
public interface DingAboutDao {

    List<UserVo> getUserInfoByUnionId(String unionId);

    Map<Long,List<UserVo>> getDingUsers(Long tenantId);
}
