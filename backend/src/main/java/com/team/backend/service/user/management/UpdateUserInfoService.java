package com.team.backend.service.user.management;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.UpdateUserType;
import com.team.backend.utils.common.excelType.UserType;

public interface UpdateUserInfoService {
    Result updateUserInfo(UpdateUserType user);
}
