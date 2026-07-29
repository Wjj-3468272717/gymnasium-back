package com.v1.web.course.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.course.entity.Course;
import com.v1.web.course.entity.CourseList;
import com.v1.web.course.entity.PageParam;
import com.v1.web.course.service.CourseService;
import com.v1.web.member.entity.Member;
import com.v1.web.member.service.MemberService;
import com.v1.web.member_course.entity.MemberCourse;
import com.v1.web.member_course.service.MemberCourseService;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @Autowired
    CourseService courseService;
    @Autowired
    private MemberCourseService memberCourseService;
    @Autowired
    private MemberService memberService;

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

    //报名课程
    @PostMapping("/joinCourse")
    public ResultVo joinCourse(@RequestBody MemberCourse memberCourse){
        //查询用户是否已经报名
        QueryWrapper<MemberCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberCourse::getCourseId,memberCourse.getCourseId()).eq(MemberCourse::getMemberId,memberCourse.getMemberId());
        MemberCourse one = memberCourseService.getOne(queryWrapper);
        if(one != null){
            return ResultUtils.error("您已经报名过该课程");
        }
        //判断余额
        Course course = courseService.getById(memberCourse.getCourseId());
        Member member = memberService.getById(memberCourse.getMemberId());
        int compared = member.getMoney().compareTo(course.getCoursePrice());
        if(compared == -1){
            return ResultUtils.error("您的余额不足，请先充值");
        }else{
            memberCourseService.joinCourse(memberCourse);
            return ResultUtils.success("报名成功");
        }
    }

    //我的课程列表
    @GetMapping("/getMyCourseList")
    public ResultVo getMyCourseList(PageParam param){
        if(param.getUserType().equals("1")){//会员
            IPage<MemberCourse> page = new Page<>(param.getCurrentPage(), param.getPageSize());
            QueryWrapper<MemberCourse> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(MemberCourse::getMemberId,param.getUserId());
            IPage<MemberCourse> list = memberCourseService.page(page,queryWrapper);
            return ResultUtils.success("查询成功",list);
        }else{//老师
            IPage<Course> page = new Page<>(param.getCurrentPage(), param.getPageSize());
            QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(Course::getTeacherId,param.getUserId());
            IPage<Course> list = courseService.page(page,queryWrapper);
            return ResultUtils.success("查询成功",list);
        }
    }


}
