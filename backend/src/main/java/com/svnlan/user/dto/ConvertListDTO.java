package com.svnlan.user.dto;

import com.svnlan.utils.PageQuery;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/23 9:47
 */
@Data
public class ConvertListDTO  extends PageQuery {
    private Long convertID;
    private Long sourceID;
    private Long fileID;
    private Long userID;
    private String name;
    private String state;
    private String remark;
    private Integer frequencyCount;
    private Integer scheduleFrequencyCount;
    private String logType;
    private String keyword;

    /**
     * 创建时间  最小
     */
    private String timeFrom;
    /**
     * 创建时间  最大
     */
    private String timeTo;
    /** 排序字段 */
    private String sortField;
    /** 排序方式  倒序desc  正序asc */
    private String sortType;
}
