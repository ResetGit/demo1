package com.example.demo.pojo;

public class Permission_Role extends BaseEntity {
    //权限id
    private String permission_id;

    //角色id
    private String role_id;


    public String getPermission_id() {
        return permission_id;
    }

    public void setPermission_id(String permission_id) {
        this.permission_id = permission_id;
    }

    public String getRole_id() {
        return role_id;
    }

    public void setRole_id(String role_id) {
        this.role_id = role_id;
    }
}
