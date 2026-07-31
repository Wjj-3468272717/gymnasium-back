package com.v1.service.user.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.sys_user.SysUserDTO;
import com.v1.api.sys_user.SysUserRpcService;
import com.v1.service.user.sys_user.entity.PageParam;
import com.v1.service.user.sys_user.entity.SysUser;
import com.v1.service.user.sys_user.service.SysUserService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

@DubboService
public class SysUserRpcProvider implements SysUserRpcService {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private SysUserService sysUserService;

    @Override
    public PageResultDTO<SysUserDTO> listUsers(PageDTO page, String nickName, String phone) {
        PageParam param = new PageParam();
        param.setCurrentPage(page.getCurrentPage());
        param.setPageSize(page.getPageSize());
        param.setNickName(nickName);
        param.setPhone(phone);

        IPage<SysUser> result = sysUserService.list(param);

        PageResultDTO<SysUserDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            SysUserDTO userDTO = new SysUserDTO();
            BeanUtils.copyProperties(entity, userDTO);
            return userDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public SysUserDTO loadUser(String username) {
        SysUser entity = sysUserService.loadUser(username);
        if (entity == null) {
            return null;
        }
        SysUserDTO dto = new SysUserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public SysUserDTO getUserById(Long userId) {
        SysUser entity = sysUserService.getById(userId);
        if (entity == null) {
            return null;
        }
        SysUserDTO dto = new SysUserDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        SysUser entity = sysUserService.getById(userId);
        if (entity != null) {
            entity.setPassword(PASSWORD_ENCODER.encode(newPassword));
            sysUserService.updateById(entity);
        }
    }
}
