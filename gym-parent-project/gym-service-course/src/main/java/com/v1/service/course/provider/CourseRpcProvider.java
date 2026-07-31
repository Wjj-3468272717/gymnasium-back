package com.v1.service.course.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.v1.api.course.CourseRpcService;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.course.CourseDTO;
import com.v1.service.course.course.entity.Course;
import com.v1.service.course.course.entity.CourseList;
import com.v1.service.course.course.service.CourseService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@DubboService
public class CourseRpcProvider implements CourseRpcService {

    @Autowired
    private CourseService courseService;

    @Override
    public PageResultDTO<CourseDTO> listCourses(PageDTO page, String courseName, String teacherName) {
        CourseList courseList = new CourseList();
        courseList.setCurrentPage(page.getCurrentPage());
        courseList.setPageSize(page.getPageSize());
        courseList.setCourseName(courseName);
        courseList.setTeacherName(teacherName);

        IPage<Course> result = courseService.list(courseList);

        PageResultDTO<CourseDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(result.getCurrent());
        dto.setPageSize(result.getSize());
        dto.setTotal(result.getTotal());
        dto.setRecords(result.getRecords().stream().map(entity -> {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(entity, courseDTO);
            return courseDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public CourseDTO getCourseById(Long courseId) {
        Course entity = courseService.getById(courseId);
        if (entity == null) {
            return null;
        }
        CourseDTO dto = new CourseDTO();
        BeanUtils.copyProperties(entity, dto);
        return dto;
    }

    @Override
    public void addCourse(CourseDTO course) {
        Course entity = new Course();
        BeanUtils.copyProperties(course, entity);
        courseService.save(entity);
    }

    @Override
    public void updateCourse(CourseDTO course) {
        Course entity = new Course();
        BeanUtils.copyProperties(course, entity);
        courseService.updateById(entity);
    }

    @Override
    public void deleteCourse(Long courseId) {
        courseService.removeById(courseId);
    }

    @Override
    public PageResultDTO<CourseDTO> getCoursesByTeacherId(PageDTO page, Long teacherId) {
        IPage<Course> iPage = new Page<>(page.getCurrentPage(), page.getPageSize());
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Course::getTeacherId, teacherId);
        IPage<Course> list = courseService.page(iPage, queryWrapper);

        PageResultDTO<CourseDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(list.getCurrent());
        dto.setPageSize(list.getSize());
        dto.setTotal(list.getTotal());
        dto.setRecords(list.getRecords().stream().map(entity -> {
            CourseDTO courseDTO = new CourseDTO();
            BeanUtils.copyProperties(entity, courseDTO);
            return courseDTO;
        }).collect(Collectors.toList()));
        return dto;
    }
}
