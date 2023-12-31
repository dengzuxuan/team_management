package com.team.backend.service.impl.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.UserMapper;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.report.management.GetUserTeamWorkService;
import com.team.backend.utils.JsonUtil;
import com.team.backend.dto.req.TeamWorks;
import com.team.backend.dto.req.WeeklyGetReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class GetUserTeamWorkServiceImpl implements GetUserTeamWorkService {
    @Autowired
    GetWeeklyReportServiceImpl getWeeklyReportService;
    @Autowired
    UserMapper userMapper;

    @Autowired
    WeeklyReportMapper weeklyReportMapper;

    @Override
    public Result getUserTeamWork(WeeklyGetReportType getReportInfo, int pageNum, int pageSize) {
        int startYear = getReportInfo.getStartTimeInfo().getYear();
        int endYear = getReportInfo.getEndTimeInfo().getYear();
        int startWeek = getReportInfo.getStartTimeInfo().getWeek();
        int endWeek = getReportInfo.getEndTimeInfo().getWeek();

        QueryWrapper<User> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.eq("student_no",getReportInfo.getStudentNo());
        User userFind = userMapper.selectOne(queryWrapper1);
        QueryWrapper<WeeklyReport> queryWrapper = new QueryWrapper<>();

        if (startYear == endYear) {
            queryWrapper.eq("student_id",userFind.getId()).eq("year",startYear).ge("week",startWeek).le("week",endWeek);
        }else if(startYear<endYear){
            //跨年
            //2022 23 - 2023 10
            for (int i = startYear; i <= endYear ; i++) {
                if(i==startYear){
                    queryWrapper.eq("student_id",userFind.getId()).eq("year",i).ge("week",startWeek);
                }else if(i==endYear){
                    queryWrapper.or().eq("student_id",userFind.getId()).eq("year",i).le("week",endWeek);
                }else{
                    queryWrapper.or().eq("student_id",userFind.getId()).eq("year",i);
                }
            }
        }
        List<TeamWorks> teamWorkLists = null;

        List<WeeklyReport> weeklyReportList = weeklyReportMapper.selectList(queryWrapper);

        for(WeeklyReport report:weeklyReportList){
            List<TeamWorks> teamWorkList = JsonUtil.JsonToReport(report.getTeamworkInfos());
            if(teamWorkList!=null){
                teamWorkLists.addAll(teamWorkList);
            }
        }

        Map<String,Object> res = new HashMap<>();
        if(teamWorkLists!=null){
            //总共可被切分的数量
            int pages = (int) Math.ceil( teamWorkLists.size()/pageSize);
            if (pageNum == pages) {
            }
        }
        return null;
    }
}
