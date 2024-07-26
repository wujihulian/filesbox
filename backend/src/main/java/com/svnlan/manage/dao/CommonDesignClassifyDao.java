package com.svnlan.manage.dao;

import com.svnlan.manage.domain.CommonDesignClassify;
import com.svnlan.manage.vo.CommonDesignClassifyVo;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/19 17:17
 */
public interface CommonDesignClassifyDao {

    int insert(CommonDesignClassify commonDesignClassify);

    int updateById(CommonDesignClassify commonDesignClassify);

    CommonDesignClassify selectById(Integer infoTypeID);

    List<CommonDesignClassifyVo> selectListByParam(Map<String, Object> map);

    List<CommonDesignClassifyVo> selectListAndCountByParam(Map<String, Object> map);

    int updateStatusById(CommonDesignClassify commonDesignClassify);

    List<CommonDesignClassify> copyInfoTypeListByLevel(String parentLevel);

    int batchUpdateParent(List<CommonDesignClassify> list);

    List<Integer> checkChild(String parentLevel, Integer designClassifyID);

    List<CommonDesignClassify> checkNameIsExist(String typeName);
}
