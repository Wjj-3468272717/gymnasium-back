package com.v1.web.login.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.v1.config.jwt.JwtUtils;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.login.entity.InfoParam;
import com.v1.web.login.entity.LoginParam;
import com.v1.web.login.entity.LoginResult;
import com.v1.web.login.entity.UserInfo;
import com.v1.web.member.entity.Member;
import com.v1.web.member.service.MemberService;
import com.v1.web.sys_menu.entiry.MakeMenuTree;
import com.v1.web.sys_menu.entiry.RouterVO;
import com.v1.web.sys_menu.entiry.SysMenu;
import com.v1.web.sys_menu.service.SysMenuService;
import com.v1.web.sys_user.entity.SysUser;
import com.v1.web.sys_user.service.SysUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;
import org.springframework.beans.factory.annotation.Autowired;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/login")
public class LoginController {

    @Autowired
    private DefaultKaptcha defaultKaptcha;
    @Autowired
    private MemberService memberService;
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
//    @Autowired
//    private AuthenticationManager authenticationManager;

    /**
     * 生成图片验证码
     */
    @PostMapping("/image")
    public ResultVo imageCode(HttpServletRequest request) {
        String text = defaultKaptcha.createText();
        HttpSession session = request.getSession();
        session.setAttribute("code", text);
        System.out.println("图片验证码：" + text);
        BufferedImage image = defaultKaptcha.createImage(text);
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            ImageIO.write(image, "jpg", outputStream);
            BASE64Encoder encoder = new BASE64Encoder();
            String base64 = encoder.encode(outputStream.toByteArray());
            String captchaBase64 = "data:image/jpeg;base64," + base64.replaceAll("\r\n", "");
            ResultVo resultVo = new ResultVo("生成成功", 200, captchaBase64);
            return resultVo;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @PostMapping("/login")
    public ResultVo login(HttpServletRequest req, @RequestBody LoginParam loginParam) {
        if (StringUtils.isEmpty(loginParam.getCode()) || StringUtils.isEmpty(loginParam.getUserType())
                || StringUtils.isEmpty(loginParam.getUsername()) || StringUtils.isEmpty(loginParam.getPassword())) {
            return ResultUtils.error("用户名，密码，验证和用户类型 都不能为空!");
        }
        //获取Session
        HttpSession session = req.getSession();
        String code = (String) session.getAttribute("code");
        //校验验证码是否过期
        if (StringUtils.isEmpty(code)) {
            return ResultUtils.error("验证码过期!");
        }
        //校验验证码
        if (!code.equals(loginParam.getCode())) {
            return ResultUtils.error("验证码错误!");
        }
        //手动认证：直接验证密码，绕过Spring Security的AuthenticationManager配��问题
        if (loginParam.getUserType().equals("1")) {//会员用户
            Member member = memberService.loadUser(loginParam.getUsername());
            if (member == null || !passwordEncoder.matches(loginParam.getPassword(), member.getPassword())) {
                return ResultUtils.error("用户名或密码错误");
            }
            //设置Spring Security上下文
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(member, null, member.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            //生成token
            Map<String, String> map = new HashMap<>();
            map.put("userId", Long.toString(member.getMemberId()));
            map.put("username", member.getUsername());
            map.put("userType", "1");
            String token = jwtUtils.generateToken(map);
            System.out.println("token:" + token);
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUserId(member.getMemberId());
            result.setUsername(member.getName());
            result.setUserType(loginParam.getUserType());
            return ResultUtils.success("登录成功", result);
        } else if (loginParam.getUserType().equals("2")) {//员工
            SysUser user = sysUserService.loadUser(loginParam.getUsername());
            if (user == null || !passwordEncoder.matches(loginParam.getPassword(), user.getPassword())) {
                return ResultUtils.error("用户名或密码错误");
            }
            //设置Spring Security上下文
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
            //生成token
            Map<String, String> map = new HashMap<>();
            map.put("userId", Long.toString(user.getUserId()));
            map.put("username", user.getUsername());
            map.put("userType", "2");
            String token = jwtUtils.generateToken(map);
            System.out.println("token:" + token);
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUserId(user.getUserId());
            result.setUsername(user.getNickName());
            result.setUserType(loginParam.getUserType());
            return ResultUtils.success("登录成功", result);
        } else {
            return ResultUtils.error("用户类型错误");
        }
    }

    @Autowired
    SysMenuService menuService;

    //查询用户信息
    @GetMapping("/getInfo")
    public ResultVo getInfo(InfoParam param) {
        UserInfo userInfo = new UserInfo();
        if (param.getUserType().equals("1")) {
            List<SysMenu> menus = menuService.getMenuByMemberId(param.getUserId());
            List<String> collection = Optional
                    .ofNullable(menus)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item != null)
                    .map(item -> item.getCode())
                    .collect(Collectors.toList());
            String[] strings = collection.toArray(new String[collection.size()]);
            Member member = memberService.getById(param.getUserId());
            userInfo.setUserId(member.getMemberId());
            userInfo.setPermissions(strings);
            userInfo.setName(member.getName());
            return ResultUtils.success("查询成功", userInfo);
        } else {
            if (param.getUserType().equals("2")) {
                SysUser user = sysUserService.getById(param.getUserId());
                List<SysMenu> menus = null;
                if (StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")) {
                    menus = menuService.list();
                } else {
                    menus = menuService.getMenuByUserId(user.getUserId());
                }
                List<String> collect = Optional
                        .ofNullable(menus)
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(item -> item != null)
                        .map(item -> item.getCode())
                        .collect(Collectors.toList());
                String[] strings = collect.toArray(new String[collect.size()]);
                userInfo.setUserId(user.getUserId());
                userInfo.setName(user.getNickName());
                return ResultUtils.success("查询成功", userInfo);
            } else {
                return ResultUtils.error("用户类型错误!");
            }
        }
    }

    //获取菜单信息
    @GetMapping("/getMenuList")
    public ResultVo getMenuList(InfoParam param) {
        if (param.getUserType().equals("1")) {
            List<SysMenu> menus = menuService.getMenuByUserId(param.getUserId());
            List<SysMenu> collect = Optional
                    .ofNullable(menus)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item != null && !item.getType().equals("2"))
                    .collect(Collectors.toList());
            List<RouterVO> router = MakeMenuTree.makeRouter(collect, 0L);
            return ResultUtils.success("查询成功", router);
        } else {
            if (param.getUserType().equals("2")) {
                SysUser user = sysUserService.getById(param.getUserId());
                List<SysMenu> menus = null;
                if (StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")) {
                    menus = menuService.list();
                } else {
                    menuService.getMenuByUserId(user.getUserId());
                }
                List<SysMenu> collect = Optional
                        .ofNullable(menus)
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(item -> item != null && !item.getType().equals("2"))
                        .collect(Collectors.toList());
                List<RouterVO> router = MakeMenuTree.makeRouter(collect, 0L);
                return ResultUtils.success("查询成功", router);
            } else {
                return ResultUtils.error("用户类型错误！");
            }
        }
    }

}
