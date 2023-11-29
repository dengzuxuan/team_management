package com.team.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.RoleMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.Role;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.LoginService;
import com.team.backend.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Result login(String studentNo, String password) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",studentNo);
        User userCheck = userMapper.selectOne(queryWrapper);
        Map<String,Object> map = new HashMap<>();
        if(userCheck==null){
            return Result.build(map, ResultCodeEnum.USER_NOT_EXIST);
        }

        if(!Objects.equals(userCheck.getPasswordReal(), password)){
            return Result.build(map, ResultCodeEnum.USER_PASSWORD_WRONG);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userCheck.getStudentNo(),password);

        UserDetailsImpl loginUser = null;
        try {
            // 可能会抛出异常的代码块
            Authentication authenicate = authenticationManager.authenticate(authenticationToken);//登录失败会自动处理
            loginUser = (UserDetailsImpl) authenicate.getPrincipal();
        } catch (Exception e) {
            // 捕获并处理异常
            return Result.build(e.getMessage(),ResultCodeEnum.USER_LOGIN_WRONG);
        }


        User user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());

        map.put("token",jwt);
        map.put("role",user.getRole());
        return Result.success(map);
    }
}
