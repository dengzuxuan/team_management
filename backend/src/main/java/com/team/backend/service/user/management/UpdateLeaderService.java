package com.team.backend.service.user.management;

import com.team.backend.config.result.Result;

public interface UpdateLeaderService {
    Result updateLeader(String oldStudentNo,String newStudentNo);
}
