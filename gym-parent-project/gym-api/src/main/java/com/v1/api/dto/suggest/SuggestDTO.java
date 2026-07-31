package com.v1.api.dto.suggest;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class SuggestDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String content;
    private Long memberId;
    private Date dateTime;
    private String status;
}
