package com.example.demo.pojo;

import lombok.Data;

import javax.persistence.Id;
import java.util.Date;
import java.util.List;

@Data
public class ProductInfo {
    @Id
    private String productId;

    /** 名字. */
    private String productName;

    /** 单价. */
    private Float productPrice;

    /** 库存. */
    private Integer productStock;

    /** 描述. */
    private String productDescription;

    /** 小图. */
    private String productIcon;

    /** 状态, 0正常1下架. */
    private Integer productStatus;

    /** 类目编号. */
    private String categoryType;

    private Date createTime;

    private Date updateTime;

    private List data;

}
