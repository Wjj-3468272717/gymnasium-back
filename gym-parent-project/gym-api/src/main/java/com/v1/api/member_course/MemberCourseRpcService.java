package com.v1.api.member_course;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_course.MemberCourseDTO;

public interface MemberCourseRpcService {
    void joinCourse(MemberCourseDTO memberCourse);

    PageResultDTO<MemberCourseDTO> getMyCourseList(PageDTO page, Long memberId);
}
