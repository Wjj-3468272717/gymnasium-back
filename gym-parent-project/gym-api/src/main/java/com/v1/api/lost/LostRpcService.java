package com.v1.api.lost;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.lost.LostDTO;

public interface LostRpcService {
    PageResultDTO<LostDTO> list(PageDTO page, String lostName);

    void add(LostDTO lost);

    void update(LostDTO lost);

    void delete(Long id);
}
