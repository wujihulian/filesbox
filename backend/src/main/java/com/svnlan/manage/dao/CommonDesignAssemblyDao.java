package com.svnlan.manage.dao;

import com.svnlan.manage.domain.CommonDesignAssembly;
import com.svnlan.manage.vo.DesignAssemblyVO;

import java.util.List;

public interface CommonDesignAssemblyDao {

    List<DesignAssemblyVO> selectList(CommonDesignAssembly commonDesignAssembly);
    int insert(CommonDesignAssembly record);
    int deleteById(Long assemblyId);
    int updateByParam(CommonDesignAssembly record);
}
