package com.team.backend.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.team.backend.utils.common.*;

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

    public static String WeekProgressToJsonString(WeekProgressType weekProgress){
        return JSONObject.toJSONString(weekProgress);
    }
    public static WeekProgressType JsonToWeekProgress(String progressJsonString){
        return JSONObject.parseObject(progressJsonString,WeekProgressType.class);
    }

    public static String WeekPlanToJsonString(WeekPlanType weekPlan){
        return JSONObject.toJSONString(weekPlan);
    }
    public static WeekPlanType JsonToWeekPlan(String planJsonString){
        return JSONObject.parseObject(planJsonString,WeekPlanType.class);
    }

    public static String attachToJsonString(List<Attach> attachs){
        return JSONObject.toJSONString(attachs);
    }
    public static List<Attach> JsonStringToAttach(String attach){
        return JSONObject.parseArray(attach,Attach.class);
    }
}
