package com.v1.web.member_card.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.member_card.entity.ListCard;
import com.v1.web.member_card.entity.MemberCard;
import com.v1.web.member_card.service.MemberCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/memberCard")
public class MemberCardController {

    @Autowired
    MemberCardService memberCardService;

    /**
     * 新增会员卡
     * @param memberCard
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody MemberCard memberCard){
        boolean updated = memberCardService.save(memberCard);
        if(updated){
            return ResultUtils.success("添加成功");
        }else{
            return ResultUtils.error("添加失败");
        }
    }

    /**
     * 修改会员卡
     * @param memberCard
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody MemberCard memberCard){
        boolean updated = memberCardService.updateById(memberCard);
        if(updated){
            return ResultUtils.success("编辑成功");
        }else{
            return ResultUtils.error("编辑失败");
        }
    }

    /**
     * 删除会员卡
     * @param cardId
     * @return
     */
    @DeleteMapping("/{cardId}")
    public ResultVo delete(@PathVariable("cardId") Long cardId){
        boolean updated = memberCardService.removeById(cardId);
        if(updated){
            return ResultUtils.success("删除成功");
        }else{
            return ResultUtils.error("删除失败");
        }
    }

    @GetMapping("/list")
    public ResultVo list(ListCard listCard){
        IPage<MemberCard> list = memberCardService.list(listCard);
        return ResultUtils.success("查询成功",list);
    }

}
