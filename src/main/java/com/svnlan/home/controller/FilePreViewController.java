package com.svnlan.home.controller;

import com.svnlan.annotation.VisitRecord;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.service.FilePreViewService;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/26 16:38
 */
@RestController
public class FilePreViewController {
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    FilePreViewService filePreViewService;

    @RequestMapping(value = "/api/disk/previewXml", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String preview(HttpServletResponse response, CheckFileDTO checkFileDTO){

        if (ObjectUtils.isEmpty(checkFileDTO.getShareCode())){
            LoginUser loginUser = loginUserUtil.getLoginUser();
            if (ObjectUtils.isEmpty(loginUser)){
                Result result = new Result(false, CodeMessageEnum.bindSignError.getCode(), null);
                return JsonUtils.beanToJson(result);
            }
            checkFileDTO.setLoginUser(loginUser);
        }
        try {
            return filePreViewService.getPreviewXml(checkFileDTO);
        } catch (SvnlanRuntimeException e){
            LogUtil.error(e, "previewXml 云盘获取预览信息失败");
        } catch (Exception e){
            LogUtil.error(e, "previewXml error 云盘获取预览信息失败");

        }
        return "";
    }
}
