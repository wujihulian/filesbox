package com.svnlan.home.service;

import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.jwt.domain.LoginUser;

import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/8/8 9:17
 */
public interface VideoImagesService {

    Map<String, Object> imagesToVideo(VideoCommonDto videoCommonDto, LoginUser loginUser);
    boolean taskAction(CheckFileDTO checkFileDTO, Map<String, Object> resultMap, LoginUser loginUser);
}
