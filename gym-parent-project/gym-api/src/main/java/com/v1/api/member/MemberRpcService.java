package com.v1.api.member;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member.MemberDTO;
import com.v1.api.dto.member_role.MemberRoleDTO;
import java.math.BigDecimal;

public interface MemberRpcService {
    PageResultDTO<MemberDTO> listMembers(PageDTO page, String name, String phone, String username, Long memberId, String userType);

    void addMember(MemberDTO member);

    void editMember(MemberDTO member);

    void deleteMember(Long memberId);

    MemberRoleDTO getRoleByMemberId(Long memberId);

    MemberDTO loadUser(String username);

    MemberDTO getMemberById(Long memberId);

    void joinCard(Long memberId, Long cardId);

    void recharge(Long memberId, BigDecimal money);
}
