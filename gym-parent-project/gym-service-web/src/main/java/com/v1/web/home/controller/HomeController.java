package com.v1.web.home.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.equipment.service.MaterialService;
import com.v1.web.goods_order.service.GoodsOrderService;
import com.v1.web.home.entity.EChart;
import com.v1.web.home.entity.EChartItem;
import com.v1.web.home.entity.ResetPassword;
import com.v1.web.home.entity.TotalCount;
import com.v1.web.member.entity.Member;
import com.v1.web.member.service.MemberService;
import com.v1.web.suggest.entity.Suggest;
import com.v1.web.suggest.service.SuggestService;
import com.v1.web.sys_user.entity.SysUser;
import com.v1.web.sys_user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    MemberService memberService;
    @Autowired
    SysUserService userService;
    @Autowired
    MaterialService materialService;
    @Autowired
    GoodsOrderService goodsOrderService;
    @Autowired
    SuggestService suggestService;

    //统计总数
    @GetMapping("/getTotal")
    public ResultVo getTotal(){
        int memberCount = memberService.count();
        int userCount = userService.count();
        int materialCount = materialService.count();
        int orderCount = goodsOrderService.count();
        TotalCount totalCount = new TotalCount(memberCount,userCount,materialCount,orderCount);
        return ResultUtils.success("查询成功",totalCount);
    }

    @GetMapping("/getSuggestList")
    public ResultVo getSuggestList(){
        QueryWrapper<Suggest> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Suggest::getDateTime).last("limit 3");
        List<Suggest> suggestList = suggestService.list(queryWrapper);
        return ResultUtils.success("查询成功",suggestList);
    }

    @GetMapping("/getHotGoods")
    public ResultVo getHotGoods(){
        List<EChartItem> eChartItems = goodsOrderService.hotGoods();
        EChart eChart = new EChart();
        if(eChartItems.size() > 0){
            for(int i = 0; i < eChartItems.size(); i++){
                eChart.getNames().add(eChartItems.get(i).getName());
                eChart.getValues().add(eChartItems.get(i).getValue());
            }
        }
        return ResultUtils.success("查询成功",eChart);
    }

    @GetMapping("/getHotCards")
    public ResultVo getHotCards(){
        List<EChartItem> eChartItems = goodsOrderService.hotCard();
        return ResultUtils.success("查询成功",eChartItems);
    }

    @GetMapping("/getHotCourse")
    public ResultVo getHotCourse(){
        List<EChartItem> eChartItems = goodsOrderService.hotCourse();
        return ResultUtils.success("查询成功",eChartItems);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    //重置密码
    @PostMapping
    public ResultVo resetPassword(@RequestBody ResetPassword resetPassword){
        if(resetPassword.getUserType().equals("1")){//会员
            Member member = memberService.getById(resetPassword.getUserId());
            String oldPassword = DigestUtils.md5DigestAsHex(resetPassword.getOldPassword().getBytes());
            if(!member.getPassword().equals(oldPassword)){
                return ResultUtils.error("原密码错误！");
            }
//            String password = DigestUtils.md5DigestAsHex("123456".getBytes());
            String password = passwordEncoder.encode("123456");
            member.setPassword(password);
            boolean updated = memberService.updateById(member);
            if(updated){
                return ResultUtils.success("密码修改成功");
            }else{
                return ResultUtils.error("密码修改失败！");
            }
        }else{
            if(resetPassword.getUserType().equals("2")){//员工
                SysUser user = userService.getById(resetPassword.getUserId());
                String oldPassword = DigestUtils.md5DigestAsHex(resetPassword.getOldPassword().getBytes());
                if(!user.getPassword().equals(oldPassword)){
                    return ResultUtils.error("原密码错误！");
                }
//                String password = DigestUtils.md5DigestAsHex("123456".getBytes());
                String password = passwordEncoder.encode("123456");
                user.setPassword(password);
                boolean updated = userService.updateById(user);
                if(updated){
                    return ResultUtils.success("密码修改成功");
                }else{
                    return ResultUtils.error("密码修改失败！");
                }
            }else{
                return ResultUtils.error("用户类型错误！");
            }
        }
    }

    //修改密码
    @PostMapping("/updatePassword")
    public ResultVo updatePassword(@RequestBody ResetPassword resetPassword){
        if(resetPassword.getUserType().equals("1")){//会员
            Member member = memberService.getById(resetPassword.getUserId());
//            String oldPassword = DigestUtils.md5DigestAsHex(resetPassword.getOldPassword().getBytes());
            if(passwordEncoder.matches(resetPassword.getOldPassword(),member.getPassword())){
                return ResultUtils.error("原密码错误！");
            }
//            String password = DigestUtils.md5DigestAsHex(resetPassword.getPassword().getBytes());
            String password = passwordEncoder.encode(resetPassword.getPassword());
            member.setPassword(password);
            boolean updated = memberService.updateById(member);
            if(updated){
                return ResultUtils.success("密码修改成功");
            }else{
                return ResultUtils.error("密码修改失败！");
            }
        }else{
            if(resetPassword.getUserType().equals("2")){//员工
                SysUser user = userService.getById(resetPassword.getUserId());
//                String oldPassword = DigestUtils.md5DigestAsHex(resetPassword.getOldPassword().getBytes());
                if(passwordEncoder.matches(resetPassword.getOldPassword(),user.getPassword())){
                    return ResultUtils.error("原密码错误！");
                }
//                String password = DigestUtils.md5DigestAsHex(resetPassword.getPassword().getBytes());
                String password = passwordEncoder.encode(resetPassword.getPassword());
                user.setPassword(password);
                boolean updated = userService.updateById(user);
                if(updated){
                    return ResultUtils.success("密码修改成功");
                }else{
                    return ResultUtils.error("密码修改失败！");
                }
            }else{
                return ResultUtils.error("用户类型错误！");
            }
        }
    }

    //退出登录
    @PostMapping("/loginOut")
    public ResultVo loginOut(HttpServletRequest req, HttpServletResponse res){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            new SecurityContextLogoutHandler().logout(req,res,authentication);
        }
        return ResultUtils.success("退出登录成功");
    }

}
