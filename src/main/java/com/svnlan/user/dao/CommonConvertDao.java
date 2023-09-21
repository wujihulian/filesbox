package com.svnlan.user.dao;

import com.svnlan.user.domain.CommonConvert;
import com.svnlan.user.vo.CommonConvertVo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/20 11:27
 */
public interface CommonConvertDao {

    int insert(CommonConvert commonConvert);
    int updateStatus(@Param("convertID") Long convertID, @Param("state") String state);
    int updateStatusAndCount(@Param("convertID") Long convertID, @Param("userID") Long userID, @Param("state") String state, @Param("remark") String remark);
    int updateScheduleStatus(@Param("convertID") Long convertID, @Param("state") String state);
    Long checkExist(@Param("sourceID") Long sourceID, @Param("fileID") Long fileID);
    List<CommonConvertVo> getConvertList(Map<String, Object> map);

    @Select("SELECT COUNT(1) FROM common_convert WHERE state = 2")
    Long queryConvertExceptionCountInstant();

}
