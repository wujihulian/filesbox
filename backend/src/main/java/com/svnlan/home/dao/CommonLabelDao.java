package com.svnlan.home.dao;

import com.svnlan.home.domain.CommonLabel;
import com.svnlan.home.vo.CommonLabelVo;

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

    List<CommonLabelVo> getUserLabelList(Long userID);

    List<CommonLabelVo> getInfoLabelList(Long userID);

    Integer getMaxSort(Long userID, Integer tagType);

    int updateSort(Long labelId, Integer sort);

    List<Long> checkLabelNameRepeat(String labelName, Long userID, Integer tagType);
}
