package com.svnlan.user.vo;


import cn.afterturn.easypoi.excel.annotation.Excel;

/**
 * @Author: sulijuan
 * @Description:
 * @Date: 2023/7/26 10:01
 */
public class SystemLogExportVO {

    @Excel(name = "用户")
    private String name;
    @Excel(name = "登录时间")
    private String loginTime;
    @Excel(name = "系统")
    private String system;
    @Excel(name = "客户端")
    private String client;
    @Excel(name = "登录地址")
    private String address;

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

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
