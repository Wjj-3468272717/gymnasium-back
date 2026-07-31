package com.v1.service.member.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_recharge.MemberRechargeDTO;
import com.v1.api.member_recharge.MemberRechargeRpcService;
import com.v1.service.member.member_recharge.entity.RechargeParamList;
import com.v1.service.member.member_recharge.entity.MemberRecharge;
import com.v1.service.member.member_recharge.service.MemberRechargeService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@DubboService
public class MemberRechargeRpcProvider implements MemberRechargeRpcService {

    @Autowired
    private MemberRechargeService memberRechargeService;

    @Override
    public PageResultDTO<MemberRechargeDTO> getRechargeList(PageDTO page) {
        RechargeParamList paramList = new RechargeParamList();
        paramList.setCurrentPage(page.getCurrentPage());
        paramList.setPageSize(page.getPageSize());

        IPage<MemberRecharge> result = memberRechargeService.getRechargeList(paramList);

        PageResultDTO<MemberRechargeDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            MemberRechargeDTO rechargeDTO = new MemberRechargeDTO();
            BeanUtils.copyProperties(entity, rechargeDTO);
            return rechargeDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public PageResultDTO<MemberRechargeDTO> getRechargeByMember(PageDTO page, Long memberId) {
        RechargeParamList paramList = new RechargeParamList();
        paramList.setCurrentPage(page.getCurrentPage());
        paramList.setPageSize(page.getPageSize());
        paramList.setMemberId(memberId);

        IPage<MemberRecharge> result = memberRechargeService.getRechargeByMember(paramList);

        PageResultDTO<MemberRechargeDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            MemberRechargeDTO rechargeDTO = new MemberRechargeDTO();
            BeanUtils.copyProperties(entity, rechargeDTO);
            return rechargeDTO;
        }).collect(Collectors.toList()));
        return dto;
    }
}
