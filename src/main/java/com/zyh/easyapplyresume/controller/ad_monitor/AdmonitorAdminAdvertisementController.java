package com.zyh.easyapplyresume.controller.ad_monitor;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.ad_monitor.AdmonitorAdminAdvertisementForm;
import com.zyh.easyapplyresume.model.query.ad_monitor.AdmonitorAdminAdvertisementQuery;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementInfoVO;
import com.zyh.easyapplyresume.model.vo.ad_monitor.AdmonitorAdminAdvertisementPageVO;
import com.zyh.easyapplyresume.service.ad_monitor.AdmonitorAdminAdvertisementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 广告管理控制器-管理端
 * @author shiningCloudEth
 */
@RestController
@RequestMapping("/admonitor/admin/advertisement")
@Tag(name = "广告管理控制器-管理端")
public class AdmonitorAdminAdvertisementController {

    @Autowired
    private AdmonitorAdminAdvertisementService admonitorAdminAdvertisementService;

    @PostMapping("/addAdmonitorAdminAdvertisement")
    @Operation(summary = "添加广告")
    public BaseResult<Integer> addAdmonitorAdminAdvertisement(@RequestBody AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm) {
        return BaseResult.ok(admonitorAdminAdvertisementService.addAdmonitorAdminAdvertisement(admonitorAdminAdvertisementForm));
    }

    @PutMapping("/updateAdmonitorAdminAdvertisement")
    @Operation(summary = "更新广告")
    public BaseResult<Integer> updateAdmonitorAdminAdvertisement(@RequestBody AdmonitorAdminAdvertisementForm admonitorAdminAdvertisementForm) {
        return BaseResult.ok(admonitorAdminAdvertisementService.updateAdmonitorAdminAdvertisement(admonitorAdminAdvertisementForm));
    }

    @DeleteMapping("/deleteAdmonitorAdminAdvertisement")
    @Operation(summary = "删除广告")
    public BaseResult<Integer> deleteAdmonitorAdminAdvertisement(@RequestParam(required = true, name = "id") Integer id) {
        return BaseResult.ok(admonitorAdminAdvertisementService.deleteAdmonitorAdminAdvertisement(id));
    }

    @GetMapping("/findAdmonitorAdminAdvertisementById")
    @Operation(summary = "查询广告")
    public BaseResult<AdmonitorAdminAdvertisementInfoVO> findAdmonitorAdminAdvertisementById(@RequestParam(required = true, name = "id") Integer id) {
        return BaseResult.ok(admonitorAdminAdvertisementService.findAdmonitorAdminAdvertisementById(id));
    }

    @PostMapping("/findAdmonitorAdminAdvertisementByPage")
    @Operation(summary = "分页查询广告")
    public BaseResult<Page<AdmonitorAdminAdvertisementPageVO>> findAdmonitorAdminAdvertisementByPage(@RequestParam(required = false, name = "pageNum",defaultValue = "1") Integer pageNum,
                                                                                                     @RequestParam(required = false, name = "pageSize",defaultValue = "10") Integer pageSize,
                                                                                                     @RequestBody(required = false) AdmonitorAdminAdvertisementQuery admonitorAdminAdvertisementQuery) {
        return BaseResult.ok(admonitorAdminAdvertisementService.findAdmonitorAdminAdvertisementByPage(pageNum, pageSize, admonitorAdminAdvertisementQuery));
    }

    @GetMapping("/findAllAdmonitorAdminAdvertisement")
    @Operation(summary = "查询所有广告")
    public BaseResult<List<AdmonitorAdminAdvertisementInfoVO>> findAllAdmonitorAdminAdvertisement() {
        return BaseResult.ok(admonitorAdminAdvertisementService.findAllAdmonitorAdminAdvertisement());
    }


}
