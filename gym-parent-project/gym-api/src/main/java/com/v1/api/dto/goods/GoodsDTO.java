package com.v1.api.dto.goods;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class GoodsDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long goodsId;
    private String name;
    private String image;
    private BigDecimal price;
    private Integer stock;
    private String description;
    private String status;
}
