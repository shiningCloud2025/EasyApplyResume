package com.zyh.easyapplyresume.controller.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorUserAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorUserAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorUserAdvertisementPageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdvertisementService;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorUserAdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 广告管理控制器-用户端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/user/advertisement")
@Tag(name = "广告管理控制器-用户端")
public class AdmonitorUserAdvertisementController {

    @Autowired
    private AdmonitorUserAdvertisementService admonitorUserAdvertisementService;

    @Operation(summary = "添加广告")
    @PostMapping("/addAdmonitorUserAdvertisement")
    @PreAuthorize("hasAuthority('/admonitor/user/advertisement/addAdmonitorUserAdvertisement')")
    public BaseResult<Integer> addAdmonitorUserAdvertisement(@RequestBody AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm) {
        return BaseResult.ok(admonitorUserAdvertisementService.addAdmonitorUserAdvertisement(admonitorUserAdvertisementForm));
    }

    @Operation(summary = "更新广告")
    @PutMapping("/updateAdmonitorUserAdvertisement")
    @PreAuthorize("hasAuthority('/admonitor/user/advertisement/updateAdmonitorUserAdvertisement')")
    public BaseResult<Integer> updateAdmonitorUserAdvertisement(@RequestBody AdmonitorUserAdvertisementForm admonitorUserAdvertisementForm) {
        return BaseResult.ok(admonitorUserAdvertisementService.updateAdmonitorUserAdvertisement(admonitorUserAdvertisementForm));
    }

    @Operation(summary = "删除广告")
    @DeleteMapping("/deleteAdmonitorUserAdvertisement")
    @PreAuthorize("hasAuthority('/admonitor/user/advertisement/deleteAdmonitorUserAdvertisement')")
    public BaseResult<Integer> deleteAdmonitorUserAdvertisement(@RequestParam(required = true, name = "id") Integer id) {
        return BaseResult.ok(admonitorUserAdvertisementService.deleteAdmonitorUserAdvertisement(id));
    }

    @Operation(summary = "查询广告")
    @GetMapping("/findAdmonitorUserAdvertisementById")
    @PreAuthorize("hasAuthority('/admonitor/user/advertisement/findAdmonitorUserAdvertisementById')")
    public BaseResult<AdmonitorUserAdvertisementInfoVO> findAdmonitorUserAdvertisementById(@RequestParam(required = true, name = "id") Integer id) {
        return BaseResult.ok(admonitorUserAdvertisementService.findAdmonitorUserAdvertisementById(id));
    }

    @Operation(summary = "分页查询广告")
    @PostMapping("/findAdmonitorUserAdvertisementByPage")
    @PreAuthorize("hasAuthority('/admonitor/user/advertisement/findAdmonitorUserAdvertisementByPage')")
    public BaseResult<Page<AdmonitorUserAdvertisementPageVO>> findAdmonitorUserAdvertisementByPage(@RequestParam(required = false, name = "pageNum",defaultValue = "1") Integer pageNum,
                                                                                                   @RequestParam(required = false, name = "pageSize",defaultValue = "10") Integer pageSize,
                                                                                                   @RequestBody(required = false) AdmonitorUserAdvertisementQuery admonitorUserAdvertisementForm) {
        return BaseResult.ok(admonitorUserAdvertisementService.findAdmonitorUserAdvertisementByPage(pageNum, pageSize, admonitorUserAdvertisementForm));

    }

    @Operation(summary = "查询所有广告")
    @GetMapping("/findAllAdmonitorUserAdvertisement")
    public BaseResult<List<AdmonitorUserAdvertisementInfoVO>> findAllAdmonitorUserAdvertisement() {
        return BaseResult.ok(admonitorUserAdvertisementService.findAllAdmonitorUserAdvertisement());
    }

}
