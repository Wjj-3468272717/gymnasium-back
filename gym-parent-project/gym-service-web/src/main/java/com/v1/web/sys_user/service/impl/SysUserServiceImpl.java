package com.v1.web.sys_user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.web.sys_user.entity.PageParam;
import com.v1.web.sys_user.entity.SysUser;
import com.v1.web.sys_user.mapper.SysUserMapper;
import com.v1.web.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import javax.management.Query;

@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper,SysUser> implements SysUserService{
    @Override
    public IPage<SysUser> list(PageParam pageParam) {
        IPage<SysUser> ipage = new Page<>();
        ipage.setSize(pageParam.getPageSize());
        ipage.setPages(pageParam.getCurrentPage());

        QueryWrapper<SysUser> q = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(pageParam.getNickName())){
            q.lambda().like(SysUser::getUsername,pageParam.getNickName());
        }
        if(StringUtils.isNotEmpty(pageParam.getPhone())){
            q.lambda().like(SysUser::getPhone,pageParam.getPhone());
        }
        return this.baseMapper.selectPage(ipage,q);
    }

    @Override
    public SysUser loadUser(String username) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(SysUser::getUsername,username);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
