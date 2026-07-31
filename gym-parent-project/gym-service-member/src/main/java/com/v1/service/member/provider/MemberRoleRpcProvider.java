package com.v1.service.member.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.api.dto.member_role.MemberRoleDTO;
import com.v1.api.member_role.MemberRoleRpcService;
import com.v1.service.member.member_role.entity.MemberRole;
import com.v1.service.member.member_role.service.MemberRoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

@DubboService
public class MemberRoleRpcProvider implements MemberRoleRpcService {

    @Autowired
    private MemberRoleService memberRoleService;

    @Override
    public MemberRoleDTO getByMemberId(Long memberId) {
        QueryWrapper<MemberRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberRole::getMemberId, memberId);
        MemberRole entity = memberRoleService.getOne(queryWrapper);
        if (entity == null) {
            return null;
        }
        MemberRoleDTO dto = new MemberRoleDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void save(MemberRoleDTO role) {
        MemberRole entity = new MemberRole();
        BeanUtils.copyProperties(role, entity);
        memberRoleService.save(entity);
    }
}
