package com.team.backend.service.impl.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.req.ChangeWeeklyReportType;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.report.teamwork.ManageTeamWorkServiceImpl;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.management.AddWeeklyReportService;
import com.team.backend.utils.JsonUtil;
import com.team.backend.dto.req.TeamWorks;
import com.team.backend.dto.resp.WeeklyReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddWeeklyReportServiceImpl implements AddWeeklyReportService {
    @Autowired
    WeeklyReportMapper weeklyReportMapper;

    @Autowired
    ManageTeamWorkServiceImpl manageTeamWorkService;

    @Override
    public Result addWeeklyReport(ChangeWeeklyReportType reportInfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<WeeklyReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_id",user.getId()).eq("time",reportInfo.getTime());
        WeeklyReport weeklyReportFind = weeklyReportMapper.selectOne(queryWrapper);
        int teamworkDuration = 0;
        Date now = new Date();
        for (TeamWorks teamWorks:reportInfo.getTeamWorks()) {
            teamworkDuration+=teamWorks.getDuration();
        }
        WeeklyReport weeklyReport = new WeeklyReport(
                null,
                reportInfo.getTime(),
                reportInfo.getYear(),
                reportInfo.getMonth(),
                reportInfo.getWeek(),
                JsonUtil.WeekProgressToJsonString(reportInfo.getWeekProgress()),
                JsonUtil.WeekPlanToJsonString(reportInfo.getWeekPlan()),
                JsonUtil.ReportToJsonString(reportInfo.getTeamWorks()),
                teamworkDuration,
                user.getId(),
                0,
                0,
                null,
                null
        );

        if(weeklyReportFind==null){
            //新建周报
            weeklyReport.setCreateTime(now);
            weeklyReport.setUpdateTime(now);
            weeklyReportMapper.insert(weeklyReport);
        }else if(!reportInfo.getCreateFlag()){
            //更新周报
            weeklyReport.setId(weeklyReportFind.getId());
            weeklyReport.setUpdateTime(now);
            weeklyReportMapper.updateById(weeklyReport);
        }else{
            //原先新建的周报既不为空 又不是更新操作
            return Result.build(null, ResultCodeEnum.REPORT_CANT_UPDATE_HERE);
        }

        reportInfo.setId(weeklyReport.getId());
        manageTeamWorkService.addTeamWork(reportInfo);
        return Result.success(null);
    }
}
