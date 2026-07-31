package com.v1.service.member.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_apply.MemberApplyDTO;
import com.v1.api.member_apply.MemberApplyRpcService;
import com.v1.service.member.member_apply.entity.MemberApply;
import com.v1.service.member.member_apply.service.MemberApplyService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@DubboService
public class MemberApplyRpcProvider implements MemberApplyRpcService {

    @Autowired
    private MemberApplyService memberApplyService;

    @Override
    public PageResultDTO<MemberApplyDTO> list(PageDTO page, Long memberId) {
        IPage<MemberApply> pageParam = new Page<>(page.getCurrentPage(), page.getPageSize());
        QueryWrapper<MemberApply> queryWrapper = new QueryWrapper<>();
        if (memberId != null) {
            queryWrapper.lambda().eq(MemberApply::getMemberId, memberId);
        }
        queryWrapper.lambda().orderByDesc(MemberApply::getCreateTime);
        IPage<MemberApply> result = memberApplyService.page(pageParam, queryWrapper);

        PageResultDTO<MemberApplyDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            MemberApplyDTO applyDTO = new MemberApplyDTO();
            BeanUtils.copyProperties(entity, applyDTO);
            return applyDTO;
        }).collect(Collectors.toList()));
        return dto;
    }
}
