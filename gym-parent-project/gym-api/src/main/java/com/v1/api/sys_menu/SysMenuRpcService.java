package com.v1.api.sys_menu;

import com.v1.api.dto.sys_menu.SysMenuDTO;
import java.util.List;

public interface SysMenuRpcService {
    List<SysMenuDTO> getMenuByMemberId(Long memberId);

    List<SysMenuDTO> getMenuByUserId(Long userId);

    List<SysMenuDTO> getAllMenus();

    List<SysMenuDTO> getParentMenus();

    void saveMenu(SysMenuDTO menu);

    void updateMenu(SysMenuDTO menu);

    void deleteMenu(Long menuId);
}
