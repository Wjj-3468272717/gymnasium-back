package com.v1.api.dto.course;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class CourseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long courseId;
    private String courseName;
    private String image;
    private String teacherName;
    private Integer courseHour;
    private String courseDetails;
    private BigDecimal coursePrice;
    private Long teacherId;
}
