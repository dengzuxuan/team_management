package com.team.backend.service.impl.user.account;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DemoData  {
    @ExcelProperty(index = 0)
    private int no;

    @ExcelProperty(index = 1)
    private String studentNo;

    @ExcelProperty(index = 2)
    private String username;

}