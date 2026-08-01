package com.v1.api.suggest;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.suggest.SuggestDTO;

public interface SuggestRpcService {
    PageResultDTO<SuggestDTO> list(PageDTO page, String title);

    void add(SuggestDTO suggest);

    void update(SuggestDTO suggest);

    void delete(Long id);
}
