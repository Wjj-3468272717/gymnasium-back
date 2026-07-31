package com.v1.service.user.provider;

import com.v1.api.sys_role_menu.SysRoleMenuRpcService;
import com.v1.service.user.sys_role_menu.entity.SaveMenuParam;
import com.v1.service.user.sys_role_menu.service.SysRoleMenuService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@DubboService
public class SysRoleMenuRpcProvider implements SysRoleMenuRpcService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Override
    public void saveRoleMenus(Long roleId, List<Long> menuIds) {
        SaveMenuParam param = new SaveMenuParam();
        param.setRoleId(roleId);
        param.setList(menuIds);
        sysRoleMenuService.save(param);
    }
}
