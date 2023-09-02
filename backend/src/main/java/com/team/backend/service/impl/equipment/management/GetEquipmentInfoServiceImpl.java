package com.team.backend.service.impl.equipment.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.mapper.EquipmentMapper;
import com.team.backend.mapper.EquipmentRecordMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.Equipment;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.management.GetEquipmentInfoService;
import com.team.backend.utils.common.equipmentPageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GetEquipmentInfoServiceImpl implements GetEquipmentInfoService {
    @Autowired
    EquipmentMapper equipmentMapper;

    @Autowired
    EquipmentRecordMapper equipmentRecordMapper;

    @Autowired
    UserMapper userMapper;
    @Override
    public Result getEquipmentInfo(int equipmentId) {

        updateEquipmentAndRecord(equipmentId);

        Equipment equipment = equipmentMapper.selectById(equipmentId);
        if(equipment==null){
            return Result.build(null, ResultCodeEnum.EQUIPMENT_NOT_EXIST);
        }
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.select("student_no","username").eq("student_no",equipment.getRecipient());
        User recipent =  userMapper.selectOne(userQueryWrapper);

        QueryWrapper<User> userQueryWrapper2 = new QueryWrapper<>();
        userQueryWrapper2.select("student_no","username").eq("student_no",equipment.getFormerRecipient());
        User formerRecipent =  userMapper.selectOne(userQueryWrapper2);


        QueryWrapper<EquipmentRecord> equipmentRecordQueryWrapper = new QueryWrapper<>();
        equipmentRecordQueryWrapper.eq("equipment_id",equipment.getId()).eq("status",2);
        List<EquipmentRecord> record = equipmentRecordMapper.selectList(equipmentRecordQueryWrapper);

        equipmentPageType pageType = new equipmentPageType(
                equipment.getId(),
                equipment.getSerialNumber(),
                equipment.getName(),
                equipment.getVersion(),
                equipment.getOriginalValue(),
                equipment.getPerformanceIndex(),
                equipment.getAddress(),
                equipment.getWarehouseEntrytime(),
                equipment.getHostRemarks(),
                equipment.getRemark(),
                equipment.getStatus(),
                record.size(),
                recipent,
                formerRecipent
        );
        return Result.success(pageType);
    }

    @Override
    public void updateEquipmentAndRecord(int equipmentId) {
        /*
        当申请记录过期时，更新申请记录状态 (审批通过使用中3->使用结束4) 或者 (待审批2->过期1)
        当设备申请到期时，更新设备状态 (使用中2->空闲1)
        */
        Date now = new Date();
        Equipment equipment = equipmentMapper.selectById(equipmentId);

        if(equipment.getStatus()==2){
            //当设备正在使用时，有且仅能有一条记录存在
            QueryWrapper<EquipmentRecord> recordQueryWrapper = new QueryWrapper<>();
            recordQueryWrapper.eq("equipment_id",equipmentId).eq("status",3);
            EquipmentRecord equipmentRecord = equipmentRecordMapper.selectOne(recordQueryWrapper);
            if(equipmentRecord!=null){
                //归还时间(deadlineTime早于当前时间)
                if(equipmentRecord.getDeadlineTime().compareTo(now)<0){
                    //需要设备
                    UpdateWrapper<Equipment> equipmentUpdateWrapper = new UpdateWrapper<>();
                    equipmentUpdateWrapper.eq("id",equipmentId);
                    equipmentUpdateWrapper.set("status",1);
                    equipmentMapper.update(null,equipmentUpdateWrapper);

                    //以及更新记录
                    UpdateWrapper<EquipmentRecord> equipmentRecordUpdateWrapper = new UpdateWrapper<>();
                    equipmentRecordUpdateWrapper.eq("id",equipmentRecord.getId());
                    equipmentRecordUpdateWrapper.set("status",4);
                    equipmentRecordMapper.update(null,equipmentRecordUpdateWrapper);
                }
            }
        } else if (equipment.getStatus()==1) {
            //当设备处于空闲时，可能有多条记录存在
            QueryWrapper<EquipmentRecord> recordQueryWrapper = new QueryWrapper<>();
            recordQueryWrapper.eq("equipment_id",equipmentId).eq("status",2);
            List<EquipmentRecord> equipmentRecords = equipmentRecordMapper.selectList(recordQueryWrapper);
            for (EquipmentRecord equipmentRecord:equipmentRecords) {
                if(equipmentRecord!=null){
                    //归还时间(deadlineTime早于当前时间)
                    if(equipmentRecord.getDeadlineTime().compareTo(now)<0){
                        //以及更新记录
                        UpdateWrapper<EquipmentRecord> equipmentRecordUpdateWrapper = new UpdateWrapper<>();
                        equipmentRecordUpdateWrapper.eq("id",equipmentRecord.getId());
                        equipmentRecordUpdateWrapper.set("status",1);
                        equipmentRecordMapper.update(null,equipmentRecordUpdateWrapper);
                    }
                }
            }

        }
    }
}
