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

    private String storeState;

    //创建时间
    private String createTime;

    //更新时间
    private String updateTime;

    private String zfbPublickey;
    private String zfbPrivatekey;
//    private String wxAppsecret;
    private String wxMachid;
    private String wxKey;

//    private String storeState;

    public String getZfbPublickey() {
        return zfbPublickey;
    }

    public void setZfbPublickey(String zfbPublickey) {
        this.zfbPublickey = zfbPublickey;
    }

    public String getZfbPrivatekey() {
        return zfbPrivatekey;
    }

    public void setZfbPrivatekey(String zfbPrivatekey) {
        this.zfbPrivatekey = zfbPrivatekey;
    }

//    public String getWxAppsecret() {
//        return wxAppsecret;
//    }
//
//    public void setWxAppsecret(String wxAppsecret) {
//        this.wxAppsecret = wxAppsecret;
//    }

    public String getWxMachid() {
        return wxMachid;
    }

    public void setWxMachid(String wxMachid) {
        this.wxMachid = wxMachid;
    }

    public String getWxKey() {
        return wxKey;
    }

    public void setWxKey(String wxKey) {
        this.wxKey = wxKey;
    }

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

    public String getStoreState() {
        return storeState;
    }

    public void setStoreState(String storeState) {
        this.storeState = storeState;
    }
}
