package com.team.backend.service.impl.equipment.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.EquipmentMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.Equipment;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.management.AddEquipmentService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.dto.req.equipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AddEquipmentServiceImpl implements AddEquipmentService {
    @Autowired
    EquipmentMapper equipmentMapper;
    @Autowired
    UserMapper userMapper;

    @Override
    public Result addEquipment(equipmentType equipmentInfo) {

        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String adminId = user.getStudentNo();

        if(user.getRole()!=1 && user.getRole()!=2){
            return Result.build(null,ResultCodeEnum.ROLE_AUTHORIZATION_NOT_ENOUGHT);
        }

        if(user.getRole()==2){
            adminId=user.getAdminNo();
        }

        ResultCodeEnum rescode = checkEquipment(equipmentInfo);
        if(rescode!=ResultCodeEnum.SUCCESS){
            return Result.build(null,rescode);
        }

        Date now = new Date();
        Equipment newEquipment = new Equipment(
                null,
                equipmentInfo.getSerialNumber(),
                equipmentInfo.getName(),
                equipmentInfo.getVersion(),
                equipmentInfo.getOriginalValue(),
                equipmentInfo.getPerformanceIndex(),
                equipmentInfo.getAddress(),
                equipmentInfo.getWarehouseEntryTime(),
                equipmentInfo.getHostRemarks(),
                equipmentInfo.getRemark(),
                1,
                adminId,
                user.getStudentNo(),
                null,
                null,
                now,
                now
        );
        equipmentMapper.insert(newEquipment);
        return Result.success(null);
    }

    @Override
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

        QueryWrapper<Equipment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("serial_number",serialNumber);
        Equipment equipment =  equipmentMapper.selectOne(queryWrapper);

        if(equipment!=null){
            return ResultCodeEnum.EQUIPMENT_ALREAY_EXIST;
        }
        if(serialNumber==null || serialNumber.length()>=50 || serialNumber.length()==0){
            return ResultCodeEnum.EQUIPMENT_SERIALNUMBER_PARAM_WRONG;
        }
        if(name == null || name.length()>=50 || name.length()==0){
            return ResultCodeEnum.EQUIPMENT_NAME_PARAM_WRONG;
        }
        if(version == null || version.length()>=50 || version.length()==0){
            return ResultCodeEnum.EQUIPMENT_VERSION_PARAM_WRONG;
        }
        if(originalValue != null && originalValue.length()>=50){
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
        if(warehouseEntryTime==null || warehouseEntryTime.length()==0){
            return ResultCodeEnum.EQUIPMENT_WAREHOUSEENTRYTIME_PARAM_WRONG;
        }
        return ResultCodeEnum.SUCCESS;
    }
}
