package com.team.backend.service.impl.user.account;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.resp.UserListInfo;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.user.account.GetListService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.team.backend.utils.common.consts.roleConst.*;

@Service
public class GetListServiceImpl implements GetListService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TeamInfoMapper teamInfoMapper;

    @Override
    public Result getList(int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User adminUser = loginUser.getUser();
        if(adminUser.getRole()!=ADMINROLE){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }
        List<UserListInfo> userListInfos = new ArrayList<>();

        Page<User> page = new Page<>(pageNum,pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        Page<User> rowPage = new Page<>();

        queryWrapper.eq("admin_no",adminUser.getStudentNo()).select(
                User.class,info->!info.getColumn().equals("password_real")
                        && !info.getColumn().equals("password")
        ).orderBy(true,false,"id");
        rowPage = userMapper.selectPage(page, queryWrapper);

        for (User member: rowPage.getRecords()){
            User leaderInfo = null;
            TeamInfo teamInfo = null;
            if(member.getRole()!=MEMBERROLE){
                if(member.getRole()==TEAMMEMBERROLE){
                    //获取leader信息
                    QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
                    queryWrapper1.eq("team_no",member.getTeamNo()).
                            eq("role",LEADERROLE).
                            eq("admin_no",member.getAdminNo()).
                            select(
                                    User.class,info->!info.getColumn().equals("password_real")
                                            && !info.getColumn().equals("password")
                            );
                    leaderInfo = userMapper.selectOne(queryWrapper1);
                }

                //获取小组信息
                QueryWrapper<TeamInfo> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("no",member.getTeamNo()).
                        eq("admin_no",member.getAdminNo());
                teamInfo = teamInfoMapper.selectOne(queryWrapper2);
            }

            UserListInfo userListInfo = new UserListInfo(
                    member.getId(),
                    member.getTeamNo(),
                    member.getLeaderId(),
                    member.getAdminNo(),
                    member.getUsername(),
                    member.getPhone(),
                    member.getEmail(),
                    member.getRole(),
                    member.getCardNo(),
                    member.getStudentNo(),
                    leaderInfo,
                    teamInfo
            );
            userListInfos.add(userListInfo);
        }

        Map<String,Object> res = new HashMap<>();
        res.put("userlist",userListInfos);
        res.put("total",rowPage.getTotal());
        res.put("size",rowPage.getSize());
        res.put("current",rowPage.getCurrent());
        return Result.success(res);
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    private class ExtendUser implements Serializable {
        private User userInfo;
        private User leaderInfo;
        private User adminInfo;

    }
}
