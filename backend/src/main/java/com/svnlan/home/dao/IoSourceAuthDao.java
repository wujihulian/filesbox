package com.svnlan.home.dao;

import com.svnlan.home.domain.IoSourceAuth;
import com.svnlan.home.vo.IoSourceAuthVo;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/11 17:21
 */
public interface IoSourceAuthDao {

    int batchInsert(List<IoSourceAuth> list);

    int deleteSourceAuth(Long sourceID);

    List<IoSourceAuthVo> getSourceAuthBySourceID(Long sourceID);

    List<IoSourceAuthVo> getGroupNameListByGID(List<Long> list);

    List<IoSourceAuthVo> getSourceAuthBySourceIDList(List<Long> list, Long targetID);
}
