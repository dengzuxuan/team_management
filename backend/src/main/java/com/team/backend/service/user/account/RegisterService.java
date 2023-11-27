package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.RegisterUserType;
import com.team.backend.utils.common.excelType.UserType;

public interface RegisterService {
    Result register(UserType user);
    Result registermore(UserType[] users);
}
