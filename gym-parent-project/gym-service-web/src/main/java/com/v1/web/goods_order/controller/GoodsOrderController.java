package com.v1.web.goods_order.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.goods_order.GoodsOrderDTO;
import com.v1.api.goods_order.GoodsOrderRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class GoodsOrderController {

    @DubboReference
    GoodsOrderRpcService goodsOrderRpcService;

    @PostMapping("/down")
    public ResultVo down(@RequestBody GoodsOrderDTO param){
        goodsOrderRpcService.createOrder(param);
        return ResultUtils.success("下单成功");
    }

    @GetMapping("/list")
    public ResultVo list(GoodsOrderDTO param){
        PageDTO page = new PageDTO();
        page.setCurrentPage(1L);
        page.setPageSize(10L);
        PageResultDTO<GoodsOrderDTO> list = goodsOrderRpcService.listOrders(page, null, null, null);
        return ResultUtils.success("查询成功", list);
    }

}
