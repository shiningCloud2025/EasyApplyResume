package com.zyh.easyapplyresume.controller.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdvertisementPageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 广告管理控制器-监测端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/admonitor/advertisement")
@Tag(name = "广告管理控制器-监测端")
public class AdmonitorAdvertisementController {

    @Autowired
    private AdmonitorAdvertisementService admonitorAdvertisementService;

    @Operation(summary = "添加广告")
    @PostMapping("/addAdmonitorAdvertisement")
    @PreAuthorize("hasAuthority('/admonitor/admonitor/advertisement/addAdmonitorAdvertisement')")
    public BaseResult<Integer> addAdmonitorAdvertisement(@RequestBody AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        return BaseResult.ok(admonitorAdvertisementService.addAdmonitorAdvertisement(admonitorAdvertisementForm));
    }

    @Operation(summary = "更新广告")
    @PutMapping("/updateAdmonitorAdvertisement")
    @PreAuthorize("hasAuthority('/admonitor/admonitor/advertisement/updateAdmonitorAdvertisement')")
    public BaseResult<Integer> updateAdmonitorAdvertisement(@RequestBody AdmonitorAdvertisementForm admonitorAdvertisementForm) {
        return BaseResult.ok(admonitorAdvertisementService.updateAdmonitorAdvertisement(admonitorAdvertisementForm));
    }

    @Operation(summary = "删除广告")
    @DeleteMapping("/deleteAdmonitorAdvertisement")
    @PreAuthorize("hasAuthority('/admonitor/admonitor/advertisement/deleteAdmonitorAdvertisement')")
    public BaseResult<Integer> deleteAdmonitorAdvertisement(@RequestParam(required = true, name = "id") Integer id) {
        return BaseResult.ok(admonitorAdvertisementService.deleteAdmonitorAdvertisement(id));
    }

    @Operation(summary = "查询广告")
    @GetMapping("/findAdmonitorAdvertisementById")
    @PreAuthorize("hasAuthority('/admonitor/admonitor/advertisement/findAdmonitorAdvertisementById')")
    public BaseResult<AdmonitorAdvertisementInfoVO> findAdmonitorAdvertisementById(@RequestParam(required = true, name = "id") Integer id) {
        return BaseResult.ok(admonitorAdvertisementService.findAdmonitorAdvertisementById(id));
    }

    @Operation(summary = "分页查询广告")
    @PostMapping("/findAdmonitorAdvertisementByPage")
    @PreAuthorize("hasAuthority('/admonitor/admonitor/advertisement/findAdmonitorAdvertisementByPage')")
    public BaseResult<Page<AdmonitorAdvertisementPageVO>> findAdmonitorAdvertisementByPage(@RequestParam(required = false, name = "pageNum",defaultValue = "1") Integer pageNum,
                                                                                           @RequestParam(required = false, name = "pageSize",defaultValue = "10") Integer pageSize,
                                                                                           @RequestBody(required = false) AdmonitorAdvertisementQuery admonitorAdvertisementQuery) {
        return BaseResult.ok(admonitorAdvertisementService.findAdmonitorAdvertisementByPage(pageNum, pageSize, admonitorAdvertisementQuery));
    }

    @Operation(summary = "查询所有广告")
    @GetMapping("/findAllAdmonitorAdvertisement")
    public BaseResult<List<AdmonitorAdvertisementInfoVO>> findAllAdmonitorAdvertisement() {
        return BaseResult.ok(admonitorAdvertisementService.findAllAdmonitorAdminAdvertisement());
    }



}
