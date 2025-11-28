package com.zyh.easyapplyresume.demo.tools;

import org.springframework.ai.tool.annotation.Tool;

/**
 * Agent终止工具类
 * @author shiningCloud2025
 */
public class TerminateTool {

    @Tool(description = """  
            Terminate the interaction when the request is met OR if the assistant cannot proceed further with the task.  
            "When you have finished all the tasks, call this tool to end the work.  
            """)
    public String doTerminate(){
        return "任务结束";
    }
}
