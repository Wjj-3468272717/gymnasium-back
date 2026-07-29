package com.v1.web.member.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.v1.web.member.entity.Member;
import com.v1.web.member.entity.RechargeParam;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;

public interface MemberMapper extends BaseMapper<Member> {
    int addMoney(@Param("para")RechargeParam param);

    void subMoney(@Param("param") RechargeParam param);
}
