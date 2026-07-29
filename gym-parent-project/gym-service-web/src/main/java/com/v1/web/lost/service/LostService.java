package com.v1.web.lost.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.lost.entity.Lost;
import com.v1.web.lost.entity.LostParam;

public interface LostService extends IService<Lost> {
    IPage<Lost> list(LostParam lostParam);
}
