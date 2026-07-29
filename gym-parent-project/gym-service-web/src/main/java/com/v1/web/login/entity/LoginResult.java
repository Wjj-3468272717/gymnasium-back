package com.v1.web.login.entity;

import lombok.Data;

@Data
public class LoginResult {

    private String token;
    private Long userId;
    private String username;
    private String userType;
}
