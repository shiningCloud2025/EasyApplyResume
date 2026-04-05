package com.zyh.easyapplyresume.controller.user;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.query.admin.AdminFaqQuery;
import com.zyh.easyapplyresume.model.vo.admin.AdminFaqInfoVO;
import com.zyh.easyapplyresume.model.vo.admin.AdminFaqPageVO;
import com.zyh.easyapplyresume.service.admin.AdminFaqService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 常见问题控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/faq")
@Tag(name = "常见问题控制器-用户端")
public class UserFaqController {
    @Autowired
    private AdminFaqService faqService;

    @Operation(summary = "获取常见问题信息")
    @GetMapping("/getInfo")
    public BaseResult<AdminFaqInfoVO> getFaqInfo(@RequestParam(required = true, value = "faqId") Integer faqId) {
        return BaseResult.ok(faqService.getFaqInfo(faqId));
    }

    @Operation(summary = "分页查询常见问题")
    @PostMapping("/getPage")
    public BaseResult<Page<AdminFaqPageVO>> getFaqPage(
            @RequestParam(required = false, value = "size", defaultValue = "10") int size,
            @RequestParam(required = false, value = "page", defaultValue = "1") int page,
            @RequestBody AdminFaqQuery query) {
        return BaseResult.ok(faqService.getFaqPage(size, page, query));
    }
}
