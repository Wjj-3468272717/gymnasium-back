package com.v1.web.course.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.course.entity.Course;
import com.v1.web.course.entity.CourseList;
import com.v1.web.course.service.CourseService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    CourseService courseService;

    /**
     * 新增课程
     * @param course
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Course course){
        boolean updated = courseService.save(course);
        if(updated){
            return ResultUtils.success("新增成功");
        }else{
            return ResultUtils.error("新增失败");
        }
    }

    /**
     * 修改课程
     * @param course
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody Course course){
        boolean updated = courseService.updateById(course);
        if(updated){
            return ResultUtils.success("编辑成功");
        }else{
            return ResultUtils.error("编辑失败");
        }
    }

    /**
     * 删除课程
     * @param courseId
     * @return
     */
    @DeleteMapping("/{courseId}")
    public ResultVo delete(@PathVariable("courseId") Long courseId){
        boolean updated = courseService.removeById(courseId);
        if(updated){
            return ResultUtils.success("删除成功");
        }else{
            return ResultUtils.error("删除失败");
        }
    }

    /**
     * 分页查询课程
     */
    @GetMapping("/list")
    public ResultVo list(CourseList courseList){
        IPage<Course> page =  courseService.list(courseList);
        return ResultUtils.success("查询成功",page);
    }
}
