package com.zyh.easyapplyresume.qiniuoss;

import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 七牛云 OSS基础配置
 * @author shiningCloud2025
 */
@Configuration
@Data
public class QiNiuOssConfig {

    @Value("${qiniu.access-key}")
    private String accessKey;

    @Value("${qiniu.secret-key}")
    private String secretKey;

    // 公有 bucket（可选）
    @Value("${qiniu.bucket-public:}")
    private String bucketPublic;

    // 私有 bucket（建议用来放简历等）
    @Value("${qiniu.bucket-private}")
    private String bucketPrivate;

    // 公有加速域名（可选）
    @Value("${qiniu.domain-public:}")
    private String domainPublic;

    // 私有加速域名
    @Value("${qiniu.domain-private}")
    private String domainPrivate;

    /** 认证对象,用来生成上传/下载token **/
    @Bean
    public Auth qiniuAuth(){
        return Auth.create(accessKey, secretKey);
    }

    /** 上传客户端 **/
    @Bean
    public UploadManager uploadManager(){
        com.qiniu.storage.Configuration cfg = com.qiniu.storage.Configuration.create(Region.autoRegion());
        cfg.resumableUploadAPIVersion = com.qiniu.storage.Configuration.ResumableUploadAPIVersion.V2;
        return new UploadManager(cfg);
    }


}
