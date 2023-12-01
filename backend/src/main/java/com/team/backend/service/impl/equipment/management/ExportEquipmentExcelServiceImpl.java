package com.team.backend.service.impl.equipment.management;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.ListUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.mapper.EquipmentMapper;
import com.team.backend.pojo.Equipment;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.management.ExportEquipmentExcelService;
import com.team.backend.service.impl.utils.UserDetailsImpl;
import com.team.backend.dto.excel.equipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ExportEquipmentExcelServiceImpl implements ExportEquipmentExcelService {
    @Autowired
    EquipmentMapper equipmentMapper;
    @Override
    public Result exportEquipmentExcel(String[] fields) throws IOException {
        String fileName =   "./simpleWrite" + System.currentTimeMillis() + ".xlsx";

        Set<String> includeColumnFiledNames = new HashSet<>(Arrays.asList(fields));

        // 这里 需要指定写用哪个class去写，然后写到第一个sheet，名字为模板 然后文件流会自动关闭
        EasyExcel.write(fileName, equipmentType.class).includeColumnFiledNames(includeColumnFiledNames).sheet("模板")
                .doWrite(data());
        File file = new File(fileName);

        // 将Excel文件读取到字节数组中
        FileInputStream fileInputStream = new FileInputStream(file);
        byte[] bytes = new byte[(int) file.length()];
        fileInputStream.read(bytes);
        fileInputStream.close();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
        headers.setContentDispositionFormData("attachment", "users.xlsx");

        file.delete();
        return Result.success(ResponseEntity.ok().headers(headers).contentLength(bytes.length)
                .body(bytes));
    }
    private List<equipmentType> data() {
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl loginUser = (UserDetailsImpl)authenticationToken.getPrincipal();
        User user = loginUser.getUser();

        QueryWrapper<Equipment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("admin_no",user.getStudentNo());
        List<Equipment> equipmentList = equipmentMapper.selectList(queryWrapper);

        List<equipmentType> list = ListUtils.newArrayList();
        for (Equipment equipment:equipmentList) {
            equipmentType data = new equipmentType();
            data.setSerialNumber(equipment.getSerialNumber());
            data.setName(equipment.getName());
            data.setVersion(equipment.getVersion());
            data.setOriginalValue(equipment.getOriginalValue());
            data.setPerformanceIndex(equipment.getPerformanceIndex());
            data.setAddress(equipment.getAddress());
            data.setWarehouseEntryTime(equipment.getWarehouseEntrytime());
            data.setHostRemarks(equipment.getHostRemarks());
            data.setRemark(equipment.getRemark());
            list.add(data);
        }
        return list;
    }
}
