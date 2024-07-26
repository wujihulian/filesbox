package com.svnlan.annotation;

import java.lang.annotation.*;

/**
 * 指定值 注解
 *
 * @author lingxu 2023/06/13 10:48
 */
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface SpecifiedValue {
}
