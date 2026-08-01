package com.v1.service.home.lost.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.service.home.lost.entity.Lost;
import com.v1.service.home.lost.entity.LostParam;

public interface LostService extends IService<Lost> {
    IPage<Lost> list(LostParam lostParam);
}
