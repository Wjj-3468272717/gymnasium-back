package com.v1.service.member.member_card.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.service.member.member_card.entity.ListCard;
import com.v1.service.member.member_card.entity.MemberCard;

public interface MemberCardService extends IService<MemberCard> {
    IPage<MemberCard> list(ListCard listCard);
}
