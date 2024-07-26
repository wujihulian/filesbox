package com.svnlan.home.controller;

import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.service.M3u8Service;
import com.svnlan.home.dto.*;
import com.svnlan.home.utils.CommonConvertUtil;
import com.svnlan.utils.LogUtil;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

/**
 * @Author:
 * @Description:
 * @Date:
 */
@RestController
public class M3u8Controller {
    @Resource
    private M3u8Service m3u8Service;
    @Resource
    private CommonConvertUtil commonConvertUtil;

    @RequestMapping(value = "/api/disk/mu/key", method = RequestMethod.GET)
    public String getKey(HttpServletResponse response,String key){
        try {
            m3u8Service.getKey(key,response);
        } catch (Exception e){
            LogUtil.error(e, "获取key失败");
        }
        return "error";
    }

    @RequestMapping(value = "/api/disk/mu/getM3u8.m3u8", method = RequestMethod.GET)
    public String getM3u8(HttpServletResponse response, HttpServletRequest request, GetM3u8DTO getM3u8DTO){
        try {
            m3u8Service.getM3u8(getM3u8DTO, response, request);
        } catch (SvnlanRuntimeException e) {
            return e.getMessage();
        } catch (Exception e){
            LogUtil.error(e, "获取m3u8失败");
        }
        return "error";
    }

    /**
       * @Description: m3u8鉴权接口
       * @params:  [response, request, getM3u8DTO]
       * @Return:  java.lang.String
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/mu/getMyM3u8.m3u8", method = RequestMethod.GET)
    public String getM3u8WithAuth(HttpServletResponse response, HttpServletRequest request,@Valid GetM3u8NewDTO getM3u8DTO){
        try {
            String m3u8WithAuth = m3u8Service.getM3u8WithAuth(getM3u8DTO, response, request, 0);
            if (m3u8WithAuth != null){
                return m3u8WithAuth;
            }
        } catch (SvnlanRuntimeException e) {
            return e.getMessage();
        } catch (Exception e){
            LogUtil.error(e, "获取m3u8失败");
        }
        return "error";
    }


    /**
     * @Description: m3u8鉴权接口api
     * @params:  [response, request, getM3u8DTO]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/service/mu/getMyM3u8.m3u8", method = RequestMethod.GET)
    public String getCommonM3u8WithAuth(HttpServletResponse response, HttpServletRequest request, GetMyM3u8DTO getMyM3u8DTO){
        GetM3u8NewDTO getM3u8DTO;
        try {
            getM3u8DTO = commonConvertUtil.convertGetM3u8NewDTO(getMyM3u8DTO);
            String m3u8WithAuth = m3u8Service.getM3u8WithAuth(getM3u8DTO, response, request, 1);
            if (m3u8WithAuth != null){
                return m3u8WithAuth;
            }
        } catch (SvnlanRuntimeException e) {
            return e.getMessage();
        } catch (Exception e){
            LogUtil.error(e, "/api/common/service/mu/getMyM3u8.m3u8 获取m3u8失败");
        }
        return "error";
    }
}
