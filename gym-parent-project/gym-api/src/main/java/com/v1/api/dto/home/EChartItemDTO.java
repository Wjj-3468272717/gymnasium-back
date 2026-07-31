package com.v1.api.dto.home;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class EChartItemDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String value;
    private String type;
    private List<Long> data;
}
