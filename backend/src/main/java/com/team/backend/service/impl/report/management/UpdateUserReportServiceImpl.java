package com.team.backend.service.impl.report.management;

import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.req.UpdateReportType;
import com.team.backend.dto.req.WeeklyReportType;
import com.team.backend.mapper.UserMapper;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.management.UpdateUserReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @ClassName UpdateUserReportServiceImpl
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/8 21:34
 * @Version 1.0
 */
@Service
public class UpdateUserReportServiceImpl implements UpdateUserReportService {
    @Autowired
    WeeklyReportMapper weeklyReportMapper;
    @Override
    public Result updateUserReportService(UpdateReportType reportInfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        WeeklyReport weeklyReport = weeklyReportMapper.selectById(reportInfo.getId());

        if(weeklyReport == null){
            return Result.build(null, ResultCodeEnum.REPORT_NOT_EXIST);
        }

        if(!Objects.equals(user.getId(), weeklyReport.getStudentId())){
            return Result.build(null, ResultCodeEnum.USER_CANT_UPDATE_REPORT);
        }


        return Result.success(null);
    }
}
