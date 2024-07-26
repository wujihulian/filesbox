package com.svnlan.home.service;

import com.svnlan.home.dto.YzOfficDto;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/10 16:31
 */
public interface YzService {

    void yzCallback(YzOfficDto officDto, MultipartFile file);
}
