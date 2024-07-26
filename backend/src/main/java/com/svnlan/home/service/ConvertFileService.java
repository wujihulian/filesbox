package com.svnlan.home.service;

import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.vo.CommonSourceVO;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/27 16:04
 */
public interface ConvertFileService {
    void tryToConvertFile(CommonSourceVO commonSourceVO, CommonSource commonSource, String busType);
}
