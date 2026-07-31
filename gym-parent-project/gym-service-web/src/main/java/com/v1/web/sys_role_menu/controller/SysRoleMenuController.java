package com.v1.web.sys_role_menu.controller;

import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.service.user.sys_role_menu.entity.SaveMenuParam;
import com.v1.service.user.sys_role_menu.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/role")
public class SysRoleMenuController {

    @Autowired
    SysRoleMenuService roleMenuService;

    //保存分配权限
    @PostMapping("/saveRoleMenu")
    public ResultVo saveRoleMenu(@RequestBody SaveMenuParam param){
        roleMenuService.save(param);
        return ResultUtils.success("分配成功");
    }

}
