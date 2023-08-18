package com.team.backend.service.user.management;

import com.team.backend.config.result.Result;

import java.util.List;

public interface AddMemberService {
    Result addMember(String leaderStudentNo, String[] memberStudentNo);
}
