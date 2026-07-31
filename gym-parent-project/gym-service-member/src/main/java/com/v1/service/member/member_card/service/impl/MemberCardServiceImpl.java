package com.v1.service.member.member_card.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.service.member.member_card.entity.ListCard;
import com.v1.service.member.member_card.entity.MemberCard;
import com.v1.service.member.member_card.mapper.MemberCardMapper;
import com.v1.service.member.member_card.service.MemberCardService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class MemberCardServiceImpl extends ServiceImpl<MemberCardMapper, MemberCard> implements MemberCardService {

    @Override
    public IPage<MemberCard> list(ListCard listCard) {
        //构造分页对象
        IPage<MemberCard> ipage = new Page<>();
        ipage.setCurrent(listCard.getCurrentPage());
        ipage.setSize(listCard.getPageSize());
        //构造查询条件
        QueryWrapper<MemberCard> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(listCard.getTitle())){
            queryWrapper.lambda().like(MemberCard::getTitle,listCard.getTitle());
        }
        return this.page(ipage, queryWrapper);
    }

}
