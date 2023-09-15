package com.svnlan.user.vo;

import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/27 14:10
 */
public class OperateLogExportVO {

    private Long id;
    @Excel(name = "用户")
    private String name;
    @Excel(name = "操作时间")
    private String loginTime;
    @Excel(name = "操作")
    private String operate;
    @Excel(name = "详情")
    private String desc;
    @Excel(name = "登录地址")
    private String address;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(String loginTime) {
        this.loginTime = loginTime;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
