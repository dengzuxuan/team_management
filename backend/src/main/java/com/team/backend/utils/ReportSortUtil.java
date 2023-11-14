package com.team.backend.utils;

import com.team.backend.pojo.ReportTeamWork;
import com.team.backend.dto.req.WeeklyReportType;

import java.util.Comparator;
import java.util.Objects;

public class ReportSortUtil {
    public static class ReportSortUtilForList  implements Comparator<WeeklyReportType>{
    @Override
    public int compare(WeeklyReportType w1, WeeklyReportType w2) {
        if(w1 != null){
            if(!Objects.equals(w1.getYear(), w2.getYear())){
                return compareWithYear(Integer.parseInt(w1.getYear()),Integer.parseInt(w2.getYear()));
            }else{
                return compareWithWeek(Integer.parseInt(w1.getWeek()),Integer.parseInt(w2.getWeek()));
            }
        }
        return 0;
    }
    private int compareWithWeek(int week1, int week2) {
        if (week1 > week2) {
            return -1;
        }
        return 1;
    }
    private int compareWithYear(int year1, int year2) {
        if (year1 > year2) {
            return -1;
        }
        return 1;
    }}

    public static class ReportTeamWorkSortUtilForList  implements Comparator<ReportTeamWork>{
        @Override
        public int compare(ReportTeamWork w1, ReportTeamWork w2) {
            if(w1 != null){
                if(!Objects.equals(w1.getYear(), w2.getYear())){
                    return compareWithYear(Integer.parseInt(w1.getYear()),Integer.parseInt(w2.getYear()));
                }else{
                    return compareWithWeek(Integer.parseInt(w1.getWeek()),Integer.parseInt(w2.getWeek()));
                }
            }
            return 0;
        }
        private int compareWithWeek(int week1, int week2) {
            if (week1 > week2) {
                return -1;
            }
            return 1;
        }
        private int compareWithYear(int year1, int year2) {
            if (year1 > year2) {
                return -1;
            }
            return 1;
        }}


}
