package com.v1.api.dto.home;

import lombok.Data;
import java.io.Serializable;
import java.util.List;

@Data
public class EChartDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<String> xData;
    private List<EChartItemDTO> series;
}
