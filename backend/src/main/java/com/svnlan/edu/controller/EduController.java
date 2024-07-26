package com.svnlan.edu.controller;

import com.svnlan.common.Result;
import com.svnlan.edu.domain.CatalogueResult;
import com.svnlan.edu.domain.EduInfo;
import com.svnlan.edu.domain.EduType;
import com.svnlan.edu.dto.EduDto;
import com.svnlan.edu.service.EduService;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.PageResult;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class EduController {
    @Resource
    JWTTool jwtTool;
    @Resource
    EduService eduService;

    /**
     * @Description: 通用教学分类树
     * @params: [dto]
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/edu/getTeachTypeTree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTeachTypeTree(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;

        try {
            List<EduType> list = eduService.getTeachTypeTree();

            result = new Result(true, CodeMessageEnum.success.getCode(), list);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, prefix + "error 失败" );
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * 搜索页-教学分类树 每个分类下包含全部
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/disk/edu/getTeachTypeTreeAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getTeachTypeTreeAll(HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;

        try {
            List<EduType> list = eduService.getTeachTypeTreeAll();

            result = new Result(true, CodeMessageEnum.success.getCode(), list);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, prefix + "error 失败" );
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * 2 学科课程-目录列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/disk/edu/catalogue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String catalogue(EduDto eduDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;

        try {
            CatalogueResult re = eduService.catalogue(eduDto);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, prefix + "error 失败" );
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * 2 搜索页-搜索课时
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/disk/edu/searchCourseList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String searchCourseList(EduDto eduDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        try {
            PageResult re = eduService.searchCourseList(eduDto);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, prefix + "error 失败" );
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * 5.拓展课程-拓展课程列表
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/disk/edu/courseCateList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String courseCateList(EduDto eduDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        try {
            PageResult re = eduService.courseCateList(eduDto);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, prefix + "error 失败" );
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }

    /**
     * 3.学科课程-标签关联的课时
     * @param request
     * @return
     */
    @RequestMapping(value = "/api/disk/edu/tagCourseList", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String tagCourseList(EduDto eduDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        try {
            PageResult re = eduService.tagCourseList(eduDto);
            result = new Result(true, CodeMessageEnum.success.getCode(), re);
        } catch (SvnlanRuntimeException e) {
            result = new Result(false, e.getErrorCode(), e.getMessage());
        } catch (Exception e) {
            LogUtil.error(e, prefix + "error 失败" );
            result = new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return JsonUtils.beanToJson(result);
    }
}
