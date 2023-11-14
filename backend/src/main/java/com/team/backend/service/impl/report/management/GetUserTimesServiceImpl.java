package com.team.backend.service.impl.report.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.ReportTeamWorkMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
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
    @Override
    public Result getUserTimes(WeeklyGetWorkType getReportInfo, int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=1 && user.getRole()!=2){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

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


            List<WeeklyReport> weeklyReportList = weeklyReportMapper.selectList(queryWrapper1);

            UserTimesInfo userTimesInfo = new UserTimesInfo(
                    userInfo.getId(),
                    userInfo.getUsername(),
                    userInfo.getStudentNo(),
                    userInfo.getPhoto(),
                    weeklyReportList.size()
            );
            userTimesInfos.add(userTimesInfo);

        }

        userTimesInfos.sort(new Comparator<UserTimesInfo>() {//使用List接口的方法排序
            @Override
            public int compare(UserTimesInfo o1, UserTimesInfo o2) {
                return o1.getTimes()-o2.getTimes();
            }
        });

        int totalCount = userTimesInfos.size(); //总数量
        int pageCount = 0; //总页数
        List<UserTimesInfo> subyList = null;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }

        if (m == 0) {
            subyList = userTimesInfos.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
        } else {
            if (pageNum == pageCount) {
                subyList = userTimesInfos.subList((pageNum - 1) * pageSize, totalCount);
            }
            if (pageNum< pageCount){
                subyList = userTimesInfos.subList((pageNum - 1) * pageSize, pageSize * (pageNum));
            }
        }
        if (pageNum > pageCount){
            subyList  = null;
        }

        Map<String,Object> res = new HashMap<>();
        res.put("userTimesInfo",subyList);
        res.put("total",userTimesInfos.size());
        res.put("size",pageSize);
        res.put("current",pageNum);
        return Result.success(res);

    }
    @Getter
    @Setter
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    private class UserTimesInfo{
        private int id;
        private String name;
        private String studentNo;
        private String photo;
        private int times;
    }

}
