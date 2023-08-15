package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;

public interface UpdatePasswordService {
    Result updatePassword(String oldPassword,String newPassword,String confirmedPassword);
}
