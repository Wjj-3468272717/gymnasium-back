package com.v1.service.goods.goods.entity;

import lombok.Data;

@Data
public class GoodsParam {

    private Integer currentPage;
    private Integer pageSize;
    private String name;

}
