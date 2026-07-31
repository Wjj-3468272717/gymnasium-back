package com.v1.api.dto.login;

import lombok.Data;
import java.io.Serializable;

@Data
public class LoginResultDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String token;
    private Long userId;
    private String username;
    private String userType;
}
