package com.v1.web.equipment.entity;

import lombok.Data;

@Data
public class ListParam {

    private Integer currentPage;
    private Integer pageSize;
    private String name;

}
