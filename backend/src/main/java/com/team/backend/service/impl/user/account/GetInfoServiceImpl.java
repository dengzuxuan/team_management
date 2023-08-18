package com.team.backend.service.impl.user.account;

import com.team.backend.config.result.Result;
import com.team.backend.mapper.RoleMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.GetInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class GetInfoServiceImpl implements GetInfoService {
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public Result getInfo() {
        User user = null;
        //获取上下文
        UsernamePasswordAuthenticationToken authentication =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authentication.getPrincipal();
        user = loginUser.getUser();

        Map<String, Object> map = new HashMap<>();
        map.put("username",user.getUsername());
        map.put("phone",user.getPhone());
        map.put("email",user.getEmail());
        map.put("role",user.getRole());
        map.put("avatar",user.getPhoto());
        map.put("studentNo",user.getStudentNo());

        return Result.success(map);
    }
}
