package com.v1.api.sys_role_menu;

import java.util.List;

public interface SysRoleMenuRpcService {
    void saveRoleMenus(Long roleId, List<Long> menuIds);
}
