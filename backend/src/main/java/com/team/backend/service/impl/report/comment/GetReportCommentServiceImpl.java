package com.team.backend.service.impl.report.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.dto.resp.ReportCommentGroup;
import com.team.backend.dto.resp.ReportCommentType;
import com.team.backend.mapper.ReportCommentMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.ReportComment;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.comment.GetReportCommentService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.team.backend.utils.common.consts.roleConst.ADMINROLE;
import static com.team.backend.utils.common.consts.roleConst.LEADERROLE;

@Service
public class GetReportCommentServiceImpl implements GetReportCommentService {
    @Autowired
    WeeklyReportMapper weeklyReportMapper;
    @Autowired
    ReportCommentMapper reportCommentMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public Result getReportComment(int reportId) {
        //todo 变更状态 未读->已读
        WeeklyReport weeklyReport = weeklyReportMapper.selectById(reportId);
        if(weeklyReport==null){
            return Result.build(null, ResultCodeEnum.REPORT_NOT_EXIST);
        }

        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User loginuserUser = loginUser.getUser();

        if((loginuserUser.getRole()==ADMINROLE && weeklyReport.getAdminStatus()==0)|| (loginuserUser.getRole()==LEADERROLE && weeklyReport.getLeaderStatus()==0)){
            UpdateWrapper<WeeklyReport> updateWrapper = new UpdateWrapper<>();

            if(loginuserUser.getRole()==ADMINROLE){
                updateWrapper.eq("id",weeklyReport.getId()).set("admin_status",1);
            }else {
                updateWrapper.eq("id",weeklyReport.getId()).set("leader_status",1);
            }
            weeklyReportMapper.update(null,updateWrapper);
        }

        //获取评论区
        QueryWrapper<ReportComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("report_id",reportId);
        List<ReportComment> reportCommentList = reportCommentMapper.selectList(queryWrapper);

        List<ReportCommentType> leaderGroupComments = new ArrayList<>();
        List<ReportCommentType> adminGroupComments = new ArrayList<>();

        for(ReportComment reportComment:reportCommentList){
            QueryWrapper<User> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.select(
                    User.class,info->!info.getColumn().equals("password_real")
                            && !info.getColumn().equals("password")
            ).eq("id",reportComment.getStudentId());
            User user = userMapper.selectOne(queryWrapper2);

            int isMyself = 0;

            if(Objects.equals(user.getStudentNo(), loginuserUser.getStudentNo())){
                isMyself = 1;
            }

            ReportCommentType groupComment = new ReportCommentType(
                    reportComment.getId(),
                    reportComment.getReportId(),
                    user,
                    reportComment.getRole(),
                    reportComment.getContent(),
                    isMyself,
                    reportComment.getCreateTime(),
                    reportComment.getUpdateTime()
            );

            if(reportComment.getRole()==ADMINROLE){
                adminGroupComments.add(groupComment);
            }else if(reportComment.getRole()==LEADERROLE){
                leaderGroupComments.add(groupComment);
            }

        }


        ReportCommentGroup reportCommentGroup = new ReportCommentGroup(
                leaderGroupComments,
                adminGroupComments
        );

        return Result.success(reportCommentGroup);
    }
}
