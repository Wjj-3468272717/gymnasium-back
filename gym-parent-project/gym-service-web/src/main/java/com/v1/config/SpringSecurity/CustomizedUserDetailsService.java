package com.v1.config.SpringSecurity;

import com.v1.api.dto.sys_menu.SysMenuDTO;
import com.v1.api.dto.sys_user.SysUserDTO;
import com.v1.api.sys_menu.SysMenuRpcService;
import com.v1.api.sys_user.SysUserRpcService;
import com.v1.web.member.entity.Member;
import com.v1.web.member.service.MemberService;
import org.apache.commons.lang.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component(value = "customizedUserDetailsService")
public class CustomizedUserDetailsService implements UserDetailsService {

    @DubboReference
    SysUserRpcService sysUserRpcService;
    @Autowired
    MemberService memberService;
    @DubboReference
    SysMenuRpcService sysMenuRpcService;

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //认证
        int index = username.indexOf(":");
        String userName = username.substring(0, index);
        String userType = username.substring(index + 1);
        if (userType.equals("1")) {//会员
            Member member = memberService.loadUser(userName);
            if (member == null) {
                throw new UsernameNotFoundException("用户名或密码错误！");
            }
            //授权:把该用户拥有的按钮权限，交给spring security进行管理
            List<SysMenuDTO> menus = sysMenuRpcService.getMenuByMemberId(member.getMemberId());
            List<String> collect = menus.stream()
                    .map(item -> item.getCode())
                    .filter(item -> StringUtils.isNotEmpty(item) && item != null)
                    .collect(Collectors.toList());
            String[] strings = collect.toArray(new String[collect.size()]);
            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
            //授权
            member.setAuthorities(authorityList);
            return member;
        } else {
            if (userType.equals("2")) {//员工
                SysUserDTO user = sysUserRpcService.loadUser(userName);
                if (user == null) {
                    throw new UsernameNotFoundException("用户名或密码错误！");
                }
                //授权:把该用户拥有的按钮权限，交给spring security进行管理
                List<SysMenuDTO> menus = sysMenuRpcService.getMenuByUserId(user.getUserId());
                List<String> collect = menus.stream()
                        .map(item -> item.getCode())
                        .filter(item -> StringUtils.isNotEmpty(item) && item != null)
                        .collect(Collectors.toList());
                String[] strings = collect.toArray(new String[collect.size()]);
                List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
                // For DTO-based auth, we create a UserDetails wrapper
                return new org.springframework.security.core.userdetails.User(
                        user.getUsername(),
                        user.getPassword(),
                        authorityList);
            } else {
                throw new UsernameNotFoundException("用户类型不存在！");
            }
        }
    }
}
