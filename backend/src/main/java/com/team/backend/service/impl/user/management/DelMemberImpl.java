package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.management.DelMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class DelMemberImpl implements DelMemberService {

    @Autowired
    UserMapper userMapper;
    @Override
    public Result delTeamMember(String StudentNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        if(loginUser.getUser().getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",StudentNo);
        User user = userMapper.selectOne(queryWrapper);

        if(user==null){
            return Result.build(null,ResultCodeEnum.USER_NAME_NOT_EXIST);
        }

        if(user.getRole()!=3){
            return Result.build(null,ResultCodeEnum.USER_IS_LEADER);
        }

        if(user.getLeaderNo()==0){
            return Result.build(null,ResultCodeEnum.USER_NOT_IN_TEAM);
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("student_no",user.getStudentNo());
        updateWrapper.set("leader_no",0);

        userMapper.update(null,updateWrapper);

        return Result.success(null);
    }
}
