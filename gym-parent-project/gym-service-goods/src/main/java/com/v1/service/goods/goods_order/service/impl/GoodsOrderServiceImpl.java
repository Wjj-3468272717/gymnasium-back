package com.v1.service.goods.goods_order.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.api.dto.sys_user.SysUserDTO;
import com.v1.api.sys_user.SysUserRpcService;
import com.v1.service.goods.goods.entity.Goods;
import com.v1.service.goods.goods.entity.GoodsParam;
import com.v1.service.goods.goods.service.GoodsService;
import com.v1.service.goods.goods_order.entity.GoodsOrder;
import com.v1.service.goods.goods_order.entity.OrderItem;
import com.v1.service.goods.goods_order.entity.OrderParam;
import com.v1.service.goods.goods_order.mapper.GoodsOrderMapper;
import com.v1.service.goods.goods_order.service.GoodsOrderService;
import com.v1.service.goods.home.entity.EChartItem;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class GoodsOrderServiceImpl extends ServiceImpl<GoodsOrderMapper, GoodsOrder> implements GoodsOrderService {

    @Autowired
    private GoodsService goodsService;

    @DubboReference
    private SysUserRpcService sysUserRpcService;

    @Override
    public boolean downOrder(OrderParam param) {
        SysUserDTO user = sysUserRpcService.getUserById(param.getUserId());
        List<OrderItem> list = param.getOrderItemList();
        List<GoodsOrder> orderList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Long goodsId = list.get(i).getGoodsId();
            Integer num = list.get(i).getNum();
            Goods goods = goodsService.getById(goodsId);
            GoodsOrder goodsOrder = new GoodsOrder();

            BeanUtils.copyProperties(goods, goodsOrder);
            goodsOrder.setNum(num);
            BigDecimal price = goods.getPrice();
            BigDecimal number = BigDecimal.valueOf(num);
            BigDecimal total = number.multiply(price);
            BigDecimal totalPrice = total.setScale(2, BigDecimal.ROUND_HALF_UP);
            goodsOrder.setTotalPrice(totalPrice);
            goodsOrder.setControlUser(user.getNickName());
            goodsOrder.setCreateTime(new Date());
            orderList.add(goodsOrder);
        }
        if (orderList.size() > 0) {
            return this.saveBatch(orderList);
        }
        return false;
    }

    @Override
    public IPage<GoodsOrder> page(GoodsParam param) {
        IPage<GoodsOrder> page = new Page<>(param.getCurrentPage(), param.getPageSize());
        QueryWrapper<GoodsOrder> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(param.getName())) {
            queryWrapper.lambda().like(GoodsOrder::getName, param.getName());
        }
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<EChartItem> hotGoods() {
        return this.baseMapper.hotGoods();
    }

    @Override
    public List<EChartItem> hotCard() {
        return this.baseMapper.hotCards();
    }

    @Override
    public List<EChartItem> hotCourse() {
        return this.baseMapper.hotCourse();
    }
}
