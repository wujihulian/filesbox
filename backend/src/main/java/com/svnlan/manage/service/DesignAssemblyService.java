package com.svnlan.manage.service;

import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.manage.dto.AddAssemblyDTO;
import com.svnlan.manage.dto.DeleteAssemblyDTO;
import com.svnlan.manage.dto.EditAssemblyDTO;
import com.svnlan.manage.dto.GetAssemblyDTO;
import com.svnlan.manage.vo.DesignAssemblyVO;

import java.util.List;

public interface DesignAssemblyService {

    List<DesignAssemblyVO> getAssemblyList(GetAssemblyDTO getAssemblyDTO, LoginUser loginUser);

    Long addAssembly(AddAssemblyDTO addAssemblyDTO, LoginUser loginUser);

    boolean editAssembly(EditAssemblyDTO editAssemblyDTO, LoginUser loginUser);

    boolean deleteAssembly(DeleteAssemblyDTO deleteAssemblyDTO, LoginUser loginUser);

}
