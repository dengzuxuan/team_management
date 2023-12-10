package com.team.backend.service.impl.report.info;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.UserMapper;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.info.GetUserInfosService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.team.backend.utils.common.consts.roleConst.ADMINROLE;
import static com.team.backend.utils.common.consts.roleConst.LEADERROLE;

@Service
public class GetUserInfosServiceImpl implements GetUserInfosService {
    @Autowired
    UserMapper userMapper;

    @Autowired
    WeeklyReportMapper weeklyReportMapper;
    /*
    * 获取组内/（团队内）所有用户
    * */
    @Override
    public Result getUserInfos() {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(user.getRole()!=ADMINROLE && user.getRole()!=LEADERROLE){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if(user.getRole()==ADMINROLE){
            //管理员
            queryWrapper.eq("admin_no",user.getStudentNo()).ne("role",1).select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            );
        }else {
            //组长
            queryWrapper.eq("leader_id",user.getId()).ne("role",1).select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            );
        }

        List<User> users = userMapper.selectList(queryWrapper);

        List<UserInfo> userInfos = new ArrayList<>();
        for(User userInfo:users){
            int notReadCnt = 0;
            if(user.getRole()==ADMINROLE){

                QueryWrapper<WeeklyReport> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("student_id",userInfo.getId()).eq("admin_status",0);
                List<WeeklyReport> weeklyReportList = weeklyReportMapper.selectList(queryWrapper1);
                notReadCnt = weeklyReportList.size();
            }else {

                QueryWrapper<WeeklyReport> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("student_id",userInfo.getId()).eq("leader_status",0);
                List<WeeklyReport> weeklyReportList = weeklyReportMapper.selectList(queryWrapper1);
                notReadCnt = weeklyReportList.size();
            }

            UserInfo userInfo1 = new UserInfo(
                    userInfo,
                    notReadCnt
            );
            userInfos.add(userInfo1);
        }

        return Result.success(userInfos);
    }
    @Getter
    @Setter
    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    private class UserInfo {
        User user;
        int notReadCnt;
    }
}
