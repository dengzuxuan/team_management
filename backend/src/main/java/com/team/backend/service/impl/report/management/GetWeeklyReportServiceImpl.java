package com.team.backend.service.impl.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.management.GetWeeklyReportService;
import com.team.backend.utils.JsonUtil;
import com.team.backend.utils.ReportSortUtil;
import com.team.backend.utils.common.WeeklyReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetWeeklyReportServiceImpl implements GetWeeklyReportService {
    @Autowired
    WeeklyReportMapper weeklyReportMapper;

    @Override
    public Result getWeeklyReport(int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        QueryWrapper<WeeklyReport> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("student_no",user.getStudentNo());

        Map<String,Object> res = getReportPage(pageNum,pageSize,queryWrapper);

        return Result.success(res);
    }

    @Override
    public Map<String,Object> getReportPage(int pageNum, int pageSize, QueryWrapper queryWrapper) {
        Map<String,Object> res = new HashMap<>();
        List<WeeklyReportType> weeklyReportTypeLists = new ArrayList<>();

        Page<WeeklyReport> page = new Page<>(pageNum,pageSize);
        Page<WeeklyReport> rowPages = new Page<>();

        rowPages = weeklyReportMapper.selectPage(page,queryWrapper);

        List<WeeklyReport> weeklyReportList = rowPages.getRecords();

        for (WeeklyReport report:weeklyReportList){
            WeeklyReportType weeklyReportType = new WeeklyReportType(
                    report.getId(),
                    report.getTime(),
                    report.getYear(),
                    report.getMonth(),
                    report.getWeek(),
                    JsonUtil.JsonToWeekProgress(report.getWeekProgress()),
                    JsonUtil.JsonToWeekPlan(report.getWeekPlan()),
                    JsonUtil.JsonToReport(report.getTeamworkInfos()),
                    report.getCreateTime(),
                    report.getUpdateTime()
            );
            weeklyReportTypeLists.add(weeklyReportType);
        }
        ReportSortUtil.ReportSortUtilForList reportSortUtilForList = new ReportSortUtil.ReportSortUtilForList();

        Collections.sort(weeklyReportTypeLists, reportSortUtilForList);
        res.put("weeklyReports",weeklyReportTypeLists);
        res.put("total",rowPages.getTotal());
        res.put("size",rowPages.getSize());
        res.put("current",rowPages.getCurrent());


        return res;
    }
}
