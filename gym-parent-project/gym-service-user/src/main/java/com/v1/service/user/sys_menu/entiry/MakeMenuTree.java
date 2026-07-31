package com.v1.service.user.sys_menu.entiry;

import org.springframework.beans.BeanUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MakeMenuTree {
    public static List<SysMenu> makeTree(List<SysMenu> menuList, Long pid){
        List<SysMenu> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item!=null && item.getParentId().equals(pid))
                .forEach(item ->{
                    SysMenu sysMenu = new SysMenu();
                    BeanUtils.copyProperties(item,sysMenu);
                    List<SysMenu> child = makeTree(menuList, item.getMenuId());
                    sysMenu.setChildren(child);
                    list.add(sysMenu);
                });
        return  list;
    }

    public static List<RouterVO> makeRouter(List<SysMenu> menuList,Long pid){
        List<RouterVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
                    RouterVO routerVO = new RouterVO();
                    routerVO.setName(item.getName());
                    routerVO.setPath(item.getPath());
                    //设置children
                    List<RouterVO> childern = makeRouter(menuList, item.getMenuId());
                    routerVO.setChildren(childern);
                    if(item.getParentId() == 0){
                        routerVO.setComponent("Layout");
                        //判断是否为菜单类型
                        if(item.getType().equals("1")){
                            routerVO.setRedirect(item.getPath());
                            //菜单也需要设置children
                            List<RouterVO> listChild = new ArrayList<>();
                            RouterVO child = new RouterVO();
                            child.setName(item.getName());
                            child.setPath(item.getPath());
                            child.setComponent(item.getUrl());
                            child.setMeta(child.new Meta(
                                    item.getTitle(),
                                    item.getIcon(),
                                    item.getCode().split(",")
                            ));
                            listChild.add(child);
                            routerVO.setChildren(listChild);
                            routerVO.setPath(item.getPath());
                            routerVO.setName(item.getName()+"parent");
                        }
                    }else{
                        routerVO.setComponent(item.getUrl());
                    }
                    routerVO.setMeta(routerVO.new Meta(
                            item.getTitle(),
                            item.getIcon(),
                            item.getCode().split(",")
                    ));
                    list.add(routerVO);
                });
        return list;
    }
}
