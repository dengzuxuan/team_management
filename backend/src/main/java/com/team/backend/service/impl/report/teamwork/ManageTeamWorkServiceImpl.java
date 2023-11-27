package com.team.backend.service.impl.report.teamwork;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.ReportTeamWorkMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.ReportTeamWork;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.teamwork.ManageTeamWorkService;
import com.team.backend.utils.JsonUtil;
import com.team.backend.utils.ReportSortUtil;
import com.team.backend.dto.req.TeamWorks;
import com.team.backend.dto.req.WeeklyGetReportType;
import com.team.backend.dto.req.WeeklyReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ManageTeamWorkServiceImpl implements ManageTeamWorkService {
    @Autowired
    ReportTeamWorkMapper reportTeamWorkMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public void addTeamWork(WeeklyReportType reportInfo) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        Date now = new Date();
        for (TeamWorks teamWork:reportInfo.getTeamWorks()) {
            ReportTeamWork reportTeamWork = new ReportTeamWork(
                    null,
                    reportInfo.getId(),
                    reportInfo.getYear(),
                    reportInfo.getWeek(),
                    user.getId(),
                    teamWork.getContent(),
                    teamWork.getDuration(),
                    teamWork.getId(),
                    teamWork.getType(),
                    teamWork.getTitle(),
                    JsonUtil.attachToJsonString(teamWork.getAttach()),
                    now,
                    now
            );
            reportTeamWorkMapper.insert(reportTeamWork);
        }
    }

    @Override
    public void updateTeamWork(WeeklyReportType reportInfo) {

    }

    @Override
    public Result getTeamWork(WeeklyGetReportType getReportInfo, int pageNum, int pageSize) {
        int startYear = getReportInfo.getStartTimeInfo().getYear();
        int endYear = getReportInfo.getEndTimeInfo().getYear();
        int startWeek = getReportInfo.getStartTimeInfo().getWeek();
        int endWeek = getReportInfo.getEndTimeInfo().getWeek();
        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("student_no",getReportInfo.getStudentNo());
        User userfind = userMapper.selectOne(queryWrapper1);

        QueryWrapper<ReportTeamWork> queryWrapper = new QueryWrapper<>();

        if (startYear == endYear) {
            queryWrapper.eq("student_no",userfind.getId()).eq("year",startYear).ge("week",startWeek).le("week",endWeek);
        }else if(startYear<endYear){
            //跨年
            //eg:2022 23 - 2023 10
            for (int i = startYear; i <= endYear ; i++) {
                if(i==startYear){
                    queryWrapper.eq("student_id",userfind.getId()).eq("year",i).ge("week",startWeek);
                }else if(i==endYear){
                    queryWrapper.or().eq("student_id",userfind.getId()).eq("year",i).le("week",endWeek);
                }else{
                    queryWrapper.or().eq("student_id",userfind.getId()).eq("year",i);
                }
            }
        }

        Page<ReportTeamWork> page = new Page<>(pageNum,pageSize);
        Page<ReportTeamWork> rowPages = new Page<>();

        rowPages = reportTeamWorkMapper.selectPage(page,queryWrapper);

        List<ReportTeamWork> reportTeamWorkList = rowPages.getRecords();
        ReportSortUtil.ReportTeamWorkSortUtilForList reportTeamworkSortUtilForList = new ReportSortUtil.ReportTeamWorkSortUtilForList();
        Collections.sort(reportTeamWorkList, reportTeamworkSortUtilForList);

        Map<String,Object> res = new HashMap<>();
        res.put("weeklyTeamWork",reportTeamWorkList);
        res.put("total",rowPages.getTotal());
        res.put("size",rowPages.getSize());
        res.put("current",rowPages.getCurrent());
        return Result.success(res);
    }

}
