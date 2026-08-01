package com.v1.service.home.provider;

import com.v1.api.dto.home.EChartDTO;
import com.v1.api.dto.home.TotalCountDTO;
import com.v1.api.home.HomeRpcService;
import com.v1.service.home.suggest.entity.Suggest;
import com.v1.service.home.suggest.service.SuggestService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;

@DubboService
public class HomeRpcProvider implements HomeRpcService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private SuggestService suggestService;

    @Override
    public TotalCountDTO getTotalCount() {
        TotalCountDTO dto = new TotalCountDTO();
        try {
            Integer memberCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM member", Integer.class);
            dto.setMemberCount(memberCount != null ? memberCount.longValue() : 0L);
        } catch (Exception e) {
            dto.setMemberCount(0L);
        }
        try {
            Integer goodsCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM goods", Integer.class);
            dto.setGoodsCount(goodsCount != null ? goodsCount.longValue() : 0L);
        } catch (Exception e) {
            dto.setGoodsCount(0L);
        }
        try {
            Integer orderCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM goods_order", Integer.class);
            dto.setOrderCount(orderCount != null ? orderCount.longValue() : 0L);
        } catch (Exception e) {
            dto.setOrderCount(0L);
        }
        try {
            Integer courseCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM course", Integer.class);
            dto.setCourseCount(courseCount != null ? courseCount.longValue() : 0L);
        } catch (Exception e) {
            dto.setCourseCount(0L);
        }
        dto.setRevenue(0L);
        return dto;
    }

    @Override
    public EChartDTO getEChartData() {
        EChartDTO dto = new EChartDTO();
        dto.setXData(Collections.emptyList());
        dto.setSeries(Collections.emptyList());
        return dto;
    }
}
