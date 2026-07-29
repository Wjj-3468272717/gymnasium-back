package com.v1.web.sys_role_menu.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.v1.web.sys_role_menu.entity.SysRoleMenu;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SysRoleMenuMapper extends BaseMapper<SysRoleMenu> {

    //保存
    boolean saveRoleMenu(@Param("roleId") Long roleId, @Param("menuIds")List<Long> menuIds);

}
