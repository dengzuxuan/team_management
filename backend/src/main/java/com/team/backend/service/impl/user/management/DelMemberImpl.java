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

import java.util.Objects;

import static com.team.backend.utils.common.consts.roleConst.ADMINROLE;
import static com.team.backend.utils.common.consts.roleConst.TEAMMEMBERROLE;

/**
 * 将组员踢出小组
 */
@Service
public class DelMemberImpl implements DelMemberService {

    @Autowired
    UserMapper userMapper;
    @Override
    public Result delTeamMember(String StudentNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();
        if(adminUser.getRole()!=ADMINROLE){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",StudentNo);
        User user = userMapper.selectOne(queryWrapper);

        if(user==null){
            return Result.build(null,ResultCodeEnum.USER_NAME_NOT_EXIST);
        }

        queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",user.getLeaderNo());
        User leaderUser = userMapper.selectOne(queryWrapper);

        if(!Objects.equals(leaderUser.getAdminNo(), adminUser.getStudentNo())){
            return Result.build(null,ResultCodeEnum.USER_ADMIN_WRONG);
        }


        if(user.getRole()!=TEAMMEMBERROLE){
            return Result.build(null,ResultCodeEnum.USER_IS_LEADER);
        }

        if("".equals(user.getLeaderNo())){
            return Result.build(null,ResultCodeEnum.USER_NOT_IN_TEAM);
        }

        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("student_no",user.getStudentNo());
        updateWrapper.set("leader_no","");

        userMapper.update(null,updateWrapper);

        return Result.success(null);
    }
}
