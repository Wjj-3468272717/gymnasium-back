package com.v1.web.home;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.v1.utils.ResultUtils;
import com.v1.utils.ResultVo;
import com.v1.web.equipment.service.MaterialService;
import com.v1.web.goods_order.service.GoodsOrderService;
import com.v1.web.home.entity.EChart;
import com.v1.web.home.entity.EChartItem;
import com.v1.web.home.entity.TotalCount;
import com.v1.web.member.service.MemberService;
import com.v1.web.suggest.entity.Suggest;
import com.v1.web.suggest.service.SuggestService;
import com.v1.web.sys_user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    @Autowired
    MemberService memberService;
    @Autowired
    SysUserService userService;
    @Autowired
    MaterialService materialService;
    @Autowired
    GoodsOrderService goodsOrderService;
    @Autowired
    SuggestService suggestService;

    //统计总数
    @GetMapping("/getTotal")
    public ResultVo getTotal(){
        int memberCount = memberService.count();
        int userCount = userService.count();
        int materialCount = materialService.count();
        int orderCount = goodsOrderService.count();
        TotalCount totalCount = new TotalCount(memberCount,userCount,materialCount,orderCount);
        return ResultUtils.success("查询成功",totalCount);
    }

    @GetMapping("/getSuggestList")
    public ResultVo getSuggestList(){
        QueryWrapper<Suggest> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Suggest::getDateTime).last("limit 3");
        List<Suggest> suggestList = suggestService.list(queryWrapper);
        return ResultUtils.success("查询成功",suggestList);
    }

    @GetMapping("/getHotGoods")
    public ResultVo getHotGoods(){
        List<EChartItem> eChartItems = goodsOrderService.hotGoods();
        EChart eChart = new EChart();
        if(eChartItems.size() > 0){
            for(int i = 0; i < eChartItems.size(); i++){
                eChart.getNames().add(eChartItems.get(i).getName());
                eChart.getValues().add(eChartItems.get(i).getValue());
            }
        }
        return ResultUtils.success("查询成功",eChart);
    }

    @GetMapping("/getHotCards")
    public ResultVo getHotCards(){
        List<EChartItem> eChartItems = goodsOrderService.hotCard();
        return ResultUtils.success("查询成功",eChartItems);
    }

    @GetMapping("/getHotCourse")
    public ResultVo getHotCourse(){
        List<EChartItem> eChartItems = goodsOrderService.hotCourse();
        return ResultUtils.success("查询成功",eChartItems);
    }

}
