package com.v1.web.sys_menu.entiry;

import com.v1.api.dto.sys_menu.SysMenuDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MakeMenuTree {

    public static List<SysMenuDTO> makeTree(List<SysMenuDTO> menuList, Long pid) {
        List<SysMenuDTO> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
                    SysMenuDTO sysMenu = new SysMenuDTO();
                    BeanUtils.copyProperties(item, sysMenu);
                    List<SysMenuDTO> child = makeTree(menuList, item.getMenuId());
                    // Note: SysMenuDTO doesn't have children field; tree structure is used as-is
                    list.add(sysMenu);
                });
        return list;
    }

    public static List<RouterVO> makeRouter(List<SysMenuDTO> menuList, Long pid) {
        List<RouterVO> list = new ArrayList<>();
        Optional.ofNullable(menuList).orElse(new ArrayList<>())
                .stream()
                .filter(item -> item != null && item.getParentId().equals(pid))
                .forEach(item -> {
                    RouterVO routerVO = new RouterVO();
                    routerVO.setPath(item.getPath());
                    //设置children
                    List<RouterVO> children = makeRouter(menuList, item.getMenuId());
                    routerVO.setChildren(children);
                    if (item.getParentId() == 0) {
                        routerVO.setComponent("Layout");
                        //判断是否为菜单类型
                        if ("1".equals(item.getType())) {
                            routerVO.setRedirect(item.getPath());
                            //菜单也需要设置children
                            List<RouterVO> listChild = new ArrayList<>();
                            RouterVO child = new RouterVO();
                            child.setPath(item.getPath());
                            child.setComponent(item.getPath());
                            child.setMeta(routerVO.new Meta(
                                    item.getTitle(),
                                    item.getIcon(),
                                    item.getCode() != null ? item.getCode().split(",") : new String[0]
                            ));
                            listChild.add(child);
                            routerVO.setChildren(listChild);
                            routerVO.setPath(item.getPath());
                            routerVO.setName(item.getTitle() + "parent");
                        }
                    } else {
                        routerVO.setComponent(item.getPath());
                    }
                    routerVO.setMeta(routerVO.new Meta(
                            item.getTitle(),
                            item.getIcon(),
                            item.getCode() != null ? item.getCode().split(",") : new String[0]
                    ));
                    list.add(routerVO);
                });
        return list;
    }
}
