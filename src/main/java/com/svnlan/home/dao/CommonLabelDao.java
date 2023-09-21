package com.svnlan.home.dao;

import com.svnlan.home.domain.CommonLabel;
import com.svnlan.home.vo.CommonLabelVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/14 17:22
 */
public interface CommonLabelDao {

    int insert(CommonLabel commonLabel);
    int update(CommonLabel commonLabel);
    int deleteTag(Long labelId);
    int updateStatus(CommonLabel commonLabel);

    List<CommonLabelVo> getUserLabelList(Long userID);
    List<CommonLabelVo> getInfoLabelList(Long userID);

    Integer getMaxSort(@Param("userID") Long userID, @Param("tagType") Integer tagType);
    int updateSort(@Param("labelId") Long labelId, @Param("sort") Integer sort);

    List<Long> checkLabelNameRepeat(@Param("labelName") String labelName, @Param("userID") Long userID, @Param("tagType") Integer tagType);
}
