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

import static com.team.backend.utils.common.consts.roleConst.*;

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
        if(adminUser.getRole()!=ADMINROLE){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        Map<String,Object> m1 = new HashMap<>();
        User userFind = getSingleInfo(StudentNo);

        if(userFind.getRole()==LEADERROLE && !Objects.equals(userFind.getAdminNo(), adminUser.getStudentNo())){
            return Result.build(null,ResultCodeEnum.USER_ADMIN_WRONG);
        }

        if(userFind.getRole() == MEMBERROLE){
            m1.put("leader_infos",null);
            m1.put("member_infos",null);
            return Result.success(m1);
        }

        if(userFind.getRole()==LEADERROLE){
            //组长
            m1.put("leader_infos",userFind);

            //组员
            QueryWrapper<User> queryWrapperMember = new QueryWrapper<>();
            queryWrapperMember.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("leader_id",userFind.getLeaderId());
            m1.put("member_infos",userMapper.selectMaps(queryWrapperMember));

        }else if(userFind.getRole()==TEAMMEMBERROLE){

            QueryWrapper<User> queryWrapperMember = new QueryWrapper<>();
            queryWrapperMember.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("leader_id",userFind.getLeaderId());
            m1.put("member_infos",userMapper.selectMaps(queryWrapperMember));

            //组长
            QueryWrapper<User> queryWrapperLeader = new QueryWrapper<>();
            queryWrapperLeader.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("id",userFind.getLeaderId());
            m1.put("leader_infos",userMapper.selectOne(queryWrapperLeader));
        }


        return Result.success(m1);
    }

    @Override
    public Result getTeamMember() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<User> queryWrapperMember = new QueryWrapper<>();
        queryWrapperMember.select(
                User.class,info->!info.getColumn().equals("password_real")
                        && !info.getColumn().equals("password")
        ).eq("leader_no",user.getStudentNo());

        List<User> members = userMapper.selectList(queryWrapperMember);

        return Result.success(members);
    }

    private User getSingleInfo(String StudentNo){
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(
                User.class,info->!info.getColumn().equals("password_real")
                        && !info.getColumn().equals("password")
        ).eq("student_no",StudentNo);
        return userMapper.selectOne(queryWrapper);
    }
}
