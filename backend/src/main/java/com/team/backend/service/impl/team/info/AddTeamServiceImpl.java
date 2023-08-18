package com.team.backend.service.impl.team.info;

import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.team.info.AddTeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddTeamServiceImpl implements AddTeamService {
    @Autowired
    TeamInfoMapper teamInfoMapper;

    @Override
    public Result addTeamService( String leaderNo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();

        if(adminUser.getRole()!=1){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        Date now = new Date();
        TeamInfo teamInfo = new TeamInfo(null, adminUser.getStudentNo(), leaderNo, "", "", "","", now, now);
        teamInfoMapper.insert(teamInfo);
        return Result.success(null);
    }
}
