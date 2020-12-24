package com.example.demo.pojo;

import java.util.Date;

public class User extends BaseEntity {
    //用户名称
    private String name;

    //用户密码
    private String password;

    //用户状态(0.正常，1.下架)
    private Integer state;

    //手机号码
    private String phone;

    //用户地址
    private String address;

    //创建时间
    private String createTime;

    //更新时间
    private String updateTime;

    private String iscombo;

    private String endTime;

    private String shopnumber;

    private String tc;
    private String occupation;

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getTc() {
        return tc;
    }

    public void setTc(String tc) {
        this.tc = tc;
    }

    public String getShopnumber() {
        return shopnumber;
    }

    public void setShopnumber(String shopnumber) {
        this.shopnumber = shopnumber;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIscombo() {
        return iscombo;
    }

    public void setIscombo(String iscombo) {
        this.iscombo = iscombo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}
