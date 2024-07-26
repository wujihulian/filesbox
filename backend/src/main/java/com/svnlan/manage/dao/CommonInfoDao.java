package com.svnlan.manage.dao;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.manage.domain.CommonInfo;
import com.svnlan.manage.domain.CommonSeo;
import com.svnlan.manage.vo.CommonInfoVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 13:31
 */
public interface CommonInfoDao {

    int insert(CommonInfo commonInfo);

    int deleteInfo(Long infoID, Long modifyUser);

    CommonInfo getInfoById(Long infoID);

    CommonInfoVo getInfoVoById(Long infoID);

    List<CommonInfoVo> getInfoVoListByParam(Map<String, Object> map);

    int updateInfo(CommonInfo commonInfo);

    Integer getMaxSort(String infoType);

    List<CommonSeo> getSimpleInfoForSEO(List<Long> list);

    int updateInfoGmtPage(List<CommonSeo> infoList);

    void setTop(Map<String, Object> paramMap);

    List<CommonInfoVo> findHomePageListBySIdPId(String infoType);

    int batchUpdateSort(List<CommonInfoVo> list);

    List<CommonInfoVo> getInfoVoSortListByParam(Map<String, Object> map);

    //    @Update("UPDATE common_info SET viewCount = viewCount + 1, actualViewCount = actualViewCount + 1 WHERE infoID = #{id}")
    int updateViewCountById(Long id);

    int updateLikeCountById(Long id, Integer isLike);

    Long getInfoVoListByParamTotal(Map<String, Object> paramMap);

    List<CommonInfoVo> selectByInfoIds(List<Long> ids);
    List<CommonInfoVo> selectByParam(JSONObject paramMap);
}
