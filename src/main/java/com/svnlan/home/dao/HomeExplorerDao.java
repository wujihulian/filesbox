package com.svnlan.home.dao;

import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.AddSubCloudDirectoryDTO;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.HomeFileDetailVO;
import com.svnlan.user.vo.GroupSizeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author KingMgg
 * @data 2023/2/6 11:51
 */
public interface HomeExplorerDao {
    /**
     * 分类列表下面的子分类列表 根据参数判断 文件 文件夹
     *
     * @return
     */
    List<HomeExplorerVO> getHomeExplorer(Map<String, Object> paramMap);
    Long getCountHomeExplorer(Map<String, Object> paramMap);
    /** 收藏夹*/
    List<HomeExplorerVO> getUserFavExplorer(Map<String, Object> paramMap);
    /** 回收站*/
    List<HomeExplorerVO> getUserRecycleExplorer(Map<String, Object> paramMap);
    /** 最近文档*/
    List<HomeExplorerVO> getUserRencentExplorer(Map<String, Object> paramMap);

    /**
     * 创建文件夹
     *
     * @return
     */
    int createDir(IOSource source);
    int createDirectory(AddSubCloudDirectoryDTO source);


    /**
     * 获取文件详情
     *
     * @param fileID
     * @return
     */
    HomeFileDetailVO getFileDetail(Long fileID);
    HomeExplorerVO getHomeSpace(@Param("targetID") Long targetID, @Param("parentID") Long parentID);

    HomeExplorerVO getEnterpriseSpace();
    List<HomeExplorerVO> getUserSpace(@Param("userID") Long userID, @Param("groupID") Long groupID);
    List<HomeExplorerVO> getSourceChileCont(List<Long> list);

    /**
     * 修改云盘空间使用大小
     *
     * @param paramMap
     * @return
     */
    void updateMemory(Map<String, Object> paramMap);
    int updateMemoryList(Map<String, Object> paramMap);
    void updateUserMemory(Map<String, Object> paramMap);
    void updateSubtractUseUserMemory(Map<String, Object> paramMap);
    int batchUpdateGroupMemoryList(@Param("list") List<GroupSizeVo> list);
    HomeExplorerVO getOneSourceInfo(Long sourceID);
    List<HomeExplorerVO> getUserGroupSourceList(Long userID);
    List<HomeExplorerVO> getSystemGroupSourceList();
    Integer getUserIdentityInfo(Long userID);
    String getParentName(Long sourceID);
    List<HomeExplorerVO> getParentNameList(@Param("list") List<Long> list);
    Integer checkUserRecycleExplorer(Long userID);
    String getGroupParentLevel(Long groupID);
    List<String> getGroupParentLevelList(@Param("list") List<Long> list);
    List<HomeExplorerVO> getImgAndAudioHomeExplorer(@Param("list") List<Long> list, @Param("lnkAudio") Integer lnkAudio);
    List<HomeExplorerVO> getFolderAndImgAndAudioHomeExplorer(@Param("list") List<Long> list, @Param("lnkAudio") Integer lnkAudio);
    List<HomeExplorerVO> getImgByFolderList(Map<String, Object> paramMap);
}
