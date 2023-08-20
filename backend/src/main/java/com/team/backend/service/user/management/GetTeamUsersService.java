package com.team.backend.service.user.management;

import com.team.backend.config.result.Result;

public interface GetTeamUsersService {
    Result getTeamUsers(String StudentNo);


    Result getTeamMember();
}
