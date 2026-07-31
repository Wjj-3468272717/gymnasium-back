package com.v1.web.sys_user.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.sys_user.entity.PageParam;
import com.v1.web.sys_user.entity.SysUser;

public interface SysUserService extends IService<SysUser> {

    IPage<SysUser> list(PageParam pageParam);

    //根据用户名获取用户
    SysUser loadUser(String username);
}
