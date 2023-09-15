package com.svnlan.home.service;

import com.svnlan.home.dto.GetM3u8DTO;
import com.svnlan.home.dto.GetM3u8NewDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author:
 * @Description:
 */
public interface M3u8Service {
    void getKey(String key, HttpServletResponse response);

    void getM3u8(GetM3u8DTO getM3u8DTO, HttpServletResponse response, HttpServletRequest request);

    String getM3u8WithAuth(GetM3u8NewDTO getM3u8DTO, HttpServletResponse response, HttpServletRequest request, int isApi);

}
