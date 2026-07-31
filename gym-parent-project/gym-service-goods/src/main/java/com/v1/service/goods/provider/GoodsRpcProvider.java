package com.v1.service.goods.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.goods.GoodsDTO;
import com.v1.api.goods.GoodsRpcService;
import com.v1.service.goods.goods.entity.Goods;
import com.v1.service.goods.goods.entity.GoodsParam;
import com.v1.service.goods.goods.service.GoodsService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@DubboService
public class GoodsRpcProvider implements GoodsRpcService {

    @Autowired
    private GoodsService goodsService;

    @Override
    public PageResultDTO<GoodsDTO> listGoods(PageDTO page, String name) {
        GoodsParam param = new GoodsParam();
        param.setCurrentPage(page.getCurrentPage().intValue());
        param.setPageSize(page.getPageSize().intValue());
        param.setName(name);

        IPage<Goods> result = goodsService.list(param);

        PageResultDTO<GoodsDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            GoodsDTO goodsDTO = new GoodsDTO();
            BeanUtils.copyProperties(entity, goodsDTO);
            return goodsDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public GoodsDTO getGoodsById(Long goodsId) {
        Goods entity = goodsService.getById(goodsId);
        if (entity == null) {
            return null;
        }
        GoodsDTO dto = new GoodsDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void addGoods(GoodsDTO goods) {
        Goods entity = new Goods();
        BeanUtils.copyProperties(goods, entity);
        goodsService.save(entity);
    }

    @Override
    public void updateGoods(GoodsDTO goods) {
        Goods entity = new Goods();
        BeanUtils.copyProperties(goods, entity);
        goodsService.updateById(entity);
    }

    @Override
    public void deleteGoods(Long goodsId) {
        goodsService.removeById(goodsId);
    }
}
