package com.v1.web.goods.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.goods.GoodsDTO;
import com.v1.api.goods.GoodsRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/goods")
@RestController
public class GoodsController {

    @DubboReference
    GoodsRpcService goodsRpcService;

    @PostMapping
    public ResultVo add(@RequestBody GoodsDTO goods){
        goodsRpcService.addGoods(goods);
        return ResultUtils.success("添加成功");
    }

    @PutMapping
    public ResultVo edit(@RequestBody GoodsDTO goods){
        goodsRpcService.updateGoods(goods);
        return ResultUtils.success("编辑成功");
    }

    @DeleteMapping("/{goodsId}")
    public ResultVo delete(@PathVariable("goodsId") Long goodsId){
        goodsRpcService.deleteGoods(goodsId);
        return ResultUtils.success("删除成功");
    }

    @GetMapping("/list")
    public ResultVo list(GoodsDTO goodsParam){
        PageDTO page = new PageDTO();
        page.setCurrentPage(goodsParam.getGoodsId() != null ? goodsParam.getGoodsId() : 1L);
        page.setPageSize(10L);
        PageResultDTO<GoodsDTO> list = goodsRpcService.listGoods(page, goodsParam.getName());
        return ResultUtils.success("查询成功", list);
    }

}
