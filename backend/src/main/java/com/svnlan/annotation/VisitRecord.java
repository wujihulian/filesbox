package com.svnlan.annotation;

import org.springframework.data.util.Pair;

import java.lang.annotation.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;


/**
 * 访问记录
 *
 * @author lingxu 2023/04/06 16:53
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VisitRecord {

    /**
     * 是否需要异步
     */
    boolean isAsync() default true;

    /**
     * 时间类型
     */
    TimeType[] timeType() default {TimeType.MILLI_SECOND, TimeType.DAY};

    /**
     * 记录哪种类型数据
     */
    RecordType[] recordType() default {RecordType.ALL};

    /**
     * 是否需要处理访问数
     */
    boolean handle() default false;

    enum RecordType {
        // 设备类型
        CLIENT_TYPE,
        // 操作系统类型
        OS_NAME,
        // 所有类型
        ALL;
    }

    enum TimeType {
        // 按天记录
        DAY,
        // 按小时记录
        HOUR,
        // 按分钟记录
        MINUTE,
        // 按秒记录
        SECOND,
        // 按毫秒记录
        MILLI_SECOND;

        /**
         * 判断是否在给定的数组中
         */
        public static boolean isContains(TimeType[] timeTypes, TimeType timeType) {
            for (TimeType item : timeTypes) {
                if (item == timeType) {
                    return true;
                }
            }
            return false;
        }

        /**
         * 获取时间类型及对应的时间戳
         *
         * @param now 当前时间
         * @return 时间类型及对应时间戳集合
         */
        public static List<Pair<TimeType, Supplier<Long>>> getTimeList(LocalDateTime now) {
            List<Pair<TimeType, Supplier<Long>>> list = new ArrayList<>();
            list.add(Pair.of(VisitRecord.TimeType.MILLI_SECOND, () -> now.toInstant(ZoneOffset.of("+8")).toEpochMilli()));
            list.add(Pair.of(VisitRecord.TimeType.SECOND, () -> now.toInstant(ZoneOffset.of("+8")).getEpochSecond()));
            list.add(Pair.of(VisitRecord.TimeType.MINUTE, () -> now.withSecond(0).withNano(0).toEpochSecond(ZoneOffset.of("+8"))));
            list.add(Pair.of(VisitRecord.TimeType.HOUR, () -> now.withSecond(0).withNano(0).withMinute(0).toEpochSecond(ZoneOffset.of("+8"))));
            list.add(Pair.of(VisitRecord.TimeType.DAY, () -> now.withSecond(0).withNano(0).withMinute(0).withHour(0).toEpochSecond(ZoneOffset.of("+8"))));
            return list;
        }
    }
}
