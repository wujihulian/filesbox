package com.svnlan.home.controller;

import com.alibaba.fastjson.JSONObject;
import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.ExplorerOperationsDTO;
import com.svnlan.home.dto.ExplorerShareDTO;
import com.svnlan.home.service.ExplorerOperationsService;
import com.svnlan.home.vo.HomeExplorerVO;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.UserDTO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


/**
 * @author KingMgg
 * @data 2023/2/7 16:07
 */
@Api("用户操作")
@RestController
public class ExplorerOperationsController {

    @Autowired
    ExplorerOperationsService operationsService;
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;


    /**
     * 初始化io_source的 parentLevel 父路径id; 例如:  ,0,2,5,10,
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/api/disk/initSourcePathLevel", method = RequestMethod.GET)
    public String initSourcePathLevel(ExplorerOperationsDTO dto) {
        Result result;
        try {
             operationsService.initSourcePathLevel(dto.getSourceID());
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e,  " initSourcePathLevel Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, " initSourcePathLevel Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
       * @Description: 根据入参统计文件夹的size
       * @params:  [operation 需要统计的文件夹ID（多个用英文逗号,隔开）,status=1则统计未删除的，不传默认都统计]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/9/9 12:51
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/countSize", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String zip(@RequestBody CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        if (ObjectUtils.isEmpty(loginUser.getUserType()) || loginUser.getUserType().intValue() != 1) {
            result = new Result(false, CodeMessageEnum.errorAdminAuth.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        try {
            operationsService.countSize(updateFileDTO, resultMap, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "压缩zip失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/setSafePwd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String setSafePwd(@RequestBody CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();

        try {
            operationsService.setSafePwd(updateFileDTO, resultMap, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "设置私密密码箱密码" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/checkSafePwd", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String checkSafePwd(@RequestBody CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();

        try {
            operationsService.checkSafePwd(updateFileDTO, resultMap, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "校验私密密码箱密码" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }


    @RequestMapping(value = "/api/disk/file/send", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendFile(@RequestBody UserDTO userDTO) {
        JSONObject check;
        Result result;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            //发送验证码
            check = operationsService.sendEmailFile(userDTO, loginUser);

        } catch (SvnlanRuntimeException e) {
            //处理异常
            result = new Result(Boolean.FALSE, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception ex) {
            //处理异常
            result = new Result(Boolean.FALSE, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回操作成功信息
        result = new Result(Boolean.TRUE, CodeMessageEnum.success.getCode(), check);
        return JsonUtils.beanToJson(result);
    }
    @RequestMapping(value = "/api/disk/file/checkSend", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String checkSend(@RequestBody UserDTO userDTO) {
        JSONObject check;
        Result result;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            //发送验证码
            check = operationsService.sendEmailCheck(userDTO, loginUser);

        } catch (SvnlanRuntimeException e) {
            //处理异常
            result = new Result(Boolean.FALSE, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception ex) {
            //处理异常
            result = new Result(Boolean.FALSE, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        String code = check.getString("code");
        if ("0".equals(code)){
            // 返回操作成功信息
            result = new Result(Boolean.TRUE, CodeMessageEnum.success.getCode(), check);
        }else {
            // 返回操作成功信息
            result = new Result(Boolean.FALSE, CodeMessageEnum.sendFail.getCode(), check);
        }
        return JsonUtils.beanToJson(result);
    }
}
