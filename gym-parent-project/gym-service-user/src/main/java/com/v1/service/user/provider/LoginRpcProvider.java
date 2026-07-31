package com.v1.service.user.provider;

import com.v1.api.dto.login.LoginDTO;
import com.v1.api.dto.login.LoginResultDTO;
import com.v1.api.dto.login.UserInfoDTO;
import com.v1.api.login.LoginRpcService;
import com.v1.service.user.config.JwtUtils;
import com.v1.service.user.sys_menu.entiry.SysMenu;
import com.v1.service.user.sys_menu.service.SysMenuService;
import com.v1.service.user.sys_user.entity.SysUser;
import com.v1.service.user.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@DubboService
public class LoginRpcProvider implements LoginRpcService {

    private static final PasswordEncoder PASSWORD_ENCODER = new BCryptPasswordEncoder();

    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public LoginResultDTO login(LoginDTO loginParam) {
        // gym-service-user 只处理员工（userType=2）登录，会员登录由 gym-service-web 本地处理
        if (loginParam == null) {
            return null;
        }
        if (StringUtils.isEmpty(loginParam.getUsername()) || StringUtils.isEmpty(loginParam.getPassword())) {
            return null;
        }
        if (!"2".equals(loginParam.getUserType())) {
            throw new UnsupportedOperationException("member login is not supported in gym-service-user");
        }
        SysUser user = sysUserService.loadUser(loginParam.getUsername());
        if (user == null || !PASSWORD_ENCODER.matches(loginParam.getPassword(), user.getPassword())) {
            return null;
        }
        Map<String, String> claims = new HashMap<>();
        claims.put("userId", Long.toString(user.getUserId()));
        claims.put("username", user.getUsername());
        claims.put("userType", "2");
        String token = jwtUtils.generateToken(claims);

        LoginResultDTO result = new LoginResultDTO();
        result.setToken(token);
        result.setUserId(user.getUserId());
        result.setUsername(user.getNickName());
        result.setUserType("2");
        return result;
    }

    @Override
    public UserInfoDTO getUserInfo(Long userId, String userType) {
        if (!"2".equals(userType)) {
            throw new UnsupportedOperationException("member userInfo is not supported in gym-service-user");
        }
        SysUser user = sysUserService.getById(userId);
        if (user == null) {
            return null;
        }
        List<SysMenu> menus;
        if (StringUtils.isNotEmpty(user.getIsAdmin()) && "1".equals(user.getIsAdmin())) {
            menus = sysMenuService.list();
        } else {
            menus = sysMenuService.getMenuByUserId(user.getUserId());
        }
        List<String> codes = Optional.ofNullable(menus).orElse(new ArrayList<>()).stream()
                .filter(item -> item != null)
                .map(SysMenu::getCode)
                .collect(Collectors.toList());

        UserInfoDTO dto = new UserInfoDTO();
        dto.setUserId(user.getUserId());
        dto.setName(user.getNickName());
        dto.setPermissions(codes.toArray(new String[0]));
        return dto;
    }

    @Override
    public String generateCaptcha() {
        // 简化实现：生成 4 位随机字母数字验证码，图片渲染由 gym-service-web 完成
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
