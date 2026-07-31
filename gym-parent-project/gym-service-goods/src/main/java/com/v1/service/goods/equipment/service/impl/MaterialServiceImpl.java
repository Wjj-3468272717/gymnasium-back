package com.v1.service.goods.equipment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.service.goods.equipment.entity.ListParam;
import com.v1.service.goods.equipment.entity.Material;
import com.v1.service.goods.equipment.mapper.MaterialMapper;
import com.v1.service.goods.equipment.service.MaterialService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialMapper, Material> implements MaterialService {
    @Override
    public IPage<Material> list(ListParam listParam) {
        IPage<Material> page = new Page<>(listParam.getCurrentPage(), listParam.getPageSize());
        QueryWrapper<Material> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(listParam.getName())) {
            queryWrapper.lambda().like(Material::getName, listParam.getName());
        }
        return this.baseMapper.selectPage(page, queryWrapper);
    }
}
