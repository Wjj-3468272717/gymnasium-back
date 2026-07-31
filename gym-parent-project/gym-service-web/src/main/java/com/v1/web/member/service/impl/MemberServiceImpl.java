package com.v1.web.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.utils.ResultUtils;
import com.v1.web.member.entity.JoinParam;
import com.v1.web.member.entity.Member;
import com.v1.web.member.entity.PageParam;
import com.v1.web.member.entity.RechargeParam;
import com.v1.web.member.mapper.MemberMapper;
import com.v1.web.member.service.MemberService;
import com.v1.web.member_apply.entity.MemberApply;
import com.v1.web.member_apply.mapper.MemberApplyMapper;
import com.v1.web.member_card.entity.MemberCard;
import com.v1.web.member_card.mapper.MemberCardMapper;
import com.v1.web.member_card.service.MemberCardService;
import com.v1.web.member_recharge.entity.MemberRecharge;
import com.v1.web.member_recharge.service.MemberRechargeService;
import com.v1.web.member_role.entity.MemberRole;
import com.v1.web.member_role.service.MemberRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberMapper, Member> implements MemberService {

    @Autowired
    MemberRoleService memberRoleService;

    @Override
    public IPage<Member> list(PageParam pageParam) {
        //构建分页对象
        IPage<Member> page = new Page(pageParam.getCurrentPage(), pageParam.getPageSize());
        //构建查询条件
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(pageParam.getName())) {
            queryWrapper.lambda().eq(Member::getName, pageParam.getName());
        }
        if (StringUtils.isNotEmpty(pageParam.getPhone())) {
            queryWrapper.lambda().eq(Member::getPhone, pageParam.getPhone());
        }
        if (StringUtils.isNotEmpty(pageParam.getUsername())) {
            queryWrapper.lambda().eq(Member::getUsername, pageParam.getUsername());
        }
        if (pageParam.getUserType().equals("1")) {
            queryWrapper.lambda().eq(Member::getMemberId, pageParam.getMemberId());
        }
        queryWrapper.lambda().orderByDesc(Member::getJoinTime);
        return this.baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional
    public void addMember(Member member) {
        int inserted = this.baseMapper.insert(member);
        if (inserted > 0) {
            MemberRole role = new MemberRole();
            role.setMemberId(member.getMemberId());
            role.setRoleId(member.getRoleId());
            memberRoleService.save(role);
        }
    }

    @Override
    @Transactional
    public void editMember(Member member) {
        int updated = this.baseMapper.updateById(member);
        if (updated > 0) {
            //删除原因的member_role关联信息
            QueryWrapper<MemberRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MemberRole::getMemberId, member.getMemberId());
            memberRoleService.remove(queryWrapper);
            //新建member_role
            MemberRole role = new MemberRole();
            role.setMemberId(member.getMemberId());
            role.setRoleId(member.getRoleId());
            memberRoleService.save(role);
        }
    }

    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        int deleted = this.baseMapper.deleteById(memberId);
        if (deleted > 0) {
            QueryWrapper<MemberRole> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MemberRole::getMemberId, memberId);
            this.memberRoleService.remove(queryWrapper);
        }
    }

    @Override
    public MemberRole getRoleByMemberId(Long memberId) {
        QueryWrapper<MemberRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberRole::getMemberId, memberId);
        MemberRole one = memberRoleService.getOne(queryWrapper);
        return one;
    }


    @Resource
    MemberApplyMapper memberApplyMapper;
    @Resource
    MemberCardMapper memberCardMapper;

    @Autowired
    MemberRechargeService memberRechargeService;

    @Override
    @Transactional
    public void joinApply(JoinParam param) throws ParseException {
        Member select = this.baseMapper.selectById(param.getMemberId());
        MemberCard card = memberCardMapper.selectById(param.getCardId());
        //更新用户信息
        Member member = new Member();
        member.setMemberId(param.getMemberId());
        member.setCardType(card.getCardType());
        member.setCardDay(card.getCardDay());
        member.setPrice(card.getPrice());
        String endTime = select.getEndTime();
        Calendar calendar = Calendar.getInstance();
        if (StringUtils.isNotEmpty(endTime)) {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(select.getEndTime());
            calendar.setTime(date);
        } else {
            Date date = new Date();
            calendar.setTime(date);
        }
        calendar.add(Calendar.DATE, card.getCardDay());
        member.setEndTime(new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime()));
        this.baseMapper.updateById(member);
        //插入充值明细
        MemberApply apply = new MemberApply();
        apply.setMemberId(param.getMemberId());
        apply.setCardDay(card.getCardDay());
        apply.setCardType(card.getCardType());
        apply.setCreateTime(new Date());
        apply.setPrice(card.getPrice());
        memberApplyMapper.insert(apply);
    }

    @Override
    @Transactional
    public void recharge(RechargeParam param) {
        //生成充值记录
        MemberRecharge recharge = new MemberRecharge();
        recharge.setMoney(param.getMoney());
        recharge.setMemberId(param.getMemberId());
        boolean saved = memberRechargeService.save(recharge);
        if (saved) {
            //更新账户余额
            this.baseMapper.addMoney(param);
        }
    }

    @Override
    public Member loadUser(String username) {
        QueryWrapper<Member> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Member::getUsername,username);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
