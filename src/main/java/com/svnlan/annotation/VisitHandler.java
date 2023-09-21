package com.svnlan.annotation;

/**
 * 当访问时的处理器
 *
 * @author lingxu 2023/06/13 10:25
 */
public interface VisitHandler {

    void handle(Object value);
}
