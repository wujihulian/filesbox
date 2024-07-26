package com.svnlan.user.vo;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author:
 * @Description: Excel的自定义注解
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ java.lang.annotation.ElementType.FIELD })
public @interface ExcelVO {

    public static String DEFALUT = "DEFALUT";

    /**
     * 导出到Excel中的名字.
     */
    public abstract String name();

    /**
     * 配置列的名称,对应A,B,C,D....
     */
    public abstract String column();

    /**
     * 配置列的宽度
     */
    public abstract int width();

    /**
     * 配置列的格式,对应#,##0.00,#,##0.000000
     */
    public abstract String dataFormat() default "";

    /**
     * 是否导出数据,应对需求:有时我们需要导出一份模板,这是标题需要但内容需要用户手工填写.
     */
    public abstract boolean isExport() default true;

}
