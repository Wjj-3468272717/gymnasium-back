package com.v1.web.goods_order.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderParam {
    private Long userId;//用户id
    private List<OrderItem> orderItemList;// 选取的商品列表
}
