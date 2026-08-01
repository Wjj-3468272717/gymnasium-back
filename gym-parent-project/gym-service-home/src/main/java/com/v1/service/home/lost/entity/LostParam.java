package com.v1.service.home.lost.entity;

import lombok.Data;

@Data
public class LostParam {

    private Long currentPage;
    private Long pageSize;
    private String lostName;

}
