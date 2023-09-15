package com.svnlan.home.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.domain.IOFile;
import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.SourceOpDto;
import com.svnlan.home.vo.IOSourceVo;
import com.svnlan.home.vo.ParentPathDisplayVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 11:13
 */
public interface IoSourceDao extends BaseMapper<IOSource> {

    int insert(IOSource source);

    int batchInsert(List<IOSource> list);

    CommonSource getSourceInfo(Long sourceID);

    List<CommonSource> getSourceInfoList(List<Long> list);

    List<IOSource> copySourceList(List<Long> list);

    List<IOSource> copySourceListByLevel(List<String> list);

    int deleteDirOrFile(List<Long> list);

    int deleteSourceByParent(List<String> list);

    int restoreSourceByParent(@Param("list") List<String> list, @Param("userID") Long userID);

    int batchFileRename(@Param("list") List<SourceOpDto> list, @Param("userID") Long userID);

    int batchUpdateParent(List<IOSource> list);

    int restoreDirOrFile(@Param("list") List<Long> list, @Param("userID") Long userID);

    int removeUserSource(List<Long> list);

    int removeUserSourceByParent(List<String> list);

    List<Long> getFileIDBySourceID(List<Long> list);

    List<IOSourceVo> getFileCountBySourceID(List<Long> list);

    int updateSourceMemoryList(@Param("list") List<Long> list, @Param("memory") Long memory);

    int subtractSourceMemoryList(@Param("list") List<Long> list, @Param("memory") Long memory);

    int batchUpdateSourceMemoryList(@Param("list") List<IOSource> list);

    int batchSubtractSourceMemoryList(@Param("list") List<IOSource> list);

    Integer getMaxSort(Long parentID);

    int updateSort(@Param("sourceID") Long sourceID, @Param("sort") Integer sort);

    int updateSourceInfo(IOSource source);

    List<IOSourceVo> copySourcePathList(List<Long> list);

    List<IOSourceVo> copySourcePathListByLevel(List<String> list);

    List<CommonSource> getSourceFileInfoList(List<Long> list);

    int updateSourceModifyUser(IOSource source);

    int updateFileSize(IOSource source);

    List<String> getSourceNameList(Long sourceID);

    List<CommonSource> checkSourceNameList(Long sourceID);

    List<JSONObject> fileCount();

    @Select("SELECT COUNT(sourceID) count, `type` FROM io_source WHERE isFolder = 0 AND `type` IS NOT NULL GROUP BY `type`")
    List<JSONObject> getFileTypeProportion();

    List<IOSourceVo> getFileCountByPath(List<String> list);


    @Select("SELECT name FROM io_source WHERE sourceID = #{id}")
    IOSource getSourceNameBySourceId(@Param("id") Long sourceID);

    List<JSONObject> getUserDirectoryAndFile(@Param("userId") Long userId, @Param("parentId") Long parentId);


    @Select("SELECT sourceID, isFolder, `name`, parentID, parentLevel, fileID, modifyTime, createTime FROM io_source WHERE targetID = #{userId} AND isDelete = 0 AND parentID = #{parentId}")
    List<IOSourceVo> getSourceInfoByParentIdAndUser(@Param("parentId") Long id, @Param("userId") long userId);


    @Select("SELECT `name`, sourceID, parentLevel, modifyTime, createTime, targetType, targetID,parentID, isFolder, storageID from io_source where targetID = #{userId} AND parentID = 0 AND isFolder = 1 AND targetType = 1 AND isDelete = 0")
    IOSourceVo getUserRootDirectory(@Param("userId") Long userId);

    @Select("SELECT sourceID, parentLevel FROM io_source WHERE targetID = #{userId} AND `name` = #{lastPathName} AND isFolder = #{isFolder} and isDelete = 0")
    List<IOSourceVo> getSourceByNameAndUserId(@Param("lastPathName") String lastPathName, @Param("userId") Long userId, @Param("isFolder") Integer isFolder);

    @Select("SELECT COUNT(sourceID) count, `type` FROM io_source WHERE createUser = #{userId} and isFolder = 0 AND `type` IS NOT NULL GROUP BY `type`")
    List<JSONObject> getFileTypeProportionByUserId(@Param("userId") Long userId);

    int batchUpdateParentAndName(List<IOSource> list);

    IOFile getFileContentByNameAndUserId(@Param("fileName") String fileName, @Param("userId") Long userId, @Param("isVideoFile") Boolean isVideoFile);

    @Select("SELECT name FROM io_source WHERE isFolder = 1 AND `name` LIKE CONCAT(#{lastPath}, '%') AND parentLevel = #{parentLevel} AND isDelete = 0")
    List<String> getDirectoryByParentLevelAndName(String parentLevel, String lastPath);


    @Select("SELECT sourceID,parentLevel,isFolder, modifyTime, createTime, `size`, fileID, `name`, targetType, targetID, parentID, storageID FROM io_source WHERE targetID = #{userId} AND parentLevel = #{parentLevel} AND name = #{name} AND isDelete = 0")
    IOSourceVo querySourceVoByParentLevelAndUserIdAndName(@Param("parentLevel") String parentLevel, @Param("userId") Long userId, @Param("name") String name);

    List<ParentPathDisplayVo> getParentPathDisplay(List<String> list);

    String getSourceName(Long sourceID);
    int updateSourceAddSizeInfo(IOSource source);
    int updateSourceConvertSize(@Param("sourceID") Long sourceID, @Param("convertSize") Long convertSize);
    int updateSourceThumbSize(@Param("sourceID") Long sourceID, @Param("thumbSize") Long thumbSize);

    List<IOSourceVo> getDesktopSourceList(@Param("parentID") Long parentID, @Param("name") String name);

    Long getSourceSize(@Param("sourceID") Long sourceID);
    int updateSourceSize(@Param("sourceID") Long sourceID, @Param("size") Long size);

    @Select("SELECT fileType ft, COUNT(sourceID) c, SUM(size) s FROM io_source WHERE isFolder = 0 AND targetType = 1 GROUP BY fileType")
    List<JSONObject> selectFileProportion();


    @Select("SELECT targetType ty, SUM(size) s FROM io_source WHERE isFolder = 0 GROUP BY targetType")
    List<JSONObject> getTargetTypeProportion();

    List<ParentPathDisplayVo> getParentPathDisplayByIds(List<Long> list);
    int updateSourceDesc(@Param("sourceID") Long sourceID, @Param("description") String description);
}
