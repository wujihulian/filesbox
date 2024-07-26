package com.svnlan.user.dao;

import com.svnlan.user.domain.CommonConvert;
import com.svnlan.user.vo.CommonConvertVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/20 11:27
 */
public interface CommonConvertDao {

    int insert(CommonConvert commonConvert);

    int updateStatus(Long convertID, String state);

    int updateStatusAndCount(Long convertID, Long userID, String state, String remark);

    int updateScheduleStatus(Long convertID, String state);

    Long checkExist(Long sourceID, Long fileID);

    List<CommonConvertVo> getConvertList(Map<String, Object> map);

    //    @Select("SELECT COUNT(1) FROM common_convert WHERE state = 2")
    Long queryConvertExceptionCountInstant();

    Long getConvertListCount(Map<String, Object> map);
}
