package com.svnlan.manage.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.manage.dto.DesignClassifyDto;
import com.svnlan.manage.service.CommonDesignClassifyService;
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
 * @Date: 2023/6/19 17:23
 */
@RestController
public class DesignClassifyController {

    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    JWTTool jwtTool;
    @Resource
    CommonDesignClassifyService commonDesignClassifyService;

    /**
     * @Description: 查看所有设置分級的列表
     * @params:
     * @Return:
     */
    @RequestMapping(value = "/api/disk/getDesignClassifyList", method = RequestMethod.GET)
    public String listAllSetting(DesignClassifyDto designClassifyDto, HttpServletRequest request) {
        List list;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> getDesignClassifyList:%s;loginUser:%s",
                    JsonUtils.beanToJson(designClassifyDto), JsonUtils.beanToJson(loginUser));

            // 查看所有列表
            list = commonDesignClassifyService.getDesignClassifyList(designClassifyDto, loginUser);
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
     * 新增一条目录分类
     */
    @RequestMapping(value = "/api/disk/saveDesignClassify", method = RequestMethod.POST)
    public String saveInfoType(@Valid @RequestBody DesignClassifyDto designClassifyDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        int id;
        try {
            paramTip = String.format(" >>> designClassifyDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(designClassifyDto), JsonUtils.beanToJson(loginUser));

            // 新增一条分类
            id = commonDesignClassifyService.saveDesignClassify(designClassifyDto, loginUser);
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

    @RequestMapping(value = "/api/disk/editDesignClassify", method = RequestMethod.POST)
    public String editInfoType(@Valid @RequestBody DesignClassifyDto designClassifyDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        int id;
        try {
            paramTip = String.format(" >>> designClassifyDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(designClassifyDto), JsonUtils.beanToJson(loginUser));

            // 新增一条分类
            id = commonDesignClassifyService.editDesignClassify(designClassifyDto, loginUser);
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
     * @Description: 分类的删除操作
     * @params:
     */
    @RequestMapping(value = "/api/disk/deleteDesignClassify", method = RequestMethod.POST)
    public String deleteInfoType(@Valid @RequestBody DesignClassifyDto designClassifyDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> designClassifyDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(designClassifyDto), JsonUtils.beanToJson(loginUser));

            // 删除分类
            commonDesignClassifyService.deleteDesignClassify(designClassifyDto, loginUser);
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
