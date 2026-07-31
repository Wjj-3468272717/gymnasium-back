package com.v1.service.course.course.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.service.course.course.entity.Course;
import com.v1.service.course.course.entity.CourseList;

public interface CourseService extends IService<Course> {
    /**
     * 获取课程分页数据
     * @param courseList
     * @return
     */
    IPage<Course> list(CourseList courseList);
}
