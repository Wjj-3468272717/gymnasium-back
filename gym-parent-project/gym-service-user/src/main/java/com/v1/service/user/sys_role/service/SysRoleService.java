package com.v1.service.user.sys_role.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.service.user.sys_menu.entiry.RolePermissionVo;
import com.v1.service.user.sys_role.entity.RoleAssignParam;
import com.v1.service.user.sys_role.entity.RoleParam;
import com.v1.service.user.sys_role.entity.SysRole;

public interface SysRoleService extends IService<SysRole> {

    IPage<SysRole> list(RoleParam roleParam);

    //查询权限数据并回显
    RolePermissionVo getMenuTree(RoleAssignParam param);
}
