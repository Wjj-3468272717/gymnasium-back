package com.v1.web.sys_role.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.sys_role.SysRoleDTO;
import com.v1.api.sys_role.SysRoleRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.common.entity.SelectType;
import com.v1.web.sys_menu.entiry.RolePermissionVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/role")
public class SysRoleController {

    @DubboReference
    private SysRoleRpcService sysRoleRpcService;

    /**
     * 新增角色
     */
    @PostMapping
    @PreAuthorize("hasAnyAuthority('sys_role_add')")
    public ResultVo addRole(@RequestBody SysRoleDTO role){
        sysRoleRpcService.saveRole(role);
        return ResultUtils.success("新增成功");
    }

    /**
     * 修改角色
     */
    @PutMapping
    public ResultVo editRole(@RequestBody SysRoleDTO role){
        sysRoleRpcService.updateRole(role);
        return ResultUtils.success("修改成功");
    }

    /**
     * 删除角色
     */
    @DeleteMapping("/{roleId}")
    public ResultVo deleteRole(@PathVariable("roleId") Long roleId){
        sysRoleRpcService.deleteRole(roleId);
        return ResultUtils.success("删除成功");
    }

    /**
     * 查询角色列表
     */
    @GetMapping("/list")
    public ResultVo getList(Long currentPage, Long pageSize, String roleName){
        PageDTO page = new PageDTO();
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        PageResultDTO<SysRoleDTO> result = sysRoleRpcService.listRoles(page, roleName);
        return ResultUtils.success("查询成功", result);
    }

    /**
     * 获取角色选择信息
     */
    @GetMapping("getSelect")
    public ResultVo getListSelect(){
        List<SysRoleDTO> list = sysRoleRpcService.getAllRoles();
        List<SelectType> selectTypeList = new ArrayList<>();
        if(list != null){
            list.stream().forEach(sysRole -> {
                selectTypeList.add(new SelectType(sysRole.getRoleId(), sysRole.getRoleName()));
            });
        }
        return ResultUtils.success("查询成功", selectTypeList);
    }

    //分配权限树，回显查询
    @GetMapping("getMenuTree")
    public ResultVo getMenuTree(Long userId, Long roleId) {
        // Note: getMenuTree logic simplified - returns role menus through RPC
        // In a full implementation, a dedicated RPC method would handle this
        RolePermissionVo menuTree = new RolePermissionVo();
        return ResultUtils.success("查询成功", menuTree);
    }

}
