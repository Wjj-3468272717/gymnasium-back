package com.v1.web.suggest.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.suggest.SuggestDTO;
import com.v1.api.suggest.SuggestRpcService;
import com.v1.service.home.suggest.entity.Suggest;
import com.v1.service.home.suggest.entity.SuggestParam;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/api/suggest")
public class SuggestController {

    @DubboReference
    SuggestRpcService suggestRpcService;

    /**
     * 新增反馈
     * @param suggest
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Suggest suggest){
        suggest.setDateTime(new Date());
        SuggestDTO dto = new SuggestDTO();
        BeanUtils.copyProperties(suggest, dto);
        suggestRpcService.add(dto);
        return ResultUtils.success("反馈成功");
    }

    /**
     * 编辑反馈
     * @param suggest
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody Suggest suggest){
        SuggestDTO dto = new SuggestDTO();
        BeanUtils.copyProperties(suggest, dto);
        suggestRpcService.update(dto);
        return ResultUtils.success("修改成功");
    }

    /**
     * 删除反馈
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public ResultVo delete(@PathVariable("id") Long id){
        suggestRpcService.delete(id);
        return ResultUtils.success("删除成功");
    }

    @GetMapping("/list")
    public ResultVo list(SuggestParam suggestParam){
        PageDTO page = new PageDTO();
        page.setCurrentPage(suggestParam.getCurrentPage());
        page.setPageSize(suggestParam.getPageSize());
        PageResultDTO<SuggestDTO> result = suggestRpcService.list(page, suggestParam.getTitle());
        return ResultUtils.success("查询成功", result);
    }

}
