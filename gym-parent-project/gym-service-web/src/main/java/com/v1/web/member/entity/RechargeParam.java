package com.v1.web.member.entity;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class RechargeParam {

    private Long memberId;
    private Long userId;
    private BigDecimal money;

}
