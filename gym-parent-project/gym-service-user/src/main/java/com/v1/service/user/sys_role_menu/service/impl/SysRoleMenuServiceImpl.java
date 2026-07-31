package com.v1.service.user.sys_role_menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.service.user.sys_role_menu.entity.SaveMenuParam;
import com.v1.service.user.sys_role_menu.entity.SysRoleMenu;
import com.v1.service.user.sys_role_menu.mapper.SysRoleMenuMapper;
import com.v1.service.user.sys_role_menu.service.SysRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuMapper, SysRoleMenu> implements SysRoleMenuService {
    @Transactional
    @Override
    public void save(SaveMenuParam param) {
        //删除原先权限
        QueryWrapper<SysRoleMenu> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysRoleMenu::getRoleId,param.getRoleId());
        this.baseMapper.delete(queryWrapper);
        //保存新的权限
        this.baseMapper.saveRoleMenu(param.getRoleId(), param.getList());
    }
}
