package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.user.management.DelTeamService;
import com.team.backend.service.user.management.UpdateRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DelTeamServiceImpl implements DelTeamService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    UpdateRoleService updateRoleService;

    @Override
    public Result delTeam(String studentNo) {
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("student_no",studentNo);
        User user = userMapper.selectOne(userQueryWrapper);
        if(user==null){
            return Result.build(null, ResultCodeEnum.USER_NOT_EXIST);
        }

        if(user.getRole()!=2){
            return Result.build(null,ResultCodeEnum.USER_ROLE_WRONG);
        }

        //1.撤掉该用户role
        updateRoleService.updateRole(studentNo,"3");

        //2.撤销该用户下所有组员的leader_no
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("leader_no",studentNo);
        updateWrapper.set("leader_no",0);

        userMapper.update(null,updateWrapper);

        return Result.success(null);
    }
}
