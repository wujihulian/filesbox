package com.svnlan.annotation;

import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.lang.annotation.*;

/**
 * 开启访问数统计
 *
 * @author lingxu 2023/04/11 15:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(MyImportSelector.class)
@EnableAspectJAutoProxy(exposeProxy = true,proxyTargetClass = true)
@EnableScheduling
public @interface EnableVisitRecord {
}
