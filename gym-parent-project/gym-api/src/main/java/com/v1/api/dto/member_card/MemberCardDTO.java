package com.v1.api.dto.member_card;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MemberCardDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long cardId;
    private String title;
    private String cardType;
    private Integer cardDay;
    private BigDecimal price;
    private String status;
}
