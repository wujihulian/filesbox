package com.svnlan.manage.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.manage.dto.AddDesignDTO;
import com.svnlan.manage.dto.DeleteDesignDTO;
import com.svnlan.manage.dto.EditDesignDTO;
import com.svnlan.manage.dto.GetDesignListDTO;
import com.svnlan.manage.service.CommonDesignService;
import com.svnlan.manage.vo.DesignDetailVO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * 功能描述: 编辑器控制器
 * @param:
 * @return:
 */

@RestController
public class CommonDesignController {
    @Resource
    private CommonDesignService commonDesignService;
    @Autowired
    private JWTTool jwtTool;
    @Autowired
    private LoginUserUtil loginUserUtil;

    /**
     * 功能描述: 添加编辑器操作
     *
     * @param:
     * @return:
     */
    @RequestMapping(value = "/api/disk/addCommonDesign" , method = RequestMethod.POST)
    public Result addCommonDesign(@Valid @RequestBody AddDesignDTO dto) {
        try {
            Long designId = commonDesignService.addCommonDesign(dto);
            return new Result(true, CodeMessageEnum.success.getCode(), designId);
        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "添加装扮失败 dto："+JsonUtils.beanToJson(dto) + ", e:"+e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
       * @Description: 编辑详情
       * @params:  [commonSourceDTO]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/editCommonDesign", method = RequestMethod.PUT)
    public Result editDesignDetail(@Valid @RequestBody EditDesignDTO editDesignDTO){
        try {
            boolean isSuccess = commonDesignService.editDesignDetail(editDesignDTO);
            if (isSuccess) {
                return new Result(true, CodeMessageEnum.success.getCode(), null);
            }
        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, "编辑装扮详情失败 dto："+JsonUtils.beanToJson(editDesignDTO) + ", e:"+e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
       * @Description: 装扮列表
       * @params:  []
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/getDesignList", method = RequestMethod.GET)
    public Result getDesignList(@Valid GetDesignListDTO getDesignListDTO){
        try{
            Map<String, Object> map = commonDesignService.getDesignList(getDesignListDTO);
            return new Result(true, CodeMessageEnum.success.getCode(), map.get("list"));

        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "查询装扮列表失败,  e:"+e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
       * @Description: 装扮详情
       * @params:  [designId]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/getDesignDetail", method = RequestMethod.GET)
    public String getDesignDetail(Long designId){
        Result result;
        try {
            DesignDetailVO designDetailVO = commonDesignService.getDesignDetail(designId);
            result = new Result(true, CodeMessageEnum.success.getCode(), designDetailVO);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "查询装扮详情失败, id:" +designId+ " e:"+e.getMessage());
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
       * @Description: 主题库装扮预览
       * @params:  [designId]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/getDesignPreview", method = RequestMethod.GET)
    public Result getDesignPreview(@RequestParam("designId") Long designId){
        try {
            DesignDetailVO designDetailVO = commonDesignService.getDesignPreview(designId);
            return new Result(true, CodeMessageEnum.success.getCode(), designDetailVO);
        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "查询装扮预览失败, id:" +designId+ " e:"+e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
       * @Description: 删除装扮
       * @params:  [designId]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/deleteCommonDesign", method = RequestMethod.DELETE)
    public Result deleteCommonDesign(@RequestBody DeleteDesignDTO deleteDesignDTO){
        try {
            boolean isSuccess = commonDesignService.deleteCommonDesign(deleteDesignDTO);
            if (isSuccess) {
                return new Result(true, CodeMessageEnum.success.getCode(), null);
            }
        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "删除装扮详情失败, dto:" + JsonUtils.beanToJson(deleteDesignDTO)+ " e:"+e.getMessage());
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
       * @Description: 启用装扮
       * @params:  [editDesignDTO]
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/setDesignUsing", method = RequestMethod.PUT)
    public Result setUsing(@Valid @RequestBody EditDesignDTO editDesignDTO){
        try {
            boolean isSuccess = commonDesignService.setUsing(editDesignDTO);
            if (isSuccess) {
                return new Result(true, CodeMessageEnum.success.getCode(), null);
            }
        } catch (SvnlanRuntimeException e){
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e, "启用装扮失败, dto:" + JsonUtils.beanToJson(editDesignDTO));
        }
        return new Result(false, CodeMessageEnum.system_error.getCode(), null);
    }

    /**
       * @Description: 获取启用的主页
       * @params:  [clientType]
       * @Return:  java.lang.String
       * @Modified:
       */
    @RequestMapping(value = "/api/disk/getUsingDesign", method = RequestMethod.GET)
    public String getUsingDesign(String clientType){
        Result result;
        try {
            DesignDetailVO designDetailVO = commonDesignService.getUsingDesign(clientType);
            result = new Result(true, CodeMessageEnum.success.getCode(), designDetailVO);
        } catch (SvnlanRuntimeException e){
            result = new Result(false, e.getErrorCode(), null);
        } catch (Exception e){
            LogUtil.error(e,"获取启用的装扮失败，clientType: " + clientType);
            result = new Result(false, CodeMessageEnum.system_error.getMsg(), null);
        }
        return JsonUtils.beanToJson(result);
    }

}
