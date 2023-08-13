package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;

public interface GetListService {
    Result getList(String range,int pageNum,int pageSize);
}
