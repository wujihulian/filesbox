package com.svnlan.home.dao;

import com.svnlan.home.domain.IOSource;
import com.svnlan.home.vo.HomeExplorerShareDetailVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author KingMgg
 * @data 2023/2/7 16:13
 */
public interface ExplorerOperationsDao {

    List<HomeExplorerShareDetailVO> getAllSourceList(Map<String, Object> paramMap);
    int batchUpdateLevel(List<HomeExplorerShareDetailVO> list);
    List<IOSource> getSourceListByLevelToContSize(@Param("parentLevel") String parentLevel, @Param("status") Integer status);
    int batchUpdateSizeByCountSize(List<IOSource> list);




}
