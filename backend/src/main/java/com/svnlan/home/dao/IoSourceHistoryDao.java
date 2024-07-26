package com.svnlan.home.dao;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IoSourceHistory;
import com.svnlan.home.vo.HomeExplorerVO;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/24 17:07
 */
public interface IoSourceHistoryDao {

    int insert(IoSourceHistory ioSourceHistory);

    List<HomeExplorerVO> getSourceHistoryBySourceID(Map<String, Object> hashMap);

    Long getCountSourceHistoryBySourceID(Map<String, Object> hashMap);

    int updateDetail(Long id, String detail);

    int updateVerSource(IoSourceHistory ioSourceHistory);

    IoSourceHistory getFileInfoBySourceID(Long sourceID);

    CommonSource getHistorySourceInfo(Long id);

    int delByID(Long id);

    int delBySourceID(Long sourceID);

    IoSourceHistory getHistoryInfo(Long id);
    IoSourceHistory getHistoryInfoByFileId(Long sourceID, Long fileID);
    int updateSize(Long id, Long size, String detail);

}
