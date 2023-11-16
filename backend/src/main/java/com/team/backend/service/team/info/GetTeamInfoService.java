package com.team.backend.service.team.info;

import com.team.backend.config.result.Result;

public interface GetTeamInfoService {
    Result getTeamInfo(String StudentNo);
    Result getTeamDetail();
    Result getTeamUserInfo(String no);
    Result getAllTeamInfos(int pageNum,int pageSize);
}
