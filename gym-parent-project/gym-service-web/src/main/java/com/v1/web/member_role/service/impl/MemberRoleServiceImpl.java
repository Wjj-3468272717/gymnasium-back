package com.v1.web.member_role.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.web.member_role.entity.MemberRole;
import com.v1.web.member_role.mapper.MemberRoleMapper;
import com.v1.web.member_role.service.MemberRoleService;
import org.springframework.stereotype.Service;

@Service
public class MemberRoleServiceImpl extends ServiceImpl<MemberRoleMapper, MemberRole> implements MemberRoleService {
}
