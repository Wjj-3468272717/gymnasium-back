package com.v1.service.member.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member.MemberDTO;
import com.v1.api.dto.member_role.MemberRoleDTO;
import com.v1.api.member.MemberRpcService;
import com.v1.service.member.member.entity.Member;
import com.v1.service.member.member.entity.PageParam;
import com.v1.service.member.member.entity.RechargeParam;
import com.v1.service.member.member.service.MemberService;
import com.v1.service.member.member_role.entity.MemberRole;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class MemberRpcProvider implements MemberRpcService {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private MemberService memberService;

    @Override
    public PageResultDTO<MemberDTO> listMembers(PageDTO page, String name, String phone, String username, Long memberId, String userType) {
        PageParam param = new PageParam();
        param.setCurrentPage(page.getCurrentPage());
        param.setPageSize(page.getPageSize());
        param.setName(name);
        param.setPhone(phone);
        param.setUsername(username);
        if (memberId != null) {
            param.setMemberId(memberId.toString());
        }
        param.setUserType(userType);

        IPage<Member> result = memberService.list(param);

        PageResultDTO<MemberDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            MemberDTO memberDTO = new MemberDTO();
            BeanUtils.copyProperties(entity, memberDTO);
            return memberDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public void addMember(MemberDTO member) {
        Member entity = new Member();
        BeanUtils.copyProperties(member, entity);
        memberService.addMember(entity);
    }

    @Override
    public void editMember(MemberDTO member) {
        Member entity = new Member();
        BeanUtils.copyProperties(member, entity);
        memberService.editMember(entity);
    }

    @Override
    public void deleteMember(Long memberId) {
        memberService.deleteMember(memberId);
    }

    @Override
    public MemberRoleDTO getRoleByMemberId(Long memberId) {
        MemberRole memberRole = memberService.getRoleByMemberId(memberId);
        if (memberRole == null) {
            return null;
        }
        MemberRoleDTO dto = new MemberRoleDTO();
        BeanUtils.copyProperties(memberRole, dto);
        return dto;
    }

    @Override
    public MemberDTO loadUser(String username) {
        Member entity = memberService.loadUser(username);
        if (entity == null) {
            return null;
        }
        MemberDTO dto = new MemberDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public MemberDTO getMemberById(Long memberId) {
        Member entity = memberService.getById(memberId);
        if (entity == null) {
            return null;
        }
        MemberDTO dto = new MemberDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void joinCard(Long memberId, Long cardId) {
        RechargeParam param = null;
        try {
            com.v1.service.member.member.entity.JoinParam joinParam = new com.v1.service.member.member.entity.JoinParam();
            joinParam.setMemberId(memberId);
            joinParam.setCardId(cardId);
            memberService.joinApply(joinParam);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void recharge(Long memberId, BigDecimal money) {
        RechargeParam param = new RechargeParam();
        param.setMemberId(memberId);
        param.setMoney(money);
        memberService.recharge(param);
    }

    @Override
    public void resetPassword(Long memberId, String newPassword) {
        Member entity = memberService.getById(memberId);
        if (entity != null) {
            entity.setPassword(PASSWORD_ENCODER.encode(newPassword));
            memberService.updateById(entity);
        }
    }
}
