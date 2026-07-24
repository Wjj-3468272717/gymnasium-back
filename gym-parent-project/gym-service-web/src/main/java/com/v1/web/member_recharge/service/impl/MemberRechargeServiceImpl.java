package com.v1.web.member_recharge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.web.member_recharge.entity.MemberRecharge;
import com.v1.web.member_recharge.mapper.MemberRechargeMapper;
import com.v1.web.member_recharge.service.MemberRechargeService;
import org.springframework.stereotype.Service;

@Service
public class MemberRechargeServiceImpl extends ServiceImpl<MemberRechargeMapper, MemberRecharge> implements MemberRechargeService {
}
