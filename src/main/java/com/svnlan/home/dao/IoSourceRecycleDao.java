package com.svnlan.home.dao;

import com.svnlan.home.domain.IOSource;
import com.svnlan.home.domain.IoSourceRecycle;
import com.svnlan.home.dto.SourceOpDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/18 16:44
 */
public interface IoSourceRecycleDao {

    int insert(IoSourceRecycle ioSourceRecycle);
    int batchInsert(List<IOSource> list);
    int deleteUserRecycle(@Param("userID")Long userID, @Param("targetType") Integer targetType, @Param("list") List<Long> list);
    List<SourceOpDto> getUserRecycleBinList(Long userID);
}
