package com.v1.service.user.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.sys_role.SysRoleDTO;
import com.v1.api.sys_role.SysRoleRpcService;
import com.v1.service.user.sys_role.entity.RoleParam;
import com.v1.service.user.sys_role.entity.SysRole;
import com.v1.service.user.sys_role.service.SysRoleService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class SysRoleRpcProvider implements SysRoleRpcService {

    @Autowired
    private SysRoleService sysRoleService;

    @Override
    public PageResultDTO<SysRoleDTO> listRoles(PageDTO page, String roleName) {
        RoleParam param = new RoleParam();
        param.setCurrentPage(page.getCurrentPage());
        param.setPageSize(page.getPageSize());
        param.setRoleName(roleName);

        IPage<SysRole> result = sysRoleService.list(param);

        PageResultDTO<SysRoleDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            SysRoleDTO roleDTO = new SysRoleDTO();
            BeanUtils.copyProperties(entity, roleDTO);
            return roleDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public void saveRole(SysRoleDTO role) {
        SysRole entity = new SysRole();
        BeanUtils.copyProperties(role, entity);
        entity.setCreateTime(new Date());
        sysRoleService.save(entity);
    }

    @Override
    public void updateRole(SysRoleDTO role) {
        SysRole entity = new SysRole();
        BeanUtils.copyProperties(role, entity);
        entity.setUpdateTime(new Date());
        sysRoleService.updateById(entity);
    }

    @Override
    public void deleteRole(Long roleId) {
        sysRoleService.removeById(roleId);
    }

    @Override
    public List<SysRoleDTO> getAllRoles() {
        List<SysRole> list = sysRoleService.list();
        if (list == null) {
            return new ArrayList<>();
        }
        return list.stream().map(entity -> {
            SysRoleDTO roleDTO = new SysRoleDTO();
            BeanUtils.copyProperties(entity, roleDTO);
            return roleDTO;
        }).collect(Collectors.toList());
    }
}
