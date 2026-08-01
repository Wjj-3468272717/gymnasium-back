package com.v1.service.home.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.suggest.SuggestDTO;
import com.v1.api.suggest.SuggestRpcService;
import com.v1.service.home.suggest.entity.Suggest;
import com.v1.service.home.suggest.entity.SuggestParam;
import com.v1.service.home.suggest.service.SuggestService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.stream.Collectors;

@DubboService
public class SuggestRpcProvider implements SuggestRpcService {

    @Autowired
    private SuggestService suggestService;

    @Override
    public PageResultDTO<SuggestDTO> list(PageDTO page, String title) {
        SuggestParam param = new SuggestParam();
        param.setCurrentPage(page.getCurrentPage());
        param.setPageSize(page.getPageSize());
        param.setTitle(title);

        IPage<Suggest> result = suggestService.list(param);

        PageResultDTO<SuggestDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            SuggestDTO suggestDTO = new SuggestDTO();
            BeanUtils.copyProperties(entity, suggestDTO);
            return suggestDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public void add(SuggestDTO suggest) {
        Suggest entity = new Suggest();
        BeanUtils.copyProperties(suggest, entity);
        entity.setDateTime(new Date());
        suggestService.save(entity);
    }

    @Override
    public void update(SuggestDTO suggest) {
        Suggest entity = new Suggest();
        entity.setId(suggest.getId());
        entity.setTitle(suggest.getTitle());
        entity.setContent(suggest.getContent());
        if (suggest.getDateTime() != null) {
            entity.setDateTime(suggest.getDateTime());
        }
        suggestService.updateById(entity);
    }

    @Override
    public void delete(Long id) {
        suggestService.removeById(id);
    }
}
