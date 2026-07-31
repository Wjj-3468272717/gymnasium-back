package com.v1.service.goods.goods_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.v1.service.goods.goods_order.entity.GoodsOrder;
import com.v1.service.goods.home.entity.EChartItem;

import java.util.List;

public interface GoodsOrderMapper extends BaseMapper<GoodsOrder> {
    List<EChartItem> hotGoods();
    List<EChartItem> hotCards();
    List<EChartItem> hotCourse();
}
