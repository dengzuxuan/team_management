package com.team.backend.utils.common.excelType;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

/**
 * @ClassName TeamType
 * @Description TODO
 * @Author Colin
 * @Date 2023/11/14 20:53
 * @Version 1.0
 */
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class TeamType {
    @ExcelProperty(index = 0)
    private String no;

    @ExcelProperty(index = 1)
    private String typename;
}
