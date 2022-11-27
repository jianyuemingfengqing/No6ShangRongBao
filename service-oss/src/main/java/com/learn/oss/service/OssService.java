package com.learn.oss.service;

import org.springframework.web.multipart.MultipartFile;

public interface OssService {
    String upload(MultipartFile file, String module);
    void deleteFile(String path);

}
