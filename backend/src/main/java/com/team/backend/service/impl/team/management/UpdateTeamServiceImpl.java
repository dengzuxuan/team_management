package com.team.backend.service.impl.team.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.req.TeamInfoType;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.team.management.UpdateTeamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UpdateTeamServiceImpl implements UpdateTeamInfoService {
    @Autowired
    TeamInfoMapper teamInfoMapper;
    @Override
    public Result updateTeamLeader(String oldStudentNo, String newStudentNo) {
        UpdateWrapper<TeamInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("leader_no",oldStudentNo);
        updateWrapper.set("leader_no",newStudentNo);
        teamInfoMapper.update(null,updateWrapper);
        return Result.success(null);
    }


    @Override
    public Result updateTeamInfo(TeamInfoType teamInfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(user.getRole()!=1 && user.getRole()!=2){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("no",teamInfo.getNo());
        TeamInfo teamInfoFind  = teamInfoMapper.selectOne(queryWrapper);

        if(teamInfoFind==null) {
            return Result.build(null,ResultCodeEnum.TEAM_NOT_EXIST);
        }

        teamInfoFind.setTeamname(teamInfo.getTeamName());
        teamInfoFind.setNo(teamInfo.getNo());
        teamInfoMapper.updateById(teamInfoFind);
        return Result.success(null);
    }
}
