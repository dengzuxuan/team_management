package com.team.backend.service.backup;

import com.team.backend.config.result.Result;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 *  管理tomcat附件
 * </p>
 *
 * @author Colin
 * @since 2023/12/12
 */
public interface ManagementAppendService {
    Result addAppend(MultipartFile file);
}
