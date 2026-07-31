package com.v1.service.goods.goods.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.service.goods.goods.entity.Goods;
import com.v1.service.goods.goods.entity.GoodsParam;
import com.v1.service.goods.goods.mapper.GoodsMapper;
import com.v1.service.goods.goods.service.GoodsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class GoodsServiceImpl extends ServiceImpl<GoodsMapper, Goods> implements GoodsService {
    @Override
    public IPage<Goods> list(GoodsParam goodsParam) {
        IPage<Goods> page = new Page<>(goodsParam.getCurrentPage(), goodsParam.getPageSize());
        QueryWrapper<Goods> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(goodsParam.getName())) {
            queryWrapper.lambda().like(Goods::getName, goodsParam.getName());
        }
        return this.baseMapper.selectPage(page, queryWrapper);
    }
}
