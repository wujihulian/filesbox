package com.svnlan.home.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.HomeExplorerDTO;
import com.svnlan.home.service.ExplorerFileService;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/4/25 14:15
 */
@RestController
public class ExplorerFileController {

    @Autowired
    ExplorerFileService explorerFileService;
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;

    /**
       * @Description: 压缩包预览
       * @params:  [homeExp, request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/4/25 14:23
       * @Modified:
       */
    @GetMapping("/api/disk/unzipList")
    public String unzipList(HomeExplorerDTO homeExp, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        Object re;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            if (ObjectUtils.isEmpty(loginUser)){
                loginUser = new LoginUser();
                loginUser.setUserID(0L);
                loginUser.setUserType(4);
                loginUser.setName("demo");
                loginUser.setNickname("游客");
            }
            re = explorerFileService.unzipList(homeExp, loginUser);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), re);
        return JsonUtils.beanToJson(result);
    }
    /**
       * @Description: 查看压缩包是否有密码
       * @params:  [homeExp, request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/5/5 14:54
       * @Modified:
       */
    @GetMapping("/api/disk/checkIsEncrypted")
    public String checkZipIsEncrypted(HomeExplorerDTO homeExp, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        Object re;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            if (ObjectUtils.isEmpty(loginUser)){
                result = new Result(false, CodeMessageEnum.bindSignError.getCode(), null);
                return JsonUtils.beanToJson(result);
            }
            re = explorerFileService.checkZipIsEncrypted(homeExp);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            //处理异常
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e, prefix + "  Exception error!");
            //处理异常
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回平台信息
        result = new Result(true, CodeMessageEnum.success.getCode(), re);
        return JsonUtils.beanToJson(result);
    }

}
