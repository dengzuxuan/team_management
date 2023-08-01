package com.team.backend.service.user;

import java.util.Map;

public interface LoginService {
    Map<String,String> login(String studentNo,String password);
}
