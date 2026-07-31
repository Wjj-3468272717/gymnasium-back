package com.v1.web.sys_role_menu.controller;

import com.v1.api.sys_role_menu.SysRoleMenuRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/role")
public class SysRoleMenuController {

    @DubboReference
    SysRoleMenuRpcService roleMenuRpcService;

    //保存分配权限
    @PostMapping("/saveRoleMenu")
    public ResultVo saveRoleMenu(@RequestBody Map<String, Object> param){
        Long roleId = Long.valueOf(param.get("roleId").toString());
        @SuppressWarnings("unchecked")
        List<Integer> menuIdsInt = (List<Integer>) param.get("list");
        java.util.List<Long> menuIds = new java.util.ArrayList<>();
        for (Integer id : menuIdsInt) {
            menuIds.add(Long.valueOf(id));
        }
        roleMenuRpcService.saveRoleMenus(roleId, menuIds);
        return ResultUtils.success("分配成功");
    }

}
