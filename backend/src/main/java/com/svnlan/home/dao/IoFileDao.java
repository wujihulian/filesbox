package com.svnlan.home.dao;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOFileMeta;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 13:57
 */
public interface IoFileDao {

    CommonSource getFileAttachment(Long sourceID);

    int insert(IOFile ioFile);

    int insertMeta(IOFileMeta ioFile);

    List<Long> getSameSourceEmptyInfo(String hashMd5, Long size);

    int updateVideoConvert(CommonSource commonSource);

    int updateSameFile(CommonSource commonSource, List<Long> list);

    int updateDocConvert(CommonSource commonSource);

    List<Long> getSameSourceEmptyInfoDoc(String hashMd5, Long size);

    int updateSameFileSwf(Map<String, Object> updateMap, List<Long> list);

    void updateSameFileDoc(Map<String, Object> docPath, List<Long> list);

    CommonSource selectByChecksum(String hashMd5, Long size);

    CommonSource selectByChecksumAndTime(String hashMd5, Long size, LocalDateTime time);

    int removeUserFile(List<Long> list);

    int removeUserFileMeta(List<Long> list);

    int removeUserFileContents(List<Long> list);

    String getFileUrlValue(Long fileID);

    List<IOFileMeta> getFileUrlValueList(List<Long> list);

    int updateFileUrlValue(List<IOFileMeta> list);

    int updateOneFileUrlValue(Long fileID, String value);

    int updateAudioConvert(CommonSource commonSource);

    int updateCameraConvert(CommonSource commonSource);

    IOFileMeta getFileValue(Long sourceID);

    CommonSource findCommonSourceById(Long sourceID);

    int deleteFileOrgPath(Long fileID);

    CommonSource getHistoryFileAttachment(Long id);

    int updateFileMd5ByPath(String hashMd5, String path);

    int updateFileMd5AndSizeByFileID(String hashMd5, Long fileID, Long size);


    int updateH264Info(CommonSource commonSource);

    //    @Select("SELECT SUM(size) FROM io_file WHERE path like CONCAT(#{location},'%')")
    Long selectStorageUsage(String location);

    IOFile selectById(Long fileID);

    List<IOFile> selectBatchIds(Set<Long> keySet);

    void deleteById(Long fileId);

    void updateById(IOFile ioFile);

    IOFile selectOneByPathLike(String path);

    List<IOFile> selectPathList(List<Long> fileIdList);
}
