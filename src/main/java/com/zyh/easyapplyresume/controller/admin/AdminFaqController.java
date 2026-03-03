package com.zyh.easyapplyresume.controller.admin;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminFaqForm;
import com.zyh.easyapplyresume.model.query.admin.AdminFaqQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFaqInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFaqPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 常见问题控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/faq")
@Tag(name = "常见问题控制器-管理端")
public class AdminFaqController {
    @Autowired
    private AdminFaqService faqService;

    @Operation(summary = "添加常见问题")
    @PostMapping("/add")
    public BaseResult<Integer> addFaq(@RequestBody AdminFaqForm faqForm) {
        return BaseResult.ok(faqService.addFaq(faqForm));
    }

    @Operation(summary = "修改常见问题")
    @PostMapping("/update")
    public BaseResult<Integer> updateFaq(@RequestBody AdminFaqForm faqForm) {
        return BaseResult.ok(faqService.updateFaq(faqForm));
    }

    @Operation(summary = "删除常见问题")
    @DeleteMapping("/delete")
    public BaseResult<Integer> deleteFaq(@RequestParam(required = true, value = "faqId") Integer faqId) {
        return BaseResult.ok(faqService.deleteFaq(faqId));
    }

    @Operation(summary = "获取常见问题信息")
    @GetMapping("/getInfo")
    public BaseResult<AdminFaqInfoVO> getFaqInfo(@RequestParam(required = true, value = "faqId") Integer faqId) {
        return BaseResult.ok(faqService.getFaqInfo(faqId));
    }

    @Operation(summary = "分页查询常见问题")
    @PostMapping("/getPage")
    public BaseResult<Page<AdminFaqPageVO>> getFaqPage(
            @RequestParam(required = false, value = "pageNum", defaultValue = "1") int pageNum,
            @RequestParam(required = false, value = "pageSize", defaultValue = "10") int pageSize,
            @RequestBody(required = false) AdminFaqQuery faqQuery) {
        return BaseResult.ok(faqService.getFaqPage(pageSize, pageNum, faqQuery));
    }
}
