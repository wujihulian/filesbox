package com.svnlan.home.dao;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.IoSourceHistoryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/24 17:07
 */
public interface IoSourceHistoryDao {

    int insert(IoSourceHistory ioSourceHistory);
    int batchInsert(List<IoSourceHistory> list);
    List<HomeExplorerVO> getSourceHistoryBySourceID(Map<String, Object> hashMap);
    Long getCountSourceHistoryBySourceID(Map<String, Object> hashMap);
    int updateDetail(@Param("id") Long id, @Param("detail") String detail);
    int updateVerDetail(IoSourceHistory ioSourceHistory);
    int updateVerSource(IoSourceHistory ioSourceHistory);
    IoSourceHistory getFileInfoBySourceID(@Param("sourceID") Long sourceID);
    CommonSource getHistorySourceInfo(Long id);
    int delByID(@Param("id") Long id);
    int delBySourceID(@Param("sourceID") Long sourceID);
    IoSourceHistory getHistoryInfo(@Param("id") Long id);
    IoSourceHistory getHistoryInfoByFileId(@Param("sourceID") Long sourceID, @Param("fileID") Long fileID);
    int updateSize(@Param("id") Long id, @Param("size") Long size, @Param("detail") String detail);
}
