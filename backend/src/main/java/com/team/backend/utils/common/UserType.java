package com.team.backend.utils.common;


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
    private String studentNo;

    @ExcelProperty(index = 2)
    private String username;

}