package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminDaliyAdminNumService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserDaliyUserNumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * 用户每日数量记录-监测端和用户端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/user/daliyUserNum")
@Tag(name = "用户每日数量记录-监测端和用户端")
public class AdmonitorUserDaliyUserNumController {

    @Autowired
    private AdmonitorUserDaliyUserNumService admonitorUserDaliyUserNumService;

    @GetMapping("/findFromTimeToEndTimeAdmonitorUserDaliyUserNum")
    @Operation(summary = "查询规定时间内的管理员数量")
    public BaseResult<List<Integer>> findFromTimeToEndTimeAdmonitorUserDaliyUserNum(@RequestParam(required = true,name = "fromDate")
                                                                                    @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                                        Date fromDate,
                                                                                      @RequestParam(required = true,name = "endDate")
                                                                                      @DateTimeFormat(pattern = "yyyy-MM-dd")
                                                                                      Date endDate){
        return BaseResult.ok(admonitorUserDaliyUserNumService.findFromTimeToEndTimeAdmonitorUserDaliyUserNum(fromDate,endDate));
    }


    @GetMapping("/calculateUserTotalNum")
    @Operation(summary = "计算总用户数量")
    public BaseResult<Integer> calculateUserTotalNum(){
        return BaseResult.ok(admonitorUserDaliyUserNumService.calculateUserTotalNum());
    }

}
