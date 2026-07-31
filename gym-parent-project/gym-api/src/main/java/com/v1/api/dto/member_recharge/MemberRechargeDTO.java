package com.v1.api.dto.member_recharge;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberRechargeDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private BigDecimal money;
    private Date createTime;
}
