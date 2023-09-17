package com.team.backend.service.impl.report.comment;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.ReportCommentMapper;
import com.team.backend.pojo.ReportComment;
import com.team.backend.pojo.User;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.service.report.comment.DelReportCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

@Service
public class DelReportCommentServiceImpl implements DelReportCommentService {
    @Autowired
    ReportCommentMapper reportCommentMapper;

    @Override
    public Result delReportComment(int commentId) {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        ReportComment reportComment = reportCommentMapper.selectById(commentId);

        if(reportComment==null){
            return Result.build(null, ResultCodeEnum.REPORT_NOT_EXIST);
        }

        if(!Objects.equals(user.getStudentNo(), reportComment.getStudentNo())){
            return Result.build(null, ResultCodeEnum.REPORT_COMMENT_USER_WRONG);
        }

        SimpleDateFormat sdf= new SimpleDateFormat("yyyy年MM月dd日 HH时mm分");
        Date now = new Date();
        System.out.println(sdf.format(now));

        String content = "该评论已于"+sdf.format(now) +"被撤回";

        UpdateWrapper<ReportComment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id",commentId);
        updateWrapper.set("content",content).set("update_time",now);

        reportCommentMapper.update(null,updateWrapper);
        return Result.success(null);
    }
}
