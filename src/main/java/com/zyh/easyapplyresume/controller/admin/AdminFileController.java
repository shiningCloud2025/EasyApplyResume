package com.zyh.easyapplyresume.controller.admin;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminFileForm;
import com.zyh.easyapplyresume.qiniuoss.OssService;
import com.zyh.easyapplyresume.qiniuoss.OssSystemTypeEnum;
import com.zyh.easyapplyresume.qiniuoss.OssUserBusinessTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * 文件控制器-管理端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admin/file")
@Tag(name = "文件控制器-管理端")
public class AdminFileController {

    @Autowired
    private OssService ossService;

    @Operation(summary = "上传管理员头像")
    @PostMapping("/uploadAdminHeadImg")
    public BaseResult<String> uploadAdminHeadImg(@ModelAttribute AdminFileForm adminFileForm){
        String url = ossService.upload(adminFileForm.getFile(), OssSystemTypeEnum.ADMIN, OssUserBusinessTypeEnum.USER_HEAD_IMG, adminFileForm.getAdminId(), false);
        return BaseResult.ok(url);
    }

    @Operation(summary = "删除文件")
    @DeleteMapping("/deleteFile")
    public BaseResult<?> deleteFile(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        ossService.deleteByUrl(fileUrl,false);
        return BaseResult.ok();
    }

    @Operation(summary = "查询某个管理员上传的所有头像")
    @GetMapping("/listFilesByAdminId")
    public BaseResult<List<String>> listFilesByAdminId(@RequestParam(required = true,value = "adminId") Integer adminId){
        List<String> urls = ossService.listFilesByOwner(OssSystemTypeEnum.ADMIN, OssUserBusinessTypeEnum.USER_HEAD_IMG, adminId, false);
        return BaseResult.ok(urls);
    }

    @Operation(summary = "获取文件预览地址")
    @GetMapping("/getPreviewUrl")
    public BaseResult<String> getPreviewUrl(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        String url = ossService.getPreviewUrl(fileUrl, false);
        return BaseResult.ok(url);
    }

    @Operation(summary = "获取文件下载地址")
    @GetMapping("/getDownloadUrl")
    public BaseResult<String> getDownloadUrl(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        String url = ossService.generateDownloadUrl(fileUrl, false,0);
        return BaseResult.ok(url);
    }


}
