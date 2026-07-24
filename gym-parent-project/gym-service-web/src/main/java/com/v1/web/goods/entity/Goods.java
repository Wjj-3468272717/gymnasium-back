package com.v1.web.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;

@Data
@TableName(value = "goods")
public class Goods {

    @TableId(type = IdType.AUTO)
    private Long goodsId;
    private String name;
    private BigDecimal price;
    private Integer store;
    private String image;
    private String details;
    private String unit;
    private String specs;

}
