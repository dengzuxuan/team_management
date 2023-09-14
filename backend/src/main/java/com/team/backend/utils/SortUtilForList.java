package com.team.backend.utils;

import com.team.backend.pojo.WeeklyReport;
import com.team.backend.utils.common.WeeklyReportType;

import java.util.Comparator;
import java.util.Objects;

public class SortUtilForList implements Comparator<WeeklyReportType> {
    @Override
    public int compare(WeeklyReportType w1, WeeklyReportType w2) {
        if(w1 != null){
            if(!Objects.equals(w1.getYear(), w2.getYear())){
                return compareWithYear(Integer.parseInt(w1.getYear()),Integer.parseInt(w2.getYear()));
            }else{
                return compareWithMonth(Integer.parseInt(w1.getMonth()),Integer.parseInt(w2.getMonth()));
            }
        }
        return 0;
    }
    private int compareWithMonth(int month1, int month2) {
        if (month1 > month2) {
            return -1;
        }
        return 1;
    }
    private int compareWithYear(int year1, int year2) {
        if (year1 > year2) {
            return -1;
        }
        return 1;
    }
}
