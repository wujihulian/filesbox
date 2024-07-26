package com.svnlan.manage.dao;

import com.svnlan.manage.domain.CommonDesign;
import com.svnlan.manage.domain.CommonDesignList;
import com.svnlan.manage.domain.DesignSource;
import com.svnlan.manage.vo.DesignDetailVO;
import com.svnlan.manage.vo.DesignListVO;
//import org.apache.ibatis.annotations.MapKey;

import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/3 13:38
 */
public interface CommonDesignDao {


    /**
     * 功能描述: 添加
     *
     */
    int insert(CommonDesign record);

    List<CommonDesignList> getDesignList(Map<String, Object> paramMap);

    List<CommonDesignList> getChildrenDesignList(Map<String, Object> paramMap);

    DesignDetailVO getDesignDetail(Map<String, Object> paramMap);

    int editDesign(CommonDesign design);

    int deleteDesign(CommonDesign commonDesign);

    CommonDesign selectById(Long designId);
    CommonDesign getDesignSimple(Map<String, Object> paramMap);
    CommonDesign selectForMainPage(String clientType);

    int updateToNotUsed(CommonDesign originDesign);
    int updateToUsed(Long designId);

    Integer getCountByClientType(CommonDesign commonDesign);

//    @MapKey("url")
    Map<String, CommonDesign> getSubPageByMain(CommonDesign mainPage);

    DesignDetailVO getUsingDesign(Map<String, Object> clientType);
    DesignDetailVO getDesignPreview(Long designId);

    CommonDesign geForExistsUrl(CommonDesign commonDesign);

    List<DesignListVO> selectClassifyListByParam();

    Long getChildrenDesignListCount(Map<String, Object> paramMap);

    Long getDesignListCount(Map<String, Object> paramMap);

    CommonDesign getDesignSimpleById(Long designId);

    Long addCopyDesign(Long designId);
}
