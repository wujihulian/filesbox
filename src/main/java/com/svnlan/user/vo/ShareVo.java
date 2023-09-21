package com.svnlan.user.vo;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.aliyuncs.ecs.model.v20140526.DescribeInstancesResponse;
import lombok.Data;
import org.springframework.util.StringUtils;

/**
 * 分享资源
 *
 * @TableName share
 */
@Data
public class ShareVo implements Serializable {
    /**
     * 自增id
     */
    private Long shareID;

    private String avatar;

    private String nickname;

    private String name;

    @Excel(name = "分享者")
    private String shareUsername;

    public void setNickname(String nickname) {
        this.nickname = nickname;
        if (StringUtils.hasText(nickname)) {
            this.shareUsername = nickname;
        }
    }

    public void setName(String name) {
        this.name = name;
        if (StringUtils.hasText(name) && !StringUtils.hasText(nickname)) {
            this.shareUsername = name;
        }
    }

    /**
     * 分享标题
     */
    @Excel(name = "分享名称")
    private String title;

    /**
     * shareid
     */
    private String shareHash;

    /**
     * 分享用户id
     */
    private Long userID;

    /**
     * 用户数据id
     */
    private Long sourceID;

    /**
     * 分享文档路径
     */
    private String sourcePath;

    /**
     * 分享别名,替代shareHash
     */
    private String url;

    /**
     * 是否外链分享；默认为0
     */
    @Excel(name = "是否外链分享", replace = {"是_1", "否_0"})
    private Integer isLink;

    /**
     * 是否为内部分享；默认为0
     */
    @Excel(name = "是否内部分享", replace = {"是_1", "否_0"})
    private Integer isShareTo;

    /**
     * 访问密码,为空则无密码
     */
    @Excel(name = "密码")
    private String password;

    public void setPassword(String password) {
        this.password = StringUtils.hasText(password) ? password : "-";
    }

    /**
     * 到期时间,0-永久生效
     */
    private Long timeTo;

    @Excel(name = "过期时间")
    private String timeToStr;

    public void setTimeTo(Long timeTo) {
        this.timeTo = timeTo;
        if (timeTo == 0L) {
            this.timeToStr = "永久";
            return;
        }
        this.timeToStr = LocalDateTime.ofInstant(Instant.ofEpochSecond(timeTo), ZoneId.of("+8"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 预览次数
     */
    @Excel(name = "浏览数", type = 10)
    private Integer numView;

    /**
     * 下载次数
     */
    @Excel(name = "下载次数", type = 10)
    private Integer numDownload;

    /**
     * json 配置信息;是否可以下载,是否可以上传等
     */
    private String options;

    /**
     * 创建时间
     */
    private Long createTime;

    @Excel(name = "创建时间")
    private String createTimeStr;

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
        this.createTimeStr = LocalDateTime.ofInstant(Instant.ofEpochSecond(createTime), ZoneId.of("+8"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    /**
     * 最后修改时间
     */
    private Long modifyTime;

    private static final long serialVersionUID = 1L;
}