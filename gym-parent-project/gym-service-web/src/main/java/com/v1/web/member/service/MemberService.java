package com.v1.web.member.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.member.entity.JoinParam;
import com.v1.web.member.entity.Member;
import com.v1.web.member.entity.PageParam;
import com.v1.web.member.entity.RechargeParam;
import com.v1.web.member_role.entity.MemberRole;

import java.text.ParseException;

public interface MemberService extends IService<Member> {
    IPage<Member> list(PageParam pageParam);

    void addMember(Member member);
    void editMember(Member member);
    void deleteMember(Long memberId);
    MemberRole getRoleByMemberId(Long memberId);

    void joinApply(JoinParam param) throws ParseException;
    void recharge(RechargeParam param);

    Member loadUser(String username);
}
