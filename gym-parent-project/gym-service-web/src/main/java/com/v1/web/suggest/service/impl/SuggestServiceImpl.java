package com.v1.web.suggest.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.web.suggest.entity.Suggest;
import com.v1.web.suggest.entity.SuggestParam;
import com.v1.web.suggest.mapper.SuggestMapper;
import com.v1.web.suggest.service.SuggestService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class SuggestServiceImpl extends ServiceImpl<SuggestMapper, Suggest> implements SuggestService {
    @Override
    public IPage<Suggest> list(SuggestParam suggestParam) {
        IPage<Suggest> page = new Page<>(suggestParam.getCurrentPage(),suggestParam.getPageSize());
        QueryWrapper<Suggest> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(suggestParam.getTitle())){
            queryWrapper.lambda().like(Suggest::getTitle,suggestParam.getTitle());
        }
        queryWrapper.lambda().orderByDesc(Suggest::getDateTime);
        return this.baseMapper.selectPage(page,queryWrapper);
    }
}
