package com.team.backend.utils.common;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class equipmentType implements Serializable {
    private Integer id;
    @ExcelProperty(index = 0)
    private String serialNumber;
    @ExcelProperty(index = 1)
    private String name;
    @ExcelProperty(index = 2)
    private String version;
    @ExcelProperty(index = 3)
    private String originalValue;
    @ExcelProperty(index = 4)
    private String performanceIndex;
    @ExcelProperty(index = 5)
    private String address;
    @ExcelProperty(index = 6)
    private String warehouseEntryTime;
    @ExcelProperty(index = 7)
    private String hostRemarks;
    @ExcelProperty(index = 8)
    private String remark;
    private String failReason;
}
