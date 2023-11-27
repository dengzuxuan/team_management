package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;

public interface GetListService {
    Result getList(int pageNum,int pageSize);
}
