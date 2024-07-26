package com.svnlan.home.dao;

import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.SourceOpDto;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/18 16:44
 */
public interface IoSourceRecycleDao {

    int batchInsert(List<IOSource> list);
    int deleteUserRecycle(Long userID,  Integer targetType, List<Long> list);
    List<SourceOpDto> getUserRecycleBinList(Long userID);
}
