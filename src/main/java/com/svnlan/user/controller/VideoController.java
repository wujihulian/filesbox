package com.svnlan.user.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.domain.CommonSource;
import com.svnlan.home.dto.convert.ConvertDTO;
import com.svnlan.home.enums.BusTypeEnum;
import com.svnlan.home.utils.ConvertUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.CommonConvertDTO;
import com.svnlan.user.dto.ConvertListDTO;
import com.svnlan.user.service.VideoService;
import com.svnlan.utils.*;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/20 14:59
 */
@RestController
public class VideoController {

    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    VideoService videoService;
    @Resource
    ConvertUtil convertUtil;
    /**
     * @Description: 转码列表
     * @params:  [paramDto, request]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/getConvertList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getConvertList(ConvertListDTO convertDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        PageResult data = null;
        try {
            data = videoService.getConvertList(loginUser, convertDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(CodeMessageEnum.success.getCode(), data);
        return JsonUtils.beanToJson(result);
    }

    /**
       * @Description: 删除视频转码记录
       * @params:  [optionDTO, request]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/3/22 10:17
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/convert/remove", method = RequestMethod.POST)
    public Result removeConvert(@RequestBody CommonConvertDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            optionDTO.setState("3");
            videoService.removeConvert(prefix,loginUser, optionDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }


    /**
     * @Description: 删除视频原文件
     * @params:  [optionDTO, request]
     * @Return:  com.svnlan.common.Result
     * @Author:  sulijuan
     * @Date:  2023/3/22 10:17
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/file/remove", method = RequestMethod.POST)
    public Result removeFilePath(@RequestBody CommonConvertDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            videoService.removeVideoFilePath(prefix,loginUser, optionDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }



    @GetMapping(value = "/api/disk/execConvert")
    public String convertToPic(HttpServletRequest request, @RequestParam(value = "sourceID", required = false) Long sourceID
            , @RequestParam(value = "fileID", required = false) Long fileID, @RequestParam(value = "isNew", required = false) Integer isNew) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> sourceID:%d;loginUser:%s",
                    sourceID, JsonUtils.beanToJson(loginUser));
            ConvertDTO convertDTO = new ConvertDTO();
            convertDTO.setBusType(BusTypeEnum.CLOUD.getBusType());
            convertDTO.setBusId(sourceID);
            convertDTO.setUserID(loginUser.getUserID());
            // 1 手动执行 2 定时任务执行
            convertDTO.setOpType("1");
            if (!ObjectUtils.isEmpty(isNew) && 1 == isNew){
                convertDTO.setIsNew(1);
            }

            CommonSource commonSource = null;

            // 状态修改为转码中
            convertUtil.saveStateConvertIngRecord(sourceID, fileID, "0");

            String serverUrl = HttpUtil.getRequestRootUrl(null);
            convertDTO.setDomain(serverUrl);
            convertUtil.doConvert(convertDTO, commonSource);
            result = Result.returnSuccess(null);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + "SvnlanRuntimeException" + paramTip);
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "Exception" + paramTip);
            result = Result.returnError(CodeMessageEnum.system_error);
        }
        return JsonUtils.beanToJson(result);
    }

}
