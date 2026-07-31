package com.v1.web.goods_order.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.goods.entity.Goods;
import com.v1.web.goods.entity.GoodsParam;
import com.v1.web.goods.service.GoodsService;
import com.v1.web.goods_order.entity.GoodsOrder;
import com.v1.web.goods_order.entity.OrderItem;
import com.v1.web.goods_order.entity.OrderParam;
import com.v1.web.goods_order.service.GoodsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class GoodsOrderController {

    @Autowired
    GoodsOrderService goodsOrderService;

    //下单
    @PostMapping("/down")
    public ResultVo down(@RequestBody OrderParam param){
        boolean updated = goodsOrderService.downOrder(param);
        if(updated){
            return ResultUtils.success("下单成功");
        }else{
            return ResultUtils.error("下单失败");
        }
    }

    @GetMapping("/list")
    public ResultVo list(GoodsParam param){
        IPage<GoodsOrder> list = goodsOrderService.page(param);
        return ResultUtils.success("查询成功",list);
    }

}
