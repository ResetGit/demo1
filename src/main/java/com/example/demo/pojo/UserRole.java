package com.example.demo.pojo;

public class UserRole extends BaseEntity {
    //用户id
    private String user_id;

    //角色id
    private String role_id;

    private String rName;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }

    public String getrName() {
        return rName;
    }

    public void setrName(String rName) {
        this.rName = rName;
    }
}
