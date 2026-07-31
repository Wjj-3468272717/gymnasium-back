package com.v1.service.member.provider;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_card.MemberCardDTO;
import com.v1.api.member_card.MemberCardRpcService;
import com.v1.service.member.member_card.entity.ListCard;
import com.v1.service.member.member_card.entity.MemberCard;
import com.v1.service.member.member_card.service.MemberCardService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@DubboService
public class MemberCardRpcProvider implements MemberCardRpcService {

    @Autowired
    private MemberCardService memberCardService;

    @Override
    public PageResultDTO<MemberCardDTO> listCards(PageDTO page, String title) {
        ListCard listCard = new ListCard();
        listCard.setCurrentPage(page.getCurrentPage());
        listCard.setPageSize(page.getPageSize());
        listCard.setTitle(title);

        IPage<MemberCard> result = memberCardService.list(listCard);

        PageResultDTO<MemberCardDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            MemberCardDTO cardDTO = new MemberCardDTO();
            BeanUtils.copyProperties(entity, cardDTO);
            return cardDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public MemberCardDTO getCardById(Long cardId) {
        MemberCard entity = memberCardService.getById(cardId);
        if (entity == null) {
            return null;
        }
        MemberCardDTO dto = new MemberCardDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void saveCard(MemberCardDTO card) {
        MemberCard entity = new MemberCard();
        BeanUtils.copyProperties(card, entity);
        memberCardService.save(entity);
    }

    @Override
    public void updateCard(MemberCardDTO card) {
        MemberCard entity = new MemberCard();
        BeanUtils.copyProperties(card, entity);
        memberCardService.updateById(entity);
    }

    @Override
    public void deleteCard(Long cardId) {
        memberCardService.removeById(cardId);
    }
}
