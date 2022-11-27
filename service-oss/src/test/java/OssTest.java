import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.BucketInfo;
import com.aliyun.oss.model.CannedAccessControlList;
import com.aliyun.oss.model.CreateBucketRequest;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.UUID;


public class OssTest {
    @Test
    public void testOss() {
// yourEndpoint填写Bucket所在地域对应的Endpoint。以华东1（杭州）为例，Endpoint填写为https://oss-cn-hangzhou.aliyuncs.com。
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
// 阿里云账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM用户进行API访问或日常运维，请登录RAM控制台创建RAM用户。
        String accessKeyId = "LTAI5tS9v3i2kDJQ6EUFnnmL";
        String accessKeySecret = "hvY1nVT5VuUwWeyN8dO6Tobo59aYGq";
// 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        BucketInfo jianyueming = ossClient.getBucketInfo("jianyueming");
        System.out.println(jianyueming);
// 关闭OSSClient。
        ossClient.shutdown();
    }

    String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
    String accessKeyId = "LTAI5tS9v3i2kDJQ6EUFnnmL";
    String accessKeySecret = "hvY1nVT5VuUwWeyN8dO6Tobo59aYGq";
    OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    String bucketName = "jianyueming-test";

    @Test
    public void testCreat() {
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
        String accessKeyId = "LTAI5tS9v3i2kDJQ6EUFnnmL";
        String accessKeySecret = "hvY1nVT5VuUwWeyN8dO6Tobo59aYGq";
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);


        try {
            // 创建CreateBucketRequest对象。
            String bucketName = "jianyueming-test";
            CreateBucketRequest createBucketRequest = new CreateBucketRequest(bucketName);

            // 如果创建存储空间的同时需要指定存储类型和数据容灾类型, 请参考如下代码。
            // 此处以设置存储空间的存储类型为标准存储为例介绍。
            //createBucketRequest.setStorageClass(StorageClass.Standard);
            // 数据容灾类型默认为本地冗余存储，即DataRedundancyType.LRS。如果需要设置数据容灾类型为同城冗余存储，请设置为DataRedundancyType.ZRS。
            //createBucketRequest.setDataRedundancyType(DataRedundancyType.ZRS);
            // 设置存储空间的权限为公共读，默认为私有。
            createBucketRequest.setCannedACL(CannedAccessControlList.PublicRead);

            // 创建存储空间。
            ossClient.createBucket(createBucketRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

    }

    @Test
    public void testUp() {

        String filePath = "J:\\img\\1.jpeg";
        String module = "imgs";

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
        } catch (Exception oe) {
            System.out.println(oe);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    @Test
    public void testDel() {
        // http://jianyueming-test.oss-cn-shanghai.aliyuncs.com/imgs/2022/11/27/1669556112587_1e8bb0.jpeg
        String imgUrl = "http://jianyueming-test.oss-cn-shanghai.aliyuncs.com/imgs/2022/11/27/1669556112587_1e8bb0.jpeg";
//        String objectName = "exampleobject.txt";
        String objectName = imgUrl.substring(imgUrl.indexOf("http://" + bucketName + ".oss-cn-shanghai.aliyuncs.com/")
                + ("http://" + bucketName + ".oss-cn-shanghai.aliyuncs.com/").length());

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        try {
            // 删除文件或目录。如果要删除目录，目录必须为空。
            ossClient.deleteObject(bucketName, objectName);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

}
