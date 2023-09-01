package com.team.backend.service.impl.equipment.management;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.listener.PageReadListener;
import com.aliyuncs.utils.IOUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.team.backend.config.result.Result;
import com.team.backend.config.result.ResultCodeEnum;
import com.team.backend.pojo.User;
import com.team.backend.service.equipment.management.AddEquipmentExcelService;
import com.team.backend.service.equipment.management.AddEquipmentService;
import com.team.backend.service.impl.user.account.RegisterExcelServiceImpl;
import com.team.backend.utils.common.UserType;
import com.team.backend.utils.common.equipmentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

@Service
public class AddEquipmentExcelServiceImpl implements AddEquipmentExcelService {
    ResultCodeEnum resultCodeEnum = ResultCodeEnum.SUCCESS;
    int totalCnt=0;
    Map<String,Integer> serialNumberMap = new HashMap<>();
    @Autowired
    AddEquipmentService addEquipmentService;
    @Override
    public Result addEquipmentExcel(MultipartFile file) {
        List<equipmentType> correctEquipment = new ArrayList<>();
        List<equipmentType> wrongEquipment = new ArrayList<>();

        InputStream inputStream =null;
        try {
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, equipmentType.class, new PageReadListener<equipmentType>(dataList -> {

                if(dataList==null){
                    resultCodeEnum= ResultCodeEnum.FILE_WRONG_EMPTY;
                }
                for (equipmentType equipmentdata : dataList) {
                    String serialNumber = equipmentdata.getSerialNumber();
                    if(serialNumber!=null){
                        if(serialNumberMap.get(serialNumber)==null){
                            serialNumberMap.put(serialNumber,1);
                        }else{
                            serialNumberMap.put(serialNumber,serialNumberMap.get(serialNumber)+1);
                        }
                    }
                }
            })).sheet().doRead();
        } catch (IOException e) {
            serialNumberMap.clear();
            throw new RuntimeException(e);
        }

        try {
            totalCnt = 0;
            inputStream = file.getInputStream();
            EasyExcel.read(inputStream, equipmentType.class, new PageReadListener<equipmentType>(dataList -> {
                totalCnt += dataList.size();

                for (equipmentType equipmentdata : dataList) {
                    resultCodeEnum = addEquipmentService.checkEquipment(equipmentdata);
                    if(resultCodeEnum!=ResultCodeEnum.SUCCESS){
                        equipmentdata.setFailReason(resultCodeEnum.getMessage());
                        wrongEquipment.add(equipmentdata);
                        continue;
                    }
                    if(serialNumberMap.get(equipmentdata.getSerialNumber())>1){
                        equipmentdata.setFailReason("文件内存在多个相同序列号"+equipmentdata.getSerialNumber());
                        wrongEquipment.add(equipmentdata);
                        continue;
                    }
                    correctEquipment.add(equipmentdata);
                }
            })).sheet().doRead();

        } catch (IOException e) {
            serialNumberMap.clear();
            throw new RuntimeException(e);
        }finally {
            serialNumberMap.clear();
            IOUtils.closeQuietly(inputStream);
        }
        Map<String,Object> res = new HashMap<>();
        res.put("totalCnt",totalCnt);
        res.put("corrcetCnt",correctEquipment.size());
        res.put("wrongCnt",wrongEquipment.size());

        res.put("correctEquipment",correctEquipment);
        res.put("wrongEquipment",wrongEquipment);

        serialNumberMap.clear();
        return Result.build(res,ResultCodeEnum.SUCCESS);
    }
}
