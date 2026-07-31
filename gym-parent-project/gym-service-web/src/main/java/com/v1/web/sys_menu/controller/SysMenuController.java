package com.v1.web.sys_menu.controller;

import com.v1.api.dto.sys_menu.SysMenuDTO;
import com.v1.api.sys_menu.SysMenuRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.sys_menu.entiry.MakeMenuTree;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class SysMenuController {

    @DubboReference
    SysMenuRpcService menuRpcService;

    /**
     * 新增菜单
     */
    @PostMapping
    public ResultVo addMenu(@RequestBody SysMenuDTO sysMenu){
        menuRpcService.saveMenu(sysMenu);
        return ResultUtils.success("新增成功!");
    }

    /**
     * 编辑菜单
     */
    @PutMapping
    public ResultVo editMenu(@RequestBody SysMenuDTO sysMenu){
        menuRpcService.updateMenu(sysMenu);
        return ResultUtils.success("编辑成功!");
    }

    /**
     * 删除菜单
     */
    @DeleteMapping("/{menuId}")
    public ResultVo deleteMenu(@PathVariable("menuId") Long menuId){
        menuRpcService.deleteMenu(menuId);
        return ResultUtils.success("删除成功!");
    }

    /**
     * 查询菜单
     */
    @GetMapping("/list")
    public ResultVo list(){
        List<SysMenuDTO> list = menuRpcService.getAllMenus();
        List<SysMenuDTO> menuList = MakeMenuTree.makeTree(list, 0L);
        return ResultUtils.success("查询成功", menuList);
    }

    /**
     * 查询上级树
     */
    @GetMapping("/parent")
    public ResultVo getParent(){
        List<SysMenuDTO> parent = menuRpcService.getParentMenus();
        return ResultUtils.success("查询成功", parent);
    }

}
