package com.v1.service.home.home.entity;

import lombok.Data;

@Data
public class ResetPassword {

    private Long userId;
    private String userType;
    private String password;
    private String oldPassword;

}
