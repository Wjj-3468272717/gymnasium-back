package com.v1.web.suggest.entity;

import lombok.Data;

@Data
public class SuggestParam {

    private Long currentPage;
    private Long pageSize;
    private String title;

}
