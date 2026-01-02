package com.zyh.easyapplyresume.qiniuoss;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 * OSS 文件服务接口
 * @author shiningCloud2025
 */
public interface OssService {

    /**
     * 上传文件到七牛 OSS
     */
    String upload(MultipartFile file,
                  OssSystemTypeEnum systemType,
                  OssBusinessType businessType,
                  Integer ownerId,  // 改成 Integer
                  boolean isPrivate);

    /**
     * 根据 URL 删除文件
     */
    void deleteByUrl(String url, boolean isPrivate);

    /**
     * 查询某个用户在某个业务类型下的所有文件
     */
    List<String> listFilesByOwner(OssSystemTypeEnum systemType,
                                  OssBusinessType businessType,
                                  Integer ownerId,  // 改成 Integer
                                  boolean isPrivate);

    /**
     * 生成文件预览 URL（自动判断公有/私有）
     */
    String getPreviewUrl(String url, boolean isPrivate);

    /**
     * 生成文件预览 URL（自定义有效期）
     */
    String getPreviewUrl(String url, boolean isPrivate, long expireInSeconds);
    /**
     * 生成文件的临时下载链接（公有/私有都支持）
     * @param url 文件URL
     * @param isPrivate 是否私有文件
     * @param expireInSeconds 有效期（秒），私有文件需要，公有文件忽略此参数
     * @return 下载链接
     */
    String generateDownloadUrl(String url, boolean isPrivate, long expireInSeconds);
}