package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.management.AddMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 新增组员
 */
@Service
public class AddMemberServiceImpl implements AddMemberService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Result addMember(String leaderStudentNo, String[] memberStudentNos) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();

        if(adminUser.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("student_no",leaderStudentNo);
        User leaderUser= userMapper.selectOne(queryWrapper1);

        if(!Objects.equals(leaderUser.getAdminNo(), adminUser.getStudentNo())){
            return Result.build(null,ResultCodeEnum.USER_ADMIN_WRONG);
        }

        for(String memberStudentNo:memberStudentNos){
            QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("student_no",memberStudentNo);
            User memberUser= userMapper.selectOne(queryWrapper1);
            if(leaderUser==null ||memberUser==null){
                return Result.build(null,ResultCodeEnum.USER_NAME_NOT_EXIST);
            }
        }

        if(!Objects.equals(adminUser.getStudentNo(), leaderUser.getAdminNo())){
            return Result.build(null,ResultCodeEnum.USER_ADMIN_WRONG);
        }

        for(String memberStudentNo:memberStudentNos){
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("student_no",memberStudentNo);
            updateWrapper.set("leader_no",leaderStudentNo);

            userMapper.update(null,updateWrapper);
        }
        return Result.success(null);
    }
}
