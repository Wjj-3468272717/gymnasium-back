package com.v1.web.goods.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.goods.entity.Goods;
import com.v1.web.goods.entity.GoodsParam;

public interface GoodsService extends IService<Goods> {
    IPage<Goods> list(GoodsParam goodsParam);
}
