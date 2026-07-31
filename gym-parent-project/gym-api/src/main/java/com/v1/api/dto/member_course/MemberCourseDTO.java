package com.v1.api.dto.member_course;

import lombok.Data;
import java.io.Serializable;
import java.util.Date;

@Data
public class MemberCourseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    private Long memberId;
    private Long courseId;
    private Date createTime;
}
