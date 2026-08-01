package com.v1.web.sys_menu.entiry;

import com.v1.api.dto.sys_menu.SysMenuDTO;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class MakeMenuTree {

    // 数据库path → Vue组件路径映射
    private static final Map<String, String> COMPONENT_MAP = new HashMap<>();
    static {
        COMPONENT_MAP.put("/userList", "/system/user/UserList");
        COMPONENT_MAP.put("/roleList", "/system/role/RoleList");
        COMPONENT_MAP.put("/menuList", "/system/menu/MenuList");
        COMPONENT_MAP.put("/memberList", "/member/list/MemberList");
        COMPONENT_MAP.put("/cardType", "/member/type/CardType");
        COMPONENT_MAP.put("/myFee", "/member/fee/MyFee");
        COMPONENT_MAP.put("/courseList", "/course/CourseList");
        COMPONENT_MAP.put("/mycourse", "/mycourse/mycourse");
        COMPONENT_MAP.put("/courseOrder", "/course/CourseList");
        COMPONENT_MAP.put("/goodsList", "/goods/GoodsList");
        COMPONENT_MAP.put("/orderList", "/order/OrderList");
        COMPONENT_MAP.put("/lostList", "/lost/LostList");
        COMPONENT_MAP.put("/suggestList", "/suggest/SuggestList");
        COMPONENT_MAP.put("/materialList", "/material/MaterialList");
    }

    private static String componentPath(String path) {
        return COMPONENT_MAP.getOrDefault(path, path);
    }

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
                            //重定向节点加到真实children前面，不覆盖真实子路由
                            RouterVO redirectChild = new RouterVO();
                            redirectChild.setPath(item.getPath());
                            redirectChild.setComponent(componentPath(item.getPath()));
                            redirectChild.setMeta(routerVO.new Meta(
                                    item.getTitle(),
                                    item.getIcon(),
                                    item.getCode() != null ? item.getCode().split(",") : new String[0]
                            ));
                            children.add(0, redirectChild);
                            routerVO.setChildren(children);
                            routerVO.setPath(item.getPath());
                            routerVO.setName(item.getTitle() + "parent");
                        }
                    } else {
                        routerVO.setComponent(componentPath(item.getPath()));
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
