package com.team.backend.service.team.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.TeamInfoType;
import org.springframework.web.multipart.MultipartFile;

public interface AddTeamService {
    Result addTeamService(String leaderNo);

    Result addTeamMoreService(TeamInfoType[] teamInfos);
    Result addTeamExcelService(MultipartFile file);
}
