package com.svnlan.home.service;

import com.svnlan.home.dto.VideoCommonDto;
import com.svnlan.jwt.domain.LoginUser;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/17 11:29
 */
public interface VideoGetService {

    List getVideoShearListAll(VideoCommonDto videoCommonDto);
    boolean checkImgCut(VideoCommonDto videoCommonDto);
    List getVideoShearList(VideoCommonDto videoCommonDto);

    Map<String, Object> cutVideo(VideoCommonDto videoCommonDto, LoginUser loginUser);
    Map<String, Object> cutVideoSave(VideoCommonDto videoCommonDto, LoginUser loginUser);
    Map<String, Object> splitVideoSave(VideoCommonDto videoCommonDto, LoginUser loginUser);
    Map<String, Object> mergeVideoSave(VideoCommonDto videoCommonDto, LoginUser loginUser);
    Map<String, Object> convertVideoSave(VideoCommonDto videoCommonDto, LoginUser loginUser);
    Map<String, Object> videoConfigSave(VideoCommonDto videoCommonDto, LoginUser loginUser);
    Map<String, Object> cutVideoImg(VideoCommonDto videoCommonDto, LoginUser loginUser);
    Map<String, Object> copyAudio(VideoCommonDto videoCommonDto, LoginUser loginUser);
    String getVideoShearImg(HttpServletResponse response, VideoCommonDto videoCommonDto, String passedFileName, Map<String, Object> resultMap);
}
