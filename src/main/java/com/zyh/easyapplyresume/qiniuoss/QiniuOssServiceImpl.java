package com.zyh.easyapplyresume.qiniuoss;

import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.FileInfo;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 七牛 OSS(基于七牛) 服务实现
 * @author shiningCloud2025
 */
@Service
public class QiniuOssServiceImpl implements OssService{

    @Autowired
    private QiNiuOssConfig qiNiuOssConfig;

    @Autowired
    private Auth auth;

    @Autowired
    private UploadManager uploadManager;


    @Value("${spring.profiles.active}")
    private String env;


    @Override
    public String upload(MultipartFile file, OssSystemTypeEnum systemType, OssBusinessType businessType, Integer ownerId, boolean isPrivate) {
        String key = buildKey(file, systemType, businessType, ownerId);

        String bucket = isPrivate ? qiNiuOssConfig.getBucketPrivate() : qiNiuOssConfig.getBucketPublic();
        String domain = isPrivate ? qiNiuOssConfig.getDomainPrivate() : qiNiuOssConfig.getDomainPublic();
        String upToken = auth.uploadToken(bucket);

        try {
            Response response = uploadManager.put(
                    file.getInputStream(), key, upToken, null, null
            );
            if (!response.isOK()) {
                throw new RuntimeException("上传文件到服务器失败: " + response.bodyString());
            }
        } catch (Exception e) {
            throw new RuntimeException("上传文件到服务器失败", e);
        }

        return domain + "/" + key;

    }

    /**
     * 生成 key：env/systemType/businessType/ownerId/yyyy/MM/dd/uuid.ext
     */
    private String buildKey(MultipartFile file,
                            OssSystemTypeEnum systemType,
                            OssBusinessType businessType,
                            Integer ownerId) {
        String originalFilename = file.getOriginalFilename();
        String ext = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        LocalDate now = LocalDate.now();
        String uuid = UUID.randomUUID().toString().replace("-", "");

        // 如果 ownerId 为 0，表示公共资源，key 中用 "public" 代替
        String ownerPath = (ownerId == 0) ? "public" : String.valueOf(ownerId);

        return String.format("%s/%s/%s/%d/%d/%02d/%02d/%s%s",
                env.toLowerCase(),
                systemType.getDir(),
                businessType.getDir(),
                ownerId,
                now.getYear(),
                now.getMonthValue(),
                now.getDayOfMonth(),
                uuid,
                ext
        );
    }

    @Override
    public void deleteByUrl(String url, boolean isPrivate) {
        String bucket = isPrivate ? qiNiuOssConfig.getBucketPrivate() : qiNiuOssConfig.getBucketPublic();
        String domain = isPrivate ? qiNiuOssConfig.getDomainPrivate() : qiNiuOssConfig.getDomainPublic();

        // 从 URL 中解析出 key
        String key = url.replace(domain + "/", "");

        try {
            BucketManager bucketManager = new BucketManager(auth, new com.qiniu.storage.Configuration());
            bucketManager.delete(bucket, key);
        } catch (Exception e) {
            throw new RuntimeException("删除文件失败", e);
        }
    }

    @Override
    public List<String> listFilesByOwner(OssSystemTypeEnum systemType, OssBusinessType businessType, Integer ownerId, boolean isPrivate) {
        String bucket = isPrivate ? qiNiuOssConfig.getBucketPrivate() : qiNiuOssConfig.getBucketPublic();
        String domain = isPrivate ? qiNiuOssConfig.getDomainPrivate() : qiNiuOssConfig.getDomainPublic();

        // 构建前缀：dev/admin/admin_avatar/1/
        String prefix = String.format("%s/%s/%s/%d/",
                env.toLowerCase(),
                systemType.getDir(),
                businessType.getDir(),
                ownerId);

        try {
            BucketManager bucketManager = new BucketManager(auth, new com.qiniu.storage.Configuration());

            // 列举指定前缀的文件
            BucketManager.FileListIterator iterator =
                    bucketManager.createFileListIterator(bucket, prefix, 1000, null);

            List<String> urls = new ArrayList<>();
            while (iterator.hasNext()) {
                FileInfo[] items = iterator.next();
                for (FileInfo item : items) {
                    urls.add(domain + "/" + item.key);
                }
            }
            return urls;

        } catch (Exception e) {
            throw new RuntimeException("查询文件列表失败", e);
        }
    }

    @Override
    public String getPreviewUrl(String url, boolean isPrivate) {
        return getPreviewUrl(url, isPrivate, 3600); // 默认1小时
    }

    @Override
    public String getPreviewUrl(String url, boolean isPrivate, long expireInSeconds) {
        if (isPrivate) {
            // 私有文件：生成带签名的临时链接
            try {
                return auth.privateDownloadUrl(url, expireInSeconds);
            } catch (Exception e) {
                throw new RuntimeException("生成私有访问链接失败: " + e.getMessage());
            }
        } else {
            // 公开文件：直接返回原 URL
            return url;
        }
    }

    @Override
    public String generateDownloadUrl(String url, boolean isPrivate, long expireInSeconds) {
        try {
            String fileName = extractFileName(url);

            if (isPrivate) {
                // 私有文件：生成带签名的下载链接
                String downloadUrl = auth.privateDownloadUrl(url, expireInSeconds);

                // 添加 attname 参数强制下载
                if (downloadUrl.contains("?")) {
                    downloadUrl += "&attname=" + fileName;
                } else {
                    downloadUrl += "?attname=" + fileName;
                }
                return downloadUrl;

            } else {
                // 公有文件：直接在 URL 后加 attname 参数
                if (url.contains("?")) {
                    return url + "&attname=" + fileName;
                } else {
                    return url + "?attname=" + fileName;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("生成下载链接失败: " + e.getMessage());
        }
    }

    /**
     * 从 URL 中提取文件名
     */
    private String extractFileName(String url) {
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        // 去掉可能的查询参数
        if (fileName.contains("?")) {
            fileName = fileName.substring(0, fileName.indexOf("?"));
        }
        return fileName;
    }
}
