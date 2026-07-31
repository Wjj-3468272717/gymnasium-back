package com.v1.api.dto;

import lombok.Data;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

@Data
public class PageResultDTO<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long currentPage;
    private Long pageSize;
    private Long total;
    private List<T> records;

    public static <T> PageResultDTO<T> empty() {
        PageResultDTO<T> result = new PageResultDTO<>();
        result.setRecords(Collections.emptyList());
        result.setTotal(0L);
        return result;
    }
}
