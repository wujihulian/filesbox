package com.svnlan.home.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOFileMeta;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/16 13:57
 */
public interface IoFileDao extends BaseMapper<IOFile> {

    CommonSource getFileAttachment(Long sourceID);

    int insert(IOFile ioFile);
    int batchInsert(List<IOFile> list);
    int insertMeta(IOFileMeta ioFile);
    int batchInsertMeta(List<IOFileMeta> list);
    List<Long> getSameSourceEmptyInfo(@Param("hashMd5") String hashMd5, @Param("size") Long size);
    int updateVideoConvert(CommonSource commonSource);
    int updateSameFile(@Param("commonSource") CommonSource commonSource, @Param("list") List<Long> list);
    int updateDocConvert(CommonSource commonSource);

    List<Long> getSameSourceEmptyInfoDoc(@Param("hashMd5") String hashMd5, @Param("size") Long size);
    int updateSameFileSwf(@Param("map") Map<String, Object> updateMap, @Param("list") List<Long> list);

    void updateSameFileDoc(@Param("map") Map<String, Object> docPath, @Param("list") List<Long> list);
    CommonSource selectByChecksum(@Param("hashMd5") String hashMd5, @Param("size") Long size);
    CommonSource selectByChecksumAndTime(@Param("hashMd5") String hashMd5, @Param("size") Long size, @Param("time") Long time);

    int removeUserFile(List<Long> list);
    int removeUserFileMeta(List<Long> list);
    int removeUserFileContents(List<Long> list);
    String getFileUrlValue(Long fileID);
    List<IOFileMeta> getFileUrlValueList(List<Long> list);
    int updateFileUrlValue(List<IOFileMeta> list);
    int updateOneFileUrlValue(@Param("fileID") Long fileID, @Param("value") String value);
    int updateAudioConvert(CommonSource commonSource);
    int updateCameraConvert(CommonSource commonSource);
    IOFileMeta getFileValue(Long sourceID);

    CommonSource findCommonSourceById(Long sourceID);
    int deleteFileOrgPath(Long fileID);
    CommonSource getHistoryFileAttachment(@Param("id") Long id);

    @Select("SELECT `name`, `path`, size, modifyTime, createTime FROM io_file WHERE fileID = #{id} AND isExistFile = 1")
    IOFile getFileByFieldId(@Param("id") Long fileID);
    int updateFileMd5ByPath(@Param("hashMd5") String hashMd5, @Param("path") String path);
    CommonSource getHistoryFileAttachmentUrl(@Param("id") Long id);
    CommonSource getFileAttachmentUrl(Long sourceID);
    int updateFileMd5AndSizeByFileID(@Param("hashMd5") String hashMd5, @Param("fileID") Long fileID, @Param("size") Long size);

    int updateVideoFilePath(@Param("fileID") Long fileID, @Param("path") String path);

    int updateH264Info(CommonSource commonSource);

    @Select("SELECT SUM(size) FROM io_file WHERE path like CONCAT(#{location},'%')")
    Long selectStorageUsage(String location);

    @Update("UPDATE io_file SET size = #{size} WHERE fileID = #{fileID}")
    void updateFileSize(@Param("fileID") Long fileID, @Param("size") long size);
}
