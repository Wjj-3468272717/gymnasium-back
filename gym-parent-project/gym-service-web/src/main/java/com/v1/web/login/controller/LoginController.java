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
import org.springframework.util.DigestUtils;
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

    /**
     * 生成图片验证码
     *
     * @param request
     * @return
     */
    @PostMapping("/image")
    public ResultVo imageCode(HttpServletRequest request) {
        //获取验证码字符
        String text = defaultKaptcha.createText();
        //存储验证码到Session当中
        HttpSession session = request.getSession();
        session.setAttribute("code", text);
        System.out.println("图片验证码：" + text);
        //生成图片
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
        HttpSession session = req.getSession();
        String code = (String) session.getAttribute("code");
        //校验验证码
        if (!code.equals(loginParam.getCode())) {
            return ResultUtils.error("验证码错误");
        }
        String password = DigestUtils.md5DigestAsHex(loginParam.getPassword().getBytes());
        //判断用户类型
        if (loginParam.getUserType().equals("1")) {//会员用户
            QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Member::getUsername, loginParam.getUsername()).eq(Member::getPassword, password);
            Member one = memberService.getOne(queryWrapper);
            if (one == null) {
                return ResultUtils.error("用户名或密码错误");
            }
            //生成token
            Map<String, String> map = new HashMap<>();
            map.put("userId", Long.toString(one.getMemberId()));
            map.put("username", one.getUsername());
            String token = jwtUtils.generateToken(map);
            System.out.println("token:" + token);
            //返回登录成功信息
            LoginResult result = new LoginResult();
            result.setToken(token);
            result.setUserId(one.getMemberId());
            result.setUsername(one.getName());
            result.setUserType(loginParam.getUserType());
            return ResultUtils.success("登录成功", result);
        } else {
            if (loginParam.getUserType().equals("2")) {//员工
                QueryWrapper<SysUser> queryWrapper = new QueryWrapper<>();
                queryWrapper.lambda().eq(SysUser::getPassword, password).eq(SysUser::getUsername, loginParam.getUsername());
                SysUser one = sysUserService.getOne(queryWrapper);
                if (one == null) {
                    return ResultUtils.error("用户名或密码错误");
                }
                //生成token
                Map<String, String> map = new HashMap<>();
                map.put("userId", Long.toString(one.getUserId()));
                map.put("username", one.getUsername());
                String token = jwtUtils.generateToken(map);
                System.out.println("token:" + token);
                //返回登录成功信息
                LoginResult result = new LoginResult();
                result.setToken(token);
                result.setUserId(one.getUserId());
                result.setUsername(one.getNickName());
                result.setUserType(loginParam.getUserType());
                return ResultUtils.success("登录成功", result);
            } else {
                return ResultUtils.error("用户类型错误");
            }
        }
    }

    @Autowired
    SysMenuService menuService;

    //查询用户信息
    @GetMapping("/getInfo")
    public ResultVo getInfo(InfoParam param) {
        UserInfo userInfo = new UserInfo();
        if (param.getUserType().equals("1")) {//会员
            //根据会员id查询权限
            List<SysMenu> menus = menuService.getMenuByMemberId(param.getUserId());
            //获取全部code字段
            List<String> collection = Optional
                    .ofNullable(menus)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item != null)
                    .map(item -> item.getCode())
                    .collect(Collectors.toList());
            //转化成数组
            String[] strings = collection.toArray(new String[collection.size()]);
            //查询用户信息
            Member member = memberService.getById(param.getUserId());
            //返回用户信息
            userInfo.setUserId(member.getMemberId());
            userInfo.setPermissions(strings);
            userInfo.setName(member.getName());
            return ResultUtils.success("查询成功",userInfo);
        } else {
            if (param.getUserType().equals("2")) {//员工
                //查询用户信息
                SysUser user = sysUserService.getById(param.getUserId());
                List<SysMenu> menus = null;
                if(StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")){//超级管理员
                     menus = menuService.list();
                }else{
                    menus = menuService.getMenuByUserId(user.getUserId());
                }
                //获取所有权限字段
                List<String> collect = Optional
                        .ofNullable(menus)
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(item -> item != null)
                        .map(item -> item.getCode())
                        .collect(Collectors.toList());
                //转换成数组
                String[] strings = collect.toArray(new String[collect.size()]);
                //返回信息
                userInfo.setUserId(user.getUserId());
                userInfo.setName(user.getNickName());
                return ResultUtils.success("查询成功",userInfo);
            }else{
                return ResultUtils.error("用户类型错误!");
            }
        }
    }

    //获取菜单信息
    @GetMapping("/getMenuList")
    public ResultVo getMenuList(InfoParam param){
        if(param.getUserType().equals("1")){//会员
            List<SysMenu> menus = menuService.getMenuByUserId(param.getUserId());
            //获取菜单和目录
            List<SysMenu> collect = Optional
                    .ofNullable(menus)
                    .orElse(new ArrayList<>())
                    .stream()
                    .filter(item -> item!=null && !item.getType().equals("2"))//排除按钮
                    .collect(Collectors.toList());
            List<RouterVO> router = MakeMenuTree.makeRouter(collect,0L);
            return ResultUtils.success("查询成功",router);
        }else{
            if(param.getUserType().equals("2")){//员工
                SysUser user = sysUserService.getById(param.getUserId());
                List<SysMenu> menus = null;
                if(StringUtils.isNotEmpty(user.getIsAdmin()) && user.getIsAdmin().equals("1")){//超级管理员
                    menus = menuService.list();
                }else{//普通员工
                    menuService.getMenuByUserId(user.getUserId());
                }
                //获取菜单和目录
                List<SysMenu> collect = Optional
                        .ofNullable(menus)
                        .orElse(new ArrayList<>())
                        .stream()
                        .filter(item -> item != null && !item.getType().equals("2"))//排除按钮
                        .collect(Collectors.toList());
                List<RouterVO> router = MakeMenuTree.makeRouter(collect, 0L);
                return ResultUtils.success("查询成功",router);
            }else{
                return ResultUtils.error("用户类型错误！");
            }
        }
    }

}
