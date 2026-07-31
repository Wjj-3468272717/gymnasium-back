package com.v1.api.sys_role;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.sys_role.SysRoleDTO;
import java.util.List;

public interface SysRoleRpcService {
    PageResultDTO<SysRoleDTO> listRoles(PageDTO page, String roleName);

    void saveRole(SysRoleDTO role);

    void updateRole(SysRoleDTO role);

    void deleteRole(Long roleId);

    List<SysRoleDTO> getAllRoles();
}
