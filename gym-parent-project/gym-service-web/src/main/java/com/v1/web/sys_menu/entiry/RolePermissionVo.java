package com.v1.web.sys_menu.entiry;

import com.v1.api.dto.sys_menu.SysMenuDTO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RolePermissionVo {

    //当前登录系统用户的菜单数据
    List<SysMenuDTO> listmenu = new ArrayList<>();
    //原来角色分配的菜单
    private Object[] checkList;

}
