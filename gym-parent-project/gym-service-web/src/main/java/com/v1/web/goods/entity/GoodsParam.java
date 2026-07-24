package com.v1.web.goods.entity;

import lombok.Data;

@Data
public class GoodsParam {

    private Integer currentPage;
    private Integer pageSize;
    private String name;

}
