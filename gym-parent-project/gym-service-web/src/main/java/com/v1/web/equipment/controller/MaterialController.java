package com.v1.web.equipment.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.equipment.MaterialDTO;
import com.v1.api.equipment.MaterialRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @DubboReference
    MaterialRpcService materialRpcService;

    @PostMapping
    public ResultVo add(@RequestBody MaterialDTO material){
        materialRpcService.addMaterial(material);
        return ResultUtils.success("新增成功");
    }

    @PutMapping
    public ResultVo edit(@RequestBody MaterialDTO material){
        materialRpcService.updateMaterial(material);
        return ResultUtils.success("编辑成功");
    }

    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        materialRpcService.deleteMaterial(id);
        return ResultUtils.success("删除成功");
    }

    @GetMapping("/list")
    public ResultVo list(MaterialDTO listParam){
        PageDTO page = new PageDTO();
        page.setCurrentPage(1L);
        page.setPageSize(10L);
        PageResultDTO<MaterialDTO> list = materialRpcService.listMaterials(page, listParam.getName());
        return ResultUtils.success("查询成功", list);
    }

}
