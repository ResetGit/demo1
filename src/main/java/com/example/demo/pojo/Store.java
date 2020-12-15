package com.example.demo.pojo;

public class Store extends BaseEntity {
    //店铺名称
    private String name;

    //店铺地址
    private String address;

    //微信appid
    private String wxAppId;

    //支付宝appid
    private String zfbAppId;

    private String user_id;

    //创建时间
    private String createTime;

    //更新时间
    private String updateTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getWxAppId() {
        return wxAppId;
    }

    public void setWxAppId(String wxAppId) {
        this.wxAppId = wxAppId;
    }

    public String getZfbAppId() {
        return zfbAppId;
    }

    public void setZfbAppId(String zfbAppId) {
        this.zfbAppId = zfbAppId;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
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
