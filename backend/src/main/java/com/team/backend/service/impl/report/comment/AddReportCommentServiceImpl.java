package com.team.backend.service.impl.report.comment;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.ReportCommentMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.ReportComment;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.comment.AddReportCommentService;
import com.team.backend.dto.req.WeeklyReportCommentAddType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

import static com.team.backend.utils.common.consts.roleConst.ADMINROLE;
import static com.team.backend.utils.common.consts.roleConst.LEADERROLE;

@Service
public class AddReportCommentServiceImpl implements AddReportCommentService {
    @Autowired
    ReportCommentMapper reportCommentMapper;
    @Autowired
    WeeklyReportMapper weeklyReportMapper;
    @Autowired
    UserMapper userMapper;
    @Override
    public Result addReportComment(WeeklyReportCommentAddType commentAddInfo) {
        //新增评论
        Date now = new Date();
        int roleGroup = commentAddInfo.getRoleGroup();
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        WeeklyReport weeklyReport = weeklyReportMapper.selectById(commentAddInfo.getReportId());

        if(weeklyReport==null){
            return Result.build(null,ResultCodeEnum.REPORT_NOT_EXIST);
        }

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",weeklyReport.getStudentId());
        User reportUser = userMapper.selectOne(queryWrapper);

        if(user.getRole()==ADMINROLE){
            roleGroup = 1;
            if(!Objects.equals(reportUser.getAdminNo(), user.getStudentNo())){
                return Result.build(null, ResultCodeEnum.REPORT_COMMENT_NOT_ADMIN);
            }
        }else if(user.getRole()==LEADERROLE){
            roleGroup = 2;
            if(!Objects.equals(reportUser.getLeaderId(), user.getId())){
                if(Objects.equals(user.getStudentNo(), reportUser.getStudentNo())){
                    roleGroup = 1;
                }else{
                    return Result.build(null, ResultCodeEnum.REPORT_COMMENT_NOT_LEADER);
                }
            }
        }else{
            if(!Objects.equals(reportUser.getStudentNo(), user.getStudentNo())){
                return Result.build(null, ResultCodeEnum.REPORT_COMMENT_NOT_MEMBER);
            }
        }

        ReportComment reportComment = new ReportComment(
                null,
                commentAddInfo.getReportId(),
                user.getId(),
                roleGroup,
                commentAddInfo.getContent(),
                now,
                now
        );

        //todo 变更状态 未评论->已评论
        if(user.getRole()==ADMINROLE || user.getRole()==LEADERROLE){
                UpdateWrapper<WeeklyReport> updateWrapper = new UpdateWrapper<>();
                if(user.getRole()==ADMINROLE){
                    updateWrapper.eq("id",commentAddInfo.getReportId()).set("admin_status",2);
                }else {
                    updateWrapper.eq("id",commentAddInfo.getReportId()).set("leader_status",2);
                }
            weeklyReportMapper.update(null,updateWrapper);
        }


        reportCommentMapper.insert(reportComment);
        return Result.success(null);
    }
}
