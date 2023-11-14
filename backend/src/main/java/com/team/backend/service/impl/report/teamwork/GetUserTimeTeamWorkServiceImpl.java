package com.team.backend.service.impl.report.teamwork;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.ReportTeamWorkMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.ReportTeamWork;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.teamwork.GetUserTimeTeamWorkService;
import com.team.backend.dto.req.WeeklyGetWorkType;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class GetUserTimeTeamWorkServiceImpl implements GetUserTimeTeamWorkService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ReportTeamWorkMapper reportTeamWorkMapper;

    @Override
    public Result getUserTimeTeamWork(WeeklyGetWorkType getReportInfo, int pageNum, int pageSize) {

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=1 && user.getRole()!=2){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        List<UserTeamWorkInfo> userTeamWorkInfos = new ArrayList<>();

        List<User> userList = new ArrayList<>();
        if(user.getRole()==1){
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("admin_no",user.getStudentNo());
            userList = userMapper.selectList(queryWrapper);
        }else{
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("leader_no",user.getStudentNo());
            userList = userMapper.selectList(queryWrapper);
        }

        for(User userInfo:userList){
            QueryWrapper<ReportTeamWork> queryWrapper1 = new QueryWrapper<>();
            int startYear = getReportInfo.getStartTimeInfo().getYear();
            int endYear = getReportInfo.getEndTimeInfo().getYear();
            int startWeek = getReportInfo.getStartTimeInfo().getWeek();
            int endWeek = getReportInfo.getEndTimeInfo().getWeek();
            int durationSum = 0;

            if (startYear == endYear) {
                queryWrapper1.eq("student_no",userInfo.getStudentNo()).eq("year",startYear).ge("week",startWeek).le("week",endWeek);
            }else if(startYear<endYear){
                //跨年
                //2022 23 - 2023 10
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
            List<ReportTeamWork> reportTeamWorkList = reportTeamWorkMapper.selectList(queryWrapper1);

            reportTeamWorkList.sort(new Comparator<ReportTeamWork>() {//使用List接口的方法排序
                @Override
                public int compare(ReportTeamWork r1, ReportTeamWork r2) {
                    if(Objects.equals(r1.getYear(), r2.getYear())){
                        return Integer.parseInt( r1.getWeek()) - Integer.parseInt(r2.getWeek());
                    }
                    return Integer.parseInt(r1.getYear())-Integer.parseInt(r2.getYear());
                }
            });

            for (ReportTeamWork report:reportTeamWorkList) {
                durationSum+=report.getDuration();
            }

            UserTeamWorkInfo userTeamWorkInfo = new UserTeamWorkInfo(
                    userInfo.getId(),
                    userInfo.getUsername(),
                    userInfo.getStudentNo(),
                    userInfo.getPhoto(),
                    reportTeamWorkList.size(),
                    durationSum,
                    reportTeamWorkList
            );

            userTeamWorkInfos.add(userTeamWorkInfo);

        }
        userTeamWorkInfos.sort(new Comparator<UserTeamWorkInfo>() {//使用List接口的方法排序
            @Override
            public int compare(UserTeamWorkInfo o1, UserTeamWorkInfo o2) {
                return -1*(o1.getWorkTotalDuration()-o2.getWorkTotalDuration());
            }
        });

        int totalCount = userTeamWorkInfos.size(); //总数量
        int pageCount = 0; //总页数
        List<UserTeamWorkInfo> subyList = null;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }

        if (m == 0) {
            subyList = userTeamWorkInfos.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
        } else {
            if (pageNum == pageCount) {
                subyList = userTeamWorkInfos.subList((pageNum - 1) * pageSize, totalCount);
            }
            if (pageNum< pageCount){
                subyList = userTeamWorkInfos.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
            }
        }
        if (pageNum > pageCount){
            subyList  = null;
        }

        Map<String,Object> res = new HashMap<>();
        res.put("userTimesTeamWorkInfo",subyList);
        res.put("total",userTeamWorkInfos.size());
        res.put("size",pageSize);
        res.put("current",pageNum);
        return Result.success(res);
    }

    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    private class UserTeamWorkInfo{
        private int id;
        private String name;
        private String studentNo;
        private String photo;
        private int workTimes;
        private int workTotalDuration;
        private List<ReportTeamWork> teamWorksList;
    }
}
