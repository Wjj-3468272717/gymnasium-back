package com.v1.api.member_recharge;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_recharge.MemberRechargeDTO;

public interface MemberRechargeRpcService {
    PageResultDTO<MemberRechargeDTO> getRechargeList(PageDTO page);

    PageResultDTO<MemberRechargeDTO> getRechargeByMember(PageDTO page, Long memberId);
}
