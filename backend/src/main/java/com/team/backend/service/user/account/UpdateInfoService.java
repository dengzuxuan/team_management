package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.dto.req.MemberChangeType;

public interface UpdateInfoService {
    Result updateInfo(String email,String photo,String phone,String username);

    Result updateStudentNo(MemberChangeType memberChangeInfo);
}
