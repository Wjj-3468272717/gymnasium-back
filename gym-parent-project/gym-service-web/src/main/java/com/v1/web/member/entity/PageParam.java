package com.v1.web.member.entity;

import lombok.Data;

@Data
public class PageParam {

    private Long currentPage;
    private Long pageSize;
    private String name;
    private String phone;
    private String username;

}
