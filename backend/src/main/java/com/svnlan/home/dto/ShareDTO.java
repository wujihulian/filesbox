package com.svnlan.home.dto;

import com.svnlan.home.vo.ShareAuthDto;
import com.svnlan.utils.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/3/3 13:40
 */
@Data
public class ShareDTO extends PageQuery {
    private Long shareID;
    private Long userID;
    private Long sourceID;
    /** 排序方式	string	倒序desc 正序asc */
    private String sortType;
    /** 排序字段	 */
    private String sortField;

    private String title;
    /** 分享文档路径 */
    private String sourcePath;
    /** 分享别名,替代shareHash */
    private String url;
    /** 是否外链分享；默认为0 */
    private Integer isLink;
    /** 是否为内部分享；默认为0 */
    private Integer isShareTo;
    /** 访问密码,为空则无密码 */
    private String password;
    /** 到期时间,0-永久生效*/
    private Long timeTo = 0L;
    /** 预览次数 */
    private Integer numView;
    /**  下载次数*/
    private Integer numDownload;
    /**  json 配置信息;是否可以下载,是否可以上传等*/
    private String options;
    private String shareCode;
    private String shareHash;
    private List<ShareAuthDto> authTo;
    private String shareIDStr;
}
