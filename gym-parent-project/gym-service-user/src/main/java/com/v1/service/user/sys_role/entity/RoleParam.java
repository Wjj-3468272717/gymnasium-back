package com.v1.service.user.sys_role.entity;

import lombok.Data;

/**
 * 封装role相关分页查询参数
 */
@Data
public class RoleParam {
    private Long currentPage;//当前页面
    private Long pageSize;//页面容量
    private String roleName;//角色名称
}
