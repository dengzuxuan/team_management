package com.team.backend.service.impl.report.teamwork;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.resp.UserTeamWorkInfo;
import com.team.backend.mapper.ReportTeamWorkMapper;
import com.team.backend.mapper.TeamInfoMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.ReportTeamWork;
import com.team.backend.pojo.TeamInfo;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.report.utils.getTimesInfo;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.teamwork.GetUserTimeTeamWorkService;
import com.team.backend.dto.req.WeeklyGetWorkType;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.team.backend.utils.common.consts.roleConst.ADMINROLE;
import static com.team.backend.utils.common.consts.roleConst.LEADERROLE;

@Service
public class GetUserTimeTeamWorkServiceImpl implements GetUserTimeTeamWorkService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    ReportTeamWorkMapper reportTeamWorkMapper;
    @Autowired
    getTimesInfo getTimesInfo;
    @Autowired
    TeamInfoMapper teamInfoMapper;
    @Override
    public Result getUserTimeTeamWork(WeeklyGetWorkType getReportInfo, int pageNum, int pageSize) {

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        if(user.getRole()!=ADMINROLE && user.getRole()!=LEADERROLE){
            return Result.build(null, ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        List<UserTeamWorkInfo> userTeamWorkInfos = new ArrayList<>();

        List<User> userList = getTimesInfo.getUserList(user,getReportInfo.getNo());

        for(User userInfo:userList){
            QueryWrapper<ReportTeamWork> queryWrapper1 = new QueryWrapper<>();
            int startYear = getReportInfo.getStartTimeInfo().getYear();
            int endYear = getReportInfo.getEndTimeInfo().getYear();
            int startWeek = getReportInfo.getStartTimeInfo().getWeek();
            int endWeek = getReportInfo.getEndTimeInfo().getWeek();
            int durationSum = 0;

            if (startYear == endYear) {
                queryWrapper1.eq("student_id",userInfo.getId()).eq("year",startYear).ge("week",startWeek).le("week",endWeek);
            }else if(startYear<endYear){
                //跨年
                //2022 23 - 2023 10
                for (int i = startYear; i <= endYear ; i++) {
                    if(i==startYear){
                        queryWrapper1.eq("student_id",userInfo.getId()).eq("year",i).ge("week",startWeek);
                    }else if(i==endYear){
                        queryWrapper1.or().eq("student_id",userInfo.getId()).eq("year",i).le("week",endWeek);
                    }else{
                        queryWrapper1.or().eq("student_id",userInfo.getId()).eq("year",i);
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

            QueryWrapper<TeamInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("no",userInfo.getTeamNo()).eq("admin_no",user.getStudentNo());

            TeamInfo teamInfo = teamInfoMapper.selectOne(queryWrapper);
            String teamNo = null;
            String teamName = null;
            if(teamInfo!=null){
                teamNo = teamInfo.getNo();
                teamName = teamInfo.getTeamname();
            }

            UserTeamWorkInfo userTeamWorkInfo = new UserTeamWorkInfo(
                    userInfo.getId(),
                    userInfo.getStudentNo(),
                    userInfo.getRole(),
                    userInfo.getUsername(),
                    teamNo,
                    teamName,
                    reportTeamWorkList.size(),
                    durationSum,
                    reportTeamWorkList
            );

            userTeamWorkInfos.add(userTeamWorkInfo);

        }
        //使用List接口的方法排序
        userTeamWorkInfos.sort((o1, o2) -> -1*(o1.getWorkTotalDuration()-o2.getWorkTotalDuration()));

        List pageList = null;
        if(!userTeamWorkInfos.isEmpty()){
            pageList = getTimesInfo.getPageList(userTeamWorkInfos, pageSize, pageNum);
        }

        Map<String,Object> res = new HashMap<>();
        res.put("userTimesTeamWorkInfo",pageList);
        res.put("total",userTeamWorkInfos.size());
        res.put("size",pageSize);
        res.put("current",pageNum);
        return Result.success(res);
    }

}
