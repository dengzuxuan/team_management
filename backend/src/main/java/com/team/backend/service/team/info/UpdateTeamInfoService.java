package com.team.backend.service.team.info;

import com.team.backend.config.result.Result;

public interface UpdateTeamInfoService {
    Result updateTeamLeader(String oldStudentNo,String newStudentNo);

    Result updateTeamInfo(String id,String teamName);
}
