package com.v1.api.dto.lost;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class LostDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String lostName;
    private String image;
    private String description;
    private Date lostTime;
    private String status;
}
