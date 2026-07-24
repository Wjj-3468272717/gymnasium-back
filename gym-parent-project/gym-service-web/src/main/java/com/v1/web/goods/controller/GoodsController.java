package com.v1.web.goods.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.goods.entity.Goods;
import com.v1.web.goods.entity.GoodsParam;
import com.v1.web.goods.service.GoodsService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/goods")
@RestController
public class GoodsController {

    @Autowired
    GoodsService goodsService;

    /**
     * 新增商品
     * @param goods
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Goods goods){
        boolean updated = goodsService.save(goods);
        if(updated){
            return ResultUtils.success("添加成功");
        }else{
            return ResultUtils.error("添加失败");
        }
    }

    /**
     * 修改商品
     * @param goods
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody Goods goods){
        boolean updated = goodsService.updateById(goods);
        if(updated){
            return ResultUtils.success("编辑成功");
        }else{
            return ResultUtils.error("编辑失败");
        }
    }

    @DeleteMapping("/{goodsId}")
    public ResultVo delete(@PathVariable("goodsId") Long goodsId){
        boolean updated = goodsService.removeById(goodsId);
        if(updated){
            return ResultUtils.success("删除成功");
        }else{
            return ResultUtils.error("删除失败");
        }
    }

    @GetMapping("/list")
    public ResultVo list(GoodsParam goodsParam){
        IPage<Goods> list = goodsService.list(goodsParam);
        return ResultUtils.success("查询成功",list);
    }

}
