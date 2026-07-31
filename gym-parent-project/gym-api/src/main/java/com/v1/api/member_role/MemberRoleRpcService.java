package com.v1.api.member_role;

import com.v1.api.dto.member_role.MemberRoleDTO;

public interface MemberRoleRpcService {
    MemberRoleDTO getByMemberId(Long memberId);

    void save(MemberRoleDTO role);
}
