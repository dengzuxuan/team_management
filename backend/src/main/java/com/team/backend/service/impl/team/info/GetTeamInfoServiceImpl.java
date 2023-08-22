package com.team.backend.service.impl.team.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.team.info.GetTeamInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetTeamInfoServiceImpl implements GetTeamInfoService {
    @Autowired
    TeamInfoMapper teamInfoMapper;

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
}
