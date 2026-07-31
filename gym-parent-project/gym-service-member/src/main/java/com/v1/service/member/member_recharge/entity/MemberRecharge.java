package com.v1.service.member.member_recharge.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Date;

@Data
@TableName(value = "member_recharge")
public class MemberRecharge {

    @TableId(type = IdType.AUTO)
    private Long rechargeId;
    private Long memberId;
    private BigDecimal money;
    private Date createTime;
    private String createUser;

    //用户名
    @TableField(exist = false)
    private String name;
    //会员卡号
    @TableField(exist = false)
    private String username;
}
