package com.v1.web.sys_menu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.web.sys_menu.entiry.MakeMenuTree;
import com.v1.web.sys_menu.entiry.SysMenu;
import com.v1.web.sys_menu.mapper.SysMenuMapper;
import com.v1.web.sys_menu.service.SysMenuService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class SysMenuServiceImpl extends ServiceImpl<SysMenuMapper, SysMenu> implements SysMenuService{
    @Override
    public List<SysMenu> getParent() {
        //查询目录和菜单
        String[] type = {"0","1"};
        List<String> list = Arrays.asList(type);
        //构建查询条件
        QueryWrapper<SysMenu> query = new QueryWrapper<>();
        query.lambda().in(SysMenu::getType,list).orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> menuList = this.baseMapper.selectList(query);
        //组装顶级菜单
        SysMenu menu = new SysMenu();
        menu.setMenuId(0L);
        menu.setTitle("顶级菜单");
        menu.setParentId(-1L);
        menuList.add(menu);

        return MakeMenuTree.makeTree(menuList,-1L);
    }
}
