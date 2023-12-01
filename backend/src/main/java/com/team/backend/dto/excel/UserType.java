package com.team.backend.dto.excel;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import java.io.Serializable;

/**
 * @ClassName UserType
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/30 21:58
 * @Version 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class UserType {
    @ExcelProperty(index = 0,value = "学号")
    private String studentNo;
    @ExcelProperty(index = 1,value = "姓名")
    private String userName;
    @ExcelProperty(index = 2,value = "手机号")
    private String tel;
    @ExcelProperty(index = 3,value = "邮箱")
    private String email;
    @ExcelProperty(index = 4,value = "身份证")
    private String cardno;
}
