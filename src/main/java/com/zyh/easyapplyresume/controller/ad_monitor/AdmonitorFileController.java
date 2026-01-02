package com.zyh.easyapplyresume.controller.ad_monitor;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.qiniuoss.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件控制器-监测端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/admonitor/file")
@Tag(name = "文件控制器-监测端")
public class AdmonitorFileController {

    @Autowired
    private OssService ossService;

    @Operation(summary = "上传管理端广告图片")
    @PostMapping("/uploadAdminAdImg")
    public BaseResult<String> uploadAdminAdImg(MultipartFile file){
        String url = ossService.upload(file, OssSystemTypeEnum.ADMIN, OssAdminBusinessTypeEnum.ADMIN_AD_IMG, 0, false);
        return BaseResult.ok(url);
    }

    @Operation(summary = "上传用户端广告图片")
    @PostMapping("/uploadUserAdImg")
    public BaseResult<String> uploadUserAdImg(MultipartFile file){
        String url = ossService.upload(file, OssSystemTypeEnum.USER, OssUserBusinessTypeEnum.USER_AD_IMG, 0, false);
        return BaseResult.ok(url);
    }

    @Operation(summary = "上传监控端广告图片")
    @PostMapping("/uploadAdmonitorAdImg")
    public BaseResult<String> uploadAdmonitorAdImg(MultipartFile file){
        String url = ossService.upload(file, OssSystemTypeEnum.AD_MONITOR, OssAdMonitorBusinessTypeEnum.ADMONITOR_ADMIN_AD_IMG, 0, false);
        return BaseResult.ok(url);
    }

    @Operation(summary = "删除文件(非私有)")
    @DeleteMapping("/deleteFile")
    public BaseResult<?> deleteFile(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        ossService.deleteByUrl(fileUrl,false);
        return BaseResult.ok();
    }

    @Operation(summary = "删除文件(私有)")
    @DeleteMapping("/deleteFilePrivate")
    public BaseResult<?> deleteFilePrivate(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        ossService.deleteByUrl(fileUrl,true);
        return BaseResult.ok();
    }


    @Operation(summary = "获取文件预览地址(非私有)")
    @GetMapping("/getPreviewUrl")
    public BaseResult<String> getPreviewUrl(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        String url = ossService.getPreviewUrl(fileUrl, false);
        return BaseResult.ok(url);
    }

    @Operation(summary = "获取文件预览地址(私有)")
    @GetMapping("/getPreviewUrlPrivate")
    public BaseResult<String> getPreviewUrlPrivate(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        String url = ossService.getPreviewUrl(fileUrl, true);
        return BaseResult.ok(url);
    }

    @Operation(summary = "获取文件下载地址(非私有)")
    @GetMapping("/getDownloadUrl")
    public BaseResult<String> getDownloadUrl(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        String url = ossService.generateDownloadUrl(fileUrl, false,0);
        return BaseResult.ok(url);
    }

    @Operation(summary = "获取文件下载地址(私有)")
    @GetMapping("/getDownloadUrlPrivate")
    public BaseResult<String> getDownloadUrlPrivate(@RequestParam(required = true,value = "fileUrl") String fileUrl){
        String url = ossService.generateDownloadUrl(fileUrl, true,3600);
        return BaseResult.ok(url);
    }

}
