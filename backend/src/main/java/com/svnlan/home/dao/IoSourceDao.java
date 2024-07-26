package com.svnlan.home.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.home.vo.ParentPathDisplayVo;
import org.springframework.context.annotation.Primary;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 11:13
 */
@Primary
public interface IoSourceDao {

    int insert(IOSource source);

    int batchInsert(List<IOSource> list);

    CommonSource getSourceInfo(Long sourceID);

    List<CommonSource> getSourceInfoList(List<Long> list);

    List<IOSource> copySourceList(List<Long> list);

    List<IOSource> copySourceListByLevel(List<String> list);

    int deleteDirOrFile(List<Long> list);

    int deleteSourceByParent(List<String> list, Long tenantId);

    int restoreSourceByParent(List<String> list, Long userID);

    void batchFileRename(List<SourceOpDto> list, Long userID);

    void batchUpdateParent(List<IOSource> list);

    int restoreDirOrFile(List<Long> list, Long userID);

    void restoreDirOrFileAndName(List<IOSource> list, Long userID);

    int removeUserSource(List<Long> list);

    int removeUserSourceByParent(List<String> list);

    List<Long> getFileIDBySourceID(List<Long> list);

    List<IOSourceVo> getFileCountBySourceID(List<Long> list);

    int updateSourceMemoryList(List<Long> list, Long memory);

    int subtractSourceMemoryList(List<Long> list, Long memory);

    void batchUpdateSourceMemoryList(List<IOSource> list);

    void batchSubtractSourceMemoryList(List<IOSource> list);

    Integer getMaxSort(Long parentID);

    int updateSort(Long sourceID, Integer sort);

    int updateSourceInfo(IOSource source);

    List<IOSourceVo> copySourcePathList(List<Long> list);

    List<IOSourceVo> copySourcePathListByLevel(List<String> list);

    List<CommonSource> getSourceFileInfoList(List<Long> list);

    int updateSourceModifyUser(IOSource source);

    int updateFileSize(IOSource source);

    List<String> getSourceNameList(Long sourceID);

    List<CommonSource> checkSourceNameList(Long sourceID);

    List<JSONObject> fileCount();

    //    @Select("SELECT COUNT(sourceID) count, type FROM io_source WHERE isFolder = 0 AND type IS NOT NULL GROUP BY type")
    List<JSONObject> getFileTypeProportion();

    List<IOSourceVo> getFileCountByPath(List<String> list);


    //    @Select("SELECT name FROM io_source WHERE sourceID = #{id}")
    IOSource getSourceNameBySourceId(Long sourceID);

    List<JSONObject> getUserDirectoryAndFile(Long userId, Long parentId);


    //    @Select("SELECT sourceID, isFolder, name, parentID, parentLevel, fileID, modifyTime, createTime FROM io_source WHERE targetID = #{userId} AND isDelete = 0 AND parentID = #{parentId}")
    List<IOSourceVo> getSourceInfoByParentIdAndUser(Long id, long userId);


//    @Select("SELECT name, sourceID, parentLevel, modifyTime, createTime, targetType, targetID,parentID, isFolder, storageID from io_source where targetID = #{userId} AND parentID = 0 AND isFolder = 1 AND targetType = 1 AND isDelete = 0")
    IOSourceVo getUserRootDirectory( Long userId);

//    @Select("SELECT sourceID, parentLevel FROM io_source WHERE targetID = #{userId} AND name = #{lastPathName} AND isFolder = #{isFolder} and isDelete = 0")
    List<IOSourceVo> getSourceByNameAndUserId( String lastPathName,  Long userId,  Integer isFolder);

//    @Select("SELECT COUNT(sourceID) count, type FROM io_source WHERE createUser = #{userId} and isFolder = 0 AND type IS NOT NULL GROUP BY type")
    List<JSONObject> getFileTypeProportionByUserId( Long userId);

    void batchUpdateParentAndName(List<IOSource> list);

    IOFile getFileContentByNameAndUserId( String fileName, Long userId,  Boolean isVideoFile);

//    @Select("SELECT name FROM io_source WHERE isFolder = 1 AND name LIKE CONCAT(#{lastPath}, '%') AND parentLevel = #{parentLevel} AND isDelete = 0")
    List<String> getDirectoryByParentLevelAndName(String parentLevel, String lastPath);


//    @Select("SELECT sourceID,parentLevel,isFolder, modifyTime, createTime, size, fileID, name, targetType, targetID, parentID, storageID FROM io_source WHERE targetID = #{userId} AND parentLevel = #{parentLevel} AND name = #{name} AND isDelete = 0")
    IOSourceVo querySourceVoByParentLevelAndUserIdAndName( String parentLevel,  Long userId,  String name);

    List<ParentPathDisplayVo> getParentPathDisplay(List<String> list);

    String getSourceName(Long sourceID);

    int updateSourceAddSizeInfo(IOSource source);

    int updateSourceConvertSize( Long sourceID,  Long convertSize);

    int updateSourceThumbSize( Long sourceID,  Long thumbSize);

    List<IOSourceVo> getDesktopSourceList( Long parentID,  String name);

    Long getSourceSize( Long sourceID);

    int updateSourceSize( Long sourceID,  Long size);

//    @Select("SELECT fileType ft, COUNT(sourceID) c, SUM(size) s FROM io_source WHERE isFolder = 0 AND targetType = 1 GROUP BY fileType")
    List<JSONObject> selectFileProportion(Long tenantId);


//    @Select("SELECT targetType ty, SUM(size) s FROM io_source WHERE isFolder = 0 GROUP BY targetType")
    List<JSONObject> getTargetTypeProportion(Long tenantId);

    List<IOSource> selectBatchIds(Set<Long> keySet);

    void deleteById(Long sourceId);

    int updateCanShare(Integer canShare, List<Long> needUpdateIds);

    List<IOSource> selectListByIds(List<Long> ids);

    IOSource selectById(Long sourceID);

    List<IOSource> selectSimpleSourceById(List<Long> sourceIdList);
    List<ParentPathDisplayVo> getParentPathDisplayByIds(List<Long> list);
    int updateSourceDesc(Long sourceID, String description);
    IOSource getSourceByName(Long parentId, String name);
    CommonSource getSourceInfoByDentryId(String dentryId);
    Long checkSourceIsOwn(Long sourceId, Long userId);
    int updateCoverId(Long sourceID, Long coverId);
    /** 获取用户私密保险箱*/
    HomeExplorerVO getMySafeBoxSource(Long userId);
    int getSourceIsSafe(Long userId, String parentLevel);
    void clearUserDentryIdByDentryId(String dentryId);

    Map<String, Long> getSourceNameListJson(Long sourceID);
}
