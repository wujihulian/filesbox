package com.svnlan.manage.service;

import com.svnlan.manage.dto.AddDesignDTO;
import com.svnlan.manage.dto.DeleteDesignDTO;
import com.svnlan.manage.dto.EditDesignDTO;
import com.svnlan.manage.dto.GetDesignListDTO;
import com.svnlan.manage.vo.DesignDetailVO;

import java.util.Map;

/**
 * 功能描述:
 *
 * @param:
 * @return:
 */
public interface CommonDesignService {
    /**
     * 功能描述:    添加编辑器
     *
     * @param:
     * @return:
     */
    Long addCommonDesign(AddDesignDTO dto);

    Map<String, Object> getDesignList(GetDesignListDTO getDesignListDTO);

    DesignDetailVO getDesignDetail(Long designId);

    boolean editDesignDetail(EditDesignDTO editDesignDTO);

    boolean deleteCommonDesign(DeleteDesignDTO deleteDesignDTO);

    boolean setUsing(EditDesignDTO editDesignDTO);

    DesignDetailVO getDesignPreview(Long designId);

    DesignDetailVO getUsingDesign(String clientType);

    
}
