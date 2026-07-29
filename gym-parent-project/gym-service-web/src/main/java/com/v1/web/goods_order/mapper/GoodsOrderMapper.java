package com.v1.web.goods_order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.v1.web.goods_order.entity.GoodsOrder;
import com.v1.web.home.entity.EChartItem;

import java.util.List;

public interface GoodsOrderMapper extends BaseMapper<GoodsOrder> {
    //热销商品
    List<EChartItem> hotGoods();
    //热销卡
    List<EChartItem> hotCards();
    //热销课程
    List<EChartItem> hotCourse();
}
