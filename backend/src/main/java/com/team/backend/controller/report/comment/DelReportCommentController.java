package com.team.backend.controller.report.comment;

import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.comment.DelReportCommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class DelReportCommentController {
    @Autowired
    DelReportCommentServiceImpl delReportCommentService;
    @PostMapping("/v1/report/comment/delreportcomment/")
    Result GetReportComment(@RequestParam Map<String,String> m1){
        int commentId = Integer.parseInt(m1.get("commentId"));
        return delReportCommentService.delReportComment(commentId);
    }
}
