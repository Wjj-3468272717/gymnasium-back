package com.v1.service.course.member_course.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.v1.api.dto.member.MemberDTO;
import com.v1.api.member.MemberRpcService;
import com.v1.service.course.course.entity.Course;
import com.v1.service.course.course.service.CourseService;
import com.v1.service.course.member_course.entity.MemberCourse;
import com.v1.service.course.member_course.mapper.MemberCourseMapper;
import com.v1.service.course.member_course.service.MemberCourseService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberCourseServiceImpl extends ServiceImpl<MemberCourseMapper, MemberCourse> implements MemberCourseService {

    @Autowired
    CourseService courseService;
    @DubboReference
    MemberRpcService memberRpcService;

    //用户选课报名
    @Override
    @Transactional
    public void joinCourse(MemberCourse memberCourse) {
        //根据课程id查询课程信息
        Long courseId = memberCourse.getCourseId();
        Course course = courseService.getById(courseId);
        BeanUtils.copyProperties(course,memberCourse);
        //插入报名表
        int inserted = this.baseMapper.insert(memberCourse);
        if(inserted > 0){//报名成功，扣除金额
            MemberDTO member = memberRpcService.getMemberById(memberCourse.getMemberId());
            member.setMoney(member.getMoney().subtract(course.getCoursePrice()));
            memberRpcService.editMember(member);
        }
    }
}
