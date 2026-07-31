package com.v1.api.dto.sys_menu;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysMenuDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long menuId;
    private Long parentId;
    private String title;
    private String code;
    private String type;
    private String path;
    private String icon;
    private Integer orderNum;
}
