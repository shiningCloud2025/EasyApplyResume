//package com.zyh.easyapplyresume.config.interceptor;
//
//import jakarta.servlet.ServletOutputStream;
//import jakarta.servlet.WriteListener;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpServletResponseWrapper;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//import org.springframework.web.servlet.ModelAndView;
//
//import java.io.CharArrayWriter;
//import java.io.IOException;
//import java.io.PrintWriter;
//
///**
// * SpringDoc配置3
// * @author shiningCloud2025
// */
//@Component
//public class FaviconInterceptor implements HandlerInterceptor {
//    // 标准图标标签（适配你的 favicon.ico，浏览器必识别）
//    private static final String FAVICON_TAG = "<link rel=\"icon\" href=\"/api/favicon.ico\" type=\"image/x-icon\">";
//
//    // 内嵌响应包装器：捕获doc.html内容（不新增外部类）
//    private static class DocHtmlWrapper extends HttpServletResponseWrapper {
//        private final CharArrayWriter charWriter = new CharArrayWriter();
//        private PrintWriter printWriter;
//
//        public DocHtmlWrapper(HttpServletResponse response) {
//            super(response);
//            this.printWriter = new PrintWriter(charWriter);
//        }
//
//        // 捕获HTML内容（后端渲染时写入此流）
//        @Override
//        public PrintWriter getWriter() throws IOException {
//            return printWriter;
//        }
//
//        // 备用：兼容字节流场景（无需修改）
//        @Override
//        public ServletOutputStream getOutputStream() throws IOException {
//            return new ServletOutputStream() {
//                @Override
//                public void write(int b) {}
//                @Override
//                public boolean isReady() { return false; }
//                @Override
//                public void setWriteListener(WriteListener listener) {}
//            };
//        }
//
//        // 核心：获取捕获到的完整doc.html内容
//        public String getOriginalHtml() {
//            printWriter.flush(); // 确保内容全部捕获
//            return charWriter.toString();
//        }
//
//        // 核心：把修改后的HTML写回浏览器
//        public void writeModifiedHtml(String modifiedHtml) throws IOException {
//            HttpServletResponse response = (HttpServletResponse) getResponse();
//            // 避免乱码：设置响应长度和编码
//            String encoding = response.getCharacterEncoding();
//            response.setContentLength(modifiedHtml.getBytes(encoding).length);
//            response.getWriter().write(modifiedHtml);
//            response.getWriter().flush();
//        }
//    }
//
//    // 原有：请求处理前执行（保留日志验证触发）
//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//        System.out.println("🚀【Favicon拦截器】已触发！当前请求路径：" + request.getRequestURI());
//        // 关键：如果是 doc.html 请求，用包装器替换响应流（准备捕获内容）
//        if ("/api/doc.html".equals(request.getRequestURI())) {
//            DocHtmlWrapper htmlWrapper = new DocHtmlWrapper(response);
//            request.setAttribute("docHtmlWrapper", htmlWrapper); // 存入请求，后续使用
//        }
//        return true; // 必须返回true，放行请求
//    }
//
//    // 核心：拿到doc.html，插入图标后返回
//    @Override
//    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
//        String requestURI = request.getRequestURI();
//        if ("/api/doc.html".equals(requestURI)) {
//            // 1. 取出preHandle中存入的包装器（里面有捕获的doc.html）
//            DocHtmlWrapper htmlWrapper = (DocHtmlWrapper) request.getAttribute("docHtmlWrapper");
//            if (htmlWrapper == null) {
//                System.out.println("【Favicon拦截器】❌ 未捕获到doc.html内容");
//                return;
//            }
//
//            try {
//                // 2. 拿到要返回的原始doc.html内容（核心需求！）
//                String originalHtml = htmlWrapper.getOriginalHtml();
//                System.out.println("【Favicon拦截器】✅ 成功拿到doc.html，长度：" + originalHtml.length());
//
//                // 3. 给doc.html插入图标标签（兼容各种<head>格式，不破坏原有内容）
//                String modifiedHtml;
//                if (originalHtml.contains("<head>")) {
//                    // 情况1：纯<head>标签（如 <head>）
//                    modifiedHtml = originalHtml.replace("<head>", "<head>\n  " + FAVICON_TAG);
//                } else if (originalHtml.contains("<head ")) {
//                    // 情况2：<head>带属性（如 <head lang="zh-CN">）
//                    modifiedHtml = originalHtml.replace("<head ", "<head " + FAVICON_TAG + "\n  ");
//                } else {
//                    // 情况3：无<head>标签（极端情况，手动添加）
//                    modifiedHtml = originalHtml.replace("<html>", "<html>\n<head>\n  " + FAVICON_TAG + "\n</head>");
//                }
//
//                // 4. 把修改后的doc.html写回浏览器（替代无效的响应头方案）
//                htmlWrapper.writeModifiedHtml(modifiedHtml);
//                System.out.println("【Favicon拦截器】✅ 成功插入图标标签，已返回修改后的doc.html");
//            } catch (Exception e) {
//                System.out.println("【Favicon拦截器】❌ 处理doc.html失败：" + e.getMessage());
//                // 异常兜底：返回原始doc.html，不影响页面访问
//                htmlWrapper.writeModifiedHtml(htmlWrapper.getOriginalHtml());
//            }
//        }
//    }
//}