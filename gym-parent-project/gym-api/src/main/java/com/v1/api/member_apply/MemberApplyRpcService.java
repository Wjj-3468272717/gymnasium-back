package com.v1.api.member_apply;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_apply.MemberApplyDTO;

public interface MemberApplyRpcService {
    PageResultDTO<MemberApplyDTO> list(PageDTO page, Long memberId);
}
