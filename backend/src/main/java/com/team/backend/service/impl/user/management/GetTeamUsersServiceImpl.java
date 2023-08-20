package com.team.backend.service.impl.user.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.management.GetTeamUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 根据组员/组长StudentNo返回所属组的成员信息
 */
@Service
public class GetTeamUsersServiceImpl implements GetTeamUsersService {
    @Autowired
    UserMapper userMapper;
    @Override
    public Result getTeamUsers(String StudentNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();
        if(adminUser.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        Map<String,Object> m1 = new HashMap<>();
        User userFind = getSingleInfo(StudentNo);

        if(userFind.getRole()==2 && !Objects.equals(userFind.getAdminNo(), adminUser.getStudentNo())){
            return Result.build(null,ResultCodeEnum.USER_ADMIN_WRONG);
        }

        if("".equals(userFind.getLeaderNo()) && userFind.getRole()!=2){
            m1.put("leader_infos",null);
            m1.put("member_infos",null);
            return Result.success(m1);
        }

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
        queryWrapper.select("student_no","username","role","admin_no","leader_no").eq("student_no",StudentNo);
        return userMapper.selectOne(queryWrapper);
    }
}
