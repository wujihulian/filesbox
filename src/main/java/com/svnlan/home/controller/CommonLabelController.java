package com.svnlan.home.controller;

import com.svnlan.common.GlobalConfig;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.CheckFileDTO;
import com.svnlan.home.dto.LabelDto;
import com.svnlan.home.service.CommonLabelService;
import com.svnlan.home.utils.UserAuthTool;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @Author: sulijuan
 * @Description: 标签
 * @Date: 2023/3/2 10:52
 */
@RestController
public class CommonLabelController {
    @Resource
    LoginUserUtil loginUserUtil;

    @Resource
    CommonLabelService commonLabelService;
    @Resource
    UserAuthTool userAuthTool;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @RequestMapping(value = "/api/disk/tag/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addTag(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            commonLabelService.addTag(labelDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "添加标签" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/tag/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String editTag(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            commonLabelService.editTag(labelDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "编辑标签" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/tag/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String list(LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            List<CommonLabelVo> list = commonLabelService.getTagList(labelDto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), list);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "编辑标签" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/tag/fileTag", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String fileTag(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            commonLabelService.fileTag(labelDto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "操作关联标签" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
    @RequestMapping(value = "/api/disk/tag/del", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String delTag(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            commonLabelService.delTag(labelDto, loginUser);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "编辑标签" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 标签下的文件置顶
     * @params:  [updateFileDTO]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/tag/moveTop", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String moveTop(@Valid @RequestBody CheckFileDTO labelDto){
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            boolean success = commonLabelService.moveTop(labelDto, resultMap, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e){
            LogUtil.error(e, "标签下的文件置顶失败" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }
    /**
     * @Description: 标签下的文件置底
     * @params:  [updateFileDTO]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/tag/moveBottom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String moveBottom(@Valid @RequestBody CheckFileDTO labelDto){
        Result result;
        Map<String, Object> resultMap = new HashMap<>(1);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            boolean success = commonLabelService.moveBottom(labelDto, resultMap, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), resultMap);
            stringRedisTemplate.opsForHash().getOperations().expire(GlobalConfig.homeExplorerRedisKey + loginUser.getUserID(), 1, TimeUnit.MILLISECONDS);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), resultMap);
        } catch (Exception e){
            LogUtil.error(e, "标签下的文件置底失败" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), resultMap);
        }
        return JsonUtils.beanToJson(result);
    }



    /**
     * @Description: 标签夹置顶
     * @params:  [updateFileDTO]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/tag/top", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String tagTop(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            boolean success = commonLabelService.tagTop(labelDto, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "标签置顶失败" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
    /**
     * @Description: 标签置底
     * @params:  [updateFileDTO]
     * @Return:  java.lang.String
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/tag/bottom", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String tagBottom(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            // 校验标签权限
            userAuthTool.checkUserTagPermission(loginUser);

            boolean success = commonLabelService.tagBottom(labelDto, loginUser);
            result = new Result(success, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "标签置底失败" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
}
