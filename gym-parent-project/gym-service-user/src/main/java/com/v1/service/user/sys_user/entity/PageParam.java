package com.v1.service.user.sys_user.entity;

import lombok.Data;

/**
 * 封装user相关分页查询参数
 */
@Data
public class PageParam {
    private Long currentPage;//当前页面
    private Long pageSize;//页面容量
    private String nickName;//角色名称
    private String phone;
}
