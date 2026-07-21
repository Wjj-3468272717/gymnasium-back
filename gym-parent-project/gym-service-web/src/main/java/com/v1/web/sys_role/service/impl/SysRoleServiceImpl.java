package com.v1.web.sys_role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.web.sys_role.entity.RoleParam;
import com.v1.web.sys_role.entity.SysRole;
import com.v1.web.sys_role.mapper.SysRoleMapper;
import com.v1.web.sys_role.service.SysRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService{

    @Override
    public IPage<SysRole> list(RoleParam roleParam) {
        IPage<SysRole> page = new Page<>();
        page.setSize(roleParam.getPageSize());
        page.setCurrent(roleParam.getCurrentPage());
        QueryWrapper<SysRole> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotBlank(roleParam.getRoleName())){
            queryWrapper.like("role_name",roleParam.getRoleName());
        }
       return this.page(page,queryWrapper);
    }

}
