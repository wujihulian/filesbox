package com.svnlan.manage.dao;

import com.svnlan.manage.domain.CommonDesignClassify;
import com.svnlan.manage.vo.CommonDesignClassifyVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/19 17:17
 */
public interface CommonDesignClassifyDao {


    int deleteById(Integer infoTypeID);
    int insert(CommonDesignClassify commonDesignClassify);
    int insertBatch(List<CommonDesignClassify> list);
    int updateById(CommonDesignClassify commonDesignClassify);
    CommonDesignClassify selectById(Integer infoTypeID);
    CommonDesignClassifyVo selectVoById(Integer infoTypeID);
    List<CommonDesignClassifyVo> selectListByParam(Map<String, Object> map);
    List<CommonDesignClassifyVo> selectListAndCountByParam(Map<String, Object> map);
    int updateStatusById(CommonDesignClassify commonDesignClassify);
    int deleteSettings(List<Integer> list);
    String selectSequenceById(Integer infoTypeID);
    Integer batchUpdate(List<CommonDesignClassify> list);
    List<CommonDesignClassify> copyInfoTypeListByLevel(String parentLevel);
    int batchUpdateParent(List<CommonDesignClassify> list);
    List<Integer> checkChild(@Param("parentLevel") String parentLevel, @Param("designClassifyID") Integer designClassifyID);
    List<CommonDesignClassify> checkNameIsExist(String typeName);
}
