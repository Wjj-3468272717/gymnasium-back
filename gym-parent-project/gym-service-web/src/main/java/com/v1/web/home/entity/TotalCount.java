package com.v1.web.home.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TotalCount {

    private int memberCount;
    private int userCount;
    private int materCount;
    private int orderCount;

}
