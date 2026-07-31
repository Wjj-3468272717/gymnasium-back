package com.v1.service.goods.goods_order.entity;

import lombok.Data;

import java.util.List;

@Data
public class OrderParam {
    private Long userId;
    private List<OrderItem> orderItemList;
}
