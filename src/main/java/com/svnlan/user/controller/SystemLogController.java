package com.svnlan.user.controller;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import com.alibaba.fastjson.JSON;
import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.home.utils.ObjUtil;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.jwt.tool.JWTTool;
import com.svnlan.user.dto.SystemLogDto;
import com.svnlan.user.service.SystemLogService;
import com.svnlan.user.vo.OperateLogExportVO;
import com.svnlan.user.vo.SystemLogExportVO;
import com.svnlan.utils.JsonUtils;
import com.svnlan.utils.LogUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.PageResult;
import lombok.SneakyThrows;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/24 14:16
 */
@RestController
public class SystemLogController {
    @Resource
    JWTTool jwtTool;
    @Resource
    SystemLogService systemLogService;
    @Resource
    LoginUserUtil loginUserUtil;

    /**
     * @Description: 获取系统日志
     * @params:  [request]
     * @Return:  java.lang.String
     * @Author:  sulijuan
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/log/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getSystemSetting(SystemLogDto systemLogDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        PageResult re = null;
        try {
            re = systemLogService.getSystemLogList(systemLogDto, loginUser);
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
        result = new Result(CodeMessageEnum.success.getCode(), re);
        return JsonUtils.beanToJson(result);
    }

    /** 用户个人文件日志 */
    @RequestMapping(value = "/api/disk/user/log/get", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getUserLog(SystemLogDto systemLogDto, HttpServletRequest request) {
        String prefix = this.jwtTool.findApiPrefix(request);
        Result result;
        LoginUser loginUser = loginUserUtil.getLoginUser(request);
        PageResult re = null;
        try {
            systemLogDto.setUserID(loginUser.getUserID());
            re = systemLogService.getSystemLogList(systemLogDto, loginUser);
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
        result = new Result(CodeMessageEnum.success.getCode(), re);
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 登录日志导出
     * @params:  [systemLogDto, response]
     * @Return:  java.lang.String
     * @Modified:
     */
    @SneakyThrows
    @RequestMapping(value = "/api/disk/exportLogExcel", method = RequestMethod.POST)
    public String exportLogExcel(@RequestBody SystemLogDto systemLogDto, HttpServletResponse response) {

        Result result;
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            LogUtil.error(e, "[exportLogExcel=>错误]");
        }
        try {
            //获取需要导出的数
            List<SystemLogExportVO> excList = systemLogService.getSystemLogExportList(systemLogDto);
            if (CollectionUtils.isEmpty(excList)) {
                SystemLogExportVO vo = new SystemLogExportVO();
                ObjUtil.initializefield(vo);
                excList.add(vo);
            }
            LogUtil.info("@@ 日志导出  /api/disk/exportLogExcel   参数:" + systemLogDto + "excList=" + JsonUtils.beanToJson(excList));
            //导出excel
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("登录日志", "sheet1"), SystemLogExportVO.class, excList);

            // 设置下载的头信息 使用URLEncoder.encode是为了解决编码问题
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode("登录日志.xls", "utf-8"));


            // 用workbook对象直接写出输出流就可以
            workbook.write(os);
            workbook.close();
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e,"downloadLogOperate dto {}"+ JSON.toJSONString(systemLogDto));
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e,"downloadLogOperate dto error {}"+ JSON.toJSONString(systemLogDto));
            result = new Result(false, CodeMessageEnum.explorerError.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回成功信息
        result = new Result(CodeMessageEnum.success.getCode(), null);
        return JsonUtils.beanToJson(result);
    }

    /**
     * @Description: 操作日志导出
     * @params:  [systemLogDto, response]
     * @Return:  java.lang.String
     * @Author:  sulijuan
     * @Date:  2023/7/27 14:07
     * @Modified:
     */
    @RequestMapping(value = "/api/disk/downloadLogOperate", method = RequestMethod.POST)
    public String downloadLogOperate(@RequestBody SystemLogDto systemLogDto, HttpServletResponse response) {

        Result result;
        ServletOutputStream os = null;
        try {
            os = response.getOutputStream();
        } catch (IOException e) {
            LogUtil.error(e, "[exportLogExcel=>错误]");
        }
        LogUtil.info("@@ 日志导出  /api/disk/downloadLogOperate   参数:" + systemLogDto);
        try {
            //获取需要导出的数
            List<OperateLogExportVO> excList = systemLogService.getOperateLogExportList(systemLogDto);
            if (CollectionUtils.isEmpty(excList)) {
                OperateLogExportVO vo = new OperateLogExportVO();
                ObjUtil.initializefield(vo);
                excList.add(vo);
            }
            //导出excel
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("操作日志", "sheet1"), OperateLogExportVO.class, excList);

            // 设置下载的头信息 使用URLEncoder.encode是为了解决编码问题
            response.setHeader("Content-Disposition", "attachment;filename=" +
                    URLEncoder.encode("操作日志.xls", "utf-8"));


            // 用workbook对象直接写出输出流就可以
            workbook.write(os);
            workbook.close();
        } catch (SvnlanRuntimeException e) {
            LogUtil.error(e,"downloadLogOperate dto {}"+ JSON.toJSONString(systemLogDto));
            result = new Result(false, e.getErrorCode(), null);
            return JsonUtils.beanToJson(result);
        } catch (Exception e) {
            LogUtil.error(e,"downloadLogOperate dto error {}"+ JSON.toJSONString(systemLogDto));
            result = new Result(false, CodeMessageEnum.explorerError.getCode(), null);
            return JsonUtils.beanToJson(result);
        }
        // 返回成功信息
        result = new Result(CodeMessageEnum.success.getCode(), null);
        return JsonUtils.beanToJson(result);
    }


}
