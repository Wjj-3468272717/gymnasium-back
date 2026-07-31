package com.v1.service.user.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.api.dto.sys_menu.SysMenuDTO;
import com.v1.api.sys_menu.SysMenuRpcService;
import com.v1.service.user.sys_menu.entiry.SysMenu;
import com.v1.service.user.sys_menu.service.SysMenuService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@DubboService
public class SysMenuRpcProvider implements SysMenuRpcService {

    @Autowired
    private SysMenuService sysMenuService;

    @Override
    public List<SysMenuDTO> getMenuByMemberId(Long memberId) {
        return convert(sysMenuService.getMenuByMemberId(memberId));
    }

    @Override
    public List<SysMenuDTO> getMenuByUserId(Long userId) {
        return convert(sysMenuService.getMenuByUserId(userId));
    }

    @Override
    public List<SysMenuDTO> getAllMenus() {
        QueryWrapper<SysMenu> wrapper = new QueryWrapper<>();
        wrapper.lambda().orderByAsc(SysMenu::getOrderNum);
        return convert(sysMenuService.list(wrapper));
    }

    @Override
    public List<SysMenuDTO> getParentMenus() {
        return convert(sysMenuService.getParent());
    }

    @Override
    public void saveMenu(SysMenuDTO menu) {
        SysMenu entity = new SysMenu();
        BeanUtils.copyProperties(menu, entity);
        entity.setCreateTime(new Date());
        sysMenuService.save(entity);
    }

    @Override
    public void updateMenu(SysMenuDTO menu) {
        SysMenu entity = new SysMenu();
        BeanUtils.copyProperties(menu, entity);
        entity.setUpdateTime(new Date());
        sysMenuService.updateById(entity);
    }

    @Override
    public void deleteMenu(Long menuId) {
        sysMenuService.removeById(menuId);
    }

    private List<SysMenuDTO> convert(List<SysMenu> entities) {
        if (entities == null) {
            return new ArrayList<>();
        }
        return entities.stream().map(entity -> {
            SysMenuDTO dto = new SysMenuDTO();
            BeanUtils.copyProperties(entity, dto);
            return dto;
        }).collect(Collectors.toList());
    }
}
