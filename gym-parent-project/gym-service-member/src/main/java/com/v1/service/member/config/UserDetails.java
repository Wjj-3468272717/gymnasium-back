package com.v1.service.member.config;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Collection;

public interface UserDetails extends org.springframework.security.core.userdetails.UserDetails {
    //用户权限字段集合
    Collection<? extends GrantedAuthority> getAuthorities();
    //密码
    String getPassword();
    //用户名
    String getUsername();
    //该用户是否被锁定 1 true, 0 false
    boolean isAccountNonLocked();
    //该用户认证是否过期 1 true,0 false
    boolean isCredentialsNonExpired();
    //该用户是否可用 1 true, 0 false
    boolean isEnabled();
}
