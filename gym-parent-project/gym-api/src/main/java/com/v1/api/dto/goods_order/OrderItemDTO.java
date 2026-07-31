package com.v1.api.dto.goods_order;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class OrderItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long orderId;
    private Long goodsId;
    private String goodsName;
    private Integer quantity;
    private BigDecimal price;
}
