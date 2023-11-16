package com.team.backend.service.impl.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.resp.UserTimesInfo;
import com.team.backend.mapper.ReportTeamWorkMapper;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.report.utils.getTimesInfo;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.management.GetUserTimesService;
import com.team.backend.dto.req.WeeklyGetWorkType;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetUserTimesServiceImpl implements GetUserTimesService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    WeeklyReportMapper weeklyReportMapper;
    @Autowired
    ReportTeamWorkMapper reportTeamWorkMapper;
    @Autowired
    TeamInfoMapper teamInfoMapper;
    @Autowired
    getTimesInfo getTimesInfo;
    @Override
    public Result getUserTimes(WeeklyGetWorkType getReportInfo, int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=1 && user.getRole()!=2){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        List<User> userList = getTimesInfo.getUserList(user, getReportInfo.getStudentNo());

        List<UserTimesInfo> userTimesInfos = new ArrayList<>();
        for(User userInfo:userList){
            QueryWrapper<WeeklyReport> queryWrapper1 = new QueryWrapper<>();
            int startYear = getReportInfo.getStartTimeInfo().getYear();
            int endYear = getReportInfo.getEndTimeInfo().getYear();
            int startWeek = getReportInfo.getStartTimeInfo().getWeek();
            int endWeek = getReportInfo.getEndTimeInfo().getWeek();

            if (startYear == endYear) {
                queryWrapper1.eq("student_no",userInfo.getStudentNo()).eq("year",startYear).ge("week",startWeek).le("week",endWeek);
            }else if(startYear<endYear){
                //跨年
                //eg:2022 23 - 2023 10
                for (int i = startYear; i <= endYear ; i++) {
                    if(i==startYear){
                        queryWrapper1.eq("student_no",userInfo.getStudentNo()).eq("year",i).ge("week",startWeek);
                    }else if(i==endYear){
                        queryWrapper1.or().eq("student_no",userInfo.getStudentNo()).eq("year",i).le("week",endWeek);
                    }else{
                        queryWrapper1.or().eq("student_no",userInfo.getStudentNo()).eq("year",i);
                    }
                }
            }


            List<WeeklyReport> weeklyReportList = weeklyReportMapper.selectList(queryWrapper1);
            
            String teamName = null;
            QueryWrapper<TeamInfo> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("no",userInfo.getTeamNo());
            TeamInfo teamInfoFind = teamInfoMapper.selectOne(queryWrapper2);
            if(teamInfoFind!=null){
                teamName = teamInfoFind.getTeamname();
            }

            UserTimesInfo userTimesInfo = new UserTimesInfo(
                    userInfo.getId(),
                    userInfo.getStudentNo(),
                    userInfo.getRole(),
                    userInfo.getUsername(),
                    userInfo.getPhoto(),
                    userInfo.getTeamNo(),
                    teamName,
                    weeklyReportList.size()
            );

            userTimesInfos.add(userTimesInfo);
        }

        //使用List接口的方法排序
        userTimesInfos.sort((o1, o2) -> o1.getTimes()-o2.getTimes());

        List pageList = getTimesInfo.getPageList(userTimesInfos, pageSize, pageNum);

        Map<String,Object> res = new HashMap<>();
        res.put("userTimesInfo",pageList);
        res.put("total",userTimesInfos.size());
        res.put("size",pageSize);
        res.put("current",pageNum);
        return Result.success(res);
    }
}
