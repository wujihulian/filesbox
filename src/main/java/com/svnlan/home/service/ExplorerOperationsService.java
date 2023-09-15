package com.svnlan.home.service;


import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.jwt.domain.LoginUser;

import java.util.Map;

/**
 * @author KingMgg
 * @data 2023/2/7 16:13
 */
public interface ExplorerOperationsService {


    void initSourcePathLevel(Long sourceID);

    void countSize(CheckFileDTO updateFileDTO, Map<String, Object> resultMap, LoginUser loginUser);

}
