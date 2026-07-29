package com.v1.web.member_recharge.entity;

import lombok.Data;

@Data
public class RechargeParamList {
    private Long currentPage;
    private Long pageSize;
    private Long memberId;
    private String userType;
}