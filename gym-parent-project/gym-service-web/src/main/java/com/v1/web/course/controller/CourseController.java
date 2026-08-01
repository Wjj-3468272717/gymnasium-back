package com.v1.web.course.controller;

import com.v1.api.course.CourseRpcService;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.course.CourseDTO;
import com.v1.api.dto.member.MemberDTO;
import com.v1.api.dto.member_course.MemberCourseDTO;
import com.v1.api.member.MemberRpcService;
import com.v1.api.member_course.MemberCourseRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.course.entity.PageParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    @DubboReference
    private CourseRpcService courseRpcService;
    @DubboReference
    private MemberCourseRpcService memberCourseRpcService;
    @DubboReference
    private MemberRpcService memberRpcService;

    /**
     * 新增课程
     * @param course
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody CourseDTO course){
        courseRpcService.addCourse(course);
        return ResultUtils.success("新增成功");
    }

    /**
     * 修改课程
     * @param course
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody CourseDTO course){
        courseRpcService.updateCourse(course);
        return ResultUtils.success("编辑成功");
    }

    /**
     * 删除课程
     * @param courseId
     * @return
     */
    @DeleteMapping("/{courseId}")
    public ResultVo delete(@PathVariable("courseId") Long courseId){
        courseRpcService.deleteCourse(courseId);
        return ResultUtils.success("删除成功");
    }

    /**
     * 分页查询课程
     */
    @GetMapping("/list")
    public ResultVo list(Long currentPage, Long pageSize, String courseName, String teacherName){
        PageDTO page = new PageDTO();
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        PageResultDTO<CourseDTO> result = courseRpcService.listCourses(page, courseName, teacherName);
        return ResultUtils.success("查询成功", result);
    }

    //报名课程
    @PostMapping("/joinCourse")
    public ResultVo joinCourse(@RequestBody MemberCourseDTO memberCourse){
        //查询用户是否已经报名
        if(memberCourseRpcService.hasJoinedCourse(memberCourse.getMemberId(), memberCourse.getCourseId())){
            return ResultUtils.error("您已经报名过该课程");
        }
        //判断余额
        CourseDTO course = courseRpcService.getCourseById(memberCourse.getCourseId());
        if (course == null) {
            return ResultUtils.error("课程不存在");
        }
        MemberDTO member = memberRpcService.getMemberById(memberCourse.getMemberId());
        if (member == null) {
            return ResultUtils.error("会员不存在");
        }
        if (member.getMoney() == null || course.getCoursePrice() == null) {
            return ResultUtils.error("余额或课程价格异常");
        }
        int compared = member.getMoney().compareTo(course.getCoursePrice());
        if(compared == -1){
            return ResultUtils.error("您的余额不足，请先充值");
        }else{
            memberCourseRpcService.joinCourse(memberCourse);
            return ResultUtils.success("报名成功");
        }
    }

    //我的课程列表
    @GetMapping("/getMyCourseList")
    public ResultVo getMyCourseList(PageParam param){
        PageDTO page = new PageDTO();
        page.setCurrentPage(param.getCurrentPage());
        page.setPageSize(param.getPageSize());
        if("1".equals(param.getUserType())){//会员
            PageResultDTO<MemberCourseDTO> list = memberCourseRpcService.getMyCourseList(page, param.getUserId());
            return ResultUtils.success("查询成功", list);
        }else{//老师
            PageResultDTO<CourseDTO> list = courseRpcService.getCoursesByTeacherId(page, param.getUserId());
            return ResultUtils.success("查询成功", list);
        }
    }

}
