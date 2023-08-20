package com.team.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Integer.parseInt;

@Service
public class RegisterServiceImpl implements RegisterService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Result register(String studentNo,String password,int role,String username) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();

        String adminNo = "";
        if(username==null){
            username="";
        }
        //23125205
        if(studentNo==null){
            return Result.build(null, ResultCodeEnum.USER_NAME_NOT_EMPTY);
        }
        if(password==null){
            password = studentNo.substring(2);
        }
        if(role==0){
            role=3;
            adminNo = adminUser.getStudentNo();
        }
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",studentNo);
        List<User> users = userMapper.selectList(queryWrapper);
        if(!users.isEmpty()){
            return Result.build(null, ResultCodeEnum.USER_NAME_ALREADY_EXIST);
        }
        String encodedPassword = passwordEncoder.encode(password);
        String defaultPhoto = "http://team-manager.oss-cn-beijing.aliyuncs.com/avatar/default.png";
        Date now = new Date();
        User user = new User(null,"",adminNo,username,encodedPassword,null,null,role,defaultPhoto,studentNo,password,now,now);
        userMapper.insert(user);
        return Result.success(null);
    }
}
