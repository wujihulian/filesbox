package com.svnlan.manage.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.dto.LabelDto;
import com.svnlan.home.service.CommonLabelService;
import com.svnlan.home.vo.CommonLabelVo;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/6/1 10:11
 */
@RestController
public class InfoTagController {

    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    CommonLabelService commonLabelService;

    @RequestMapping(value = "/api/disk/infoTag/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String infTagList(LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            List<CommonLabelVo> list = commonLabelService.getInfoLabelList(labelDto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), list);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "资讯标签列表" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/infoTag/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String addTag(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            labelDto.setTagType(2);
            commonLabelService.addTag(labelDto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "添加标签" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/infoTag/edit", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String editTag(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            labelDto.setTagType(2);
            commonLabelService.editTag(labelDto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "编辑资讯标签" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    @RequestMapping(value = "/api/disk/infoTag/del", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String delTag(@Valid @RequestBody LabelDto labelDto){
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            labelDto.setTagType(2);
            commonLabelService.delTag(labelDto, loginUser);
            result = new Result(true, CodeMessageEnum.success.getCode(), null);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "编辑资讯标签" + JsonUtils.beanToJson(labelDto));
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
}
