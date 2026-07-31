package com.v1.service.user.sys_role.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.service.user.sys_menu.entiry.MakeMenuTree;
import com.v1.service.user.sys_menu.entiry.RolePermissionVo;
import com.v1.service.user.sys_menu.entiry.SysMenu;
import com.v1.service.user.sys_menu.service.SysMenuService;
import com.v1.service.user.sys_role.entity.RoleAssignParam;
import com.v1.service.user.sys_role.entity.RoleParam;
import com.v1.service.user.sys_role.entity.SysRole;
import com.v1.service.user.sys_role.mapper.SysRoleMapper;
import com.v1.service.user.sys_role.service.SysRoleService;
import com.v1.service.user.sys_user.entity.SysUser;
import com.v1.service.user.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper,SysRole> implements SysRoleService{

    @Autowired
    private SysMenuService sysMenuService;

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

    @Autowired
    SysUserService userService;
    @Autowired
    SysMenuService menuService;

    @Override
    public RolePermissionVo getMenuTree(RoleAssignParam param) {
        SysUser user = userService.getById(param.getUserId());
        List<SysMenu> list = null;
        if(StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")){//超级管理员
            list = menuService.list();
        }else{
            list = menuService.getMenuByUserId(param.getUserId());
        }
        //组装树形数据
        List<SysMenu> menuTree = MakeMenuTree.makeTree(list,0L);
        //查询角色原来的菜单分配信息
        List<SysMenu> roleList = menuService.getMenuByRoleId(param.getRoleId());
        List<Long> ids = new ArrayList<>();
        Optional.ofNullable(roleList)
                .orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null)
                .forEach(item -> {
                    ids.add(item.getMenuId());
                });
        RolePermissionVo vo = new RolePermissionVo();
        vo.setListmenu(menuTree);
        vo.setCheckList(ids.toArray());
        return vo;
    }

}
