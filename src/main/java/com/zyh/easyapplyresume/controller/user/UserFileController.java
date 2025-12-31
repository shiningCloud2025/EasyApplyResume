package com.zyh.easyapplyresume.controller.user;

import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BaseResult;
import com.zyh.easyapplyresume.model.form.admin.AdminFileForm;
import com.zyh.easyapplyresume.model.form.user.UserFileForm;
import com.zyh.easyapplyresume.qiniuoss.OssService;
import com.zyh.easyapplyresume.qiniuoss.OssSystemTypeEnum;
import com.zyh.easyapplyresume.qiniuoss.OssUserBusinessTypeEnum;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户文件控制器-用户端
 * @author shiningCloud2025
 */
@RestController
@RequestMapping("/user/file")
@Tag(name = "用户文件控制器-用户端")
public class UserFileController {

    @Autowired
    private OssService ossService;

    @Operation(summary = "上传用户头像")
    @PostMapping("/uploadUserHeadImg")
    public BaseResult<String> uploadUserHeadImg(@ModelAttribute UserFileForm userFileForm){
        String url = ossService.upload(userFileForm.getFile(), OssSystemTypeEnum.USER, OssUserBusinessTypeEnum.USER_HEAD_IMG, userFileForm.getUserId(), false);
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

    @Operation(summary = "查询某个管理员上传的所有头像")
    @GetMapping("/listFilesByUserId")
    public BaseResult<List<String>> listFilesByUserId(@RequestParam(required = true,value = "userId") Integer userId){
        List<String> urls = ossService.listFilesByOwner(OssSystemTypeEnum.USER, OssUserBusinessTypeEnum.USER_HEAD_IMG, userId, false);
        return BaseResult.ok(urls);
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
