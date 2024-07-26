package com.svnlan.home.dao;

import com.svnlan.home.domain.Share;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.user.vo.UserVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/3 13:46
 */
public interface ShareDao {

    int insert(Share share);

    int update(Share share);

    int updateNumView(Long shareID, Integer numView);

    int updateNumDownload(Long shareID, Integer numDownload);

    int deleteList(List<Long> list);

    ShareVo getShare(Long sourceID, Long userID, Integer isShareTo, Integer isLink);

    ShareVo getShareById(Long shareID);

    List<HomeExplorerVO> getShareList(Map<String, Object> map);

    Long getShareListCount(Map<String, Object> hashMap);

    List<Long> checkUserIsShare(Long userID);

    List<ShareVo> getShareByCode(String shareCode);

    HomeExplorerVO getLinkShareInfo(Long sourceID);

    List<HomeExplorerVO> getShareToMeList(Map<String, Object> map);

    Long getShareToMeListCount(Map<String, Object> hashMap);

    List<HomeExplorerVO> getLinkShareList(Map<String, Object> map);

    Long getLinkShareListCount(Map<String, Object> hashMap);

    List<UserVo> getSelectUserListByParam(Map<String, Object> map);

    List<UserVo> getNotGroupUserListByParam(Map<String, Object> map);

    List<ShareVo> getShareByIdList(List<Long> list);

    void updateStatus(Integer operateType, List<Long> ids);
}
