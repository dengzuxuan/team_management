package com.team.backend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.RoleMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.Role;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.LoginService;
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
    @Autowired
    private RoleMapper roleMapper;
    @Override
    public Result login(String studentNo, String password) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",studentNo);
        User userCheck = userMapper.selectOne(queryWrapper);

        Map<String,String> map = new HashMap<>();
        if(userCheck==null){
            return Result.build(map, ResultCodeEnum.USER_NOT_EXIST);
        }

        if(!Objects.equals(userCheck.getPasswordReal(), password)){
            return Result.build(map, ResultCodeEnum.USER_PASSWORD_WRONG);
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userCheck.getUsername(),password);
        Authentication authenicate = authenticationManager.authenticate(authenticationToken);//登录失败会自动处理
        //System.out.println("getDetails"+authenicate.getDetails());
        UserDetailsImpl loginUser = (UserDetailsImpl) authenicate.getPrincipal();

        User user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());

        QueryWrapper<Role> queryWrapperRole = new QueryWrapper<>();
        queryWrapperRole.eq("role_id",user.getRole());

        map.put("token",jwt);
        map.put("role",roleMapper.selectOne(queryWrapperRole).getRoleName());
        return Result.success(map);
    }
}
