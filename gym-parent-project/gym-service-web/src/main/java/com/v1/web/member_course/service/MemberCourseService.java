package com.v1.web.member_course.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.v1.web.member_course.entity.MemberCourse;

public interface MemberCourseService extends IService<MemberCourse> {

    void joinCourse(MemberCourse memberCourse);

}
