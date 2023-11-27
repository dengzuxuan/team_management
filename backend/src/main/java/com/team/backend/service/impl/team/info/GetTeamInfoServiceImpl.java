package com.team.backend.service.impl.team.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.team.info.GetTeamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.team.backend.utils.common.consts.roleConst.*;

@Service
public class GetTeamInfoServiceImpl implements GetTeamInfoService {
    @Autowired
    TeamInfoMapper teamInfoMapper;
    @Autowired
    UserMapper userMapper;
    /**
     * 管理员获取到的信息是旗下所有组的信息 （管理员需要传入studentNO）
     * 组长及组员获取到的信息是所在组的信息 （组长及组员不需要传入参数）
     * **/
    @Override
    public Result getTeamInfo(String StudentNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Integer userid = null;
        if(StudentNo==null){
            if(user.getRole()==LEADERROLE){ //组长
                userid = user.getId();
            }else if(user.getRole()==TEAMMEMBERROLE){ //组员
                userid = user.getLeaderId();
            } else if(user.getRole()==ADMINROLE){
                return Result.build(null,ResultCodeEnum.PARAMS_WRONG);
            }
        }else{
            QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.eq("student_no",StudentNo);
            User user1 = userMapper.selectOne(queryWrapper1);
        }


        QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("leader_id",userid);
        TeamInfo teamInfos = teamInfoMapper.selectOne(queryWrapper);
        return Result.success(teamInfos);
    }

    @Override
    public Result getTeamDetail() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        ArrayList<Object> teamDeatilInfos = new ArrayList<>();
        if(user.getRole() == ADMINROLE){
            QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("admin_no",user.getStudentNo());
            List<TeamInfo> teamInfos = teamInfoMapper.selectList(queryWrapper);

            for(TeamInfo teamInfo : teamInfos){
                User leaderUser = null;
                Map<String,Object> teamDeatilInfo = new HashMap<>();
                QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select(
                        User.class,info->!info.getColumn().equals("password_real")
                        && !info.getColumn().equals("password")
                ).eq("leader_id",teamInfo.getLeaderId());
                List<User> teamMembers = userMapper.selectList(queryWrapper1);

                //加上组长
                if(teamInfo.getLeaderId()!=null){
                    QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.select(
                            User.class,info->!info.getColumn().equals("password_real")
                                    && !info.getColumn().equals("password")
                    ).eq("id",teamInfo.getLeaderId());
                    leaderUser = userMapper.selectOne(queryWrapper2);
                }
                teamDeatilInfo.put("members",teamMembers);
                teamDeatilInfo.put("leader",leaderUser);
                teamDeatilInfo.put("teamname",teamInfo.getTeamname());
                teamDeatilInfo.put("no",teamInfo.getNo());
                teamDeatilInfos.add(teamDeatilInfo);
            }
        }else {
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
            //组长角色
//            Map<String,Object> teamDeatilInfo = new HashMap<>();
//            QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("leader_no",user.getStudentNo());
//            TeamInfo teamInfo = teamInfoMapper.selectOne(queryWrapper);
//
//            QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
//            queryWrapper1.select(
//                    User.class,info->!info.getColumn().equals("password_real")
//                            && !info.getColumn().equals("password")
//            ).eq("leader_no",user.getStudentNo());
//            List<User> teamMembers = userMapper.selectList(queryWrapper1);
//
//            teamDeatilInfo.put("userinfos",teamMembers);
//            teamDeatilInfo.put("teaminfo",teamInfo);
//            teamDeatilInfos.put(teamInfo.getTeamname(),teamDeatilInfo);
        }
        return Result.success(teamDeatilInfos);
    }

    @Override
    public Result getTeamUserInfo(String no) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(user.getRole()!=ADMINROLE && user.getRole()!=LEADERROLE){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.select(
                User.class,info->!info.getColumn().equals("password_real")
                        && !info.getColumn().equals("password")
        ).eq("team_no",no);
        List<User> users = userMapper.selectList(queryWrapper);
        return Result.success(users);
    }

    @Override
    public Result getAllTeamInfos(int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(user.getRole()!=ADMINROLE){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        Page<TeamInfo> page = new Page<>(pageNum,pageSize);
        QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
        Page<TeamInfo> rowPages = new Page<>();

        queryWrapper.eq("admin_no",user.getStudentNo());
        rowPages = teamInfoMapper.selectPage(page,queryWrapper);
        List<TeamInfo> teamInfos = rowPages.getRecords();

        for(TeamInfo teamInfo:teamInfos){

        }
        return null;
    }
}
