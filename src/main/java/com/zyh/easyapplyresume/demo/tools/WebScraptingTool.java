//package com.zyh.easyapplyresume.demo.tools;
//
//import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
//import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.ai.tool.annotation.ToolParam;
//
///**
// * 网页抓取工具类
// * @author shiningCloud2025
// */
//public class WebScraptingTool {
//    @Tool(description = "Scrape the content of a web page")
//    public String scrapeWebPage(@ToolParam( description = "URL of the web page to scrape") String url){
//        try{
//            Document doc = Jsoup.connect(url).get();
//            return doc.html();
//        }catch (Exception e){
//            throw new BusException(AdminCodeEnum.WEBSCRAPTING_TOOL_CALLING_FAIL);
//        }
//    }
//}
