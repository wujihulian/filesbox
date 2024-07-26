package com.svnlan.interceptor;

import com.svnlan.annotation.SpecifiedValue;
import com.svnlan.annotation.VisitHandler;
import com.svnlan.annotation.VisitRecord;
import com.svnlan.jwt.domain.LoginUser;
import com.svnlan.tools.SystemLogTool;
import com.svnlan.utils.CaffeineUtil;
import com.svnlan.utils.LoginUserUtil;
import com.svnlan.utils.VisitRecordExecutor;
import lombok.SneakyThrows;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.data.util.Pair;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.Optional;

/**
 * 访问记录AOP
 *
 * @author lingxu 2023/04/06 16:55
 */
@Aspect
public class VisitRecordAop {
    @Resource
    private VisitRecordExecutor visitRecordExecutor;
    @Resource
    private SystemLogTool systemLogTool;
    @Resource
    private LoginUserUtil loginUserUtil;
    @Resource
    private ThreadPoolTaskExecutor asyncTaskExecutor;

    @Resource
    private VisitHandler visitHandler;

    @Pointcut("@annotation(com.svnlan.annotation.VisitRecord)")
    public void pointcut() {
    }

    @Around(value = "pointcut() && @annotation(visitRecord)")
    public Object operationLogRecord(ProceedingJoinPoint pjp, VisitRecord visitRecord) throws Throwable {
        // first 为 clientType  second 为操作系统 osName
        Pair<Integer, String> pair = systemLogTool.getClientTypeAndOsName(null);
        LoginUser loginUser = Optional.ofNullable(loginUserUtil.getLoginUser()).orElseGet(() -> {
            // 由于用户没有登录，会取不到用户信息，给一个默认的 userId 为0 表示匿名用户
            LoginUser user = new LoginUser();
            user.setUserID(0L);
            return user;
        });
        CaffeineUtil.CURRENT_ONLINE_USER.put(loginUser.getUserID(), new Date().getTime());

        if (visitRecord.isAsync()) {
            asyncTaskExecutor.execute(() -> visitRecordExecutor.executeRecord(pair, loginUser, visitRecord.recordType(), visitRecord.timeType()));
        } else {
            // 同步的方式
            visitRecordExecutor.executeRecord(pair, loginUser, visitRecord.recordType(), visitRecord.timeType());
        }

        if (visitRecord.handle()) {
            // 表示要特殊处理访问数
            MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
            Annotation[][] annotations = methodSignature.getMethod().getParameterAnnotations();

            Object[] args = pjp.getArgs();
            for (int i = 0; i < args.length; i++) {
                for (Annotation annotation : annotations[i]) {
                    if (annotation instanceof SpecifiedValue) {
                        Object value = getValueFromAnnotationSpecified(args[i]);
                        visitHandler.handle(value);
                        break;
                    }
                }
            }
        }
        return pjp.proceed();
    }

    @SneakyThrows
    private Object getValueFromAnnotationSpecified(Object obj) {
        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(SpecifiedValue.class)) {
                field.setAccessible(true);
                return field.get(obj);
            }
        }
        return null;
    }

}
