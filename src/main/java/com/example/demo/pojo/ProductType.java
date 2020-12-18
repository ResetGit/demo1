package com.example.demo.pojo;


import java.util.Date;

public class ProductType extends BaseEntity{

    //属性编号
    private String pro_type;

    //商品价格
    private String pro_type_price;

    //属性名称
    private String pro_type_name;

    //店铺id
    private String store_id;

    private String create_time;

    private String update_time;

    public String getPro_type() {
        return pro_type;
    }

    public void setPro_type(String pro_type) {
        this.pro_type = pro_type;
    }

    public String getPro_type_price() {
        return pro_type_price;
    }

    public void setPro_type_price(String pro_type_price) {
        this.pro_type_price = pro_type_price;
    }

    public String getPro_type_name() {
        return pro_type_name;
    }

    public void setPro_type_name(String pro_type_name) {
        this.pro_type_name = pro_type_name;
    }

    public String getStore_id() {
        return store_id;
    }

    public void setStore_id(String store_id) {
        this.store_id = store_id;
    }

    public String getCreate_time() {
        return create_time;
    }

    public void setCreate_time(String create_time) {
        this.create_time = create_time;
    }

    public String getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }
}
