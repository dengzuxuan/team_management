package com.team.backend.service.impl.equipment.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.EquipmentMapper;
import com.team.backend.pojo.Equipment;
import com.team.backend.service.equipment.management.AddEquipmentService;
import com.team.backend.service.equipment.management.UpdateEquipmentService;
import com.team.backend.utils.common.equipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UpdateEquipmentServiceImpl implements UpdateEquipmentService {
    @Autowired
    EquipmentMapper equipmentMapper;
    @Override
    public Result updateEquipment(equipmentType equipmentInfo) {
        QueryWrapper<Equipment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id",equipmentInfo.getId());
        Equipment equipmentfind = equipmentMapper.selectOne(queryWrapper);
        if(equipmentfind==null){
            return Result.build(null, ResultCodeEnum.EQUIPMENT_NOT_EXIST);
        }

        if(!Objects.equals(equipmentfind.getSerialNumber(), equipmentInfo.getSerialNumber())){
            QueryWrapper<Equipment> queryWrapper2 = new QueryWrapper<>();
            queryWrapper2.eq("serial_number",equipmentInfo.getSerialNumber());
            Equipment equipment =  equipmentMapper.selectOne(queryWrapper2);

            if(equipment!=null){
                return Result.build(null,ResultCodeEnum.EQUIPMENT_ALREAY_EXIST);
            }
        }

        ResultCodeEnum resultCodeEnum = checkEquipment(equipmentInfo);
        if(resultCodeEnum!=ResultCodeEnum.SUCCESS){
            return Result.build(null,resultCodeEnum);
        }

        UpdateWrapper<Equipment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("serial_number",equipmentInfo.getSerialNumber());
        Equipment equipmentupdate = new Equipment();
        equipmentupdate.setId(equipmentInfo.getId());
        equipmentupdate.setSerialNumber(equipmentInfo.getSerialNumber());
        equipmentupdate.setName(equipmentInfo.getName());
        equipmentupdate.setVersion(equipmentInfo.getVersion());
        equipmentupdate.setOriginalValue(equipmentInfo.getOriginalValue());
        equipmentupdate.setPerformanceIndex(equipmentInfo.getPerformanceIndex());
        equipmentupdate.setAddress(equipmentInfo.getAddress());
        equipmentupdate.setWarehouseEntrytime(equipmentInfo.getWarehouseEntryTime());
        equipmentupdate.setHostRemarks(equipmentInfo.getHostRemarks());
        equipmentupdate.setRemark(equipmentInfo.getRemark());

        equipmentMapper.update(equipmentupdate,updateWrapper);
        return Result.success(null);
    }

    public ResultCodeEnum checkEquipment(equipmentType equipmentInfo){
        String serialNumber = equipmentInfo.getSerialNumber();
        String name = equipmentInfo.getName();
        String version = equipmentInfo.getVersion();
        String originalValue = equipmentInfo.getOriginalValue();
        String performanceIndex = equipmentInfo.getPerformanceIndex();
        String address = equipmentInfo.getAddress();
        String warehouseEntryTime = equipmentInfo.getWarehouseEntryTime();
        String hostRemarks = equipmentInfo.getHostRemarks();
        String remark = equipmentInfo.getRemark();

        if(serialNumber==null || serialNumber.length()>=50 ){
            return ResultCodeEnum.EQUIPMENT_SERIALNUMBER_PARAM_WRONG;
        }
        if(name == null || name.length()>=50){
            return ResultCodeEnum.EQUIPMENT_NAME_PARAM_WRONG;
        }
        if(version == null || version.length()>=50){
            return ResultCodeEnum.EQUIPMENT_VERSION_PARAM_WRONG;
        }
        if(originalValue.length()>=50){
            return ResultCodeEnum.EQUIPMENT_ORIGINAL_VALUE_PARAM_WRONG;
        }
        if(performanceIndex!=null && performanceIndex.length()>=255){
            return ResultCodeEnum.EQUIPMENT_PERFORMANCEINDEX_PARAM_WRONG;
        }
        if(address!=null &&address.length()>=50){
            return ResultCodeEnum.EQUIPMENT_ADDRESS_PARAM_WRONG;
        }
        if(hostRemarks!=null &&hostRemarks.length()>=50){
            return ResultCodeEnum.EQUIPMENT_HOSTREMARKS_PARAM_WRONG;
        }
        if(remark!=null &&remark.length()>=50){
            return ResultCodeEnum.EQUIPMENT_REMARK_PARAM_WRONG;
        }
        if(warehouseEntryTime==null){
            return ResultCodeEnum.EQUIPMENT_WAREHOUSEENTRYTIME_PARAM_WRONG;
        }
        return ResultCodeEnum.SUCCESS;
    }
}
