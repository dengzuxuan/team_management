package com.team.backend.controller.report.comment;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.comment.AddReportCommentServiceImpl;
import com.team.backend.utils.common.WeeklyReportCommentAddType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AddReportCommentController {
    @Autowired
    AddReportCommentServiceImpl addReportCommentService;
    @PostMapping(value = "/v1/report/comment/addreportcomment/",consumes = "application/json")
    public Result AddReportComment(@RequestBody WeeklyReportCommentAddType commentAddInfo){
        return addReportCommentService.addReportComment(commentAddInfo);
    }
}
