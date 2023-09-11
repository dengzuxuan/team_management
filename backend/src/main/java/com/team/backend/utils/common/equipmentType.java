package com.team.backend.utils.common;

import com.alibaba.excel.annotation.ExcelIgnore;
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
    @ExcelProperty(index = 0,value = "编号")
    private String serialNumber;
    @ExcelProperty(index = 1,value = "名称")
    private String name;
    @ExcelProperty(index = 2,value = "型号")
    private String version;
    @ExcelProperty(index = 3,value = "原值")
    private String originalValue;
    @ExcelProperty(index = 4,value = "设备性能指标")
    private String performanceIndex;
    @ExcelProperty(index = 5,value = "存放地")
    private String address;
    @ExcelProperty(index = 6,value = "入库时间")
    private String warehouseEntryTime;
    @ExcelProperty(index = 7,value = "主机备注")
    private String hostRemarks;
    @ExcelProperty(index = 8,value = "备注")
    private String remark;
    @ExcelIgnore
    private String failReason;
}
