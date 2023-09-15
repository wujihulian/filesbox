package com.svnlan.annotation;

import com.svnlan.interceptor.VisitRecordAop;
import com.svnlan.utils.timer.VisitRecordSchedule;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 自定义注入类
 *
 * @author lingxu 2023/04/11 16:02
 */
public class MyImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{VisitRecordAop.class.getName(), VisitRecordSchedule.class.getName()};
    }
}
