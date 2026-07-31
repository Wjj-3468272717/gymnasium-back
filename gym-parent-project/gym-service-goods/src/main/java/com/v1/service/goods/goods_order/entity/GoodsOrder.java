package com.v1.service.goods.goods_order.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@TableName(value = "goods_order")
public class GoodsOrder {

    @TableId(type = IdType.AUTO)
    private Long orderId;
    private Long goodsId;
    private String name;
    private BigDecimal price;
    private String image;
    private String details;
    private String unit;
    private String specs;
    private Integer num;
    private BigDecimal totalPrice;
    private String controlUser;
    private Date createTime;

}
