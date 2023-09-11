package com.team.backend.service.impl.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.management.AddWeeklyReportService;
import com.team.backend.utils.JsonUtil;
import com.team.backend.utils.common.TeamWorks;
import com.team.backend.utils.common.WeeklyReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddWeeklyReportServiceImpl implements AddWeeklyReportService {
    @Autowired
    WeeklyReportMapper weeklyReportMapper;

    @Override
    public Result addWeeklyReport(WeeklyReportType reportInfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        QueryWrapper<WeeklyReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",user.getStudentNo()).eq("time",reportInfo.getTime());
        WeeklyReport weeklyReportFind = weeklyReportMapper.selectOne(queryWrapper);
        int teamworkDuration = 0;
        Date now = new Date();
        String teamworkJsonInfos = JsonUtil.ReportToJsonString(reportInfo.getTeamWorks());
        for (TeamWorks teamWorks:reportInfo.getTeamWorks()) {
            teamworkDuration+=teamWorks.getDuration();
        }
        WeeklyReport weeklyReport = new WeeklyReport(
                null,
                reportInfo.getTime(),
                reportInfo.getYear(),
                reportInfo.getMonth(),
                reportInfo.getWeekPlanHtml(),
                reportInfo.getWeekPlanHtml(),
                teamworkJsonInfos,
                teamworkDuration,
                user.getStudentNo(),
                null,
                null
        );
        if(weeklyReportFind==null){
            //新建周报
            weeklyReport.setCreateTime(now);
            weeklyReportMapper.insert(weeklyReport);
        }else{
            //更新周报
            weeklyReport.setId(weeklyReportFind.getId());
            weeklyReport.setUpdateTime(now);
            weeklyReportMapper.updateById(weeklyReport);
        }
        return Result.success(null);
    }
}
