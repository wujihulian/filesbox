package com.svnlan.manage.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.manage.dto.CommonInfoTypeDto;
import com.svnlan.manage.service.CommonInfoTypeService;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/9 16:54
 */
@RestController
public class CommonInfoTypeController {

    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    JWTTool jwtTool;
    @Resource
    CommonInfoTypeService commonInfoTypeService;

    /**
     * @Description: 查看所有资讯设置分級的列表
     * @params:
     * @Return:
     */
    @RequestMapping(value = "/api/disk/getInfoTypeList", method = RequestMethod.GET)
    public String listAllSetting(CommonInfoTypeDto infoTypeDto, HttpServletRequest request) {
        List list;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> getInfoTypeList:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoTypeDto), JsonUtils.beanToJson(loginUser));

            // 查看所有资讯设置分級的列表
            list = commonInfoTypeService.getInfoTypeList(infoTypeDto, loginUser);
            result = Result.returnSuccess(list);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + "SvnlanRuntimeException" + paramTip);
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "Exception" + paramTip);
            result = Result.returnError(CodeMessageEnum.system_error);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * 新增一条分类
     */
    @RequestMapping(value = "/api/disk/saveInfoType", method = RequestMethod.POST)
    public String saveInfoType(@Valid @RequestBody CommonInfoTypeDto infoTypeDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        int id;
        try {
            paramTip = String.format(" >>> infoTypeDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoTypeDto), JsonUtils.beanToJson(loginUser));

            // 新增一条分类
            id = commonInfoTypeService.saveInfoType(infoTypeDto, loginUser);
            result = Result.returnSuccess(id);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + "SvnlanRuntimeException" + paramTip);
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "Exception" + paramTip);
            result = Result.returnError(CodeMessageEnum.system_error);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/editInfoType", method = RequestMethod.POST)
    public String editInfoType(@Valid @RequestBody CommonInfoTypeDto infoTypeDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        int id;
        try {
            paramTip = String.format(" >>> infoTypeDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoTypeDto), JsonUtils.beanToJson(loginUser));

            // 新增一条分类
            id = commonInfoTypeService.editInfoType(infoTypeDto, loginUser);
            result = Result.returnSuccess(id);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + "SvnlanRuntimeException" + paramTip);
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "Exception" + paramTip);
            result = Result.returnError(CodeMessageEnum.system_error);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 资讯分类的删除操作
     * @params:
     */
    @RequestMapping(value = "/api/disk/deleteInfoType", method = RequestMethod.POST)
    public String deleteInfoType(@Valid @RequestBody CommonInfoTypeDto infoTypeDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> infoTypeDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoTypeDto), JsonUtils.beanToJson(loginUser));

            // 删除分类
            commonInfoTypeService.deleteInfoType(infoTypeDto, loginUser);
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
