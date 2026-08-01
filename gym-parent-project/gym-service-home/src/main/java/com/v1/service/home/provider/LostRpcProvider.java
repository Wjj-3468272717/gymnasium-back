package com.v1.service.home.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.lost.LostDTO;
import com.v1.api.lost.LostRpcService;
import com.v1.service.home.lost.entity.Lost;
import com.v1.service.home.lost.entity.LostParam;
import com.v1.service.home.lost.service.LostService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@DubboService
public class LostRpcProvider implements LostRpcService {

    @Autowired
    private LostService lostService;

    @Override
    public PageResultDTO<LostDTO> list(PageDTO page, String lostName) {
        LostParam param = new LostParam();
        param.setCurrentPage(page.getCurrentPage());
        param.setPageSize(page.getPageSize());
        param.setLostName(lostName);

        IPage<Lost> result = lostService.list(param);

        PageResultDTO<LostDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            LostDTO lostDTO = new LostDTO();
            lostDTO.setId(entity.getLostId());
            lostDTO.setLostName(entity.getLostName());
            return lostDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public void add(LostDTO lost) {
        Lost entity = new Lost();
        entity.setLostName(lost.getLostName());
        lostService.save(entity);
    }

    @Override
    public void update(LostDTO lost) {
        Lost entity = new Lost();
        entity.setLostId(lost.getId());
        entity.setLostName(lost.getLostName());
        lostService.updateById(entity);
    }

    @Override
    public void delete(Long id) {
        lostService.removeById(id);
    }
}
