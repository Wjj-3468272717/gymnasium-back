package com.v1.api.member_card;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_card.MemberCardDTO;

public interface MemberCardRpcService {
    PageResultDTO<MemberCardDTO> listCards(PageDTO page, String title);

    MemberCardDTO getCardById(Long cardId);

    void saveCard(MemberCardDTO card);

    void updateCard(MemberCardDTO card);

    void deleteCard(Long cardId);
}
