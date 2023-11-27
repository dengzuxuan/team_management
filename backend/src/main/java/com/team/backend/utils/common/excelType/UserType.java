package com.team.backend.utils.common.excelType;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserType {
    @ExcelProperty(index = 0)
    private int no;
    @ExcelProperty(index = 1)
    private String username;
    @ExcelProperty(index = 2)
    private String studentNo;
    @ExcelProperty(index = 3)
    private String tel;
    @ExcelProperty(index = 4)
    private String email;
    @ExcelProperty(index = 5)
    private String cardNo;
    @ExcelProperty(index = 6)
    private String teamNo;
    @ExcelProperty(index = 7)
    private String teamName;
    @ExcelProperty(index = 8)
    private String Role;
}