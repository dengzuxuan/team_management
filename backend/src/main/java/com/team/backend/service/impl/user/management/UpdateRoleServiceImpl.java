package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.management.UpdateRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.lang.Integer.parseInt;

@Service
public class UpdateRoleServiceImpl implements UpdateRoleService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Result updateRole(String studentNo,String role) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if (user.getRole() != 1) {
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        if(!isNumeric(role)||parseInt(role)!=2 && parseInt(role)!=3){
            return Result.build(null, ResultCodeEnum.ROLE_PARAM_WRONG);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",studentNo);
        User user1 = userMapper.selectOne(queryWrapper);
        if(user1==null){
            return Result.build(null, ResultCodeEnum.USER_NAME_NOT_EXIST);
        }

        if(user1.getLeaderNo()!=0 && parseInt(role)==2 ){
            return Result.build(null, ResultCodeEnum.USER_ALREADY_IN_TEAM);
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("student_no",studentNo);
        updateWrapper.set("role",parseInt(role));
        userMapper.update(null,updateWrapper);

        return Result.success(null);
    }
    public static boolean isNumeric(String str){
        for (int i = 0; i < str.length(); i++) {
            if(!Character.isDigit(str.charAt(i))){
                return false;
            }
        }
        return true;
    }

}