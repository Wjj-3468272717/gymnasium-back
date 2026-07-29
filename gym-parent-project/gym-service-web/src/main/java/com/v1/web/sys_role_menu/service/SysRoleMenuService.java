package com.v1.web.sys_role_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.sys_role.service.SysRoleService;
import com.v1.web.sys_role_menu.entity.SaveMenuParam;
import com.v1.web.sys_role_menu.entity.SysRoleMenu;

public interface SysRoleMenuService extends IService<SysRoleMenu> {

    //保存角色权限
    void save(SaveMenuParam param);

}
