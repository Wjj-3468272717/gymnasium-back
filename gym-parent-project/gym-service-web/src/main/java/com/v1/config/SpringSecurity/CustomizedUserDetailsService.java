package com.v1.config.SpringSecurity;

import com.v1.web.member.entity.Member;
import com.v1.web.member.service.MemberService;
import com.v1.service.user.sys_menu.entiry.SysMenu;
import com.v1.service.user.sys_menu.service.SysMenuService;
import com.v1.service.user.sys_user.entity.SysUser;
import com.v1.service.user.sys_user.service.SysUserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import com.v1.service.user.config.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component(value = "customizedUserDetailsService")
public class CustomizedUserDetailsService implements UserDetailsService {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    MemberService memberService;
    @Autowired
    SysMenuService sysMenuService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //认证
        int index = username.indexOf(":");
        String userName = username.substring(0,index);
        String userType = username.substring(index+1);
        UserDetails userDetails = null;
        if(userType.equals("1")){//会员
            Member member = memberService.loadUser(userName);
            if(member == null){
                throw new UsernameNotFoundException("用户名或密码错误！");
            }
            //授权:把该用户拥有的按钮权限，交给spring secuity进行管理
            List<SysMenu> menus = sysMenuService.getMenuByMemberId(member.getMemberId());
            List<String> collect = menus.stream()
                    .map(item -> item.getCode())
                    .filter(item -> StringUtils.isNotEmpty(item) && item != null)
                    .collect(Collectors.toList());
            String[] strings = collect.toArray(new String[collect.size()]);
            List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
            //授权
            member.setAuthorities(authorityList);
            return (UserDetails) member;
        }else{
            if(userType.equals("2")){//员工
                SysUser user = sysUserService.loadUser(userName);
                if(user == null){
                    throw new UsernameNotFoundException("用户名或密码错误！");
                }
                //授权:把该用户拥有的按钮权限，交给spring secuity进行管理
                List<SysMenu> menus = sysMenuService.getMenuByUserId(user.getUserId());
                List<String> collect = menus.stream()
                        .map(item -> item.getCode())
                        .filter(item -> StringUtils.isNotEmpty(item) && item != null)
                        .collect(Collectors.toList());
                String[] strings = collect.toArray(new String[collect.size()]);
                List<GrantedAuthority> authorityList = AuthorityUtils.createAuthorityList(strings);
                //授权
                user.setAuthorities(authorityList);
                return (UserDetails) user;
            }else{
                throw new UsernameNotFoundException("用户类型不存在！");
            }
        }
    }
}
