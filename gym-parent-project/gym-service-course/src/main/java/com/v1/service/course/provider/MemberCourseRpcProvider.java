package com.v1.service.course.provider;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_course.MemberCourseDTO;
import com.v1.api.member_course.MemberCourseRpcService;
import com.v1.service.course.member_course.entity.MemberCourse;
import com.v1.service.course.member_course.service.MemberCourseService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.stream.Collectors;

@DubboService
public class MemberCourseRpcProvider implements MemberCourseRpcService {

    @Autowired
    private MemberCourseService memberCourseService;

    @Override
    public void joinCourse(MemberCourseDTO memberCourse) {
        MemberCourse entity = new MemberCourse();
        entity.setCourseId(memberCourse.getCourseId());
        entity.setMemberId(memberCourse.getMemberId());
        memberCourseService.joinCourse(entity);
    }

    @Override
    public PageResultDTO<MemberCourseDTO> getMyCourseList(PageDTO page, Long memberId) {
        IPage<MemberCourse> iPage = new Page<>(page.getCurrentPage(), page.getPageSize());
        QueryWrapper<MemberCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberCourse::getMemberId, memberId);
        IPage<MemberCourse> list = memberCourseService.page(iPage, queryWrapper);

        PageResultDTO<MemberCourseDTO> dto = new PageResultDTO<>();
        dto.setCurrentPage(list.getCurrent());
        dto.setPageSize(list.getSize());
        dto.setTotal(list.getTotal());
        dto.setRecords(list.getRecords().stream().map(entity -> {
            MemberCourseDTO memberCourseDTO = new MemberCourseDTO();
            BeanUtils.copyProperties(entity, memberCourseDTO);
            memberCourseDTO.setId(entity.getMemberCourseId());
            return memberCourseDTO;
        }).collect(Collectors.toList()));
        return dto;
    }

    @Override
    public boolean hasJoinedCourse(Long memberId, Long courseId) {
        QueryWrapper<MemberCourse> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MemberCourse::getCourseId, courseId).eq(MemberCourse::getMemberId, memberId);
        return memberCourseService.count(queryWrapper) > 0;
    }
}
