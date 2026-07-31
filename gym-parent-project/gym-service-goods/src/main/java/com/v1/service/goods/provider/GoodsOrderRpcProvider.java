package com.v1.service.goods.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.goods_order.GoodsOrderDTO;
import com.v1.api.dto.home.EChartItemDTO;
import com.v1.api.goods_order.GoodsOrderRpcService;
import com.v1.service.goods.goods.entity.GoodsParam;
import com.v1.service.goods.goods_order.entity.GoodsOrder;
import com.v1.service.goods.goods_order.entity.OrderItem;
import com.v1.service.goods.goods_order.entity.OrderParam;
import com.v1.service.goods.goods_order.service.GoodsOrderService;
import com.v1.service.goods.home.entity.EChartItem;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class GoodsOrderRpcProvider implements GoodsOrderRpcService {

    @Autowired
    private GoodsOrderService goodsOrderService;

    @Override
    public PageResultDTO<GoodsOrderDTO> listOrders(PageDTO page, String username, String userType, Long userId) {
        GoodsParam param = new GoodsParam();
        param.setCurrentPage(page.getCurrentPage().intValue());
        param.setPageSize(page.getPageSize().intValue());
        param.setName(username);

        IPage<GoodsOrder> result = goodsOrderService.page(param);

        PageResultDTO<GoodsOrderDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            GoodsOrderDTO orderDTO = new GoodsOrderDTO();
            BeanUtils.copyProperties(entity, orderDTO);
            return orderDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public void createOrder(GoodsOrderDTO order) {
        OrderParam param = new OrderParam();
        param.setUserId(order.getUserId());
        if (order.getItems() != null) {
            List<OrderItem> items = order.getItems().stream().map(itemDTO -> {
                OrderItem item = new OrderItem();
                item.setGoodsId(itemDTO.getGoodsId());
                item.setNum(itemDTO.getQuantity());
                return item;
            }).collect(Collectors.toList());
            param.setOrderItemList(items);
        } else {
            param.setOrderItemList(new ArrayList<>());
        }
        goodsOrderService.downOrder(param);
    }

    @Override
    public List<GoodsOrderDTO> getEChartData() {
        List<GoodsOrder> list = goodsOrderService.list();
        return list.stream().map(entity -> {
            GoodsOrderDTO dto = new GoodsOrderDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public int count() {
        return (int) goodsOrderService.count();
    }

    @Override
    public List<EChartItemDTO> getHotGoodsData() {
        List<EChartItem> items = goodsOrderService.hotGoods();
        return items.stream().map(item -> {
            EChartItemDTO dto = new EChartItemDTO();
            dto.setName(item.getName());
            dto.setValue(item.getValue());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EChartItemDTO> getHotCardsData() {
        List<EChartItem> items = goodsOrderService.hotCard();
        return items.stream().map(item -> {
            EChartItemDTO dto = new EChartItemDTO();
            dto.setName(item.getName());
            dto.setValue(item.getValue());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<EChartItemDTO> getHotCourseData() {
        List<EChartItem> items = goodsOrderService.hotCourse();
        return items.stream().map(item -> {
            EChartItemDTO dto = new EChartItemDTO();
            dto.setName(item.getName());
            dto.setValue(item.getValue());
            return dto;
        }).collect(Collectors.toList());
    }
}
