package com.zyh.easyapplyresume.demo.tools;

import cn.hutool.core.io.FileUtil;
import com.zyh.easyapplyresume.bean.constant.ToolCallingFileConstant;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

/**
 * 文件操作工具类
 * @author shiningCloud2025
 */
public class FileOperationTool {

    private final String FILE_DIR = ToolCallingFileConstant.FILE_SAVE_DIR+"/file";

    @Tool(description = "Read content from a file")
    public String readFile(@ToolParam(description = "Name of the file to read")String fileName){
        String filePath = FILE_DIR+"/"+fileName;
        try{
            return FileUtil.readUtf8String(filePath);
        }catch (Exception e){
            throw new BusException(AdminCodeEnum.READFILE_TOOL_CALLING_FAIL);
        }
    }
    @Tool(description = "Write content to a file")
    public String writeFile(
            @ToolParam(description = "Name of the file to write") String fileName,
            @ToolParam(description = "Content to write to the file") String content){
        String filePath = FILE_DIR+"/"+fileName;
        try{
            // 创建目录
            FileUtil.mkdir(FILE_DIR);
            FileUtil.writeUtf8String(content,filePath);
            return "File written successfully to: " + filePath;
        }catch (Exception e){
            throw new BusException(AdminCodeEnum.WRITEFILE_TOOL_CALLING_FAIL);
        }

    }

}
