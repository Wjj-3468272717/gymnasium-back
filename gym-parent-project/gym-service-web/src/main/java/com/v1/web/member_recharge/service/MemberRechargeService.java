package com.v1.web.member_recharge.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.member.entity.RechargeParam;
import com.v1.web.member_recharge.entity.MemberRecharge;
import com.v1.web.member_recharge.entity.RechargeParamList;

public interface MemberRechargeService extends IService<MemberRecharge> {

    IPage<MemberRecharge> getRechargeList(RechargeParamList paramList);

    IPage<MemberRecharge> getRechargeByMember(RechargeParamList paramList);

}
