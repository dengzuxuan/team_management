package com.team.backend.controller.report.management;


import com.team.backend.config.result.Result;
import com.team.backend.service.impl.report.management.GetUserAllReportServiceImpl;
import com.team.backend.service.report.management.GetUserAllReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class GetUserAllReportController {
    @Autowired
    GetUserAllReportServiceImpl getUserAllReportService;

    @GetMapping("/v1/report/management/getuserallreport/")
    Result getUserAllReport(@RequestParam Map<String,String>m1){
        String studentNo = m1.get("studentno");
        int pageNum = Integer.parseInt(m1.get("pageNum"));
        int pageSize = Integer.parseInt(m1.get("pageSize"));
        return getUserAllReportService.getUserAllReport(studentNo,pageNum,pageSize);
    }
}
