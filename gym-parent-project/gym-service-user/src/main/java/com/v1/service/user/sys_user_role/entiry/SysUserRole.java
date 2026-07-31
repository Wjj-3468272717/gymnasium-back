package com.v1.service.user.sys_user_role.entiry;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName(value = "sys_user_role")
public class SysUserRole {

    @TableId(type = IdType.AUTO)
    private Long UserRoleId;

    private Long userId;
    private Long roleId;
}
