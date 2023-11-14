package com.team.backend.service.team.info;

import com.team.backend.config.result.Result;
import org.springframework.web.multipart.MultipartFile;

public interface AddTeamService {
    Result addTeamService(String leaderNo);
    Result addTeamExcelService(MultipartFile file);
}
