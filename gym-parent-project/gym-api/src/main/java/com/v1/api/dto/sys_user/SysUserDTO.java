package com.v1.api.dto.sys_user;

import lombok.Data;
import java.io.Serializable;

@Data
public class SysUserDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String username;
    private String password;
    private String nickName;
    private String phone;
    private String email;
    private String sex;
    private String isAdmin;
    private String status;
}
