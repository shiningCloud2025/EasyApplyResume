//package com.zyh.easyapplyresume.demo.tools;
//
//import cn.hutool.core.io.FileUtil;
//import cn.hutool.http.HttpUtil;
//import com.alibaba.nacos.api.naming.pojo.healthcheck.impl.Http;
//import com.zyh.easyapplyresume.bean.constant.ToolCallingFileConstant;
//import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
//import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.ai.tool.annotation.ToolParam;
//
//import java.io.File;
//
///**
// * 资源下载工具类
// * @author shiningCloud2025
// */
//public class ResourceDownloadTool {
//
//    @Tool(description = "Download a resource from a given URL")
//    public String downloadResource(@ToolParam(description = "URL of the resource to download") String url,
//                                   @ToolParam(description = "Name of the file to save the downloaded resource") String fileName){
//        String fileDir = ToolCallingFileConstant.FILE_SAVE_DIR+"/download";
//        String filePath = getUniqueFilePath(fileDir, fileName);
//        try{
//            // 创建目录
//            FileUtil.mkdir(fileDir);
//            // 使用 Hutool 的 downloadFile 方法下载资源
//            HttpUtil.downloadFile(url, new File(filePath));
//            return "Resource downloaded successfully to: " + filePath;
//        }catch (Exception e){
//            throw new BusException(AdminCodeEnum.RESOURCEDOWNLOAD_TOOL_CALLING_FAIL);
//        }
//    }
//    // 生成不重复的文件名（添加时间戳）
//    private String getUniqueFilePath(String dir, String fileName) {
//        File file = new File(dir, fileName);
//        if (!file.exists()) {
//            return file.getAbsolutePath();
//        }
//        // 若文件存在，在文件名后添加时间戳
//        String nameWithoutExt = FileUtil.mainName(fileName);
//        String ext = FileUtil.extName(fileName);
//        String newName = nameWithoutExt + "_" + System.currentTimeMillis() + (ext.isEmpty() ? "" : "." + ext);
//        return new File(dir, newName).getAbsolutePath();
//    }
//}
