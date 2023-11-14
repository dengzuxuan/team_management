package com.team.backend.service.impl.team.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.team.detailinfos.GetTeamDetailInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName GetTeamDetailServiceImpl
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 10:53
 * @Version 1.0
 */
@Service
public class GetTeamDetailServiceImpl implements GetTeamDetailInfoService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    TeamInfoMapper teamInfoMapper;

    @Override
    public Result getTeamDetail() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Map<String,Object> res = new HashMap<>();

        if(user.getRole() == 1){//导师角色
             QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
             queryWrapper.eq("admin_no",user.getStudentNo());
            List<TeamInfo> teamInfos = teamInfoMapper.selectList(queryWrapper);

        }
        return null;
    }
}
