package com.svnlan.manage.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.annotation.SpecifiedValue;
import com.svnlan.annotation.VisitRecord;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.AsyncUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.manage.domain.LogImportArticle;
import com.svnlan.manage.dto.CommonInfoDto;
import com.svnlan.manage.service.CommonInfoService;
import com.svnlan.manage.service.ImportConvertPicService;
import com.svnlan.manage.service.ImportHomepageService;
import com.svnlan.manage.vo.CommonInfoVo;
import com.svnlan.manage.vo.ConvertToPicResultDTO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.PageResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/5/10 16:48
 */
@RestController
public class CommonInfoController {

    @Resource
    CommonInfoService commonInfoService;
    @Resource
    ImportHomepageService importHomepageService;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    JWTTool jwtTool;
    @Resource
    ImportConvertPicService importConvertPicService;
    @Resource
    AsyncUtil asyncUtil;

    /**
     * 查看所有资讯列表
     */
    @RequestMapping(value = "/api/disk/getInfoList", method = RequestMethod.GET)
    public String getInfoList(CommonInfoDto infoDto, HttpServletRequest request) {
        PageResult list = null;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> getInfoList:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoDto), JsonUtils.beanToJson(loginUser));

            list = commonInfoService.getInfoList(infoDto, loginUser);
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
     * 新增一条资讯
     */
    @RequestMapping(value = "/api/disk/saveCommonInfo", method = RequestMethod.POST)
    public String saveInfoType(@Valid @RequestBody CommonInfoDto infoDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        Long id;
        try {
            paramTip = String.format(" >>> infoDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoDto), JsonUtils.beanToJson(loginUser));

            // 新增一条
            id = commonInfoService.saveCommonInfo(infoDto, loginUser);
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
     * 修改一条资讯
     */
    @RequestMapping(value = "/api/disk/editCommonInfo", method = RequestMethod.POST)
    public String editCommonInfo(@Valid @RequestBody CommonInfoDto infoDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        Long id;
        try {
            paramTip = String.format(" >>> infoDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoDto), JsonUtils.beanToJson(loginUser));

            // 新增一条
            id = commonInfoService.editCommonInfo(infoDto, loginUser);
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
     * @Description: 资讯的删除操作
     * @params:
     */
    @RequestMapping(value = "/api/disk/deleteInfo", method = RequestMethod.POST)
    public String deleteInfo(@Valid @RequestBody CommonInfoDto infoDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> infoTypeDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoDto), JsonUtils.beanToJson(loginUser));

            // 删除
            commonInfoService.deleteInfo(infoDto, loginUser);
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

    /**
     * @Description: 编辑回显
     * @params: [infoDto, request]
     * @Return: java.lang.String
     * @Author: sulijuan
     * @Date: 2023/5/29 15:16
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/getCommonInfo", method = RequestMethod.GET)
    public String getCommonInfo(CommonInfoDto infoDto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        CommonInfoVo re;
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> infoDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoDto), JsonUtils.beanToJson(loginUser));

            // 查询某一条咨询信息
            re = commonInfoService.getCommonInfo(infoDto, loginUser);
            result = Result.returnSuccess(re);
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
     * @Description: 导入资讯
     * @params: [importHomepageDTO, response]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/importHomepage", method = RequestMethod.POST)
    public String importHomepage(@RequestBody CommonInfoDto infoDto) {
        Result result;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogImportArticle re = importHomepageService.importHomepage(infoDto, loginUser);
            result = Result.returnSuccess(re);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error("导入文章失败, ", e.getErrorCode() + ", " + e.getMessage());
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error(e, "导入资讯失败");
            result = Result.returnError(CodeMessageEnum.system_error);
        }
        return JsonUtils.beanToJson(result);

    }

    /**
     * @param sourceID
     * @param request
     * @return java.lang.String
     * @description: 发送将PPT、PDF、WORD转图片的指令
     */
    @GetMapping(value = "/api/disk/convertToPic")
    public String convertToPic(HttpServletRequest request, @RequestParam(value = "sourceID", required = false) Long sourceID) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> sourceID:%d;loginUser:%s",
                    sourceID, JsonUtils.beanToJson(loginUser));
            //
            this.importConvertPicService.convertToPic(prefix, sourceID, loginUser);
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

    /**
     * @param request
     * @param sourceID
     * @return java.lang.String
     * @description: 查PPT、PDF、WORD转图片的结果
     */
    @GetMapping(value = "/api/disk/getConvertPicResult")
    public String getConvertPicResult(HttpServletRequest request, @RequestParam(value = "sourceID", required = false) Long sourceID) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> sourceID:%d;loginUser:%s",
                    sourceID, JsonUtils.beanToJson(loginUser));
            //
            ConvertToPicResultDTO resultDTO = this.importConvertPicService.getConvertPicResult(prefix, sourceID, loginUser);
            result = Result.returnSuccess(resultDTO);
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
     * @Description: 违规词检查
     * @params: [infoDto]
     * @Return: java.lang.String
     * @Modified:
     */
    @PostMapping(value = "/api/disk/checkBanWord")
    public String checkBanWord(@RequestBody CommonInfoDto infoDto) {
        Result result = null;
        List<String> re = null;
        try {
            re = importConvertPicService.checkBanWord(infoDto);
            result = Result.returnSuccess(re);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error("checkBanWord :" + e);
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error("checkBanWord :" + e);
            result = Result.returnError(CodeMessageEnum.system_error);
        }
        return JsonUtils.beanToJson(result);
    }


    /**
     * 资讯详情页
     */
    @VisitRecord(handle = true)
    @RequestMapping(value = "/api/disk/getHomepageDetail", method = RequestMethod.GET)
    public String getHomepageDetail(@SpecifiedValue CommonInfoDto infoDto, HttpServletRequest request) {
        CommonInfoVo commonInfoVo;
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        String paramTip = "";
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            paramTip = String.format(" >>> infoDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(infoDto), JsonUtils.beanToJson(loginUser));
            // 查询某一条咨询信息
            commonInfoVo = this.commonInfoService.getHomepageDetail(infoDto,
                    loginUser);
            result = Result.returnSuccess(commonInfoVo);
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
     * @Description: 设置置顶
     * @params: [saveHomepageInfoDTO]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/infoToTop", method = RequestMethod.POST)
    public String infoTop(@RequestBody CommonInfoDto commonInfoDto) {
        Result result;
        try {
            CommonInfoVo commonInfoVo = commonInfoService.homePageInfoToTop(commonInfoDto);

            result = Result.returnSuccess(commonInfoVo);
        } catch (SvnlanRuntimeException e) {
            result = Result.returnSvnException(e);
        } catch (Exception e) {
            LogUtil.error(e, "设置置顶失败");
            result = Result.returnError(CodeMessageEnum.system_error);
        }
        return JsonUtils.beanToJson(result);

    }

    /**
     * @Description: 上下移动排序
     * @params: [commonInfoDto, request]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/modifySort", method = RequestMethod.POST)
    public String modifyPageinfoSort(@RequestBody CommonInfoDto commonInfoDto,
                                     HttpServletRequest request) {
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            paramTip = String.format(" >>> commonInfoDto:%s;loginUser:%s",
                    JsonUtils.beanToJson(commonInfoDto), JsonUtils.beanToJson(loginUser));

            commonInfoService.modifyPageinfoSort(loginUser, commonInfoDto);
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

    @RequestMapping(value = "/api/disk/getHomepagePageSort", method = RequestMethod.GET)
    public String getHomepagePageSort(CommonInfoDto dto, HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        List<Integer> sortList;
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            paramTip = String.format(" >>> dto:%s;loginUser:%s",
                    JsonUtils.beanToJson(dto), JsonUtils.beanToJson(loginUser));

            // 根据查询条件获取资讯信息的列表
            sortList = commonInfoService.getHomepagePageSort(dto, loginUser);
            result = Result.returnSuccess(sortList);
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
     * @Description: 审核资讯
     * @params: [verifyHomepageInfoDTO, request]
     * @Return: java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/verifyHomepageInfo", method = RequestMethod.POST)
    public String verifyHomepageInfo(@RequestBody CommonInfoDto infoDto,
                                     HttpServletRequest request) {
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String prefix = this.jwtTool.findApiPrefix(request);
        String paramTip = "";
        try {
            CommonInfoVo vo = commonInfoService.verifyHomepageInfo(infoDto, loginUser);
            result = Result.returnSuccess(new HashMap<String, Object>(1) {{
                put("state", 1);
            }});

            infoDto.setInfoIdList(Arrays.asList(infoDto.getInfoID()));
            asyncUtil.genInformationPage(infoDto, loginUser);
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
     * 设置资讯点赞
     */
    @PostMapping("/api/common/like")
    public Result operateLike(@RequestBody JSONObject jsonObj) {
        Long id = Optional.ofNullable(jsonObj.getLong("infoID")).orElseThrow(() -> new IllegalArgumentException("infoID 不能为空"));
        Integer isLike = Optional.ofNullable(jsonObj.getInteger("isLike")).orElseThrow(() -> new IllegalArgumentException("isLike 不能为空"));

        commonInfoService.operateLike(id, isLike);
        return Result.returnSuccess();
    }
}
