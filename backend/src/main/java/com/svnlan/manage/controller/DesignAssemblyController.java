package com.svnlan.manage.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.manage.dto.AddAssemblyDTO;
import com.svnlan.manage.dto.DeleteAssemblyDTO;
import com.svnlan.manage.dto.EditAssemblyDTO;
import com.svnlan.manage.dto.GetAssemblyDTO;
import com.svnlan.manage.service.DesignAssemblyService;
import com.svnlan.manage.vo.DesignAssemblyVO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author:
 * @Description: 组件设计控制器(特效，组合)
 * @Date:
 */
@RestController
public class DesignAssemblyController {
    @Resource
    private DesignAssemblyService designAssemblyService;
    @Resource
    JWTTool jwtTool;
    @Resource
    LoginUserUtil loginUserUtil;

    private static final Logger LOGGER = LoggerFactory.getLogger("error");

    /**
       * @Description: 组件列表
       * @params:  [type]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/getAssemblyList", method = RequestMethod.GET)
    public String getAssemblyList(GetAssemblyDTO getAssemblyDTO, HttpServletRequest request){
        String prefix = this.jwtTool.findApiPrefix(request);
        LoginUser loginUser = loginUserUtil.getLoginUser();
        Result result;
        String paramTip = "";
        try{
            paramTip = String.format(" >>> getDesignClassifyList:%s;loginUser:%s",
                    JsonUtils.beanToJson(getAssemblyDTO), JsonUtils.beanToJson(loginUser));
            List<DesignAssemblyVO> assemblyList = designAssemblyService.getAssemblyList(getAssemblyDTO, loginUser);

            result = Result.returnSuccess(assemblyList);

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
       * @Description:
       * @params:  [addAssemblyDTO]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/addDesignAssembly", method = RequestMethod.POST)
    public Result addAssembly(@Valid @RequestBody AddAssemblyDTO addAssemblyDTO){
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            Long assemblyId = designAssemblyService.addAssembly(addAssemblyDTO, loginUser);
            if (assemblyId>0){
                return new Result(true, CodeMessageEnum.success.getCode(), assemblyId);
            }
        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getMessage() , e.getErrorCode(), null);
        } catch (Exception e){
            LOGGER.error("添加装扮组件失败， e：" + e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
       * @Description: 编辑组件
       * @params:  [editAssemblyDTO]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/editDesignAssembly", method = RequestMethod.PUT)
    public Result editAssembly(@Valid @RequestBody EditAssemblyDTO editAssemblyDTO){
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            boolean isSuccess = designAssemblyService.editAssembly(editAssemblyDTO, loginUser);
            if (isSuccess){
                return new Result(true, CodeMessageEnum.success.getCode(), null);
            }
        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getMessage() , e.getErrorCode(), null);
        } catch (Exception e){
            LOGGER.error("编辑装扮组件失败， e:" + e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
       * @Description: 删除组件
       * @params:  [deleteAssemblyDTO]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/deleteDesignAssembly", method = RequestMethod.DELETE)
    public Result deleteAssembly(@Valid @RequestBody DeleteAssemblyDTO deleteAssemblyDTO ){
        LoginUser loginUser = loginUserUtil.getLoginUser();
        try {
            boolean isSuccess = designAssemblyService.deleteAssembly(deleteAssemblyDTO, loginUser);
            if (isSuccess){
                return new Result(true, CodeMessageEnum.success.getCode(), null);
            }
        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getMessage() , e.getErrorCode(), null);
        } catch (Exception e){
            LOGGER.error("删除装扮组件失败， e:" + e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }
}
