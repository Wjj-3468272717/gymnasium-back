package com.v1.web.sys_menu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.sys_menu.entiry.SysMenu;

import java.util.List;

public interface SysMenuService extends IService<SysMenu> {

    /**
     * 获取父级菜单
     * @return
     */
    List<SysMenu> getParent();

}
