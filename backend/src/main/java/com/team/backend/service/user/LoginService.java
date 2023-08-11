package com.team.backend.service.user;

import com.team.backend.config.result.Result;

import java.util.Map;

public interface LoginService {
    Result login(String studentNo, String password);
}
