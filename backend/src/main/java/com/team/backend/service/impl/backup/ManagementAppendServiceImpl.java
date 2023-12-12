package com.team.backend.service.impl.backup;

import com.team.backend.config.RemoteConfig;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.service.backup.ManagementAppendService;
import com.team.backend.utils.remote.FtpUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * <p>
 *  TODO
 * </p>
 *
 * @author Colin
 * @since 2023/12/12
 */
@Service
public class ManagementAppendServiceImpl implements ManagementAppendService {
    @Resource
    RemoteConfig remoteConfig;
    @Override
    public Result addAppend(MultipartFile file) {
        String APPEND_PREFIX = "http://"+remoteConfig.getIp()+":"+remoteConfig.getPort()+"/append/";
        String fileName = System.currentTimeMillis()+"_"+file.getOriginalFilename();
        try {
            //上传文件
            byte[] fileBytes = file.getBytes();
            FtpUtils.sshSftp(remoteConfig,fileBytes,fileName);

        }  catch (Exception e) {
            return Result.build("上传文件失败，错误信息:"+e, ResultCodeEnum.APPEND_FILE_INPUT_WRONG);
        }

        Map<String,String> map = new HashMap<>();
        map.put("photo",APPEND_PREFIX+fileName);
        return Result.success(map);
    }
}
