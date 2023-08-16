package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.user.management.GetTeamUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetTeamUsersServiceImpl implements GetTeamUsersService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Result getTeamUsers(String StudentNo) {
        Map<String,Object> m1 = new HashMap<>();
        User userFind = getSingleInfo(StudentNo);
        if(userFind.getRole()==2){
            //组长
            m1.put("leader_infos",userFind);

            //组员
            QueryWrapper<User> queryWrapperMember = new QueryWrapper<>();
            queryWrapperMember.select("student_no","username").eq("leader_no",StudentNo);
            m1.put("member_infos",userMapper.selectMaps(queryWrapperMember));

        }else if(userFind.getRole()==3){
            //组员
            QueryWrapper<User> queryWrapperLeaderNo = new QueryWrapper<>();
            queryWrapperLeaderNo.select("leader_no","student_no").eq("student_no",StudentNo);
            User memberUser = userMapper.selectOne(queryWrapperLeaderNo);

            QueryWrapper<User> queryWrapperMember = new QueryWrapper<>();
            queryWrapperMember.select("student_no","username").eq("leader_no",memberUser.getLeaderNo());
            m1.put("member_infos",userMapper.selectMaps(queryWrapperMember));

            //组长
            QueryWrapper<User> queryWrapperLeader = new QueryWrapper<>();
            queryWrapperLeader.select("student_no","username").eq("student_no",memberUser.getLeaderNo());
            m1.put("leader_infos",userMapper.selectOne(queryWrapperLeader));
        }


        return Result.success(m1);
    }
    private User getSingleInfo(String StudentNo){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("student_no","username","role").eq("student_no",StudentNo);
        User user = userMapper.selectOne(queryWrapper);
        return user;
    }
}
