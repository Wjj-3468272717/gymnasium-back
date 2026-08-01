package com.v1.web.home.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.home.EChartItemDTO;
import com.v1.api.dto.member.MemberDTO;
import com.v1.api.dto.suggest.SuggestDTO;
import com.v1.api.dto.sys_user.SysUserDTO;
import com.v1.api.equipment.MaterialRpcService;
import com.v1.api.goods_order.GoodsOrderRpcService;
import com.v1.api.member.MemberRpcService;
import com.v1.api.suggest.SuggestRpcService;
import com.v1.api.sys_user.SysUserRpcService;
import com.v1.service.home.home.entity.EChart;
import com.v1.service.home.home.entity.ResetPassword;
import com.v1.service.home.home.entity.TotalCount;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @DubboReference
    MemberRpcService memberRpcService;
    @DubboReference
    SysUserRpcService userRpcService;
    @DubboReference
    MaterialRpcService materialRpcService;
    @DubboReference
    GoodsOrderRpcService goodsOrderRpcService;
    @DubboReference
    SuggestRpcService suggestRpcService;

    //统计总数
    @GetMapping("/getTotal")
    public ResultVo getTotal(){
        PageDTO page = new PageDTO();
        page.setCurrentPage(1L);
        page.setPageSize(1L);
        PageResultDTO<MemberDTO> result = memberRpcService.listMembers(page, null, null, null, null, null);
        int memberCount = (int) result.getTotal().longValue();
        int userCount = userRpcService.count();
        int materialCount = materialRpcService.count();
        int orderCount = goodsOrderRpcService.count();
        TotalCount totalCount = new TotalCount(memberCount, userCount, materialCount, orderCount);
        return ResultUtils.success("查询成功", totalCount);
    }

    @GetMapping("/getSuggestList")
    public ResultVo getSuggestList(){
        PageDTO page = new PageDTO();
        page.setCurrentPage(1L);
        page.setPageSize(3L);
        PageResultDTO<SuggestDTO> result = suggestRpcService.list(page, null);
        return ResultUtils.success("查询成功", result.getRecords());
    }

    @GetMapping("/getHotGoods")
    public ResultVo getHotGoods(){
        List<EChartItemDTO> eChartItems = goodsOrderRpcService.getHotGoodsData();
        EChart eChart = new EChart();
        if(eChartItems.size() > 0){
            for(int i = 0; i < eChartItems.size(); i++){
                eChart.getNames().add(eChartItems.get(i).getName());
                if (eChartItems.get(i).getValue() != null) {
                    eChart.getValues().add(eChartItems.get(i).getValue());
                }
            }
        }
        return ResultUtils.success("查询成功", eChart);
    }

    @GetMapping("/getHotCards")
    public ResultVo getHotCards(){
        List<EChartItemDTO> eChartItems = goodsOrderRpcService.getHotCardsData();
        return ResultUtils.success("查询成功", eChartItems);
    }

    @GetMapping("/getHotCourse")
    public ResultVo getHotCourse(){
        List<EChartItemDTO> eChartItems = goodsOrderRpcService.getHotCourseData();
        return ResultUtils.success("查询成功", eChartItems);
    }

    @Autowired
    PasswordEncoder passwordEncoder;

    //重置密码
    @PostMapping("/resetPassword")
    public ResultVo resetPassword(@RequestBody ResetPassword resetPassword){
        if("1".equals(resetPassword.getUserType())){//会员
            String password = passwordEncoder.encode("123456");
            memberRpcService.resetPassword(resetPassword.getUserId(), password);
            return ResultUtils.success("密码修改成功");
        }else{
            if("2".equals(resetPassword.getUserType())){//员工
                String password = passwordEncoder.encode("123456");
                userRpcService.resetPassword(resetPassword.getUserId(), password);
                return ResultUtils.success("密码修改成功");
            }else{
                return ResultUtils.error("用户类型错误！");
            }
        }
    }

    //修改密码
    @PostMapping("/updatePassword")
    public ResultVo updatePassword(@RequestBody ResetPassword resetPassword){
        if("1".equals(resetPassword.getUserType())){//会员
            MemberDTO member = memberRpcService.getMemberById(resetPassword.getUserId());
            if(member == null || !passwordEncoder.matches(resetPassword.getOldPassword(), member.getPassword())){
                return ResultUtils.error("原密码错误！");
            }
            String password = passwordEncoder.encode(resetPassword.getPassword());
            memberRpcService.resetPassword(resetPassword.getUserId(), password);
            return ResultUtils.success("密码修改成功");
        }else{
            if("2".equals(resetPassword.getUserType())){//员工
                SysUserDTO user = userRpcService.getUserById(resetPassword.getUserId());
                if(user == null || !passwordEncoder.matches(resetPassword.getOldPassword(), user.getPassword())){
                    return ResultUtils.error("原密码错误！");
                }
                String password = passwordEncoder.encode(resetPassword.getPassword());
                userRpcService.resetPassword(resetPassword.getUserId(), password);
                return ResultUtils.success("密码修改成功");
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
            new SecurityContextLogoutHandler().logout(req, res, authentication);
        }
        return ResultUtils.success("退出登录成功");
    }

}
