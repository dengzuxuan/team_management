package com.team.backend.service.user.management;

import com.team.backend.config.result.Result;

public interface AddMemberService {
    Result addMember(String leaderStudentNo,String memberStudentNo);
}
