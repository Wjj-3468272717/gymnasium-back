package com.v1.web.member.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.member.entity.JoinParam;
import com.v1.web.member.entity.Member;
import com.v1.web.member.entity.PageParam;
import com.v1.web.member.entity.RechargeParam;
import com.v1.web.member.service.MemberService;
import com.v1.web.member_card.entity.MemberCard;
import com.v1.web.member_card.service.MemberCardService;
import com.v1.web.member_recharge.entity.MemberRecharge;
import com.v1.web.member_recharge.entity.RechargeParamList;
import com.v1.web.member_recharge.service.MemberRechargeService;
import com.v1.web.member_role.entity.MemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @Autowired
    MemberService memberService;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 新增会员
     * @param member
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Member member){
        //判断会员卡号是否重复
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Member::getUsername,member.getUsername());
        Member one = memberService.getOne(queryWrapper);
        if(one != null){
            return ResultUtils.error("会员卡号被占用");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberService.addMember(member);
        return ResultUtils.success("会员信息添加成功");
    }

    /**
     * 修改会员信息
     * @param member
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody Member member){
        //判断会员卡号是否重复
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Member::getUsername,member.getUsername());
        Member one = memberService.getOne(queryWrapper);
        if(one != null && !one.getMemberId().equals(member.getMemberId())){
            return ResultUtils.error("会员卡号已被占用");
        }
        memberService.editMember(member);
        return ResultUtils.success("编辑成功");
    }

    /**
     * 删除会员
     * @param memberId
     * @return
     */
    @DeleteMapping("/{memberId}")
    public ResultVo delete(@PathVariable("memberId") Long memberId){
        memberService.deleteMember(memberId);
        return ResultUtils.success("删除成功");
    }

    /**
     * 查询会员信息
     * @param pageParam
     * @return
     */
    @GetMapping("/list")
    public ResultVo list(PageParam pageParam){
        IPage<Member> list =  memberService.list(pageParam);
        return ResultUtils.success("查询成功",list);
    }

    @GetMapping("/getRoleMemberId")
    public ResultVo getRoleByMemberId(Long memberId){
        MemberRole memberRole = memberService.getRoleByMemberId(memberId);
        return ResultUtils.success("查询成功",memberRole);
    }

    @Autowired
    MemberCardService memberCardService;


    /**
     * 查询会员卡列表
     * @return
     */
    @GetMapping("/getCardList")
    public ResultVo getCardList(){
        QueryWrapper<MemberCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberCard::getStatus,"1");
        List<MemberCard> list = memberCardService.list(queryWrapper);
        return ResultUtils.success("查询成功",list);
    }


    /**
     * 办卡提交
     * @param param
     * @return
     * @throws ParseException
     */
    @PostMapping("/joinApply")
    public ResultVo joinApply(@RequestBody JoinParam param) throws ParseException {
        memberService.joinApply(param);
        return ResultUtils.success("办卡成功");
    }

    /**
     * 会员充值
     * @param param
     * @return
     */
    @PostMapping("/recharge")
    public ResultVo recharge(@RequestBody RechargeParam param){
        memberService.recharge(param);
        return ResultUtils.success("充值成功");
    }

    @Autowired
    MemberRechargeService rechargeService;

    @GetMapping("getMyRecharge")
    public ResultVo getMyRecharge(RechargeParamList paramList){
        //判断当前用户是会员还是员工
        if(paramList.getUserType().equals("1")){//会员
            IPage<MemberRecharge> rechargeIPage = rechargeService.getRechargeByMember(paramList);
            return ResultUtils.success("查询成功",rechargeIPage);
        }else{
            if(paramList.getUserType().equals("2")){//员工
                IPage<MemberRecharge> rechargeList = rechargeService.getRechargeList(paramList);
                return ResultUtils.success("查询成功",rechargeList);
            }else{
                return ResultUtils.error("用户类型不存在");
            }
        }
    }

}
