package com.example.cr.cs.controller.admin;

import com.aliyun.oss.OSS;
import com.example.cr.common.response.R;
import com.example.cr.common.util.SnowflakeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("admin/file")
public class FileAdminController {
    private static final Logger log = LoggerFactory.getLogger(FileAdminController.class);

    @Autowired
    private OSS ossClient;
    
    @Value("${alibaba.cloud.oss.bucket}")
    private String bucket;
    
    @Value("${alibaba.cloud.oss.endpoint}")
    private String endpoint;

    @PostMapping("upload")
    public R<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String originalFilename = file.getOriginalFilename();
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
            String fileType = detectFileType(file.getContentType());
            String objectName = fileType + "/" + SnowflakeUtil.getId() + suffix;
            ossClient.putObject(bucket, objectName, file.getInputStream());
            String url = String.format("https://%s.%s/%s", bucket, endpoint, objectName);
            
            return R.ok(url);
        } catch (Exception e) {
            log.error("文件上传失败：{}", e.getMessage());
            return R.fail("上传失败");
        }
    }

    private String detectFileType(String contentType) {
        if (contentType.startsWith("video/")) {
            return "video";
        } else if (contentType.startsWith("image/")) {
            return "image";
        } else if (contentType.startsWith("audio/")) {
            return "audio";
        } else if (contentType.startsWith("application/pdf")) {
            return "pdf";
        } else if (contentType.startsWith("application/msword") || contentType.startsWith("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
            return "word";
        } else {
            return "other";
        }
    }
}
