package com.v1.web.lost.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.lost.entity.Lost;
import com.v1.web.lost.entity.LostParam;
import com.v1.web.lost.service.LostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lost")
public class LostController {

    @Autowired
    LostService lostService;

    /**
     * 新增丢失物品
     * @param lost
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Lost lost){
        boolean updated = lostService.save(lost);
        if(updated){
            return ResultUtils.success("新增成功");
        }else{
            return ResultUtils.error("新增失败");
        }
    }

    /**
     * 编辑丢失物品
     * @param lost
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody Lost lost){
        boolean updated = lostService.updateById(lost);
        if(updated){
            return ResultUtils.success("编辑成功");
        }else{
            return ResultUtils.error("编辑失败");
        }
    }

    /**
     * 删除失物招领
     * @param lostId
     * @return
     */
    @DeleteMapping("/{lostId}")
    public ResultVo delete(@PathVariable("lostId") Long lostId){
        boolean updated = lostService.removeById(lostId);
        if(updated){
            return ResultUtils.success("删除成功");
        }else{
            return ResultUtils.error("删除失败");
        }
    }

    /**
     * 查询失物招领分页数据
     * @param lostParam
     * @return
     */
    @GetMapping("/list")
    public ResultVo list(LostParam lostParam){
        IPage<Lost> list = lostService.list(lostParam);
        return ResultUtils.success("查询成功",list);
    }

}
