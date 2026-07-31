package com.v1.api.dto.member_apply;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class MemberApplyDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private String cardType;
    private Integer cardDay;
    private BigDecimal price;
    private Date createTime;
}
