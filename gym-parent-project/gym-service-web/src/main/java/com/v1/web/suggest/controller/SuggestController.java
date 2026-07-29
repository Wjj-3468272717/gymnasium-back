package com.v1.web.suggest.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.suggest.entity.Suggest;
import com.v1.web.suggest.entity.SuggestParam;
import com.v1.web.suggest.service.SuggestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @Autowired
    SuggestService suggestService;

    /**
     * 新增反馈
     * @param suggest
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Suggest suggest){
        suggest.setDateTime(new Date());
        boolean updated = suggestService.save(suggest);
        if(updated){
            return ResultUtils.success("反馈成功");
        }else{
            return ResultUtils.error("反馈失败");
        }
    }

    /**
     * 编辑反馈
     * @param suggest
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody Suggest suggest){
        boolean updated = suggestService.updateById(suggest);
        if(updated){
            return ResultUtils.success("修改成功");
        }else{
            return ResultUtils.error("修改失败");
        }
    }

    /**
     * 删除反馈
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        boolean updated = suggestService.removeById(id);
        if(updated){
            return ResultUtils.success("删除成功");
        }else{
            return ResultUtils.error("删除失败");
        }
    }

    @GetMapping("/list")
    public ResultVo list(SuggestParam suggestParam){
        IPage<Suggest> list = suggestService.list(suggestParam);
        return ResultUtils.success("查询成功",list);
    }

}
