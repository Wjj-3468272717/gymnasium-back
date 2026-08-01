package com.v1.service.home.suggest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.service.home.suggest.entity.Suggest;
import com.v1.service.home.suggest.entity.SuggestParam;

public interface SuggestService extends IService<Suggest> {
    IPage<Suggest> list(SuggestParam suggestParam);
}
