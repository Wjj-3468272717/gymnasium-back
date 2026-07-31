package com.v1.web.sys_role.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.sys_menu.entiry.RolePermissionVo;
import com.v1.web.sys_role.entity.RoleAssignParam;
import com.v1.web.sys_role.entity.RoleParam;
import com.v1.web.sys_role.entity.SysRole;
import com.v1.web.sys_role.service.SysRoleService;
import com.v1.web.sys_role.entity.SelectType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class SysRoleController {

    @Autowired
    private SysRoleService sysRoleService;

    /**
     * 新增角色
     * @param role
     * @return
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys_role_add')")
    public ResultVo addRole(@RequestBody SysRole role){
        role.setCreateTime(new Date());
        boolean saved = sysRoleService.save(role);
        if(saved){
            return ResultUtils.success("新增成功");
        }
        return ResultUtils.error("新增失败");
    }

    /**
     * 修改角色
     * @param role
     * @return
     */
    @PutMapping
    public ResultVo editRole(@RequestBody SysRole role){
        role.setUpdateTime(new Date());
        boolean updated = sysRoleService.updateById(role);
        if(updated){
            return ResultUtils.success("修改成功");
        }
        return ResultUtils.error("修改失败");
    }

    /**
     * 删除角色
     * @param roleId
     * @return
     */
    @DeleteMapping("/{roleId}")
    public ResultVo deleteRole(@PathVariable("roleId") Long roleId){
        boolean removed = sysRoleService.removeById(roleId);
        if(removed){
            return ResultUtils.success("删除成功");
        }
        return ResultUtils.error("删除失败");
    }

    /**
     *  查询角色列表
     * @param roleParam
     * @return
     */
    @GetMapping("/list")
    public ResultVo getList(RoleParam roleParam){
        IPage<SysRole> iPage = sysRoleService.list(roleParam);
        return ResultUtils.success("查询成功",iPage);
    }

    /**
     * 获取角色选择信息
     * @return
     */
    @GetMapping("getSelect")
    public ResultVo getListSelect(){
        List<SysRole> list = sysRoleService.list();
        List<SelectType> selectTypeList = new ArrayList<>();
        if(list != null){
            list.stream().forEach(sysRole -> {
                selectTypeList.add(new SelectType(sysRole.getRoleId(),sysRole.getRoleName()));
            });
        }
        return ResultUtils.success("查询成功",selectTypeList);
    }

    //分配权限树，回显查询
    @GetMapping("getMenuTree")
    public ResultVo getMenuTree(RoleAssignParam param){
        RolePermissionVo menuTree = sysRoleService.getMenuTree(param);
        return ResultUtils.success("查询成功",menuTree);
    }

}
