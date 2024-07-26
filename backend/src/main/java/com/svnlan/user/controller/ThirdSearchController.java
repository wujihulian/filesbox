package com.svnlan.user.controller;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.SearchCourseDTO;
import com.svnlan.user.service.ThirdSearchService;
import com.svnlan.user.vo.TeachTypeTree;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.PageResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class ThirdSearchController {
    @Resource
    LoginUserUtil loginUserUtil;
    @Resource
    JWTTool jwtTool;
    @Resource
    ThirdSearchService thirdSearchService;


    /**
     * @Description: 搜索页-搜索课时
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/third/searchCourseList", method = RequestMethod.GET)
    public Result searchCourseList(SearchCourseDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        PageResult data = null;
        try {
            data = thirdSearchService.searchCourseList(prefix, optionDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), data);
    }


    /**
     * @Description: 搜索页-教学分类树 每个分类下包含全部
     * @Modified:https://k.zjer.cn/api/s/x/teachType/treeHasAll
     */
    @RequestMapping(value = "/api/disk/third/treeHasAll", method = RequestMethod.GET)
    public Result treeHasAll(SearchCourseDTO optionDTO, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        List<TeachTypeTree> data = null;
        try {
            data = thirdSearchService.treeHasAll(prefix, optionDTO);
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e, prefix + " Svnlan error!");
            return new Result(false, e.getErrorCode(), null);
        } catch (Exception e) {
            LogUtil.error(e, prefix + " Exception error!");
            return new Result(false, CodeMessageEnum.system_error.getCode(), null);
        }
        return new Result(CodeMessageEnum.success.getCode(), data);
    }
}
