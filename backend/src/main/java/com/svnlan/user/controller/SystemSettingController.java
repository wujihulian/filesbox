package com.svnlan.user.controller;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.service.DingEventService;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.dto.UserDTO;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.PluginDto;
import com.svnlan.user.dto.SystemOptionDTO;
import com.svnlan.user.service.SystemSettingService;
import com.svnlan.user.tools.OptionTool;
import com.svnlan.user.vo.DingConfigVo;
import com.svnlan.user.vo.PluginListVo;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.TenantUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/8 15:07
 */
@RestController
public class SystemSettingController {
    @Resource
    JWTTool jwtTool;

    @Resource
    SystemSettingService systemSettingService;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    TenantUtil tenantUtil;
    @Resource
    OptionTool optionTool;
    @Resource
    DingEventService dingEventService;

    /**
       * @Description: 获取系统设置信息
       * @params:  [request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/2/8 15:59
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/setting", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSystemSetting(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        Map<String, Object> map = null;
        try {
            map = systemSettingService.getSystemSetting(loginUser);
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
        result = new Result(CodeMessageEnum.success.getCode(), map);
        return JsonUtils.beanToJson(result);
    }

    /**
       * @Description: 系统设置
       * @params:  [userDTO]
       * @Return:  com.svnlan.common.Result
       * @Author:  sulijuan
       * @Date:  2023/2/8 15:59 
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/setSetting", method = RequestMethod.POST)
    public Result setSetting(@RequestBody SystemOptionDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        DingConfigVo dingConfigOld = optionTool.getDingDingConfig();
        try {
            systemSettingService.setSystemSetting(prefix, optionDTO, loginUser);
            stringRedisTemplate.delete(GlobalConfig.systemConfig_captcha);
            stringRedisTemplate.delete(GlobalConfig.systemConfig_dingding_captcha+loginUser.getTenantId());
            stringRedisTemplate.delete(GlobalConfig.systemConfig_enWebchat_captcha+loginUser.getTenantId());
            stringRedisTemplate.delete(GlobalConfig.systemConfig_cube_captcha+loginUser.getTenantId());
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        } finally {
            DingConfigVo dingConfigNew = optionTool.getDingDingConfig();
            String clientIdOld = !ObjectUtils.isEmpty(dingConfigOld) && !ObjectUtils.isEmpty(dingConfigOld.getAppKey()) ? dingConfigOld.getAppKey() : "";
            String clientSecretOld = !ObjectUtils.isEmpty(dingConfigOld) && !ObjectUtils.isEmpty(dingConfigOld.getAppSecret()) ? dingConfigOld.getAppSecret() : "";
            String clientIdNew = !ObjectUtils.isEmpty(dingConfigNew) && !ObjectUtils.isEmpty(dingConfigNew.getAppKey()) ? dingConfigNew.getAppKey() : "";
            String clientSecretNew = !ObjectUtils.isEmpty(dingConfigOld) && !ObjectUtils.isEmpty(dingConfigOld.getAppSecret()) ? dingConfigOld.getAppSecret() : "";
            boolean isStart = false;
            if (!ObjectUtils.isEmpty(clientIdNew) && !clientIdNew.equals(clientIdOld)){
                isStart = true;
            }
            if (!isStart && !ObjectUtils.isEmpty(clientSecretNew) && !clientSecretNew.equals(clientSecretOld)){
                isStart = true;
            }
            if (isStart){
                String clientSecret = dingConfigNew.getAppSecret();
                LogUtil.info("DingTalk connection clientId is：" + clientIdNew + "clientSecret is：" + clientSecret );
                try {
                    dingEventService.configureStreamClient(clientIdNew, clientSecret);
                }catch (Exception e){
                    LogUtil.error(e, "修改钉钉配置重启configureStreamClient失败");
                }
            }
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }

    /**
       * @Description: 插件列表
       * @params:  [request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/4/19 11:52
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/pluginList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String pluginList(HttpServletRequest request, PluginDto optionDTO) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        List map = null;
        try {
            map = systemSettingService.pluginList(optionDTO);
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
        result = new Result(CodeMessageEnum.success.getCode(), map);
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/setPlugin", method = RequestMethod.POST)
    public Result setPlugin(@RequestBody PluginDto optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            systemSettingService.setPlugin(prefix, optionDTO, loginUser);
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
       * @Description: 消息预警
       * @params:  [request, optionDTO]
       * @Return:  java.lang.String
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/msgWarning", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String msgWarningList(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        PluginListVo map = null;
        try {
            map = systemSettingService.getMsgWarningSetting(tenantUtil.getTenantIdByServerName());
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
        result = new Result(CodeMessageEnum.success.getCode(), map);
        return JsonUtils.beanToJson(result);
    }
    @RequestMapping(value = "/api/disk/setMsgWarning", method = RequestMethod.POST)
    public Result setMsgWarning(@RequestBody PluginDto optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        try {
            systemSettingService.setMsgWarning(prefix, optionDTO, loginUser);

        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        } finally {
            // 删除消息预警缓存
            stringRedisTemplate.delete(GlobalConfig.mesWarning_cache_key);
        }
        return new Result(CodeMessageEnum.success.getCode(), null);
    }

    @RequestMapping(value = "/api/disk/getWingSystemInfo", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getWingSystemInfo(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        Map<String, Object>  map = null;
        try {
            map = systemSettingService.getSystemInfo();
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
        result = new Result(CodeMessageEnum.success.getCode(), map);
        return JsonUtils.beanToJson(result);
    }
}
