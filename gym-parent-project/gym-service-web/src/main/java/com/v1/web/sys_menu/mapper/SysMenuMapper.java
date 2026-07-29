package com.v1.web.sys_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.v1.web.sys_menu.entiry.SysMenu;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    //根据员工id进行查询菜单
    List<SysMenu> getMenuByUserId(@PathVariable("userId") Long userId);
    //根据会员id进行查询菜单
    List<SysMenu> getMenuByMemberId(@PathVariable("memberId") Long memberId);
    //根据角色id进行查询菜单
    List<SysMenu> getMenuByRoleId(@PathVariable("roleId") Long roleId);
}
