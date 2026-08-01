package com.v1.service.user.sys_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.v1.service.user.sys_menu.entiry.SysMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysMenuMapper extends BaseMapper<SysMenu> {

    //根据员工id进行查询菜单
    List<SysMenu> getMenuByUserId(@Param("userId") Long userId);
    //根据会员id进行查询菜单
    List<SysMenu> getMenuByMemberId(@Param("memberId") Long memberId);
    //根据角色id进行查询菜单
    List<SysMenu> getMenuByRoleId(@Param("roleId") Long roleId);
}
