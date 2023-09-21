package com.svnlan.home.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.svnlan.home.domain.Share;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.home.vo.ShareVo;
import com.svnlan.user.vo.UserVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/3 13:46
 */
public interface ShareDao extends BaseMapper<Share> {

    int insert(Share share);
    int update(Share share);
    int updateNumView(@Param("shareID") Long shareID, @Param("numView") Integer numView);
    int updateNumDownload(@Param("shareID") Long shareID, @Param("numDownload") Integer numDownload);
    int delete(Long shareID);
    int deleteList(List<Long> list);
    ShareVo getShare(@Param("sourceID") Long sourceID, @Param("userID") Long userID,  @Param("isShareTo") Integer isShareTo, @Param("isLink") Integer isLink);
    ShareVo getShareById(Long shareID);
    List<HomeExplorerVO> getShareList(Map<String, Object> map);
    List<Long> checkUserIsShare(@Param("userID") Long userID);
    List<ShareVo> getShareByCode(@Param("shareCode") String shareCode);

    HomeExplorerVO getLinkShareInfo(Long sourceID);
    List<HomeExplorerVO> getShareToMeList(Map<String, Object> map);
    List<HomeExplorerVO> getLinkShareList(Map<String, Object> map);

    List<UserVo> getSelectUserListByParam(Map<String, Object> map);
    List<UserVo> getNotGroupUserListByParam(Map<String, Object> map);
    List<ShareVo> getShareByIdList(List<Long> list);

    void updateStatus(@Param("operateType") Integer operateType,@Param("ids") List<Long> ids);
}
