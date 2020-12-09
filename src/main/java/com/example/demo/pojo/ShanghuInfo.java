package com.example.demo.pojo;


import lombok.Data;

import javax.persistence.Id;

@Data
public class ShanghuInfo {
    @Id
    private String sh_id;
    private String id;
    private String sh_name;
    private String sh_password;
    private String flag;
    private String appid;
    private String appsecret;
    private String shopname;
    private String aliappid;

}
