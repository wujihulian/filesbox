package com.svnlan.home.dao;

import com.svnlan.home.domain.IOSource;
import com.svnlan.home.dto.AddSubCloudDirectoryDTO;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.HomeFileDetailVO;
import com.svnlan.user.vo.GroupSizeVo;

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

    Map<Long, String> getFileInfoMoreMap(List<Long> fileIds, Long tenantId);

    Long getCountHomeExplorer(Map<String, Object> paramMap);

    /**
     * 收藏夹
     */
    List<HomeExplorerVO> getUserFavExplorer(Map<String, Object> paramMap);

    Long getUserFavExplorerCount(Map<String, Object> hashMap);

    /**
     * 回收站
     */
    List<HomeExplorerVO> getUserRecycleExplorer(Map<String, Object> paramMap);


    Long getUserRecycleExplorerCount(Map<String, Object> hashMap);
    /**
     * 最近文档
     */
    List<HomeExplorerVO> getUserRecentExplorer(Map<String, Object> paramMap);

    Long getUserRecentExplorerCount(Map<String, Object> hashMap);
    /**
     * 获得文档描述列表
     */
    List<HomeExplorerVO> getSourceDescList(List<Long> list);

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

    HomeExplorerVO getHomeSpace(Long targetID, Long parentID);

    List<HomeExplorerVO> getUserSpace(Long userID, Long groupID);

    List<HomeExplorerVO> getSourceChileCont(List<Long> list);


    int updateMemoryList(Map<String, Object> paramMap);

    void updateUserMemory(Map<String, Object> paramMap);

    void updateSubtractUseUserMemory(Map<String, Object> paramMap);

    void batchUpdateGroupMemoryList(List<GroupSizeVo> list);

    HomeExplorerVO getOneSourceInfo(Long sourceID);

    List<HomeExplorerVO> getUserGroupSourceList(Long userID);

    List<HomeExplorerVO> getSystemGroupSourceList(Long tenantId);

    Integer getUserIdentityInfo(Long userID);

    List<HomeExplorerVO> getParentNameList(List<Long> list);

    Integer checkUserRecycleExplorer(Long userID);

    String getGroupParentLevel(Long groupID);

    List<String> getGroupParentLevelList(List<Long> list);

    List<HomeExplorerVO> getFolderAndImgAndAudioHomeExplorer(List<Long> list, Integer lnkAudio);
    List<HomeExplorerVO> getImgByFolderList(List<String> fileTypeList, Long parentID);

    HomeExplorerVO getHomeExplorerOne(Long sourceId);
}
