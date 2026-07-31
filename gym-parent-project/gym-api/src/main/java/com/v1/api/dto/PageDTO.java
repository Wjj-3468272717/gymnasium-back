package com.v1.api.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class PageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long currentPage;
    private Long pageSize;
}
