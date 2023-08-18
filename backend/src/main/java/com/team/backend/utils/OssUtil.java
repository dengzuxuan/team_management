package com.team.backend.utils;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class OssUtil {
    public static Result uploadOss(InputStream inputStream, String avatarName ) throws Exception {
        // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
        String endpoint = "https://oss-cn-beijing.aliyuncs.com";
        // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
        // RAM用户的访问密钥（AccessKey ID和AccessKey Secret）。
        String accessKeyId = "LTAI5tCq3h3XaPtSV4nTTAkL";
        String accessKeySecret = "8s4Za6MP73MuOLcdQOgsB0zqcMWrj8";
        // 使用代码嵌入的RAM用户的访问密钥配置访问凭证。
        CredentialsProvider credentialsProvider = new DefaultCredentialProvider(accessKeyId, accessKeySecret);// 填写Bucket名称，例如examplebucket。
        String bucketName = "team-manager";
        // 填写Object完整路径，完整路径中不能包含Bucket名称，例如exampledir/exampleobject.txt。
        String objectName = "avatar/"+avatarName;

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        Map<String, String> map = new HashMap<>();
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, objectName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            map.put("error_reason",oe.getErrorMessage());
            return Result.fail(map);
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            map.put("error_reason",ce.getMessage());
            return Result.fail(map);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
            inputStream.close();
        }
        String ossUrl = "http://team-manager.oss-cn-beijing.aliyuncs.com/avatar/"+avatarName;
        map.put("photo",ossUrl);
        return Result.success(map);
    }
}
