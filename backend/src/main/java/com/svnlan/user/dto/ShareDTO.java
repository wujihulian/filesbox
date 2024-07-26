package com.svnlan.user.dto;

import com.svnlan.utils.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

/**
 * 分享查询参数
 *
 * @author lingxu 2023/04/04 14:19
 */
@EqualsAndHashCode(callSuper = false)
@Data
public class ShareDTO extends PageQuery {
    /**
     * 分享id
     */
    private Long id;
    /**
     * 按 某个字段排序 numView 浏览量  numDownload 下载数  timeTo 过期时间  createTime【默认】 分享时间
     */
    private String sortField;
    /**
     * 排序方式 up 升序  down 降序
     */
    private String sortType;
    /**
     * 关键字
     */
    private String words;
    /**
     * 分享时间
     */
    private Long timeFrom;
    /**
     * 过期时间
     */
    private Long timeTo;
    /**
     * isShareTo 内部协作     isLink 外链分享
     */
    private String type;
    /**
     * 分享者id
     */
    private Long userId;

    private Long tenantId;

    public void initValue() {
        if (!StringUtils.hasText(sortField)) {
            sortField = "createTime";
        }
        if (!StringUtils.hasText(sortType)) {
            sortType = "down";
        }
    }
}
