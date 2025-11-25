//package com.zyh.easyapplyresume.demo.tools;
//
//import cn.hutool.http.HttpUtil;
//import cn.hutool.json.JSONArray;
//import cn.hutool.json.JSONObject;
//import cn.hutool.json.JSONUtil;
//import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.AdminCodeEnum;
//import com.zyh.easyapplyresume.bean.usallyexceptionandEnum.BusException;
//import org.springframework.ai.tool.annotation.Tool;
//import org.springframework.ai.tool.annotation.ToolParam;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
///**
// * 联网搜索工具类
// * @author shiningCloud2025
// */
//@Component
//public class WebSearchTool {
//    // SearchAPI的搜索接口地址
//    private static final String SEARCH_API_URL = "https://www.searchapi.io/api/v1/search";
//
//    @Value("${search-api.api-key}")
//    private String apiKey;
//    @Tool(description = "Search for information from Baidu Search Engine")
//    public String searchWeb(@ToolParam(description = "Search query keyword") String query){
//        Map<String, Object> paramMap = new HashMap<>();
//        paramMap.put("q", query);
//        paramMap.put("api_key", apiKey);
//        paramMap.put("engine", "baidu");
//        try {
//            System.out.println(apiKey);
//            String response = HttpUtil.get(SEARCH_API_URL, paramMap);
//            // 取出返回结果的前 5 条
//            JSONObject jsonObject = JSONUtil.parseObj(response);
//            // 提取 organic_results 部分
//            JSONArray organicResults = jsonObject.getJSONArray("organic_results");
//            List<Object> objects = organicResults.subList(0, 5);
//            // 拼接搜索结果为字符串
//            String result = objects.stream().map(obj -> {
//                JSONObject tmpJSONObject = (JSONObject) obj;
//                return tmpJSONObject.toString();
//            }).collect(Collectors.joining(","));
//            return result;
//        } catch (Exception e) {
//            throw new BusException(AdminCodeEnum.WEBSEARCH_TOOL_CALLING_FAIL);
//        }
//    }
//}
