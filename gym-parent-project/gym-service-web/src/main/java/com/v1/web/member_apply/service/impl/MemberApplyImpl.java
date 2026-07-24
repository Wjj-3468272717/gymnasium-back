package com.v1.web.member_apply.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.web.member_apply.entity.MemberApply;
import com.v1.web.member_apply.mapper.MemberApplyMapper;
import com.v1.web.member_apply.service.MemberApplyService;
import org.springframework.stereotype.Service;

@Service
public class MemberApplyImpl extends ServiceImpl<MemberApplyMapper, MemberApply> implements MemberApplyService {
}
