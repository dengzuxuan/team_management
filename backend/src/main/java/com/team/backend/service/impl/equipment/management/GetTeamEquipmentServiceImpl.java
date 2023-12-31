package com.team.backend.service.impl.equipment.management;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.EquipmentMapper;
import com.team.backend.mapper.EquipmentRecordMapper;
import com.team.backend.mapper.UserMapper;
import com.team.backend.pojo.Equipment;
import com.team.backend.pojo.EquipmentRecord;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.management.GetEquipmentInfoService;
import com.team.backend.service.equipment.management.GetTeamEquipmentService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.dto.resp.equipmentPageType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.team.backend.utils.common.consts.roleConst.LEADERROLE;
import static com.team.backend.utils.common.consts.roleConst.TEAMMEMBERROLE;

@Service
public class GetTeamEquipmentServiceImpl implements GetTeamEquipmentService {
    @Autowired
    private GetEquipmentInfoService getEquipmentInfoService;

    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EquipmentRecordMapper equipmentRecordMapper;
    @Override
    public Result getTeamEquipment(int pageNum, int pageSize) {
        UsernamePasswordAuthenticationToken authenticationToken =
                (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl) authenticationToken.getPrincipal();
        User user = loginUser.getUser();
        String adminNo = user.getStudentNo();
        if(user.getRole()==LEADERROLE || user.getRole()==TEAMMEMBERROLE){
            adminNo = user.getAdminNo();
        }
        Map<String,Object> res = new HashMap<>();
        List<equipmentPageType> equipmentPageTypes = new ArrayList<>();

        Page<Equipment> page = new Page<>(pageNum,pageSize);
        QueryWrapper<Equipment> queryWrapper = new QueryWrapper<>();
        Page<Equipment> rowPages = new Page<>();

        queryWrapper.eq("admin_no",adminNo);
        rowPages = equipmentMapper.selectPage(page, queryWrapper);

        List<Equipment> equipmentList = rowPages.getRecords();
        for (Equipment equipment: equipmentList) {
            getEquipmentInfoService.updateEquipmentAndRecord(equipment.getId());

            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.select("student_no","username").eq("id",equipment.getRecipient());
            User recipent =  userMapper.selectOne(userQueryWrapper);

            QueryWrapper<User> userQueryWrapper2 = new QueryWrapper<>();
            userQueryWrapper2.select("student_no","username").eq("id",equipment.getFormerRecipient());
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
                    formerRecipent,
                    recipent
            );
            equipmentPageTypes.add(pageType);
        }
        res.put("equipments",equipmentPageTypes);
        res.put("total",rowPages.getTotal());
        res.put("size",rowPages.getSize());
        res.put("current",rowPages.getCurrent());
        return Result.success(res);
    }
}
