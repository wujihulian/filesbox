package com.svnlan.user.dto;

import com.svnlan.utils.PageQuery;
import lombok.Data;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/2/24 14:23
 */
@Data
public class SystemLogDto extends PageQuery {

    private String logType;

    private Long userID;
    /**
     * 创建时间  最小
     */
    private String timeFrom;
    /**
     * 创建时间  最大
     */
    private String timeTo;
    /** 排序字段 */
    private String sortField = "createTime";
    /** 排序方式  倒序desc  正序asc */
    private String sortType = "desc";
}
