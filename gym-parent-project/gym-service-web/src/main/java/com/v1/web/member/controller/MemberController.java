package com.v1.web.member.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member.MemberDTO;
import com.v1.api.dto.member_card.MemberCardDTO;
import com.v1.api.dto.member_recharge.MemberRechargeDTO;
import com.v1.api.dto.member_role.MemberRoleDTO;
import com.v1.api.member.MemberRpcService;
import com.v1.api.member_card.MemberCardRpcService;
import com.v1.api.member_recharge.MemberRechargeRpcService;
import com.v1.service.member.member.entity.JoinParam;
import com.v1.service.member.member.entity.RechargeParam;
import com.v1.service.member.member_recharge.entity.RechargeParamList;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/member")
public class MemberController {

    @DubboReference
    MemberRpcService memberRpcService;
    @DubboReference
    MemberCardRpcService memberCardRpcService;
    @DubboReference
    MemberRechargeRpcService memberRechargeRpcService;
    @Autowired
    PasswordEncoder passwordEncoder;

    /**
     * 新增会员
     * @param member
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody MemberDTO member){
        //判断会员卡号是否重复
        MemberDTO one = memberRpcService.loadUser(member.getUsername());
        if(one != null){
            return ResultUtils.error("会员卡号被占用");
        }
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRpcService.addMember(member);
        return ResultUtils.success("会员信息添加成功");
    }

    /**
     * 修改会员信息
     * @param member
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody MemberDTO member){
        //判断会员卡号是否重复
        MemberDTO one = memberRpcService.loadUser(member.getUsername());
        if(one != null && !one.getMemberId().equals(member.getMemberId())){
            return ResultUtils.error("会员卡号已被占用");
        }
        memberRpcService.editMember(member);
        return ResultUtils.success("编辑成功");
    }

    /**
     * 删除会员
     * @param memberId
     * @return
     */
    @DeleteMapping("/{memberId}")
    public ResultVo delete(@PathVariable("memberId") Long memberId){
        memberRpcService.deleteMember(memberId);
        return ResultUtils.success("删除成功");
    }

    /**
     * 查询会员信息
     * @param param
     * @return
     */
    @GetMapping("/list")
    public ResultVo list(com.v1.service.member.member.entity.PageParam param){
        PageDTO page = new PageDTO();
        page.setCurrentPage(param.getCurrentPage());
        page.setPageSize(param.getPageSize());
        Long memberId = null;
        if (param.getMemberId() != null && !param.getMemberId().isEmpty()) {
            memberId = Long.parseLong(param.getMemberId());
        }
        PageResultDTO<MemberDTO> list = memberRpcService.listMembers(page, param.getName(), param.getPhone(), param.getUsername(), memberId, param.getUserType());
        return ResultUtils.success("查询成功", list);
    }

    @GetMapping("/getRoleByMemberId")
    public ResultVo getRoleByMemberId(Long memberId){
        MemberRoleDTO memberRole = memberRpcService.getRoleByMemberId(memberId);
        return ResultUtils.success("查询成功", memberRole);
    }

    /**
     * 查询会员卡列表
     * @return
     */
    @GetMapping("/getCardList")
    public ResultVo getCardList(){
        PageDTO page = new PageDTO();
        page.setCurrentPage(1L);
        page.setPageSize(9999L);
        PageResultDTO<MemberCardDTO> result = memberCardRpcService.listCards(page, null);
        List<MemberCardDTO> list = result.getRecords().stream()
                .filter(card -> "1".equals(card.getStatus()))
                .collect(Collectors.toList());
        return ResultUtils.success("查询成功", list);
    }


    /**
     * 办卡提交
     * @param param
     * @return
     */
    @PostMapping("/joinApply")
    public ResultVo joinApply(@RequestBody JoinParam param){
        memberRpcService.joinCard(param.getMemberId(), param.getCardId());
        return ResultUtils.success("办卡成功");
    }

    /**
     * 会员充值
     * @param param
     * @return
     */
    @PostMapping("/recharge")
    public ResultVo recharge(@RequestBody RechargeParam param){
        memberRpcService.recharge(param.getMemberId(), param.getMoney());
        return ResultUtils.success("充值成功");
    }

    @GetMapping("getMyRecharge")
    public ResultVo getMyRecharge(RechargeParamList paramList){
        //判断当前用户是会员还是员工
        if(paramList.getUserType().equals("1")){//会员
            PageDTO page = new PageDTO();
            page.setCurrentPage(paramList.getCurrentPage());
            page.setPageSize(paramList.getPageSize());
            PageResultDTO<MemberRechargeDTO> rechargeIPage = memberRechargeRpcService.getRechargeByMember(page, paramList.getMemberId());
            return ResultUtils.success("查询成功", rechargeIPage);
        }else{
            if(paramList.getUserType().equals("2")){//员工
                PageDTO page = new PageDTO();
                page.setCurrentPage(paramList.getCurrentPage());
                page.setPageSize(paramList.getPageSize());
                PageResultDTO<MemberRechargeDTO> rechargeList = memberRechargeRpcService.getRechargeList(page);
                return ResultUtils.success("查询成功", rechargeList);
            }else{
                return ResultUtils.error("用户类型不存在");
            }
        }
    }

}
