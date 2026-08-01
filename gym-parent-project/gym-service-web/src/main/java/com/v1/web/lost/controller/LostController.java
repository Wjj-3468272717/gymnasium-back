package com.v1.web.lost.controller;

import com.v1.api.dto.PageDTO;
import com.v1.api.dto.PageResultDTO;
import com.v1.api.dto.lost.LostDTO;
import com.v1.api.lost.LostRpcService;
import com.v1.service.home.lost.entity.Lost;
import com.v1.service.home.lost.entity.LostParam;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lost")
public class LostController {

    @DubboReference
    LostRpcService lostRpcService;

    /**
     * 新增丢失物品
     * @param lost
     * @return
     */
    @PostMapping
    public ResultVo add(@RequestBody Lost lost){
        LostDTO dto = new LostDTO();
        BeanUtils.copyProperties(lost, dto);
        dto.setId(lost.getLostId());
        lostRpcService.add(dto);
        return ResultUtils.success("新增成功");
    }

    /**
     * 编辑丢失物品
     * @param lost
     * @return
     */
    @PutMapping
    public ResultVo edit(@RequestBody Lost lost){
        LostDTO dto = new LostDTO();
        BeanUtils.copyProperties(lost, dto);
        dto.setId(lost.getLostId());
        lostRpcService.update(dto);
        return ResultUtils.success("编辑成功");
    }

    /**
     * 删除失物招领
     * @param lostId
     * @return
     */
    @DeleteMapping("/{lostId}")
    public ResultVo delete(@PathVariable("lostId") Long lostId){
        lostRpcService.delete(lostId);
        return ResultUtils.success("删除成功");
    }

    /**
     * 查询失物招领分页数据
     * @param lostParam
     * @return
     */
    @GetMapping("/list")
    public ResultVo list(LostParam lostParam){
        PageDTO page = new PageDTO();
        page.setCurrentPage(lostParam.getCurrentPage());
        page.setPageSize(lostParam.getPageSize());
        PageResultDTO<LostDTO> result = lostRpcService.list(page, lostParam.getLostName());
        return ResultUtils.success("查询成功", result);
    }

}
