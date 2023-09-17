package com.team.backend.controller.report.comment;

import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.comment.GetReportCommentServiceImpl;
import com.team.backend.service.report.comment.GetReportCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetReportCommentController {
    @Autowired
    GetReportCommentServiceImpl getReportCommentService;
    @GetMapping("/v1/report/comment/getreportcomment/")
    Result GetReportComment(@RequestParam Map<String,String>m1){
        int recordId = Integer.parseInt(m1.get("recordId"));
        return getReportCommentService.getReportComment(recordId);
    }
}
