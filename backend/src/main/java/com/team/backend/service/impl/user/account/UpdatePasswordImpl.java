package com.team.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.UpdatePasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static com.team.backend.utils.common.consts.roleConst.ADMINROLE;

@Service
public class UpdatePasswordImpl implements UpdatePasswordService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Result updatePassword(String oldPassword,String newPassword, String confirmedPassword) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user =  loginUser.getUser();

        if(oldPassword.length()==0 || confirmedPassword.length()==0||newPassword.length()==0){
            return Result.build(null, ResultCodeEnum.PASSWORD_NOT_EMPTY);
        }
        if(newPassword.length()<6){
            return Result.build(null,ResultCodeEnum.PASSWORD_PARAM_WRONG);
        }
        if(!newPassword.equals(confirmedPassword)){
            return Result.build(null,ResultCodeEnum.PASSWORD_NOT_EQUAL);
        }
        if(!user.getPasswordReal().equals(oldPassword)){
            return Result.build(null,ResultCodeEnum.USER_PASSWORD_WRONG);
        }
        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setPasswordReal(newPassword);

        userMapper.updateById(user);

        return Result.success(null);
    }

    @Override
    public Result adminUpdatePassword(String studentNo, String newPassword) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User loginuser =  loginUser.getUser();
        if(loginuser.getRole() != ADMINROLE){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        if(newPassword.isEmpty()){
            return Result.build(null,ResultCodeEnum.USER_PASSWORD_WRONG);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",studentNo);
        User user = userMapper.selectOne(queryWrapper);
        if(user == null){
            return Result.build(null,ResultCodeEnum.USER_NAME_NOT_EXIST);
        }

        if(!Objects.equals(user.getAdminNo(), loginuser.getStudentNo())){
            return Result.build(null,ResultCodeEnum.USER_ADMIN_WRONG);
        }

        String encodedPassword = passwordEncoder.encode(newPassword);

        user.setPassword(encodedPassword);
        user.setPasswordReal(newPassword);

        userMapper.updateById(user);
        return Result.success(null);
    }
}
