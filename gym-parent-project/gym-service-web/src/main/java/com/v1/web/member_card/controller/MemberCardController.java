package com.v1.web.member_card.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.member_card.MemberCardDTO;
import com.v1.api.member_card.MemberCardRpcService;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberCard")
public class MemberCardController {

    @DubboReference
    MemberCardRpcService memberCardRpcService;

    /**
     * 新增会员卡
     * @param memberCard
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody MemberCardDTO memberCard){
        memberCardRpcService.saveCard(memberCard);
        return ResultUtils.success("添加成功");
    }

    /**
     * 修改会员卡
     * @param memberCard
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody MemberCardDTO memberCard){
        memberCardRpcService.updateCard(memberCard);
        return ResultUtils.success("编辑成功");
    }

    /**
     * 删除会员卡
     * @param cardId
     * @return
     */
    @DeleteMapping("/{cardId}")
    public ResultVo delete(@PathVariable("cardId") Long cardId){
        memberCardRpcService.deleteCard(cardId);
        return ResultUtils.success("删除成功");
    }

    @GetMapping("/list")
    public ResultVo list(Long currentPage, Long pageSize, String title){
        PageDTO page = new PageDTO();
        page.setCurrentPage(currentPage);
        page.setPageSize(pageSize);
        PageResultDTO<MemberCardDTO> list = memberCardRpcService.listCards(page, title);
        return ResultUtils.success("查询成功", list);
    }

}
