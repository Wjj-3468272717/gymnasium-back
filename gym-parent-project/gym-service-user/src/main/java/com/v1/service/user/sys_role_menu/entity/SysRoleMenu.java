package com.v1.service.user.sys_role_menu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_role_menu")
public class SysRoleMenu {

    @TableId(type = IdType.AUTO)
    private Long roleMenuId;
    private Long menuId;
    private Long roleId;

}
