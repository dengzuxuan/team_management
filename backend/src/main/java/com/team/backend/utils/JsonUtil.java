package com.team.backend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.team.backend.utils.common.TeamWorks;
import com.team.backend.utils.common.WeeklyReportType;

import java.util.List;

public class JsonUtil {
    //Java对象转换成JSON字符串
    public static String ReportToJsonString(List<TeamWorks> reportInfo) {
        return JSONObject.toJSONString(reportInfo);
    }


    public static List<TeamWorks> JsonToReport(String reportJsonString) {
        //JSON字符串转换成JSON对象
        return JSON.parseArray(reportJsonString,TeamWorks.class);
    }
}
