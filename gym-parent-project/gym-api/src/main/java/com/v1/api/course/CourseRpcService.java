package com.v1.api.course;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.course.CourseDTO;

public interface CourseRpcService {
    PageResultDTO<CourseDTO> listCourses(PageDTO page, String courseName, String teacherName);

    CourseDTO getCourseById(Long courseId);

    void addCourse(CourseDTO course);

    void updateCourse(CourseDTO course);

    void deleteCourse(Long courseId);
}
