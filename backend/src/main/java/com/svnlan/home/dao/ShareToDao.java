package com.svnlan.home.dao;

import com.svnlan.home.domain.ShareTo;
import com.svnlan.home.vo.ShareToVo;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/8 13:11
 */
public interface ShareToDao {
    int batchInsert(List<ShareTo> list);
    int delete(Long shareID);
    int deleteList(List<Long> list);
    List<ShareToVo> getShareToList(Long shareID);
}
