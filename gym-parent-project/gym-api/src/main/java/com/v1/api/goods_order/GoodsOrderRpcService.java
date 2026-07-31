package com.v1.api.goods_order;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.goods_order.GoodsOrderDTO;
import com.v1.api.dto.home.EChartItemDTO;

import java.util.List;

public interface GoodsOrderRpcService {
    PageResultDTO<GoodsOrderDTO> listOrders(PageDTO page, String username, String userType, Long userId);

    void createOrder(GoodsOrderDTO order);

    List<GoodsOrderDTO> getEChartData();

    int count();

    List<EChartItemDTO> getHotGoodsData();

    List<EChartItemDTO> getHotCardsData();

    List<EChartItemDTO> getHotCourseData();
}
