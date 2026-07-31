package com.v1.api.dto.member_role;

import lombok.Data;
import java.io.Serializable;

@Data
public class MemberRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private Long roleId;
}
