package com.svnlan.home.controller;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.service.FileDocConvertService;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/28 13:40
 */
@RestController
public class FileDocConvertController {

    @Resource
    FileDocConvertService fileDocConvertService;
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    /**
       * @Description: 文件互转功能
       * @params:  [updateFileDTO, request]
       * @Return:  java.lang.String
       * @Author:  sulijuan
       * @Date:  2023/6/28 14:02
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/doc/convert", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String doc2Convert(@RequestBody CheckFileDTO updateFileDTO, HttpServletRequest request){
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        Map<String, Object> re = null;
        try {
            LoginUser loginUser = loginUserUtil.getLoginUser();
            LogUtil.info(prefix + " videoCommonDto=" + JsonUtils.beanToJson(updateFileDTO));
            re = fileDocConvertService.doc2Convert(updateFileDTO, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, prefix + "文件互转失败 " + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        // 视频转码
        /*if (!CollectionUtils.isEmpty(list)) {
            for (CommonSource source : list) {
                ConvertDTO convertDTO = new ConvertDTO();
                convertDTO.setBusId(source.getSourceID());
                convertDTO.setBusType("cloud");
                convertDTO.setOtherType("docConvert");
                String serverUrl = HttpUtil.getRequestRootUrl(null);
                source.setDomain(serverUrl);
                convertDTO.setDomain(serverUrl);
                convertUtil.doConvert(convertDTO, source);
            }
        }*/
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/doc/convert/taskAction", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String zipTaskAction(HttpServletResponse response, CheckFileDTO updateFileDTO) {
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        boolean success = true;
        try {
            success = fileDocConvertService.taskAction(updateFileDTO, resultMap, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e) {
            LogUtil.error(e, "taskAction转换失败" + JsonUtils.beanToJson(updateFileDTO));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        if (!ObjectUtils.isEmpty(resultMap) && resultMap.containsKey("status") && "1".equals(resultMap.get("status").toString())) {
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        }
        return JsonUtils.beanToJson(result);
    }
}
