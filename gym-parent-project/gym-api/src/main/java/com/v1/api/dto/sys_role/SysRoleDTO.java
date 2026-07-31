package com.v1.api.dto.sys_role;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysRoleDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long roleId;
    private String roleName;
    private String remark;
}
