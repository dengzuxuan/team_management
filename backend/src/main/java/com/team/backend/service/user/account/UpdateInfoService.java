package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;

public interface UpdateInfoService {
    Result updateInfo(String email,String photo,String phone);
}
