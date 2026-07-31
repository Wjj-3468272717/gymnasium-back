package com.v1.api.dto.login;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserInfoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long userId;
    private String name;
    private String[] permissions;
}
