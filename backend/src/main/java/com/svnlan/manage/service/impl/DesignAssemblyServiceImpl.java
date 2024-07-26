package com.svnlan.manage.service.impl;

import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dao.CommonDesignAssemblyDao;
import com.svnlan.manage.domain.CommonDesignAssembly;
import com.svnlan.manage.dto.AddAssemblyDTO;
import com.svnlan.manage.dto.DeleteAssemblyDTO;
import com.svnlan.manage.dto.EditAssemblyDTO;
import com.svnlan.manage.dto.GetAssemblyDTO;
import com.svnlan.manage.enums.AssemblyTypeEnum;
import com.svnlan.manage.service.DesignAssemblyService;
import com.svnlan.manage.vo.DesignAssemblyVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class DesignAssemblyServiceImpl implements DesignAssemblyService {

    @Resource
    private CommonDesignAssemblyDao commonDesignAssemblyDao;


    /**
     * @Description: 特效组合列表
     * @params:  [type]
     * @Modified:
     */
    @Override
    public List<DesignAssemblyVO> getAssemblyList(GetAssemblyDTO getAssemblyDTO, LoginUser loginUser) {
        CommonDesignAssembly commonDesignAssembly = new CommonDesignAssembly();

        commonDesignAssembly.setDesignType(getAssemblyDTO.getDesignType());
        commonDesignAssembly.setClientType(getAssemblyDTO.getClientType());
        commonDesignAssembly.setAssemblyType(getAssemblyDTO.getType());
        return commonDesignAssemblyDao.selectList(commonDesignAssembly);
    }


    /**
     * @Description: 添加组件
     * @params:  [addAssemblyDTO]
     * @Return:  java.lang.Long
     * @Modified:
     */
    @Override
    public Long addAssembly(AddAssemblyDTO addAssemblyDTO, LoginUser loginUser) {
        CommonDesignAssembly commonDesignAssembly = new CommonDesignAssembly();

        //非组合模块的，设置为特效
        if (!addAssemblyDTO.getType().equals(AssemblyTypeEnum.MODULE.getCode())){
            addAssemblyDTO.setType(AssemblyTypeEnum.EFFECT.getCode());
        }
        commonDesignAssembly.setAssemblyType(addAssemblyDTO.getType());
        commonDesignAssembly.setDetail(addAssemblyDTO.getDetail());
        commonDesignAssembly.setTitle(addAssemblyDTO.getTitle());
        commonDesignAssembly.setSize(addAssemblyDTO.getSize());
        commonDesignAssembly.setSetting(addAssemblyDTO.getSetting());
        commonDesignAssembly.setClientType(addAssemblyDTO.getClientType());
        commonDesignAssembly.setDesignType(addAssemblyDTO.getDesignType());
        ObjUtil.initializefield(commonDesignAssembly);

        commonDesignAssemblyDao.insert(commonDesignAssembly);
        return commonDesignAssembly.getId();
    }

    /**
     * @Description: 编辑组件
     * @params:  [editAssemblyDTO]
     * @Return:  boolean
     * @Modified:
     */
    @Override
    public boolean editAssembly(EditAssemblyDTO editAssemblyDTO, LoginUser loginUser) {
        CommonDesignAssembly commonDesignAssembly = new CommonDesignAssembly();
        commonDesignAssembly.setCommonDesignAssemblyId(editAssemblyDTO.getAssemblyId());
        commonDesignAssembly.setTitle(editAssemblyDTO.getTitle());
        commonDesignAssembly.setDetail(editAssemblyDTO.getDetail());
        commonDesignAssembly.setGmtModified(new Date());
        commonDesignAssembly.setSetting(editAssemblyDTO.getSetting());
        commonDesignAssembly.setSize(editAssemblyDTO.getSize());

        int rows = commonDesignAssemblyDao.updateByParam(commonDesignAssembly);
        if (rows == 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode(), "找不到对应的组件");
        }
        return rows > 0;
    }

    /**
     * @Description: 删除组件
     * @params:  [deleteAssemblyDTO]
     * @Return:  boolean
     * @Modified:
     */
    @Override
    public boolean deleteAssembly(DeleteAssemblyDTO deleteAssemblyDTO, LoginUser loginUser) {

        int rows = commonDesignAssemblyDao.deleteById(deleteAssemblyDTO.getAssemblyId());
        if (rows == 0){
            throw new SvnlanRuntimeException(CodeMessageEnum.paramFormatError.getCode(), "找不到对应的组件");
        }
        return rows > 0;
    }

}
