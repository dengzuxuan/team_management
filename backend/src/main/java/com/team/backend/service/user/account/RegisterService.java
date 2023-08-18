package com.team.backend.service.user.account;

import com.team.backend.config.result.Result;

public interface RegisterService {
    Result register(String studentNo,String password,int role,String username);
}
