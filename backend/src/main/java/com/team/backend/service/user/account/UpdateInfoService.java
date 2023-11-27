package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.MemberChangeType;
import com.team.backend.utils.common.excelType.UserType;

public interface UpdateInfoService {
    Result updateInfo(UserType user);

    Result updateStudentNo(MemberChangeType memberChangeInfo);
}
