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

import java.util.List;

@Service
public class AddMemberServiceImpl implements AddMemberService {
    @Autowired
    UserMapper userMapper;

    @Override
    public Result addMember(String leaderStudentNo, String memberStudentNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("student_no",leaderStudentNo);
        List<User> users1= userMapper.selectList(queryWrapper1);

        QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.eq("student_no",memberStudentNo);
        List<User> users2= userMapper.selectList(queryWrapper1);

        if(users1.isEmpty()||users2.isEmpty()){
            return Result.build(null,ResultCodeEnum.USER_NAME_NOT_EXIST);
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("student_no",memberStudentNo);
        updateWrapper.set("leader_no",leaderStudentNo);

        userMapper.update(null,updateWrapper);
        return Result.success(null);
    }
}
