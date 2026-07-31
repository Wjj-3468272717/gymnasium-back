package com.v1.api.login;

import com.v1.api.dto.login.LoginDTO;
import com.v1.api.dto.login.LoginResultDTO;
import com.v1.api.dto.login.UserInfoDTO;

public interface LoginRpcService {
    LoginResultDTO login(LoginDTO loginParam);

    UserInfoDTO getUserInfo(Long userId, String userType);

    String generateCaptcha();
}
