package com.v1.web.member_recharge.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.web.member_recharge.entity.MemberRecharge;
import org.apache.ibatis.annotations.Param;

public interface MemberRechargeMapper extends BaseMapper<com.v1.web.member_recharge.entity.MemberRecharge> {

    IPage<MemberRecharge> getRechargeList(IPage<MemberRecharge> page);

    IPage<MemberRecharge> getRechargeByMember(IPage<MemberRecharge> page, @Param("memberId") Long memberId);
}
