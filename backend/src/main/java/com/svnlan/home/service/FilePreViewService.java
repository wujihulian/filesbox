package com.svnlan.home.service;

import com.svnlan.home.dto.CheckFileDTO;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/26 16:41
 */
public interface FilePreViewService {
    String getPreviewXml(CheckFileDTO checkFileDTO);
}
