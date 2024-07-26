package com.svnlan.interceptor;

import com.svnlan.common.Result;
import com.svnlan.exception.CodeMessageEnum;
import com.svnlan.exception.SvnlanRuntimeException;
import com.svnlan.utils.LogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

/**
 * 异常处理
 *
 * @author lingxu 2023/03/21 10:31
 */
@Slf4j
@Profile({"pro", "pre", "test"})
@RestControllerAdvice(basePackages = {
        "com.svnlan.home.controller",
        "com.svnlan.user.controller",
        "com.svnlan.jwt.controller",
        "com.svnlan.manager.controller",
        "com.svnlan.webdav"})
public class HandlerExceptionAdvice {

    /**
     * 处理业务 数据上的一些错误
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public Result handlerIllegalArgumentException(IllegalArgumentException ex) {
        LogUtil.error(ex, "错误类型 => {}", ex.toString());
        return Result.returnError(ex.getMessage());
    }

    /**
     * 参数未校验通过的
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MissingServletRequestParameterException.class})
    public Result MethodArgumentNotValidExceptionHandler(Exception ex) {
        StringBuilder detailMessage = new StringBuilder();
        if (ex instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException ex1 = (MethodArgumentNotValidException) ex;
            List<ObjectError> list = ex1.getBindingResult().getAllErrors();
            list.forEach(a -> detailMessage.append(a.getDefaultMessage()).append(" "));
            return Result.returnError(detailMessage.toString());
        } else {
            MissingServletRequestParameterException ex1 = (MissingServletRequestParameterException) ex;
            detailMessage.append(ex1.getMessage());
        }
        LogUtil.error(ex, "错误类型 => {}", ex.toString());
        ex.printStackTrace();
        return Result.returnError(CodeMessageEnum.shareErrorParam);
    }

    @ExceptionHandler({BadSqlGrammarException.class})
    public Result handlerSQLException(Exception ex) {
        LogUtil.error(ex, "错误类型 => {}", ex.toString());
        return Result.returnError("系统错误，请联系管理员");
    }

    @ExceptionHandler(SvnlanRuntimeException.class)
    public Result HandlerException(SvnlanRuntimeException ex) {
        LogUtil.error(ex, "错误类型 => {}", ex.toString());
        return new Result(false, ex.getErrorCode(), null);
    }

    @ExceptionHandler(Exception.class)
    public Result handlerException(Exception ex) {
        log.error("错误类型 => {}", ex.toString());
        ex.printStackTrace();
        return Result.returnError("系统内部错误，请联系管理员");
    }

}
