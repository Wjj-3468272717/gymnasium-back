package com.v1.web.login.controller;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.v1.api.dto.member.MemberDTO;
import com.v1.api.dto.sys_menu.SysMenuDTO;
import com.v1.api.dto.sys_user.SysUserDTO;
import com.v1.api.member.MemberRpcService;
import com.v1.api.sys_menu.SysMenuRpcService;
import com.v1.api.sys_user.SysUserRpcService;
import com.v1.config.jwt.JwtUtils;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.login.entity.InfoParam;
import com.v1.web.login.entity.LoginParam;
import com.v1.web.login.entity.LoginResult;
import com.v1.web.login.entity.UserInfo;
import com.v1.web.sys_menu.entiry.MakeMenuTree;
import com.v1.web.sys_menu.entiry.RouterVO;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import sun.misc.BASE64Encoder;

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
    @DubboReference
    private MemberRpcService memberRpcService;
    @DubboReference
    private SysUserRpcService sysUserRpcService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @DubboReference
    SysMenuRpcService menuRpcService;

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
        //手动认证：直接验证密码，绕过Spring Security的AuthenticationManager配置问题
        if (loginParam.getUserType().equals("1")) {//会员用户
            MemberDTO member = memberRpcService.loadUser(loginParam.getUsername());
            if (member == null || !passwordEncoder.matches(loginParam.getPassword(), member.getPassword())) {
                return ResultUtils.error("用户名或密码错误");
            }
            //设置Spring Security上下文
            User userDetails = new User(member.getUsername(), member.getPassword(), new ArrayList<>());
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
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
            SysUserDTO user = sysUserRpcService.loadUser(loginParam.getUsername());
            if (user == null || !passwordEncoder.matches(loginParam.getPassword(), user.getPassword())) {
                return ResultUtils.error("用户名或密码错误");
            }
            //设置Spring Security上下文
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
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

    //查询用户信息
    @GetMapping("/getInfo")
    public ResultVo getInfo(InfoParam param) {
        UserInfo userInfo = new UserInfo();
        if (param.getUserType().equals("1")) {
            List<SysMenuDTO> menus = menuRpcService.getMenuByMemberId(param.getUserId());
            List<String> collection = Optional
                    .ofNullable(menus)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item != null)
                    .map(item -> item.getCode())
                    .collect(Collectors.toList());
            String[] strings = collection.toArray(new String[collection.size()]);
            MemberDTO member = memberRpcService.getMemberById(param.getUserId());
            userInfo.setUserId(member.getMemberId());
            userInfo.setPermissions(strings);
            userInfo.setName(member.getName());
            return ResultUtils.success("查询成功", userInfo);
        } else {
            if (param.getUserType().equals("2")) {
                SysUserDTO user = sysUserRpcService.getUserById(param.getUserId());
                List<SysMenuDTO> menus = null;
                if (StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")) {
                    menus = menuRpcService.getAllMenus();
                } else {
                    menus = menuRpcService.getMenuByUserId(user.getUserId());
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
                userInfo.setPermissions(strings);
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
            List<SysMenuDTO> menus = menuRpcService.getMenuByMemberId(param.getUserId());
            List<SysMenuDTO> collect = Optional
                    .ofNullable(menus)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item != null && !"2".equals(item.getType()))
                    .collect(Collectors.toList());
            List<RouterVO> router = MakeMenuTree.makeRouter(collect, 0L);
            return ResultUtils.success("查询成功", router);
        } else {
            if (param.getUserType().equals("2")) {
                SysUserDTO user = sysUserRpcService.getUserById(param.getUserId());
                List<SysMenuDTO> menus = null;
                if (StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")) {
                    menus = menuRpcService.getAllMenus();
                } else {
                    menus = menuRpcService.getMenuByUserId(user.getUserId());
                }
                List<SysMenuDTO> collect = Optional
                        .ofNullable(menus)
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(item -> item != null && !"2".equals(item.getType()))
                        .collect(Collectors.toList());
                List<RouterVO> router = MakeMenuTree.makeRouter(collect, 0L);
                return ResultUtils.success("查询成功", router);
            } else {
                return ResultUtils.error("用户类型错误！");
            }
        }
    }

}
