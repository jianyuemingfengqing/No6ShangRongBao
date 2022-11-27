package com.learn.oss.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.learn.common.exception.BusinessException;
import com.learn.common.result.ResponseEnum;
import com.learn.oss.config.OssProperties;
import com.learn.oss.service.OssService;
import org.joda.time.DateTime;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;

@Service
public class OssServiceImpl implements OssService {
    String endpoint = OssProperties.ENDPOINT;
    String accessKeyId = OssProperties.ACCESSKEYID;
    String accessKeySecret = OssProperties.ACCESSKEYSECRET;
    String bucketName = OssProperties.BUCKETNAME;

    @Override
    public String upload(MultipartFile file, String module) {
//        String filePath = "J:\\img\\1.jpeg";
//        String module = "imgs";

        String filePath = "J:\\img\\" + file.getOriginalFilename(); // 获取文件名

        String objectName = module + new DateTime().toString("/yyyy/MM/dd/")//时间戳
                                    + System.currentTimeMillis()
                                    + "_"
                                    + UUID.randomUUID().toString().substring(0, 6) //6位的uuid
                                    + filePath.substring(filePath.lastIndexOf(".")); // 文件后缀
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            InputStream inputStream = new FileInputStream(filePath);
            // 创建PutObject请求。
            ossClient.putObject(bucketName, objectName, inputStream);
            String imgUrl = "http://" + bucketName + ".oss-cn-shanghai.aliyuncs.com/" + objectName;
            System.out.println(imgUrl);
            return imgUrl;
        } catch (Exception oe) {
            System.out.println(oe);
            throw new BusinessException(ResponseEnum.UPLOAD_ERROR, oe);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Override
    public void deleteFile(String path) {
//        String imgUrl = "http://jianyueming-test.oss-cn-shanghai.aliyuncs.com/imgs/2022/11/27/1669556112587_1e8bb0.jpeg";
//        String objectName = "exampleobject.txt";
        String imgUrl = path;
        String objectName = imgUrl.substring(imgUrl.indexOf("http://" + bucketName + ".oss-cn-shanghai.aliyuncs.com/")
                + ("http://" + bucketName + ".oss-cn-shanghai.aliyuncs.com/").length());

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, objectName);
        } catch (Exception oe) {
            throw new BusinessException(ResponseEnum.OSS_DELETE_ERROR ,oe);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
