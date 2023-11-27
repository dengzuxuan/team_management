package com.team.backend.service.impl.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.management.GetUserAllReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.team.backend.utils.common.consts.roleConst.ADMINROLE;
import static com.team.backend.utils.common.consts.roleConst.LEADERROLE;

@Service
public class GetUserAllReportServiceImpl implements GetUserAllReportService {
    @Autowired
    WeeklyReportMapper weeklyReportMapper;

    @Autowired
    GetWeeklyReportServiceImpl getWeeklyReportService;

    @Override
    public Result getUserAllReport(String studentNo,int pageNum,int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        if(user.getRole()!=ADMINROLE && user.getRole()!=LEADERROLE){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        QueryWrapper<WeeklyReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",studentNo);

        Map<String,Object> res = getWeeklyReportService.getReportPage(pageNum,pageSize,queryWrapper);

        return Result.success(res);
    }
}
