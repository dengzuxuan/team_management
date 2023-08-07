package com.team.backend.service.impl.user;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.mapper.UserMapper;
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
    @Override
    public Map<String, String> login(String studentNo, String password) {

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",studentNo);
        User userCheck = userMapper.selectOne(queryWrapper);

        Map<String,String> map = new HashMap<>();
        if(userCheck==null){
            map.put("message","用户名不存在");
            return map;
        }

        if(!Objects.equals(userCheck.getPasswordReal(), password)){
            map.put("message","密码错误");
            return map;
        }
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userCheck.getUsername(),password);
        Authentication authenicate = authenticationManager.authenticate(authenticationToken);//登录失败会自动处理
        System.out.println("getDetails"+authenicate.getDetails());
        UserDetailsImpl loginUser = (UserDetailsImpl) authenicate.getPrincipal();

        User user = loginUser.getUser();
        String jwt = JwtUtil.createJWT(user.getId().toString());


        map.put("message","success");
        map.put("token",jwt);
        return map;
    }
}
