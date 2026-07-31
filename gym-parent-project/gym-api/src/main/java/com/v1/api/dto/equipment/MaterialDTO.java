package com.v1.api.dto.equipment;

import lombok.Data;
import java.io.Serializable;

@Data
public class MaterialDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private String image;
    private Integer quantity;
    private String status;
}
