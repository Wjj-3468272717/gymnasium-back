package com.v1.service.goods.equipment.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.service.goods.equipment.entity.ListParam;
import com.v1.service.goods.equipment.entity.Material;

public interface MaterialService extends IService<Material> {
    IPage<Material> list(ListParam listParam);
}
