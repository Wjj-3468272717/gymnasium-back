package com.v1.api.sys_user;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.sys_user.SysUserDTO;

public interface SysUserRpcService {
    PageResultDTO<SysUserDTO> listUsers(PageDTO page, String nickName, String phone);

    SysUserDTO loadUser(String username);

    SysUserDTO getUserById(Long userId);

    void resetPassword(Long userId, String newPassword);
}
