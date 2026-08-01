package com.v1.service.home.lost.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.service.home.lost.entity.Lost;
import com.v1.service.home.lost.entity.LostParam;
import com.v1.service.home.lost.mapper.LostMapper;
import com.v1.service.home.lost.service.LostService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class LostServiceImpl extends ServiceImpl<LostMapper, Lost> implements LostService {
    @Override
    public IPage<Lost> list(LostParam lostParam) {
        IPage<Lost> page = new Page<>(lostParam.getCurrentPage(),lostParam.getPageSize());
        QueryWrapper<Lost> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(lostParam.getLostName())){
            queryWrapper.lambda().like(Lost::getLostName,lostParam.getLostName());
        }
        return this.baseMapper.selectPage(page,queryWrapper);
    }
}
