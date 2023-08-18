package com.team.backend.service.team.info;

import com.team.backend.config.result.Result;

public interface UpdateTeamInfoService {
    Result updateTeamLeader(String oldStudentNo,String newStudentNo);

    Result updateTeamInfo(int id,String teamName,String description,String performance,String task);
}
