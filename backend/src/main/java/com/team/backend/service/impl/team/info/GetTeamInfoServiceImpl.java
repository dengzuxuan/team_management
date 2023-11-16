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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class GetTeamInfoServiceImpl implements GetTeamInfoService {
    @Autowired
    TeamInfoMapper teamInfoMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public Result getTeamInfo(String StudentNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(StudentNo==null){
            if(user.getRole()==2){ //组长
                StudentNo = user.getStudentNo();
            }else if(user.getRole()==3){ //组员
                StudentNo = user.getLeaderNo();
            } else if(user.getRole()==1){
                return Result.build(null,ResultCodeEnum.PARAMS_WRONG);
            }
        }
        QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("leader_no",StudentNo);
        TeamInfo teamInfos = teamInfoMapper.selectOne(queryWrapper);
        return Result.success(teamInfos);
    }

    @Override
    public Result getTeamDetail() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        Map<String,Object> teamDeatilInfo = new HashMap<>();

        if(user.getRole() == 1){//导师角色
            QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("admin_no",user.getStudentNo());
            List<TeamInfo> teamInfos = teamInfoMapper.selectList(queryWrapper);

            for(TeamInfo teamInfo : teamInfos){
                QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.select(
                        User.class,info->!info.getColumn().equals("password_real")
                        && !info.getColumn().equals("password")
                ).eq("leader_no",teamInfo.getLeaderNo());
                List<User> teamMembers = userMapper.selectList(queryWrapper1);

                //加上组长
                if(!Objects.equals(teamInfo.getLeaderNo(), "")){
                    QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
                    queryWrapper2.select(
                            User.class,info->!info.getColumn().equals("password_real")
                                    && !info.getColumn().equals("password")
                    ).eq("student_no",teamInfo.getLeaderNo());
                    User leaderUser = userMapper.selectOne(queryWrapper2);
                    teamMembers.add(0,leaderUser);
                }

                teamDeatilInfo.put(teamInfo.getTeamname(),teamMembers);
            }
        }else if(user.getRole() == 2){//组长角色
            QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("leader_no",user.getStudentNo());
            TeamInfo teamInfo = teamInfoMapper.selectOne(queryWrapper);

            QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
            queryWrapper1.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("leader_no",user.getStudentNo());
            List<User> teamMembers = userMapper.selectList(queryWrapper1);

            teamDeatilInfo.put(teamInfo.getTeamname(),teamMembers);
        }
        return Result.success(teamDeatilInfo);
    }

    @Override
    public Result getAllTeamInfos(int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(user.getRole()!=1){
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
