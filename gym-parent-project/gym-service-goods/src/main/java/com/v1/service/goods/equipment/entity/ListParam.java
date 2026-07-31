package com.v1.service.goods.equipment.entity;

import lombok.Data;

@Data
public class ListParam {

    private Integer currentPage;
    private Integer pageSize;
    private String name;

}
