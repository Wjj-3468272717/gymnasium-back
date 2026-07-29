package com.v1.web.goods_order.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.goods.entity.GoodsParam;
import com.v1.web.goods_order.entity.GoodsOrder;
import com.v1.web.goods_order.entity.OrderParam;
import com.v1.web.home.entity.EChartItem;

import java.util.List;

public interface GoodsOrderService extends IService<GoodsOrder> {

    boolean downOrder(OrderParam param);
    IPage<GoodsOrder> page(GoodsParam param);

    //热销商品
    List<EChartItem> hotGoods();
    //热销卡
    List<EChartItem> hotCard();
    //热销课程
    List<EChartItem> hotCourse();
}
