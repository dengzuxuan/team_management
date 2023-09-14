package com.team.backend.service.impl.report.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.info.GetUserInfosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetUserInfosServiceImpl implements GetUserInfosService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Result getUserInfos() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(user.getRole()!=1 && user.getRole()!=2){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(user.getRole()==1){
            //管理员
            queryWrapper.eq("admin_no",user.getStudentNo()).ne("role",1).select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            );
        }else {
            //组长
            queryWrapper.eq("leader_no",user.getStudentNo()).ne("role",1).select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            );
        }

        List<User> users = userMapper.selectList(queryWrapper);
        return Result.success(users);
    }
}
