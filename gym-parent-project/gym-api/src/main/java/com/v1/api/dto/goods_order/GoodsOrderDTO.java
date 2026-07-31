package com.v1.api.dto.goods_order;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
public class GoodsOrderDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long orderId;
    private Long userId;
    private String username;
    private BigDecimal totalPrice;
    private Date createTime;
    private String status;
    private List<OrderItemDTO> items;
}
