package com.team.backend.service.impl.report.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.ReportCommentMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.mapper.WeeklyReportMapper;
import com.team.backend.pojo.ReportComment;
import com.team.backend.pojo.User;
import com.team.backend.pojo.WeeklyReport;
import com.team.backend.service.report.comment.GetReportCommentService;
import lombok.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
            ).eq("student_no",reportComment.getStudentNo());
            User user = userMapper.selectOne(queryWrapper2);

            ReportCommentType groupComment = new ReportCommentType(
                    reportComment.getId(),
                    reportComment.getReportId(),
                    user,
                    reportComment.getRole(),
                    reportComment.getContent(),
                    reportComment.getCreateTime(),
                    reportComment.getUpdateTime()
            );

            if(reportComment.getRole()==1){
                adminGroupComments.add(groupComment);
            }else if(reportComment.getRole()==2){
                leaderGroupComments.add(groupComment);
            }

        }


        ReportCommentGroup reportCommentGroup = new ReportCommentGroup(
                leaderGroupComments,
                adminGroupComments
        );

        return Result.success(reportCommentGroup);
    }

    @Getter
    @Setter
    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    private class ReportCommentGroup{
        List<ReportCommentType> leaderGroupComments;
        List<ReportCommentType> adminGroupComments;
    }
    @Getter
    @Setter
    @Data
    @EqualsAndHashCode
    @AllArgsConstructor
    @NoArgsConstructor
    private class ReportCommentType{
        @TableId(type = IdType.AUTO)
        private Integer id;
        private Integer ReportId;
        private User userInfo;
        private Integer role;
        private String content;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
        private Date createTime;
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "Asia/Shanghai")
        private Date updateTime;
    }
}
