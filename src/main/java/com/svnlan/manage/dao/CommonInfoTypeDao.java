package com.svnlan.manage.dao;

import com.svnlan.manage.domain.CommonInfoType;
import com.svnlan.manage.vo.CommonInfoTypeVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 13:31
 */
public interface CommonInfoTypeDao {

    int deleteById(Integer infoTypeID);
    int insert(CommonInfoType commonInfoType);
    int insertBatch(List<CommonInfoType> list);
    int updateById(CommonInfoType commonInfoType);
    CommonInfoType selectById(Integer infoTypeID);
    CommonInfoTypeVo selectVoById(Integer infoTypeID);
    List<CommonInfoTypeVo> selectListByParam(Map<String, Object> map);
    List<CommonInfoTypeVo> selectListAndCountByParam(Map<String, Object> map);
    int updateStatusById(CommonInfoType commonInfoType);
    int deleteSettings(List<Integer> list);
    String selectSequenceById(Integer infoTypeID);
    Integer batchUpdate(List<CommonInfoType> list);
    List<CommonInfoType> copyInfoTypeListByLevel(String parentLevel);
    int batchUpdateParent(List<CommonInfoType> list);
    List<Integer> checkChild(@Param("parentLevel") String parentLevel, @Param("infoTypeID") Integer infoTypeID);
    List<CommonInfoType> checkNameIsExist(String typeName);
}
