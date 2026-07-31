package com.v1.service.goods.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.service.goods.goods.entity.Goods;
import com.v1.service.goods.goods.entity.GoodsParam;

public interface GoodsService extends IService<Goods> {
    IPage<Goods> list(GoodsParam goodsParam);
}
