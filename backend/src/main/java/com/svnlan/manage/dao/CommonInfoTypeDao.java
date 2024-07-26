package com.svnlan.manage.dao;

import com.svnlan.manage.domain.CommonInfoType;
import com.svnlan.manage.vo.CommonInfoTypeVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 13:31
 */
public interface CommonInfoTypeDao {

    int insert(CommonInfoType commonInfoType);
    int updateById(CommonInfoType commonInfoType);
    CommonInfoType selectById(Integer infoTypeID);
    List<CommonInfoTypeVo> selectListByParam(Map<String, Object> map);
    List<CommonInfoTypeVo> selectListAndCountByParam(Map<String, Object> map);
    int updateStatusById(CommonInfoType commonInfoType);

    List<CommonInfoType> copyInfoTypeListByLevel(String parentLevel);
    int batchUpdateParent(List<CommonInfoType> list);
    List<Integer> checkChild(String parentLevel, Integer infoTypeID);
    List<CommonInfoType> checkNameIsExist(String typeName);
}
