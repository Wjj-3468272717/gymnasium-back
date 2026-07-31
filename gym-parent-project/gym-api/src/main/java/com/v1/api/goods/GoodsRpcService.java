package com.v1.api.goods;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.goods.GoodsDTO;

public interface GoodsRpcService {
    PageResultDTO<GoodsDTO> listGoods(PageDTO page, String name);

    GoodsDTO getGoodsById(Long goodsId);

    void addGoods(GoodsDTO goods);

    void updateGoods(GoodsDTO goods);

    void deleteGoods(Long goodsId);
}
