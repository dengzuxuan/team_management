package com.team.backend.service.impl.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.report.management.GetUserTimeReportService;
import com.team.backend.dto.req.WeeklyGetReportType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class GetUserTimeReportServiceImpl implements GetUserTimeReportService {
    @Autowired
    GetWeeklyReportServiceImpl getWeeklyReportService;

    @Override
    public Result getUserTimeReport(WeeklyGetReportType getReportInfo, int pageNum, int pageSize) {
        int startYear = getReportInfo.getStartTimeInfo().getYear();
        int endYear = getReportInfo.getEndTimeInfo().getYear();
        int startWeek = getReportInfo.getStartTimeInfo().getWeek();
        int endWeek = getReportInfo.getEndTimeInfo().getWeek();

        QueryWrapper<WeeklyReport> queryWrapper = new QueryWrapper<>();

        if (startYear == endYear) {
            queryWrapper.eq("student_no",getReportInfo.getStudentNo()).eq("year",startYear).ge("week",startWeek).le("week",endWeek);
        }else if(startYear<endYear){
            //跨年
            //2022 23 - 2023 10
            for (int i = startYear; i <= endYear ; i++) {
                if(i==startYear){
                    queryWrapper.eq("student_no",getReportInfo.getStudentNo()).eq("year",i).ge("week",startWeek);
                }else if(i==endYear){
                    queryWrapper.or().eq("student_no",getReportInfo.getStudentNo()).eq("year",i).le("week",endWeek);
                }else{
                    queryWrapper.or().eq("student_no",getReportInfo.getStudentNo()).eq("year",i);
                }
            }
        }

        Map<String,Object> res = getWeeklyReportService.getReportPage(pageNum,pageSize,queryWrapper);
        return Result.success(res);
    }
}
