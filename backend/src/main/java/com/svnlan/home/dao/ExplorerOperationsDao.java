package com.svnlan.home.dao;

import com.svnlan.home.domain.IOSource;
import com.svnlan.home.vo.HomeExplorerShareDetailVO;

import java.util.List;
import java.util.Map;

/**
 * @author KingMgg
 * @data 2023/2/7 16:13
 */
public interface ExplorerOperationsDao {

    List<HomeExplorerShareDetailVO> getAllSourceList(Map<String, Object> paramMap);
    // 已弃用
    int batchUpdateLevel(List<HomeExplorerShareDetailVO> list);
    List<IOSource> getSourceListByLevelToContSize(String parentLevel,Integer status);
    void batchUpdateSizeByCountSize(List<IOSource> list);




}
