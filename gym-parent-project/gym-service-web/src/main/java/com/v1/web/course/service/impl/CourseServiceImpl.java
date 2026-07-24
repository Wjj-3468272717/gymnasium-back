package com.v1.web.course.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.web.course.entity.Course;
import com.v1.web.course.entity.CourseList;
import com.v1.web.course.mapper.CourseMapper;
import com.v1.web.course.service.CourseService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
    @Override
    public IPage<Course> list(CourseList courseList) {
        //创建分页对象
        IPage<Course> page = new Page<>(courseList.getCurrentPage(),courseList.getPageSize());
        //构造查询器
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        if(StringUtils.isNotEmpty(courseList.getCourseName())){
            queryWrapper.lambda().like(Course::getCourseName,courseList.getCourseName());
        }
        if(StringUtils.isNotEmpty(courseList.getTeacherName())){
            queryWrapper.lambda().like(Course::getTeacherName,courseList.getTeacherName());
        }
        return this.baseMapper.selectPage(page,queryWrapper);
    }
}
