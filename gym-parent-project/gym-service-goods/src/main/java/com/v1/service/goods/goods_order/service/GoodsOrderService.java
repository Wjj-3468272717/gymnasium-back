package com.v1.service.goods.goods_order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.service.goods.goods.entity.GoodsParam;
import com.v1.service.goods.goods_order.entity.GoodsOrder;
import com.v1.service.goods.goods_order.entity.OrderParam;
import com.v1.service.goods.home.entity.EChartItem;

import java.util.List;

public interface GoodsOrderService extends IService<GoodsOrder> {

    boolean downOrder(OrderParam param);
    IPage<GoodsOrder> page(GoodsParam param);

    List<EChartItem> hotGoods();
    List<EChartItem> hotCard();
    List<EChartItem> hotCourse();
}
