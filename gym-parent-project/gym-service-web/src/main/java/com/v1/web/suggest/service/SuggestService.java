package com.v1.web.suggest.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.suggest.entity.Suggest;
import com.v1.web.suggest.entity.SuggestParam;

public interface SuggestService extends IService<Suggest> {
    IPage<Suggest> list(SuggestParam suggestParam);
}
