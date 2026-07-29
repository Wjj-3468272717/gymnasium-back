package com.v1.web.sys_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.sys_menu.entiry.SysMenu;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取父级菜单
     * @return
     */
    List<SysMenu> getParent();

    //根据员工id查询菜单
    List<SysMenu> getMenuByUserId(Long userId);
    //根据会员id查询菜单
    List<SysMenu> getMenuByMemberId(Long userId);
    //根据角色id查询菜单
    List<SysMenu> getMenuByRoleId(Long roleId);

}
