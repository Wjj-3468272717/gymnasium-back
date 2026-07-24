package com.v1.web.equipment.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.equipment.entity.ListParam;
import com.v1.web.equipment.entity.Material;
import com.v1.web.equipment.service.MaterialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @Autowired
    MaterialService materialService;

    /**
     * 新增设备
     * @param material
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Material material){
        boolean updated = materialService.save(material);
        if(updated){
            return ResultUtils.success("新增成功");
        }else{
            return ResultUtils.error("新增失败");
        }
    }

    /**
     * 修改设备
     * @param material
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody Material material){
        boolean updated = materialService.updateById(material);
        if(updated){
            return ResultUtils.success("编辑成功");
        }else{
            return ResultUtils.error("编辑失败");
        }
    }

    /**
     * 删除设备
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        boolean updated = materialService.removeById(id);
        if(updated){
            return ResultUtils.success("删除成功");
        }else{
            return ResultUtils.error("删除失败");
        }
    }

    @GetMapping("/list")
    public ResultVo list(ListParam listParam){
        IPage<Material> list =  materialService.list(listParam);
        return ResultUtils.success("查询成功",list);
    }

}
