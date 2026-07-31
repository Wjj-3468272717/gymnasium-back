package com.v1.api.dto.member;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class MemberDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long memberId;
    private Long roleId;
    private String name;
    private String sex;
    private String phone;
    private Integer age;
    private String birthday;
    private Integer height;
    private Integer weight;
    private Integer waist;
    private String joinTime;
    private String endTime;
    private String username;
    private String password;
    private String status;
    private String cardType;
    private Integer cardDay;
    private BigDecimal price;
    private BigDecimal money;
}
