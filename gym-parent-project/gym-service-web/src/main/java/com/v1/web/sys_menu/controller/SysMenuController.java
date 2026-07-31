package com.v1.web.sys_menu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.sys_menu.entiry.MakeMenuTree;
import com.v1.web.sys_menu.entiry.SysMenu;
import com.v1.web.sys_menu.service.SysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class SysMenuController {

    @Autowired
    SysMenuService menuService;

    /**
     * 新增菜单
     * @param sysMenu
     * @return
     */
    @PostMapping
    public ResultVo addMenu(@RequestBody SysMenu sysMenu){
        sysMenu.setCreateTime(new Date());
        boolean updated = menuService.save(sysMenu);
        if(updated){
            return ResultUtils.success("新增成功!");
        }else{
            return ResultUtils.error("新增失败!");
        }
    }

    /**
     * 编辑菜单
     * @param sysMenu
     * @return
     */
    @PutMapping
    public ResultVo editMenu(@RequestBody SysMenu sysMenu){
        sysMenu.setUpdateTime(new Date());
        boolean updated = menuService.updateById(sysMenu);
        if(updated){
            return ResultUtils.success("编辑成功!");
        }else{
            return ResultUtils.error("编辑失败!");
        }
    }

    /**
     * 删除菜单
     * @param menuId
     * @return
     */
    @DeleteMapping("/{menuId}")
    public ResultVo deleteMenu(@PathVariable("menuId") Long menuId){
        boolean updated = menuService.removeById(menuId);
        if(updated){
            return ResultUtils.success("删除成功!");
        }else{
            return ResultUtils.error("删除失败!");
        }
    }

    /**
     * 查询菜单
     * @return
     */
    @GetMapping("/list")
    public ResultVo list(){
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByAsc(SysMenu::getOrderNum);
        List<SysMenu> list = menuService.list(wrapper);
        List<SysMenu> menuList = MakeMenuTree.makeTree(list, 0L);
        return ResultUtils.success("查询成功",menuList);
    }

    /**
     * 查询上级树
     * @return
     */
    @GetMapping("/parent")
    public ResultVo getParent(){
        List<SysMenu> parent = menuService.getParent();
        return ResultUtils.success("查询成功",parent);
    }

}
