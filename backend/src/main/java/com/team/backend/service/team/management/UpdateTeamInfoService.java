package com.team.backend.service.team.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.TeamInfoType;

public interface UpdateTeamInfoService {
    Result updateTeamLeader(String oldStudentNo,String newStudentNo);

    Result updateTeamInfo(TeamInfoType info);
}
