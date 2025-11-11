package com.zyh.easyapplyresume.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

/**
 * 最高优先级Filter，确保提前拦截doc.html
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FaviconFilter implements Filter {

    private static final String FAVICON_TAG = "<link rel=\"icon\" href=\"/api/favicon.ico\" type=\"image/x-icon\">";
    private static final String DOC_HTML_PATH = "/api/doc.html";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // 只拦截doc.html请求
        if (!DOC_HTML_PATH.equals(req.getRequestURI())) {
            chain.doFilter(request, response);
            return;
        }

        System.out.println("✅ FaviconFilter 开始拦截 /api/doc.html");

        // 双流包装器：同时捕获字节流和字符流
        DualStreamResponseWrapper wrapper = new DualStreamResponseWrapper(resp);
        chain.doFilter(request, wrapper);

        // 拿到原始HTML内容
        String originalHtml = wrapper.getHtmlContent();
        if (originalHtml.isEmpty()) {
            System.out.println("⚠️ 仍未捕获到内容，请检查安全配置是否拦截了请求");
            chain.doFilter(request, response);
            return;
        }

        System.out.println("✅ 成功捕获doc.html，长度：" + originalHtml.length());

        // 插入图标标签（兼容各种<head>格式）
        String modifiedHtml = originalHtml.replaceAll("<head(\\s*|\\s+[^>]*?)>", "<head$1>\n" + FAVICON_TAG);

        // 写回浏览器（强制HTML类型和UTF-8编码）
        resp.setContentType("text/html;charset=UTF-8");
        resp.setContentLength(modifiedHtml.getBytes(StandardCharsets.UTF_8).length);
        resp.getOutputStream().write(modifiedHtml.getBytes(StandardCharsets.UTF_8));
        resp.getOutputStream().flush();

        System.out.println("✅ 图标标签插入成功！");
    }

    // 双流包装器：确保捕获所有类型的响应流
    static class DualStreamResponseWrapper extends HttpServletResponseWrapper {
        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        private final CharArrayWriter caw = new CharArrayWriter();
        private ServletOutputStream sos;
        private PrintWriter pw;

        public DualStreamResponseWrapper(HttpServletResponse response) {
            super(response);
            this.sos = new ByteArrayServletOutputStream(bos);
            this.pw = new PrintWriter(caw);
        }

        @Override
        public ServletOutputStream getOutputStream() throws IOException {
            return sos;
        }

        @Override
        public PrintWriter getWriter() throws IOException {
            return pw;
        }

        public String getHtmlContent() throws IOException {
            pw.flush();
            sos.flush();
            if (caw.size() > 0) {
                return caw.toString();
            } else {
                return new String(bos.toByteArray(), StandardCharsets.UTF_8);
            }
        }

        static class ByteArrayServletOutputStream extends ServletOutputStream {
            private final ByteArrayOutputStream bos;

            public ByteArrayServletOutputStream(ByteArrayOutputStream bos) {
                this.bos = bos;
            }

            @Override
            public void write(int b) throws IOException {
                bos.write(b);
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(WriteListener listener) {}
        }
    }
}