package com.v1.api.dto.home;

import lombok.Data;
import java.io.Serializable;

@Data
public class TotalCountDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long memberCount;
    private Long courseCount;
    private Long goodsCount;
    private Long orderCount;
    private Long revenue;
}
