package com.svnlan.user.domain;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

/**
 * @TableName visit_count_record
 */
@Data
public class VisitCountRecord implements Serializable {
    /**
     * 主键id
     */
    private Long id;

    /**
     * 用户访问次数
     */
    private Long visitCount;

    /**
     * 设备类型 1 pc , 2 h5, 3 安卓app, 4 ios-app, 5 小程序, 6 电脑app, 7 其他
     */
    private Integer deviceType;

    /**
     * 操作系统名称
     */
    private String osName;
    /**
     * 1 客户端访问 2 用户访问 3 操作系统访问 4操作系统总的访问 默认为 1
     */
    private Integer type;

    /**
     * 访问的日期
     */
    private LocalDate visitDay;

    /**
     * 最后修改时间
     */
    private Long modifyTime;

    /**
     * 创建时间
     */
    private Long createTime;

    private static final long serialVersionUID = 1L;

    public VisitCountRecord() {
    }

    @Override
    public boolean equals(Object obj) {
        if (Objects.isNull(obj)) {
            return false;
        }
        if (obj instanceof VisitCountRecord) {
            VisitCountRecord visitCountRecord = (VisitCountRecord) obj;
            if (Objects.equals((visitCountRecord.getId()), id)) {
                return true;
            }
            if (StringUtils.hasText(osName)) {
                if (Objects.equals(type, visitCountRecord.getType()) && Objects.equals(osName, visitCountRecord.getOsName())) {
                    if (type == 4) {
                        return true;
                    } else {
                        return Objects.equals(visitDay, visitCountRecord.getVisitDay());
                    }
                }
            } else {
                if (Objects.equals(type, visitCountRecord.getType()) && Objects.equals(deviceType, visitCountRecord.getDeviceType())) {
                    return Objects.equals(visitDay, visitCountRecord.getVisitDay());
                }
                return false;
            }
            return false;
        }
        return false;
    }
        /**
         * 用户客户端和用户访问 创建的记录
         */
    public VisitCountRecord(Long visitCount, Integer deviceType, Integer type, LocalDate visitDay) {
            this.visitCount = visitCount;
            this.deviceType = deviceType;
            this.visitDay = visitDay;
            this.type = type;
            this.createTime = this.modifyTime = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
        }

        /**
         * 用户操作系统访问 创建的记录
         */
    public VisitCountRecord(Long visitCount, Integer type, LocalDate visitDay, String osName) {
            this.visitCount = visitCount;
            this.osName = osName;
            this.visitDay = visitDay;
            this.type = type;
            this.createTime = this.modifyTime = LocalDateTime.now().toEpochSecond(ZoneOffset.ofHours(8));
        }


    }