package com.team.backend.utils.common;


import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class equipmentPageType implements Serializable {
    private Integer id;
    private String serialNumber;
    private String name;
    private String version;
    private String originalValue;
    private String performanceIndex;
    private String address;
    private String warehouseEntryTime;
    private String hostRemarks;
    private String remark;
    private Integer status;
    private Integer applyNumber;

    private Object formerRecipient;
    private Object recipient;
}
