package com.v1.service.user.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.api.sys_user_role.SysUserRoleRpcService;
import com.v1.service.user.sys_user_role.entiry.SysUserRole;
import com.v1.service.user.sys_user_role.service.SysUserRoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class SysUserRoleRpcProvider implements SysUserRoleRpcService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public void assignRole(Long userId, Long roleId) {
        QueryWrapper<SysUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUserRole::getUserId, userId);
        SysUserRole userRole = sysUserRoleService.getOne(queryWrapper);
        if (userRole == null) {
            SysUserRole newRole = new SysUserRole();
            newRole.setUserId(userId);
            newRole.setRoleId(roleId);
            sysUserRoleService.save(newRole);
        } else {
            userRole.setRoleId(roleId);
            sysUserRoleService.updateById(userRole);
        }
    }
}
